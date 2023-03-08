package com.interview;

import static org.junit.Assert.assertEquals;

import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.joda.money.Money;

import com.interview.customer.Customer;
import com.interview.customer.PurchaseOrder;
import com.interview.store.Apple;
import com.interview.store.Fruit;
import com.interview.store.Mango;
import com.interview.store.Promotion;
import com.interview.store.Store;
import com.interview.store.Strawberry;
import com.interview.strategy.PricingStrategy;

public class Story {
	
	private static Store store;
	
	public static int randomInteger(int min, int max) {
		return new Random().ints(1, min, max).findFirst().getAsInt();
	}
	
	public static void init() {
		Story.store = new Store();
	}
	
	public static void story1() {
		Store store = Story.store;
		store.purchase(Apple.class, 100);
		store.purchase(Strawberry.class, 50);
		store.updatePricingStrategy(new PricingStrategy(Apple.class, 8.d));
		store.updatePricingStrategy(new PricingStrategy(Strawberry.class, 13.d));
		
		int qtyOfApple = 10;
		int qtyOfStrawberry = 2;
		
//		int qtyOfApple = Story.randomInteger(0, store.getRemainingQtyOfFruit(Apple.class));
//		int qtyOfStrawberry = Story.randomInteger(0, store.getRemainingQtyOfFruit(Apple.class));
		
		Customer ca = new Customer("A");
		List<PurchaseOrder> poList = new ArrayList<PurchaseOrder>() {{
			add(new PurchaseOrder(Apple.class, qtyOfApple));
			add(new PurchaseOrder(Strawberry.class, qtyOfStrawberry));
		}};
		
		Money total = ca.checkoutTotalPrice(poList, store);

		assertEquals(Money.of(Config.CURRENCY_UNIT, 106.d), total);
	}
	
	public static void story2() {
		Store store = Story.store;
		store.purchase(Mango.class, 20);
		store.updatePricingStrategy(new PricingStrategy(Mango.class, 20.d));
		
		int qtyOfApple = 11;
		int qtyOfStrawberry = 1;
		int qtyOfMango = 3;
		
		Customer cb = new Customer("B");
		List<PurchaseOrder> poList = new ArrayList<PurchaseOrder>() {{
			add(new PurchaseOrder(Apple.class, qtyOfApple));
			add(new PurchaseOrder(Strawberry.class, qtyOfStrawberry));
			add(new PurchaseOrder(Mango.class, qtyOfMango));
		}};
		
		Money total = cb.checkoutTotalPrice(poList, store);
		
		assertEquals(Money.of(Config.CURRENCY_UNIT, 161.d), total);
		
	}
	
	public static void story3() {
		Store store = Story.store;
		store.addPromotion("Strawberry 20% off", new Promotion() {
			
			@Override
			public Money applyToTotalPrice(Money price) {
				return price;
			}
			
			@Override
			public Money applyToSpecificFruit(Class<? extends Fruit> fruitCls, Money price) {
				if (Strawberry.class.equals(fruitCls) == true) {
					return price.multipliedBy(0.8, RoundingMode.CEILING);
				}
				return price;
			}
		});
		
		int qtyOfApple = 10;
		int qtyOfStrawberry = 2;
		int qtyOfMango = 2;
		
		Customer cc = new Customer("C");
		
		List<PurchaseOrder> poList = new ArrayList<PurchaseOrder>() {{
			add(new PurchaseOrder(Apple.class, qtyOfApple));
			add(new PurchaseOrder(Strawberry.class, qtyOfStrawberry));
			add(new PurchaseOrder(Mango.class, qtyOfMango));
		}};
		
		Money total = cc.checkoutTotalPrice(poList, store);
		
		assertEquals(Money.of(Config.CURRENCY_UNIT, 140.8d), total);
	}
	
	public static void story4() {
		Store store = Story.store;
		store.addPromotion("per 100 - 10", new Promotion() {
			
			@Override
			public Money applyToTotalPrice(Money price) {
				int discount = price.dividedBy(100, RoundingMode.FLOOR).getAmountMajorInt() * 10;
				return price.minus(discount);
			}
			
			@Override
			public Money applyToSpecificFruit(Class<? extends Fruit> fruitCls, Money price) {
				return price;
			}
		});
		
		int qtyOfApple = 10;
		int qtyOfStrawberry = 13;
		int qtyOfMango = 5;
		
		Customer cd = new Customer("D");
		
		List<PurchaseOrder> poList = new ArrayList<PurchaseOrder>() {{
			add(new PurchaseOrder(Apple.class, qtyOfApple));
			add(new PurchaseOrder(Strawberry.class, qtyOfStrawberry));
			add(new PurchaseOrder(Mango.class, qtyOfMango));
		}};
		
		Money total = cd.checkoutTotalPrice(poList, store);

		assertEquals(Money.of(Config.CURRENCY_UNIT, 285.2d), total);
	}

	public static void main(String[] args) {
		
		init();
		story1();
		story2();
		story3();
		story4();

	}

}
