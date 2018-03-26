package com.jsonar.datadiscovery.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.jsonar.datadiscovery.configuration.Column;
import com.jsonar.datadiscovery.configuration.Table;

import lombok.Data;

@Data
@Table
public class Order implements Serializable {
	
	private static final long serialVersionUID = -7570686058207904610L;

	@Column("orderNumber")
	private final long number;
	
	@Column("orderDate")
	private Date date;
	
	@Column("requiredDate")
	private Date required;
	
	@Column("shippedDate")
	private Date shipped;
	
	@Column
	private String status;
	
	@Column
	private String comments;
	
	private List<ProductOrder> products;
	
}
