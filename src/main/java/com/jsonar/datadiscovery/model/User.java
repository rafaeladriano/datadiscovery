package com.jsonar.datadiscovery.model;

import java.io.Serializable;

import lombok.Data;

@Data
public class User implements Serializable {
	
	private static final long serialVersionUID = -2532583780069221156L;

	private String username;
	
	private String password;
	
}
