package com.jsonar.datadiscovery.controller.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.jsonar.datadiscovery.model.Address;
import com.jsonar.datadiscovery.model.Contact;
import com.jsonar.datadiscovery.model.Customer;

public class CustomerDataAccessObjet {
	
	private PreparedStatement selectAll;
	
	public CustomerDataAccessObjet(Connection connection) throws SQLException {
		selectAll = connection.prepareStatement("select * from customers order by customerName");
	}
	
	public List<Customer> getCustomers() throws SQLException {
		List<Customer> customers = new ArrayList<>();
		ResultSet resultSet = selectAll.executeQuery();
		
		try {
			
			while (resultSet.next()) {
				customers.add(createCustomer(resultSet));
			}
			
			return customers;
			
		} finally {
			resultSet.close();
		}
		
	}
	
	private Customer createCustomer(ResultSet result) throws SQLException {
		Customer customer = new Customer(result.getLong("customerNumber"));
		
		Address address = new Address();
		address.setAddressLine1(result.getString("addressLine1"));
		address.setAddressLine2(result.getString("addressLine2"));
		address.setPostalCode(result.getString("postalCode"));
		address.setCity(result.getString("city"));
		address.setState(result.getString("state"));
		address.setCountry(result.getString("country"));
		
		Contact contact = new Contact();
		contact.setFirstName(result.getString("contactFirstName"));
		contact.setLastName(result.getString("contactLastName"));
		contact.setPhone(result.getString("phone"));
		
		customer.setName(result.getString("customerName"));
		customer.setCreditLimit(result.getBigDecimal("creditLimit"));
		customer.setAddress(address);
		customer.setContact(contact);
		
		return customer;
	}
	

}
