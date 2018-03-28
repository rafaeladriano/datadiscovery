package com.jsonar.datadiscovery.controller;

import java.io.Serializable;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

import com.jsonar.datadiscovery.model.User;

@ManagedBean(name = "authenticationService")
@ApplicationScoped
public class AuthenticationService implements Serializable {

	private static final long serialVersionUID = 3256521192094135982L;

	private final static User[] USERS;

	static {

		USERS = new User[2];

		USERS[0] = new User();
		USERS[0].setUsername("Test1");
		USERS[0].setPassword("test1@mytest.com");

		USERS[1] = new User();
		USERS[1].setUsername("Test2");
		USERS[1].setPassword("test2@mytest.com");

	}

	public boolean authenticate(String username, String password) {

		User user = findUser(username);
		if (user == null) {
			return false;
		}

		return user.getPassword().equals(password);
	}

	private User findUser(String username) {

		for (User user : USERS) {
			if (user.getUsername().equals(username)) {
				return user;
			}
		}

		return null;
	}

}
