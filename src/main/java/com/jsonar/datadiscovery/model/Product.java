package com.jsonar.datadiscovery.model;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class Product {
	
	private final String code;
	private String name;
	private ProductCategory category;
	private String scale;
	private String vendor;
	private String description;
	private int quantityInStock;
	private BigDecimal price;
	private BigDecimal msrp;

}
