package com.interview.store;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.joda.money.Money;

import com.interview.Config;
import com.interview.customer.PurchaseOrder;
import com.interview.strategy.PricingStrategy;

public class Store {
	
	private Map<Class<? extends Fruit>, List<Fruit>> fruitMap = new HashMap<>();
	private Map<Class<? extends Fruit>, PricingStrategy> pricingStrategyMap = new HashMap<>();
	private Map<String, Promotion> promotionMap = new HashMap<>();
	
	public void purchase(Class<? extends Fruit> fruitCls, Integer quantity) {
		List<Fruit> specificFruits = null;

		if (fruitMap.containsKey(fruitCls) == true) {
			specificFruits = fruitMap.get(fruitCls);
		} else {
			specificFruits = new LinkedList<Fruit>();
			fruitMap.put(fruitCls, specificFruits);
		}
		
		specificFruits.addAll(Fruit.Farm.provideFruits(fruitCls, quantity));
		
		if (this.pricingStrategyMap.containsKey(fruitCls) == true) {
			this.updatePricingStrategy(this.pricingStrategyMap.get(fruitCls));
		}
	}
	
	public void updatePricingStrategy(PricingStrategy ps) {
		Class<? extends Fruit> fruitCls = ps.getFruitCls();
		this.pricingStrategyMap.put(fruitCls, ps);
		if (this.fruitMap.containsKey(fruitCls) == true) {

			this.fruitMap.get(fruitCls).forEach(fruit -> {
				fruit.setPricePerUnit(ps.getPricePerUnit());
				fruit.setUnit(ps.getFruitUnit());
			});
		}
	}
	
	public Money calcTotalPrice(List<PurchaseOrder> poList) {
		Money total = poList.stream()
				.map(po -> this.calcPriceOfPurchaseOrder(po))
				.filter(poPrice -> poPrice != null)
				.reduce(Money.of(Config.CURRENCY_UNIT, 0.d), (result, poPrice) -> result.plus(poPrice));
		
		for (Promotion p : promotionMap.values()) {
			total = p.applyToTotalPrice(total);
		}
		
		return total;
	}
	
	public Money calcPriceOfPurchaseOrder(PurchaseOrder po) {
		Class<? extends Fruit> fruitCls = po.getFruitCls();
		
		if (po.getQuantity() <= 0) {
			return null;
		}
		
		if (pricingStrategyMap.containsKey(fruitCls) == false || fruitMap.containsKey(fruitCls) == false) {
			return null;
		}
		
		PricingStrategy ps = pricingStrategyMap.get(fruitCls);
		List<Fruit> specificFruits = fruitMap.get(fruitCls);
		
		Money total = Money.of(ps.getPricePerUnit().getCurrencyUnit(), 0.d);
		
		for (int i = 0; i < po.getQuantity(); i++) {
			if (specificFruits.size() <= 0) {
				break;
			}
			Fruit fruit = specificFruits.remove(0);
			total = total.plus(fruit.getPricePerUnit());
		}
		
		for (Promotion p : promotionMap.values()) {
			total = p.applyToSpecificFruit(fruitCls, total);
		}
		
		return total;
	}
	
	public Money getPricePerUnitOfFruit(Class<? extends Fruit> fruitCls) {
		if (pricingStrategyMap.containsKey(fruitCls) == true) {
			return pricingStrategyMap.get(fruitCls).getPricePerUnit();
		}
		return null;
	}
	
	public int getRemainingQtyOfFruit(Class<? extends Fruit> fruitCls) {
		if (fruitMap.containsKey(fruitCls) == true) {
			return fruitMap.get(fruitCls).size();
		}
		return 0;
	}
	
	public void addPromotion(String promotionName, Promotion promotion) {
		promotionMap.put(promotionName, promotion);
	}

}
