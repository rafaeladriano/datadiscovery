package com.jsonar.datadiscovery.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.NoSuchElementException;

import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

import com.jsonar.datadiscovery.configuration.Property;

public class ConnectionPool extends GenericObjectPool<Connection> {

	private static volatile ConnectionPool connectionPool;

	private ConnectionPool() {

		super(new ConnectionPoolFactory());

		GenericObjectPoolConfig objectPoolConfiguration = new GenericObjectPoolConfig();
		objectPoolConfiguration.setMaxTotal(100);
		objectPoolConfiguration.setBlockWhenExhausted(true);
		objectPoolConfiguration.setMinIdle(5);
		objectPoolConfiguration.setMaxIdle(20);
		objectPoolConfiguration.setMaxWaitMillis(30 * 1000);
		objectPoolConfiguration.setTestOnBorrow(true);
		objectPoolConfiguration.setTestWhileIdle(true);
		objectPoolConfiguration.setTimeBetweenEvictionRunsMillis(300 * 1000);

		setConfig(objectPoolConfiguration);
	}

	public static Connection getConnection() {

		loadConnectionPool();

		try {
			return connectionPool.borrowObject();
			// TODO tratar excecoes
		} catch (NoSuchElementException e) {
			throw new RuntimeException(e);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public static void returnConnection(Connection connection) {
		connectionPool.returnObject(connection);
	}
	
	private static void loadConnectionPool() {
		
		if (connectionPool == null) {
			synchronized (ConnectionPool.class) {
				if (connectionPool == null) {
					connectionPool = new ConnectionPool();
				}
			}
		}
		
	}

	public static class ConnectionPoolFactory implements PooledObjectFactory<Connection> {

		@Override
		public PooledObject<Connection> makeObject() throws Exception {
			Class.forName("com.mysql.jdbc.Driver");
			return new DefaultPooledObject<Connection>(DriverManager.getConnection(Property.DATABASE_URL.get(), Property.DATABASE_USER.get(), Property.DATABASE_PASSWORD.get()));
		}

		@Override
		public void destroyObject(PooledObject<Connection> p) throws Exception {
			p.getObject().close();
		}

		@Override
		public boolean validateObject(PooledObject<Connection> p) {
			try {
				Connection connection = p.getObject();
				return !connection.isClosed() && connection.isValid(5);
			} catch (final SQLException e) {
				return false;
			}
		}

		@Override
		public void activateObject(PooledObject<Connection> p) throws Exception {

		}

		@Override
		public void passivateObject(PooledObject<Connection> p) throws Exception {

		}

	}

}
