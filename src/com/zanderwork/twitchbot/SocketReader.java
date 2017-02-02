package com.zanderwork.twitchbot;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by Zander Work on 2/1/2017.
 */
public class SocketReader implements Runnable {

	private Socket socket;
	private BufferedReader reader;

	private boolean running = false;
	private Thread myThread;

	private String chatMessage;
	private final Lock chatLock = new ReentrantLock();

	public SocketReader(TwitchSocket socket) throws IOException {
		this.socket = socket.getSocket();

		reader = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));

		myThread = new Thread(this);
	}

	public String getChatMessage() {
		synchronized (chatLock) {
			return chatMessage;
		}
	}

	public boolean isRunning() {
		return running;
	}

	@Override
	public void run() {
		running = true;
		String message;
		while (running) {
			try {
				message = reader.readLine();
				if (message.contentEquals("PING :tmi.twitch.tv")) {
					//avoid getting auto-disconnected
					Bot.getWriter().sendIrcMessage("PONG");
				} else if (message.contains("bot.stop")) {
					//shut down bot
					Bot.getWriter().sendChatMessage("techgod52", "Shutting down bot MrDestructoid");
					stop();
				}
				System.out.println(message);
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
		running = false;
	}
}
