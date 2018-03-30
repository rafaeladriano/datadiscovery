package com.jsonar.datadiscovery.configuration;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

public class PropertiesReader {
	
	private static Logger LOGGER = Logger.getLogger(PropertiesReader.class);
	
	private static volatile Properties properties;

	public static String getValue(String key) {
		loadProperties();
		return properties.getProperty(key);
	}

	private static void loadProperties() {
		
		if (properties == null) {
			synchronized (PropertiesReader.class) {
				if (properties == null) {
					properties = new Properties();
					InputStream stream = PropertiesReader.class.getResourceAsStream("/configuration.properties");
					try {
						properties.load(stream);
					} catch (IOException e) {
						LOGGER.error(e.getMessage(), e);
						throw new RuntimeException(e);
					}
				}
			}
		}
		
	}

}
