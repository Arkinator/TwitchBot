package com.zanderwork.twitchbot;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by Zander Work on 2/1/2017.
 */
public class SocketWriter {
	private PrintWriter writer;

	public SocketWriter(Socket socket) throws IOException {
		writer = new PrintWriter(socket.getOutputStream());
	}

	public void sendIRCMessage(String message) {
		Bot.log("IRC_OUT", message);
		writer.println(message);
		writer.flush();
	}

	public void sendChatMessage(String channel, String message) {
		Bot.log("CHAT_OUT:" + channel, message);
		writer.println(String.format("PRIVMSG #%s :%s", channel, message));
		writer.flush();
	}
}
