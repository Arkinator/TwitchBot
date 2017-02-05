package com.zanderwork.twitchbot;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Properties;

/**
 * Created by Zander Work on 2/1/2017.
 */
public class ConfigLoader {
	private final String filename;
	private InputStream inputStream;
	private static HashMap<String, String> config;

	private static ArrayList<String> moderators;

	private ArrayList<String> configItems = new ArrayList<>(Arrays.asList(
			"host",
	        "port",
	        "ssl_port",
	        "oauth_token",
	        "nickname",
	        "channel",
			"command_prefix"));

	public ConfigLoader(String filename) {
		this.filename = filename;
	}

	public Properties execute() {
		try(InputStream inputStream = new FileInputStream(filename)) {
			Properties properties = new Properties();
			properties.load(inputStream);
			return properties;
		} catch (Exception e) {
			throw new RuntimeException("Unable to load configuration: ", e);
		}
	}
/*			Iterator<String> configItemIterator = configItems.iterator();
			String next;
			while (configItemIterator.hasNext()) {
				next = configItemIterator.next();
				Bot.log("CONFIG_CONSTRUCTION", String.format("Adding '%s':'%s'", next, properties.getProperty(next)));
				config.put(next, properties.getProperty(next));
			}
			String modList = properties.getProperty("moderators");
			moderators = new ArrayList<>(Arrays.asList(modList.split(",")));
			moderators.add(config.get("channel"));
			Bot.log("CONFIG_CONSTRUCTION", "Registering the following moderators: " + modList + "," + config.get("channel"));*/


	public static HashMap<String, String> getConfig() {
		return config;
	}

	public static ArrayList<String> getModerators() {
		return moderators;
	}
}
