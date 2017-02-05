package com.zanderwork.twitchbot;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Properties;

/**
 * Created by Zander Work on 2/1/2017.
 */
public class Bot {
	private static Properties properties;
	private static HashMap<String, String> config;

	private static TwitchSocket socket;

	public static void main(String[] args) {
		try {
			properties = new ConfigLoader("config.properties").execute();
			socket = new TwitchSocket(properties);
		} catch (IOException e) {
			System.err.println(e.toString());
			System.exit(1);
		}

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			System.err.println(e.toString());
		}

		socket.sendChatMessage("meme machine good to go sir MrDestructoid");
	}

	public static void log(String tag, String message) {
		String timeStamp = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss").format(new Date());
		System.out.println(String.format("%s\t[%s] %s", timeStamp, tag, message));
	}

	public static void sendChatMessage(String message) {
		socket.sendChatMessage(message);
	}
}
