package com.jsonar.datadiscovery.model;

import lombok.Data;

@Data
public class ProductCategory {

	private final String line;
	private String textDescription;
	private String htmlDescription;
	private byte[] image;
	
}
