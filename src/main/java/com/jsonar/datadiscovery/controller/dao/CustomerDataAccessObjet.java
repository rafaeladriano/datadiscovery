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

	private final PreparedStatement selectTotal;
	private final PreparedStatement selectAllByPage;

	public CustomerDataAccessObjet(Connection connection) throws SQLException {
		selectAllByPage = connection.prepareStatement("select * from customers order by customerName limit ?, ?");
		selectTotal = connection.prepareStatement("select count(*) from customers");
	}

	public int getTotalCustomers() throws SQLException {

		ResultSet resultSet = selectTotal.executeQuery();
		try {
			
			if (resultSet.next()) {
				return resultSet.getInt(1);
			}
			return 0;
			
		} finally {
			resultSet.close();
		}

	}

	public List<Customer> getCustomers(int first, int pageSize) throws SQLException {

		selectAllByPage.setInt(1, first);
		selectAllByPage.setInt(2, pageSize);

		ResultSet resultSet = selectAllByPage.executeQuery();

		List<Customer> customers = new ArrayList<>();

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
