package com.hortonworks.streaming.results.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

public class ConfigurationUtil {
	private static Properties configuration = null;
	private static ConfigurationUtil instance = null;
	private static Logger logger = Logger.getLogger(ConfigurationUtil.class);

	private ConfigurationUtil() {
		configuration = new Properties();
		InputStream in = this.getClass().getClassLoader()
				.getResourceAsStream("config.properties");
		if (in != null) {
			try {
				configuration.load(in);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else
			logger.error("Can't find configuration file config.properties in classpath");
	}

	public static ConfigurationUtil getInstance() {
		if (instance == null)
			instance = new ConfigurationUtil();
		return instance;
	}

	public String getProperty(String propertyName) {
		if (configuration != null)
			return configuration.getProperty(propertyName);
		return null;
	}
}
