package com.jsonar.datadiscovery.model;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.Data;

@Data
public class Customer implements Serializable {
	
	private static final long serialVersionUID = 5767498664874792191L;

	private final long number;
	
	private String name;

	private Address address;
	
	private Contact contact;
	
	private BigDecimal creditLimit;

}
