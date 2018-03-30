package com.jsonar.datadiscovery.controller;

public class ConnectionPoolException extends Exception {

	private static final long serialVersionUID = -1463424868054521117L;
	
	public ConnectionPoolException(String message) {
		super(message);
	}
	
	public ConnectionPoolException(String message, Throwable throwable) {
		super(message, throwable);
	}

}
