package com.zanderwork.twitchbot;

import java.io.*;
import java.util.HashMap;
import java.util.Properties;

/**
 * Created by Zander Work on 2/1/2017.
 */
public class ConfigLoader {
	private InputStream inputStream;
	private static HashMap<String, String> config;

	public ConfigLoader(String filename) throws IOException {
		try {
			Properties properties = new Properties();
			config = new HashMap<>();

			inputStream = new FileInputStream(filename);
			if (inputStream != null) {
				properties.load(inputStream);
			} else {
				throw new FileNotFoundException(String.format("The config file %s was not found", filename));
			}
			//TODO arraylist all of the config items, iterate through list and populate the config hashmap
			config.put("host", properties.getProperty("host"));
			config.put("port", properties.getProperty("port"));
			config.put("ssl_port", properties.getProperty("ssl_port"));
			config.put("oauth_token", properties.getProperty("oauth_token"));
			config.put("nickname", properties.getProperty("nickname"));
			config.put("channel", properties.getProperty("channel"));
		} catch (Exception e) {
			System.err.println(e.toString());
		} finally {
			inputStream.close();
		}
	}

	public static HashMap<String, String> getConfig() {
		return config;
	}
}
