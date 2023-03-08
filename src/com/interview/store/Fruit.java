package com.interview.store;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.joda.money.Money;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter(AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class Fruit {
	
	public enum Unit {
		KG,
		HALF_KG
	}
	
	public static class Farm {
		public static List<? extends Fruit> provideFruits(Class<? extends Fruit> fruitCls, Integer quantity) {
//			if (Fruit.class.isAssignableFrom(fruitClass) == false) {
//				throw new RuntimeException("Cannot provide fruit of class: " + fruitClass.getName());
//			}
			return Stream.generate(() -> {
				try {
					return fruitCls.newInstance();
				} catch (InstantiationException | IllegalAccessException e) {
					e.printStackTrace();
				}
				return null;
			})
					.limit(quantity)
					.filter(fruit -> fruit != null)
					.collect(Collectors.toList());
		}
	}
	
	protected Money pricePerUnit;
	
	protected Unit unit;
	
	public String name() {
		return this.getClass().getSimpleName();
	}
}
