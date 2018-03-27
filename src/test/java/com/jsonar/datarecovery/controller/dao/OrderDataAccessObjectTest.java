package com.jsonar.datarecovery.controller.dao;

import static org.junit.Assert.assertEquals;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.jsonar.datadiscovery.controller.dao.ConnectionPool;
import com.jsonar.datadiscovery.controller.dao.OrderDataAccessObject;
import com.jsonar.datadiscovery.model.Customer;
import com.jsonar.datadiscovery.model.Order;

public class OrderDataAccessObjectTest {

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
	public void testSelectOrderByCustomer() throws SQLException {
		Customer customer = new Customer(103);

		OrderDataAccessObject orderDAO = new OrderDataAccessObject(connection);
		List<Order> orders = orderDAO.getOrders(customer);

		assertEquals(3, orders.size());

		Order order = orders.get(0);

		assertEquals(10123, order.getNumber());
		assertEquals(4, order.getDetails().size());

		order = orders.get(1);

		assertEquals(10298, order.getNumber());
		assertEquals(2, order.getDetails().size());

		order = orders.get(2);

		assertEquals(10345, order.getNumber());
		assertEquals(1, order.getDetails().size());
	}
}
