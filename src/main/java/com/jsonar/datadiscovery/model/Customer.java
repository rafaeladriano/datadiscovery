package com.jsonar.datadiscovery.model;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class Customer {
	
	private final long number;
	
	private String name;

	private Address address;
	
	private Contact contact;
	
	private BigDecimal creditLimit;

}
