package com.jsonar.datadiscovery.controller.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jsonar.datadiscovery.model.Customer;
import com.jsonar.datadiscovery.model.Order;
import com.jsonar.datadiscovery.model.Product;
import com.jsonar.datadiscovery.model.ProductCategory;
import com.jsonar.datadiscovery.model.OrderDetail;

public class OrderDataAccessObject extends DataAccessObject<Order> {

	private final PreparedStatement allOrdersByCustomer;
	
	public OrderDataAccessObject(Connection connection) throws SQLException {
		super(Order.class);
		
		StringBuilder projection = new StringBuilder();
		projection.append(createQueryProjection(Order.class, "o")).append(", ");
		projection.append(createQueryProjection(OrderDetail.class, "d")).append(", ");
		projection.append(createQueryProjection(Product.class, "p")).append(", ");
		projection.append(createQueryProjection(ProductCategory.class, "c")).append(" ");
		
		allOrdersByCustomer = connection.prepareStatement("select " + projection.toString() +
				"from orders o " + 
				"inner join orderdetails d on o.orderNumber = d.orderNumber " + 
				"inner join products p on d.productCode = p.productCode " + 
				"inner join productlines c on p.productLine = c.productLine " + 
				"where o.customerNumber = ? " + 
				"order by o.orderDate, d.orderLineNumber");
	}
	
	public List<Order> getOrders(Customer customer) throws SQLException {
		
		allOrdersByCustomer.setLong(1, customer.getNumber());

		Map<Long, Order> fetchedOrders = new HashMap<>();
		
		List<Order> orders = new ArrayList<>();
		ResultSet resultSet = allOrdersByCustomer.executeQuery();
		try {
			while (resultSet.next()) {
				Order order = createOrder(fetchedOrders, resultSet);
				if (!orders.contains(order)) {
					orders.add(order);
				}
			}
		} finally {
			resultSet.close();
		}

		return orders;
	}
	
	private Order createOrder(Map<Long, Order> fetchedOrders, ResultSet result) throws SQLException {
		
		long orderNumber = result.getLong("o." + getColumn("number"));
		Order order = fetchedOrders.get(orderNumber);
		if (order == null) {
			order = new Order(orderNumber);
			setResult(order, "o", result);
			fetchedOrders.put(orderNumber, order);
		}

		order.getDetails().add(createOrderDetail(result));
		
		return order;
	}
	
	private OrderDetail createOrderDetail(ResultSet result) throws SQLException {
		OrderDetail orderDetail = new OrderDetail();
		setResult(orderDetail, "d", result);
		
		Product product = new Product(result.getString("p." + getColumn(Product.class, "code")));
		setResult(product, "p", result);
		
		ProductCategory productCategory = new ProductCategory(result.getString("c." + getColumn(ProductCategory.class, "name")));
		setResult(productCategory, "c", result);

		product.setCategory(productCategory);
		orderDetail.setProduct(product);
		
		return orderDetail;
	}
	
	
}
