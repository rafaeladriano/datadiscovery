package com.jsonar.datarecovery.controller.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.jsonar.datadiscovery.dao.ConnectionPool;
import com.jsonar.datadiscovery.dao.CustomerDataAccessObjet;
import com.jsonar.datadiscovery.model.Customer;

public class CustomerDataAccessObjectTest {
	
	private Connection connection;
	
	@Before
	public void beforeTest() {
		connection = ConnectionPool.getConnection();
	}
	
	@After
	public void afterTest() {
		ConnectionPool.returnConnection(connection);
	}

	@Test
	public void testSelectPage() throws SQLException {
		CustomerDataAccessObjet customerDAO = new CustomerDataAccessObjet(connection);
		List<Customer> customers = customerDAO.getCustomers(0, 10, null);
		assertEquals(10, customers.size());
	}

	@Test
	public void testSelectFirst() throws SQLException {
		CustomerDataAccessObjet customerDAO = new CustomerDataAccessObjet(connection);
		List<Customer> customers = customerDAO.getCustomers(0, 1, null);
		
		Customer customer = customers.get(0);
		
		assertEquals(242, customer.getNumber());
		assertEquals("Alpha Cognac", customer.getName());
		assertEquals(new BigDecimal("61100.00"), customer.getCreditLimit());
		
		assertEquals("Annette ", customer.getContact().getFirstName());
		assertEquals("Roulet", customer.getContact().getLastName());
		assertEquals("61.77.6555", customer.getContact().getPhone());
		
		assertNull(customer.getAddress().getAddressLine2());
		assertNull(customer.getAddress().getState());
		assertEquals("1 rue Alsace-Lorraine", customer.getAddress().getAddressLine1());
		assertEquals("31000", customer.getAddress().getPostalCode());
		assertEquals("Toulouse", customer.getAddress().getCity());
		assertEquals("France", customer.getAddress().getCountry());
		
	}
	
	@Test
	public void testSelectLast() throws SQLException {
		CustomerDataAccessObjet customerDAO = new CustomerDataAccessObjet(connection);
		List<Customer> customers = customerDAO.getCustomers(121, 1, null);
		
		Customer customer = customers.get(customers.size() - 1);
		
		assertEquals(475, customer.getNumber());
		assertEquals("West Coast Collectables Co.", customer.getName());
		assertEquals(new BigDecimal("55400.00"), customer.getCreditLimit());
		
		assertEquals("Steve", customer.getContact().getFirstName());
		assertEquals("Thompson", customer.getContact().getLastName());
		assertEquals("3105553722", customer.getContact().getPhone());
		
		assertNull(customer.getAddress().getAddressLine2());
		assertEquals("3675 Furth Circle", customer.getAddress().getAddressLine1());
		assertEquals("94019", customer.getAddress().getPostalCode());
		assertEquals("Burbank", customer.getAddress().getCity());
		assertEquals("CA", customer.getAddress().getState());
		assertEquals("USA", customer.getAddress().getCountry());
		
	}
	
	@Test
	public void testSelectWithFilter() throws SQLException {
		
		HashMap<String, Object> filter = new HashMap<>();
		filter.put("name", "%Coast%");
		filter.put("creditLimit", new BigDecimal("55400.00"));
		
		CustomerDataAccessObjet customerDAO = new CustomerDataAccessObjet(connection);
		List<Customer> customers = customerDAO.getCustomers(0, 10, filter);
		
		assertEquals(1, customers.size());
		
		Customer customer = customers.get(0);
		
		assertEquals(475, customer.getNumber());
		assertEquals("West Coast Collectables Co.", customer.getName());
		assertEquals(new BigDecimal("55400.00"), customer.getCreditLimit());
		
		assertEquals("Steve", customer.getContact().getFirstName());
		assertEquals("Thompson", customer.getContact().getLastName());
		assertEquals("3105553722", customer.getContact().getPhone());
		
		assertNull(customer.getAddress().getAddressLine2());
		assertEquals("3675 Furth Circle", customer.getAddress().getAddressLine1());
		assertEquals("94019", customer.getAddress().getPostalCode());
		assertEquals("Burbank", customer.getAddress().getCity());
		assertEquals("CA", customer.getAddress().getState());
		assertEquals("USA", customer.getAddress().getCountry());
		
	}
	
}
