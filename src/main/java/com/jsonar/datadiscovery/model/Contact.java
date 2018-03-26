package com.jsonar.datadiscovery.model;

import java.io.Serializable;

import com.jsonar.datadiscovery.configuration.Column;
import com.jsonar.datadiscovery.configuration.TableExtention;

import lombok.Data;

@Data
@TableExtention
public class Contact implements Serializable {
	
	private static final long serialVersionUID = 1579457200371004098L;

	@Column("contactFirstName")
	private String firstName;
	
	@Column("contactLastName")
	private String lastName;
	
	@Column
	private String phone;

}
