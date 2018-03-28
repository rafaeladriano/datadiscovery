package com.jsonar.datadiscovery.model;

import java.io.Serializable;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(exclude = { "password" })
public class User implements Serializable {

	private static final long serialVersionUID = -2532583780069221156L;

	private String username;

	private String password;

}
