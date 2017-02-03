package com.zanderwork.twitchbot;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Properties;
import java.util.Iterator;

/**
 * Created by Zander Work on 2/1/2017.
 */
public class ConfigLoader {
	private InputStream inputStream;
	private static HashMap<String, String> config;

	private static ArrayList<String> moderators;

	private ArrayList<String> configItems = new ArrayList<>(Arrays.asList(
			"host",
	        "port",
	        "ssl_port",
	        "oauth_token",
	        "nickname",
	        "channel"));

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
			Iterator<String> configItemIterator = configItems.iterator();
			String next;
			while (configItemIterator.hasNext()) {
				next = configItemIterator.next();
				Bot.log("CONFIG_CONSTRUCTION", String.format("Adding '%s':'%s'", next, properties.getProperty(next)));
				config.put(next, properties.getProperty(next));
			}
			String modList = properties.getProperty("moderators");
			moderators = new ArrayList<>(Arrays.asList(modList.split(",")));
			Bot.log("CONFIG_CONSTRUCTION", "Registering the following moderators: " + modList);
		} catch (Exception e) {
			System.err.println(e.toString());
		} finally {
			inputStream.close();
		}
	}

	public static HashMap<String, String> getConfig() {
		return config;
	}

	public static ArrayList<String> getModerators() {
		return moderators;
	}
}
