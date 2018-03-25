package com.jsonar.datadiscovery.controller;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import com.jsonar.datadiscovery.model.User;

import lombok.Getter;
import lombok.Setter;

@ManagedBean
@SessionScoped
public class AuthenticationController implements Serializable {
	
	private static final long serialVersionUID = 1370127878912965125L;

	private static final String DATADISCOVERY_PAGE_PATH = "/secured/datadiscovery.xhtml?faces-redirect=true";

	private User user;
	
	@Getter
	@Setter
	private String username;
	
	@Getter
	@Setter
	private String password;

	public String authenticate() {
		user = new User();
		user.setUsername(username);
		user.setPassword(password);
		return DATADISCOVERY_PAGE_PATH;
	}

	public boolean isAuthenticated() {
		return user != null;
	}

	public String attemptRedirect() {
		
		if (isAuthenticated()) {
			return DATADISCOVERY_PAGE_PATH;
		}

		return null;
	}
	
}
