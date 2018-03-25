package com.jsonar.datadiscovery.configuration;

import org.apache.commons.lang3.StringUtils;

public enum Property {
	
	DATABASE_URL("database.url", true), //
	DATABASE_USER("database.user", true), //
	DATABASE_PASSWORD("database.password", true);

	private final String key;

	private final boolean isRequired;

	Property(final String key, final boolean isRequired) {
		this.key = key;
		this.isRequired = isRequired;
	}

	public String getKey() {
		return key;
	}

	public String get() {
		String value = PropertiesReader.getValue(key);
		if (isRequired) {
			if (StringUtils.isEmpty(value)) {
				throw new IllegalArgumentException(String.format("The configuration property '%s' is required", key));
			}
		}
		
		return value;
	}

}
