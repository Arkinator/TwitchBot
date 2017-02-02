package com.zanderwork.twitchbot;

import java.io.IOException;
import java.net.Socket;

/**
 * Created by Zander Work on 2/1/2017.
 */
public class TwitchSocket {
	private String hostName;
	private int port;

	private String channel;

	private Socket socket;

	public TwitchSocket(String hostName, int port) throws IOException {
		this.hostName = hostName;
		this.port = port;

		this.socket = new Socket(hostName, port);
	}

	public String getChannel() {
		return channel;
	}

	public Socket getSocket() {
		return socket;
	}

	public void connect(SocketWriter writer, String pass, String nick) {
		writer.sendIrcMessage(String.format("PASS %s", pass));
		writer.sendIrcMessage(String.format("NICK %s", nick));
	}

	public void setChannel(SocketWriter writer, String channel) {
		this.channel = channel;
		writer.sendIrcMessage(String.format("JOIN #%s", channel));
	}
}
