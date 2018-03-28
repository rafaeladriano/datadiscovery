package com.jsonar.datadiscovery.controller;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.primefaces.event.SelectEvent;

import com.jsonar.datadiscovery.dao.ConnectionPool;
import com.jsonar.datadiscovery.dao.OrderDataAccessObject;
import com.jsonar.datadiscovery.model.Customer;
import com.jsonar.datadiscovery.model.Order;
import com.jsonar.datadiscovery.model.OrderDetail;

import lombok.Getter;
import lombok.Setter;

@ManagedBean
@ViewScoped
public class DataDiscoveryController implements Serializable {

	private static final long serialVersionUID = -5915670661765139271L;

	@Getter
	private CustomerLazyDataModel lazyDataModel;

	@Getter
	@Setter
	private List<Order> orders = new ArrayList<>();

	@Getter
	@Setter
	private List<OrderDetail> orderDetails = new ArrayList<>();
	
	@PostConstruct
	public void initialize() {
		lazyDataModel = new CustomerLazyDataModel();
	}

	public void onCustomerSelect(SelectEvent event) {
		Customer customer = (Customer) event.getObject();

		Connection connection = ConnectionPool.getConnection();
		try {
			OrderDataAccessObject orderDataAccess = new OrderDataAccessObject(connection);
			orders = orderDataAccess.getOrders(customer);
			orderDetails.clear();
		} catch (SQLException e) {
			// TODO tratar excecao
			e.printStackTrace();
		} finally {
			ConnectionPool.returnConnection(connection);
		}
	}

	public void onOrderSelect(SelectEvent event) {
		Order order = (Order) event.getObject();
		orderDetails = order.getDetails();
	}

}
