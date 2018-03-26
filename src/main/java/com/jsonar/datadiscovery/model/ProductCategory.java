package com.jsonar.datadiscovery.model;

import java.io.Serializable;

import com.jsonar.datadiscovery.configuration.Column;
import com.jsonar.datadiscovery.configuration.Table;

import lombok.Data;

@Data
@Table
public class ProductCategory implements Serializable {

	private static final long serialVersionUID = 1860433271358290250L;

	@Column("productLine")
	private final String name;
	
	@Column("textDescription")
	private String description;
	
}
