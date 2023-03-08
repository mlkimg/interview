package com.interview.store;

import org.joda.money.Money;

public interface Promotion {
	public Money applyToSpecificFruit(Class<? extends Fruit> fruitCls, Money price);
	public Money applyToTotalPrice(Money price);
}
