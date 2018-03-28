package com.jsonar.datadiscovery.controller;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import com.jsonar.datadiscovery.dao.ConnectionPool;
import com.jsonar.datadiscovery.dao.CustomerDataAccessObjet;
import com.jsonar.datadiscovery.model.Customer;

public class CustomerLazyDataModel extends LazyDataModel<Customer> {

	private static final long serialVersionUID = 4872852190639740259L;

	private List<Customer> customers = new ArrayList<>();
	
	@Override
	public Customer getRowData(String rowKey) {

		for (Customer customer : customers) {
			if (customer.getNumber() == Long.parseLong(rowKey)) {
				return customer;
			}
		}

		return null;
	}

	@Override
	public Object getRowKey(Customer customer) {
		return customer.getNumber();
	}

	@Override
	public List<Customer> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {

		Connection connection = ConnectionPool.getConnection();
		
		try {
			
			CustomerDataAccessObjet customerDataAccess = new CustomerDataAccessObjet(connection);
			setRowCount(customerDataAccess.getTotalCustomers(filters));
			customers = customerDataAccess.getCustomers(first, pageSize, filters);
			return customers;
			
		} catch (SQLException e) {
			// TODO tratar excessao
			throw new RuntimeException(e);
		} finally {
			ConnectionPool.returnConnection(connection);
		}
	}
}
