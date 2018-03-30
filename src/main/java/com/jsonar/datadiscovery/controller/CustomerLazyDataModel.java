package com.jsonar.datadiscovery.controller;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

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

		Connection connection;
		try {
			connection = ConnectionPool.getConnection();

			try {

				Map<String, Object> handledFilters = new HashMap<String, Object>();

				for (Iterator<Entry<String, Object>> it = filters.entrySet().iterator(); it.hasNext();) {

					Entry<String, Object> fieldValue = it.next();
					String field = fieldValue.getKey();
					Object value = fieldValue.getValue();

					String[] scopes = field.split("\\.");
					if (scopes.length > 1) {
						field = scopes[scopes.length - 1];
					}

					handledFilters.put(field, value);
				}

				CustomerDataAccessObjet customerDataAccess = new CustomerDataAccessObjet(connection);
				setRowCount(customerDataAccess.getTotalCustomers(handledFilters));
				customers = customerDataAccess.getCustomers(first, pageSize, handledFilters);

			} catch (SQLException e) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Customer filter failed", null));
			} finally {
				ConnectionPool.returnConnection(connection);
			}
			
		} catch (ConnectionPoolException e1) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, e1.getMessage(), null));
		}
		
		return customers;
	}
}
