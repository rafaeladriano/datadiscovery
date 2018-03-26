package com.jsonar.datadiscovery.model;

import java.io.Serializable;
import java.math.BigDecimal;

import com.jsonar.datadiscovery.configuration.Column;
import com.jsonar.datadiscovery.configuration.Table;

import lombok.Data;

@Data
@Table
public class Product implements Serializable {
	
	private static final long serialVersionUID = 7254667108592416844L;

	@Column("productCode")
	private final String code;
	
	@Column("productName")
	private String name;
	
	private ProductCategory category;
	
	@Column("productScale")
	private String scale;
	
	@Column("productVendor")
	private String vendor;
	
	@Column("productDescription")
	private String description;
	
	@Column("quantityInStock")
	private int quantityInStock;
	
	@Column("buyPrice")
	private BigDecimal price;
	
	@Column("MSRP")
	private BigDecimal msrp;

}
