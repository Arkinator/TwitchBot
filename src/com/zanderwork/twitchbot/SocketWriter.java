package com.zanderwork.twitchbot;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by Zander Work on 2/1/2017.
 */
public class SocketWriter {
	private PrintWriter writer;
	private Socket socket;

	public SocketWriter(TwitchSocket socket) throws IOException {
		this.socket = socket.getSocket();

		writer = new PrintWriter(this.socket.getOutputStream());
	}

	public void sendIrcMessage(String message) {
		writer.println(message);
		writer.flush();
	}

	public void sendChatMessage(String channel, String message) {
		writer.println(String.format("PRIVMSG #%s :%s", channel, message));
		writer.flush();
	}
}
