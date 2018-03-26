package com.jsonar.datadiscovery.model;

import java.io.Serializable;
import java.math.BigDecimal;

import com.jsonar.datadiscovery.configuration.Column;
import com.jsonar.datadiscovery.configuration.Table;

import lombok.Data;

@Data
@Table
public class ProductOrder implements Serializable {
	
	private static final long serialVersionUID = 7822205947570726040L;

	private Product product;
	
	@Column("quantityOrdered")
	private int quantity;
	
	@Column("priceEach")
	private BigDecimal productPrice;
	
}
