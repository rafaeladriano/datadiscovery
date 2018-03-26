package com.jsonar.datadiscovery.model;

import java.io.Serializable;

import lombok.Data;

@Data
public class ProductCategory implements Serializable {

	private static final long serialVersionUID = 1860433271358290250L;

	private final String line;
	private String textDescription;
	private String htmlDescription;
	private byte[] image;
	
}
