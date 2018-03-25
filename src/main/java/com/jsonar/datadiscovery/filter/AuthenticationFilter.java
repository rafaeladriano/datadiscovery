package com.jsonar.datadiscovery.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jsonar.datadiscovery.controller.AuthenticationController;

public class AuthenticationFilter implements Filter {
	
	private static final String AUTHENTICATION_PAGE_PATH = "/authentication.xhtml";

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {

		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;

		AuthenticationController authenticationController = (AuthenticationController) httpRequest.getSession().getAttribute("authenticationController");

		if (authenticationController != null) {
			
			if (authenticationController.isAuthenticated()) {
				filterChain.doFilter(request, response);
			} else {
				httpResponse.sendRedirect(httpRequest.getContextPath() + AUTHENTICATION_PAGE_PATH);
			}
			
		} else {
			httpResponse.sendRedirect(httpRequest.getContextPath() + AUTHENTICATION_PAGE_PATH);
		}

	}

	@Override
	public void destroy() {

	}

}
