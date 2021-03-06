package com.jsonar.datadiscovery.dao;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.NotImplementedException;
import org.apache.commons.lang3.StringUtils;

import com.jsonar.datadiscovery.configuration.Column;
import com.jsonar.datadiscovery.configuration.Table;
import com.jsonar.datadiscovery.configuration.TableExtention;

import lombok.Data;

public abstract class DataAccessObject<Model> {

	private Map<String, TableMapping> tablesMapping = new HashMap<>();
	private Class<Model> baseModel;

	protected DataAccessObject(Class<Model> model) {
		this.baseModel = model;
		loadTable(model);
	}

	private void loadTable(Class<?> model) {

		TableMapping tableMapping = new TableMapping(model.getName());
		tablesMapping.put(model.getName(), tableMapping);

		loadColumns(tableMapping, model);

	}

	private void loadColumns(TableMapping tableMapping, Class<?> model) {

		Field[] fields = model.getDeclaredFields();
		for (Field field : fields) {

			Column columnAnnotation = field.getAnnotation(Column.class);
			if (columnAnnotation != null) {
				String columnName = columnAnnotation.value();
				String fieldName = field.getName();
				if (StringUtils.isEmpty(columnName)) {
					columnName = fieldName;
				}
				tableMapping.getColumns().put(fieldName, new ColumnMapping(fieldName, columnName, field.getType()));

				continue;
			}

			Class<?> referencedModel = field.getType();

			Type genericType = field.getGenericType();
			if (genericType instanceof ParameterizedType) {
				referencedModel = (Class<?>) ((ParameterizedType) genericType).getActualTypeArguments()[0];
			}

			TableExtention tableExtentionAnnotation = referencedModel.getAnnotation(TableExtention.class);
			if (tableExtentionAnnotation != null) {
				tablesMapping.put(referencedModel.getName(), tableMapping);
				loadColumns(tableMapping, referencedModel);
				continue;
			}

			Table tableAnnotation = referencedModel.getAnnotation(Table.class);
			if (tableAnnotation != null) {
				loadTable(referencedModel);
			}

		}

	}

	protected String getColumn(String fieldName) {
		return getColumn(baseModel, fieldName);
	}

	protected String getColumn(Class<?> model, String fieldName) {
		ColumnMapping columnMapping = getColumnMapping(model, fieldName);
		return columnMapping.getColumnName();
	}

	private ColumnMapping getColumnMapping(Class<?> model, String fieldName) {

		TableMapping tableMapping = tablesMapping.get(model.getName());
		if (tableMapping != null) {
			ColumnMapping columnMapping = tableMapping.getColumns().get(fieldName);
			if (columnMapping != null) {
				return columnMapping;
			}
		}

		throw new IllegalArgumentException(String.format("Column not found for the field %s", fieldName));
	}
	
	protected String createQueryFilter(Class<?> model, String alias, List<String> filterFields) {
		
		if (filterFields == null || filterFields.isEmpty()) {
			return null;
		}
		
		final String and = " and ";
		
		StringBuilder filter = new StringBuilder();
		
		for (String fieldName : filterFields) {
			
			if (!StringUtils.isEmpty(alias)) {
				filter.append(alias).append(".");
			}
			
			ColumnMapping columnMapping = getColumnMapping(model, fieldName);
			
			filter.append(columnMapping.getColumnName());
			filter.append(getQueryComparator(columnMapping));
			filter.append("?").append(and);
			
		}
		filter.setLength(filter.length() - and.length());
		
		return filter.toString();
	}

	protected String createQueryProjection(Class<?> model, String alias) {
		final String comma = ", ";
		
		StringBuilder projection = new StringBuilder();

		TableMapping tableMapping = tablesMapping.get(model.getName());
		for (ColumnMapping columnMapping : tableMapping.getColumns().values()) {
			projection.append(alias).append(".").append(columnMapping.columnName).append(comma);
		}
		projection.setLength(projection.length() - comma.length());

		return projection.toString();
	}

	protected void setResult(Object model, String alias, ResultSet result) throws SQLException {

		Field[] fields = model.getClass().getDeclaredFields();
		for (Field field : fields) {

			Column columnAnnotation = field.getAnnotation(Column.class);
			if (columnAnnotation != null) {

				try {
					Method setMethod = model.getClass().getDeclaredMethod("set" + StringUtils.capitalize(field.getName()), field.getType());
					setMethod.invoke(model, getValue(result, getColumnMapping(model.getClass(), field.getName()), alias));
				} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
					// ignore - method not found or method is not public
				}

			}

		}

	}

	private Object getValue(ResultSet result, ColumnMapping columnMapping, String alias) throws SQLException {

		String columnName = alias + "." + columnMapping.getColumnName();

		if (columnMapping.getType().equals(String.class)) {
			return result.getString(columnName);
		}

		if (columnMapping.getType().equals(Date.class)) {
			return new Date(result.getTimestamp(columnName).getTime());
		}

		if (columnMapping.getType().equals(BigDecimal.class)) {
			return result.getBigDecimal(columnName);
		}

		if (columnMapping.getType().equals(Long.class) || columnMapping.getType().equals(long.class)) {
			return result.getLong(columnName);
		}

		if (columnMapping.getType().equals(Integer.class) || columnMapping.getType().equals(int.class)) {
			return result.getInt(columnName);
		}

		return null;
	}
	
	private String getQueryComparator(ColumnMapping columnMapping) {
		
		if (columnMapping.getType().equals(String.class)) {
			return " like ";
		}

		if (columnMapping.getType().equals(Date.class) || 
				columnMapping.getType().equals(BigDecimal.class) ||
				columnMapping.getType().equals(Long.class) ||
				columnMapping.getType().equals(long.class) ||
				columnMapping.getType().equals(Integer.class) ||
				columnMapping.getType().equals(int.class)) {
			return " = ";
		}
		
		throw new NotImplementedException(String.format("The type %s are not being handled", columnMapping.getType().getName()));

	}

	@Data
	private class TableMapping {

		private final String modelName;
		private Map<String, ColumnMapping> columns = new HashMap<>();

	}

	@Data
	private class ColumnMapping {

		private final String fieldName;
		private final String columnName;
		private final Class<?> type;

	}

}
