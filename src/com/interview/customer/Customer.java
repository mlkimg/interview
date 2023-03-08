package com.interview.customer;

import java.util.List;

import org.joda.money.Money;

import com.interview.Config;
import com.interview.store.Store;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Customer {
	
	private String name;
	
	public Customer(String name) {
		this.name = name;
	}
	
	public Money checkoutTotalPrice(List<PurchaseOrder> poList, Store store) {
		return store.calcTotalPrice(poList);
	}
	
}
