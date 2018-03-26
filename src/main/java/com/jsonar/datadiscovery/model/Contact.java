package com.jsonar.datadiscovery.model;

import java.io.Serializable;

import lombok.Data;

@Data
public class Contact implements Serializable {
	
	private static final long serialVersionUID = 1579457200371004098L;

	private String firstName;
	private String lastName;
	private String phone;

}
