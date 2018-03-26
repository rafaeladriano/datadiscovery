package com.jsonar.datadiscovery.model;

import java.io.Serializable;

import com.jsonar.datadiscovery.configuration.Column;
import com.jsonar.datadiscovery.configuration.TableExtention;

import lombok.Data;

@Data
@TableExtention
public class Address implements Serializable {
	
	private static final long serialVersionUID = -5074931604355572269L;

	@Column
	private String addressLine1;
	
	@Column
	private String addressLine2;
	
	@Column
	private String postalCode;
	
	@Column
	private String city;
	
	@Column
	private String state;
	
	@Column
	private String country;

}
