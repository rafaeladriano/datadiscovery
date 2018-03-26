package com.jsonar.datadiscovery.controller.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jsonar.datadiscovery.model.Address;
import com.jsonar.datadiscovery.model.Contact;
import com.jsonar.datadiscovery.model.Customer;

public class CustomerDataAccessObjet {

	private final Connection connection;

	private Map<String, String> columns;

	public CustomerDataAccessObjet(Connection connection) throws SQLException {
		this.connection = connection;

		columns = new HashMap<>();

		columns.put("number", "customerNumber");
		columns.put("name", "customerName");
		columns.put("creditLimit", "creditLimit");
		columns.put("addressLine1", "addressLine1");
		columns.put("addressLine2", "addressLine2");
		columns.put("postalCode", "postalCode");
		columns.put("city", "city");
		columns.put("state", "state");
		columns.put("country", "country");
		columns.put("firstName", "contactFirstName");
		columns.put("lastName", "contactLastName");
		columns.put("phone", "phone");

	}

	public int getTotalCustomers(Map<String, Object> filter) throws SQLException {

		PreparedStatement statement = createStatement("select count(*) from customers", null, null, filter);
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

		PreparedStatement statement = createStatement("select * from customers", "order by customerName", "limit ?, ?", filter);
		statement.setInt(filter == null ? 1 : filter.size() + 1, first);
		statement.setInt(filter == null ? 2 : filter.size() + 2, pageSize);

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

	private PreparedStatement createStatement(String baseQuery, String orderBy, String limit, Map<String, Object> filter) throws SQLException {

		StringBuilder query = new StringBuilder(baseQuery);

		List<String> filterFields = new ArrayList<>();
		
		if (filter != null) {
			filterFields.addAll(filter.keySet());
		}

		if (!filterFields.isEmpty()) {
			query.append(" where ");
			for (String field : filterFields) {
				query.append(columns.get(field));
				if (filter.get(field) instanceof String) {
					query.append(" like ?");
				} else {
					query.append(" = ?");
				}
				query.append(" and ");
			}
			query.setLength(query.length() - " and ".length());
		}

		if (orderBy != null) {
			query.append(" ").append(orderBy);
		}

		if (limit != null) {
			query.append(" ").append(limit);
		}
		
		int paramIndex = 1;
		PreparedStatement statement = connection.prepareStatement(query.toString());
		for (String field : filterFields) {
			statement.setObject(paramIndex, filter.get(field));
			paramIndex++;
		}

		return statement;
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
