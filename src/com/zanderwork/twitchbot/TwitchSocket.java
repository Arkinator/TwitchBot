package com.zanderwork.twitchbot;

import java.io.IOException;
import java.net.Socket;

/**
 * Created by Zander Work on 2/1/2017.
 */
public class TwitchSocket {
	private Socket socket;

	public TwitchSocket(String hostName, int port) throws IOException {
		this.socket = new Socket(hostName, port);
	}

	public Socket getSocket() {
		return socket;
	}

	public void connect(SocketWriter writer, String pass, String nick) {
		writer.sendIRCMessage(String.format("PASS %s", pass));
		writer.sendIRCMessage(String.format("NICK %s", nick));
	}

	public void setChannel(SocketWriter writer, String channel) {
		writer.sendIRCMessage(String.format("JOIN #%s", channel));
	}
}
