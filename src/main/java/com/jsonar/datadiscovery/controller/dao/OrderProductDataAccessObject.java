package com.jsonar.datadiscovery.controller.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import com.jsonar.datadiscovery.model.Customer;
import com.jsonar.datadiscovery.model.Order;
import com.jsonar.datadiscovery.model.Product;
import com.jsonar.datadiscovery.model.ProductCategory;
import com.jsonar.datadiscovery.model.ProductOrder;

public class OrderProductDataAccessObject extends DataAccessObject<Order> {

	private final PreparedStatement allOrdersByCustomer;
	
	public OrderProductDataAccessObject(Connection connection) throws SQLException {
		super(Order.class);
		
		StringBuilder projection = new StringBuilder();
		projection.append(createQueryProjection(Order.class, "o")).append(", ");
		projection.append(createQueryProjection(ProductOrder.class, "d")).append(", ");
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
		return null;
	}
	
	private Order createOrder(ResultSet result) throws SQLException {
		Order order = new Order(result.getLong("o." + getColumn("number")));
		
		setResult(order, "o", result);
		
		return order;
	}
	
	
}
