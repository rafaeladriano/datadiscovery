package com.jsonar.datarecovery.controller.dao;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.sql.SQLException;

import org.junit.Test;

import com.jsonar.datadiscovery.controller.AuthenticationService;

public class AuthenticationServiceTest {

	@Test
	public void testSuccessAuthentication01() throws SQLException {
		AuthenticationService authenticationService = new AuthenticationService();
		assertTrue(authenticationService.authenticate("Test1", "test1@mytest.com"));
	}
	
	@Test
	public void testSuccessAuthentication02() throws SQLException {
		AuthenticationService authenticationService = new AuthenticationService();
		assertTrue(authenticationService.authenticate("Test2", "test2@mytest.com"));
	}
	
	@Test
	public void testWrongPasswordAuthentication() throws SQLException {
		AuthenticationService authenticationService = new AuthenticationService();
		assertFalse(authenticationService.authenticate("Test1", "12345"));
	}

	@Test
	public void testWrongUsernameAuthentication() throws SQLException {
		AuthenticationService authenticationService = new AuthenticationService();
		assertFalse(authenticationService.authenticate("Test1", null));
	}
	
	@Test
	public void testEmptyParamsAuthentication() throws SQLException {
		AuthenticationService authenticationService = new AuthenticationService();
		assertFalse(authenticationService.authenticate(null, null));
	}
}
