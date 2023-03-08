package com.interview.strategy;

import org.joda.money.Money;

import com.interview.Config;
import com.interview.store.Fruit;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class PricingStrategy {
	
	private Class<? extends Fruit> fruitCls;
	private Money pricePerUnit;
	private Fruit.Unit fruitUnit;
	
	public PricingStrategy(Class<? extends Fruit> fruitCls, double pricePerUnit, Fruit.Unit fruitUnit) {
		this.fruitCls = fruitCls;
		this.pricePerUnit = Money.of(Config.CURRENCY_UNIT, pricePerUnit);
		this.fruitUnit = fruitUnit;
	}

	public PricingStrategy(Class<? extends Fruit> fruitCls, double pricePerUnit) {
		this.fruitCls = fruitCls;
		this.pricePerUnit = Money.of(Config.CURRENCY_UNIT, pricePerUnit);
		this.fruitUnit = Fruit.Unit.HALF_KG;
	}
}
