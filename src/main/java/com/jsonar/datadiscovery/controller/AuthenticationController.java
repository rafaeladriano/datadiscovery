package com.jsonar.datadiscovery.controller;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import com.jsonar.datadiscovery.model.User;

@ManagedBean
@SessionScoped
public class AuthenticationController implements Serializable {
	
	private static final long serialVersionUID = 1370127878912965125L;

	private static final String DATADISCOVERY_PAGE_PATH = "/secured/datadiscovery.xhtml?faces-redirect=true";

	private User user;
	
	private String username;
	private String password;
	
	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}

	public User getUser() {
		return user;
	}

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
