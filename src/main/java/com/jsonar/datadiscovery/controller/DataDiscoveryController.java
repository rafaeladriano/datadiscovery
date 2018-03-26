package com.jsonar.datadiscovery.controller;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;

import com.jsonar.datadiscovery.model.Customer;

import lombok.Getter;
import lombok.Setter;

@ManagedBean
public class DataDiscoveryController {

	@Getter
	private CustomerLazyDataModel lazyDataModel;

	@Getter
	@Setter
	private Customer selectedCustomer;

	@PostConstruct
	public void initialize() {
		lazyDataModel = new CustomerLazyDataModel();
	}

}
