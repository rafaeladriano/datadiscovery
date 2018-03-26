package com.jsonar.datadiscovery.model;

import java.io.Serializable;

import lombok.Data;

@Data
public class Address implements Serializable {
	
	private static final long serialVersionUID = -5074931604355572269L;

	private String addressLine1;
	private String addressLine2;
	private String postalCode;
	private String city;
	private String state;
	private String country;

}
