package com.zanderwork.twitchbot;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.HashMap;

/**
 * Created by Zander Work on 2/1/2017.
 */
public class SocketReader implements Runnable {
	private BufferedReader reader;

	private boolean running = false;
	private Thread myThread;

	public SocketReader(Socket socket) throws IOException {
		reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

		myThread = new Thread(this);
		myThread.setName("SocketReader");
	}

	public boolean isRunning() {
		return running;
	}

	public HashMap<String, String> parseIRC(String message) {
		//example incoming message:
		//:memebot42!memebot42@memebot42.tmi.twitch.tv PRIVMSG #techgod52 :test
		HashMap<String, String> messageParts = new HashMap<>();
		int numColons = 0;
		if (!message.contains("PRIVMSG")) {
			//not a chat message, return
			messageParts.put("error", "irc message not twitch chat message");
			return messageParts;
		}

		//we have a valid chat message, continue
		for (int i = 0; i < message.length(); i++) {
			if (message.charAt(i) == ':') {
				numColons++;
			}
		}
		String user = null;
		String chatMessage = null;
		String[] colon = message.split(":");
		user = colon[1].split("!")[0];
		chatMessage = colon[2];
		if (numColons > 2) {
			//the chat message had a colon(s) in it, add them and the message back in
			for (int i = 3; i <= numColons; i++) {
				chatMessage += ":";
				chatMessage += colon[i];
			}
		}
		messageParts.put("user", user);
		messageParts.put("message", chatMessage);
		return messageParts;
	}

	@Override
	public void run() {
		Bot.log("STATUS", "Bot SocketReader starting up");

		running = true;
		String message;
		HashMap<String, String> parsedMessage;
		while (running) {
			try {
				message = reader.readLine();
				parsedMessage = parseIRC(message);
				if (parsedMessage.containsKey("error")) {
					//we attempted to parse a non-twitch chat message
					Bot.log("IRC_IN", message);
				}
				//we parsed a valid twitch chat message
				Bot.log("CHAT_IN:" + ConfigLoader.getConfig().get("channel"),
				        String.format("%s: %s", parsedMessage.get("user"), parsedMessage.get("message")));
				if (parsedMessage.get("message").charAt(0) == ConfigLoader.getConfig().get("command_prefix").charAt(0)) {
					//user sent a command, process it
					String command = parsedMessage.get("message").split(ConfigLoader.getConfig().get("command_prefix"))[1];
					ChatCommands.runCommand(command, ConfigLoader.getModerators().contains(parsedMessage.get("user")));
				}
			} catch (IOException e) {
				System.err.println(e.toString());
			}
			myThread.yield();
		}
	}

	public void start() {
		myThread.start();
	}

	public void stop() {
		Bot.log("STATUS", "Bot SocketReader shutting down");
		running = false;
	}
}
