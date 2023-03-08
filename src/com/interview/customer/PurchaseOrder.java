package com.interview.customer;

import com.interview.store.Fruit;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PurchaseOrder {
	private Class<? extends Fruit> fruitCls;
	private Integer quantity;
}
