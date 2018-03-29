package com.jsonar.datadiscovery.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.jsonar.datadiscovery.model.Address;
import com.jsonar.datadiscovery.model.Contact;
import com.jsonar.datadiscovery.model.Customer;

public class CustomerDataAccessObjet extends DataAccessObject<Customer> {

	private final Connection connection;

	public CustomerDataAccessObjet(Connection connection) throws SQLException {
		super(Customer.class);
		this.connection = connection;
	}

	public int getTotalCustomers(Map<String, Object> filter) throws SQLException {

		LinkedHashMap<String, Object> filterValues = new LinkedHashMap<>(filter == null ? Collections.emptyMap() : filter);
		List<String> fieldParams = new ArrayList<>(filterValues.keySet());
		List<Object> valueParams = new ArrayList<>(filterValues.values());
		
		PreparedStatement statement = createStatement(
				"count(*)", 
				"customers", 
				createQueryFilter(Customer.class, null, fieldParams), 
				null, null, valueParams);
		
		ResultSet resultSet = statement.executeQuery();
		try {

			if (resultSet.next()) {
				return resultSet.getInt(1);
			}
			return 0;

		} finally {
			resultSet.close();
			statement.close();
		}

	}

	public List<Customer> getCustomers(int first, int pageSize, Map<String, Object> filter) throws SQLException {
		
		LinkedHashMap<String, Object> filterValues = new LinkedHashMap<>(filter == null ? Collections.emptyMap() : filter);
		List<String> fieldParams = new ArrayList<>(filterValues.keySet());
		List<Object> valueParams = new ArrayList<>(filterValues.values());
		
		valueParams.add(first);
		valueParams.add(pageSize);

		PreparedStatement statement = createStatement(
				createQueryProjection(Customer.class, "c"), 
				"customers c",  
				createQueryFilter(Customer.class, "c", fieldParams), 
				"order by c.customerName", 
				"limit ?, ?", 
				valueParams);
		
		ResultSet resultSet = statement.executeQuery();
		List<Customer> customers = new ArrayList<>();

		try {

			while (resultSet.next()) {
				customers.add(createCustomer(resultSet));
			}

			return customers;

		} finally {
			resultSet.close();
			statement.close();
		}

	}

	private PreparedStatement createStatement(String projection, String from, String filter, String orderBy, String limit, List<Object> paramValues) throws SQLException {

		StringBuilder query = new StringBuilder();

		query.append("select ").append(projection);
		query.append(" from ").append(from);
		
		if (!StringUtils.isEmpty(filter)) {
			query.append(" where ").append(filter);
		}

		if (!StringUtils.isEmpty(orderBy)) {
			query.append(" ").append(orderBy);
		}

		if (!StringUtils.isEmpty(limit)) {
			query.append(" ").append(limit);
		}
		
		PreparedStatement statement = connection.prepareStatement(query.toString());
		
		if (!paramValues.isEmpty()) {
			int paramIndex = 1;
			for (Object value : paramValues) {
				statement.setObject(paramIndex, value);
				paramIndex++;
			}
		}
		
		return statement;
	}

	private Customer createCustomer(ResultSet result) throws SQLException {
		Customer customer = new Customer(result.getLong("customerNumber"));

		Address address = new Address();
		Contact contact = new Contact();
		customer.setAddress(address);
		customer.setContact(contact);
		
		setResult(customer, "c", result);
		setResult(address, "c", result);
		setResult(contact, "c", result);

		return customer;
	}

}
