package com.jsonar.datarecovery.controller.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.jsonar.datadiscovery.controller.ConnectionPoolException;
import com.jsonar.datadiscovery.dao.ConnectionPool;
import com.jsonar.datadiscovery.dao.CustomerDataAccessObjet;
import com.jsonar.datadiscovery.model.Address;
import com.jsonar.datadiscovery.model.Contact;
import com.jsonar.datadiscovery.model.Customer;

public class CustomerDataAccessObjectTest {
	
	private Connection connection;
	
	@Before
	public void beforeTest() throws ConnectionPoolException {
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
		
		Customer expectedCustomer = createCustomer(
				242, 
				"Alpha Cognac", 
				"61100.00",
				createContact("Annette ", "Roulet", "61.77.6555"),
				createAddress("1 rue Alsace-Lorraine", null, "31000", "Toulouse", null, "France")
				);
		
		assertCustomer(expectedCustomer, customer);
	}
	
	@Test
	public void testSelectLast() throws SQLException {
		CustomerDataAccessObjet customerDAO = new CustomerDataAccessObjet(connection);
		List<Customer> customers = customerDAO.getCustomers(121, 1, null);
		
		Customer customer = customers.get(customers.size() - 1);
		
		Customer expectedCustomer = createCustomer(
				475, 
				"West Coast Collectables Co.", 
				"55400.00",
				createContact("Steve", "Thompson", "3105553722"),
				createAddress("3675 Furth Circle", null, "94019", "Burbank", "CA", "USA")
				);
		
		assertCustomer(expectedCustomer, customer);
		
	}
	
	@Test
	public void testSelectTwoFieldsFilter() throws SQLException {
		
		HashMap<String, Object> filter = new HashMap<>();
		filter.put("name", "%Coast%");
		filter.put("creditLimit", new BigDecimal("55400.00"));
		
		CustomerDataAccessObjet customerDAO = new CustomerDataAccessObjet(connection);
		List<Customer> customers = customerDAO.getCustomers(0, 10, filter);
		
		assertEquals(1, customers.size());
		
		for (Customer customer : customers) {
			assertTrue(customer.getName().contains("Coast"));
			assertEquals(new BigDecimal("55400.00"), customer.getCreditLimit());
		}
		
	}
	
	@Test
	public void testSelectNumberFilter() throws SQLException {
		
		HashMap<String, Object> filter = new HashMap<>();
		filter.put("number", 242);
		
		CustomerDataAccessObjet customerDAO = new CustomerDataAccessObjet(connection);
		List<Customer> customers = customerDAO.getCustomers(0, 5, filter);
		
		assertEquals(1, customers.size());
		
		for (Customer customer : customers) {
			assertEquals(242L, customer.getNumber());
		}
		
	}
	
	@Test
	public void testSelectStringFilter() throws SQLException {
		
		HashMap<String, Object> filter = new HashMap<>();
		filter.put("country", "%Fra%");
		
		CustomerDataAccessObjet customerDAO = new CustomerDataAccessObjet(connection);
		List<Customer> customers = customerDAO.getCustomers(0, 10, filter);
		
		assertEquals(10, customers.size());
		
		for (Customer customer : customers) {
			assertTrue(customer.getAddress().getCountry().contains("Fra"));
		}
		
	}
	
	@Test
	public void testTotalCustomers() throws SQLException {
		CustomerDataAccessObjet customerDAO = new CustomerDataAccessObjet(connection);
		int totalCustomers = customerDAO.getTotalCustomers(null);
		
		assertEquals(122, totalCustomers);
	}

	@Test
	public void testTotalCustomersFiltered() throws SQLException {
		
		HashMap<String, Object> filter = new HashMap<>();
		filter.put("country", "France");
		
		CustomerDataAccessObjet customerDAO = new CustomerDataAccessObjet(connection);
		int totalCustomers = customerDAO.getTotalCustomers(filter);
		
		assertEquals(12, totalCustomers);
	}
	
	private Customer createCustomer(long number, String name, String creditLimit, Contact expectedContact, Address expectedAddress) {
		Customer expectedCustomer = new Customer(number);
		expectedCustomer.setName(name);
		expectedCustomer.setCreditLimit(new BigDecimal(creditLimit));
		expectedCustomer.setContact(expectedContact);
		expectedCustomer.setAddress(expectedAddress);
		return expectedCustomer;
	}

	private Contact createContact(String firstName, String lastName, String phone) {
		Contact expectedContact = new Contact();
		expectedContact.setFirstName(firstName);
		expectedContact.setLastName(lastName);
		expectedContact.setPhone(phone);
		return expectedContact;
	}

	private Address createAddress(String address1, String address2, String postalCode, String city, String state, String country) {
		Address expectedAddress = new Address();
		expectedAddress.setAddressLine1(address1);
		expectedAddress.setAddressLine2(address2);
		expectedAddress.setPostalCode(postalCode);
		expectedAddress.setCity(city);
		expectedAddress.setState(state);
		expectedAddress.setCountry(country);
		return expectedAddress;
	}
	
	private void assertCustomer(Customer expected, Customer actual) {
		
		assertEquals(expected.getNumber(), actual.getNumber());
		assertEquals(expected.getName(), actual.getName());
		assertEquals(expected.getCreditLimit(), actual.getCreditLimit());
		
		Contact actualContact = actual.getContact();
		Contact expectedContact = expected.getContact();
		
		assertEquals(expectedContact.getFirstName(), actualContact.getFirstName());
		assertEquals(expectedContact.getLastName(), actualContact.getLastName());
		assertEquals(expectedContact.getPhone(), actualContact.getPhone());
		
		Address actualAddress = actual.getAddress();
		Address expectedAddress = expected.getAddress();
		
		assertEquals(expectedAddress.getAddressLine2(), actualAddress.getAddressLine2());
		assertEquals(expectedAddress.getAddressLine1(), actualAddress.getAddressLine1());
		assertEquals(expectedAddress.getPostalCode(), actualAddress.getPostalCode());
		assertEquals(expectedAddress.getCity(), actualAddress.getCity());
		assertEquals(expectedAddress.getState(), actualAddress.getState());
		assertEquals(expectedAddress.getCountry(), actualAddress.getCountry());
		
	}
	
}
