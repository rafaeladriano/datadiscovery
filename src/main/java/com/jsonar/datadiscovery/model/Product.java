package com.jsonar.datadiscovery.model;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.Data;

@Data
public class Product implements Serializable {
	
	private static final long serialVersionUID = 7254667108592416844L;

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
