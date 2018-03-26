package com.jsonar.datadiscovery.model;

import java.io.Serializable;
import java.math.BigDecimal;

import com.jsonar.datadiscovery.configuration.Column;
import com.jsonar.datadiscovery.configuration.Table;

import lombok.Data;

@Data
@Table
public class Customer implements Serializable {
	
	private static final long serialVersionUID = 5767498664874792191L;

	@Column("customerNumber")
	private final long number;
	
	@Column("customerName")
	private String name;

	@Column
	private BigDecimal creditLimit;

	private Address address;
	
	private Contact contact;


}
