package com.jsonar.datadiscovery.controller;

import java.io.Serializable;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import lombok.Getter;
import lombok.Setter;

@ManagedBean
@SessionScoped
public class AuthenticationController implements Serializable {

	private static final long serialVersionUID = 1370127878912965125L;

	private static final String DATADISCOVERY_PAGE_PATH = "/secured/datadiscovery.xhtml?faces-redirect=true";

	@Setter
	@ManagedProperty("#{authenticationService}")
	private AuthenticationService authenticationService;

	@Getter
	private boolean isAuthenticated;

	@Getter
	@Setter
	private String username;

	@Getter
	@Setter
	private String password;

	public String authenticate() {

		isAuthenticated = authenticationService.authenticate(username, password);
		
		if (isAuthenticated) {
			return DATADISCOVERY_PAGE_PATH;
		}

		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Authentication failed", "Invalid credentials."));
		
		return null;
	}

	public String attemptRedirect() {

		if (isAuthenticated()) {
			return DATADISCOVERY_PAGE_PATH;
		}

		return null;
	}

}
