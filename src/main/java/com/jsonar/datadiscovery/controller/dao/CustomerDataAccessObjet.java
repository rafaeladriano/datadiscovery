package com.jsonar.datadiscovery.controller.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

		PreparedStatement statement = createStatement("select " + createQueryProjection(Customer.class, "c") + " from customers c", "order by c.customerName", "limit ?, ?", filter);
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
				query.append(getColumn(field));
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
		Contact contact = new Contact();
		customer.setAddress(address);
		customer.setContact(contact);
		
		setResult(customer, "c", result);
		setResult(address, "c", result);
		setResult(contact, "c", result);

		return customer;
	}

}
