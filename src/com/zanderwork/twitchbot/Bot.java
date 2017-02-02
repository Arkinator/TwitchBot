package com.zanderwork.twitchbot;

import java.io.IOException;
import java.util.HashMap;

/**
 * Created by Zander Work on 2/1/2017.
 */
public class Bot {
	private static ConfigLoader configLoader;
	private static HashMap<String, String> config;

	private static TwitchSocket socket;
	private static SocketReader reader;
	private static SocketWriter writer;

	public static void main(String[] args) {
		try {
			configLoader = new ConfigLoader("config.properties");
			config = configLoader.getConfig();
			socket = new TwitchSocket(config.get("host"), Integer.parseInt(config.get("port")));
			reader = new SocketReader(socket);
			writer = new SocketWriter(socket);
		} catch (IOException e) {
			System.err.println(e.toString());
			System.exit(1);
		}
		socket.connect(writer, config.get("oauth_token"), config.get("nickname"));
		socket.setChannel(writer, config.get("channel"));
		reader.start();

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			System.err.println(e.toString());
		}

		writer.sendChatMessage(config.get("channel"), "meme machine good to go sir MrDestructoid");
	}

	public static SocketReader getReader() {
		return reader;
	}

	public static SocketWriter getWriter() {
		return writer;
	}
}
