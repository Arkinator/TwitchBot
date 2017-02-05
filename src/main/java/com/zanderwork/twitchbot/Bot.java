package com.zanderwork.twitchbot;

import java.util.Properties;

/**
 * Created by Zander Work on 2/1/2017.
 */
public class Bot {
    private Properties properties;

    private static TwitchSocket socket;
    private ChatCommands chatCommands;

    public static void main(String[] args) throws InterruptedException {
        new Bot().execute();
    }

    // just rethrowing the exception here because we are staying within the same class
    private void execute() throws InterruptedException {
        properties = new ConfigLoader("config.properties").execute();
        socket = new TwitchSocket(properties, this);
        chatCommands = new ChatCommands(this, properties);

        Thread.sleep(1000);

        socket.sendChatMessage("meme machine good to go sir MrDestructoid");
    }
/*
    public static void log(String tag, String message) {
		String timeStamp = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss").format(new Date());
		System.out.println(String.format("%s\t[%s] %s", timeStamp, tag, message));
	}*/

    public void sendChatMessage(String message) {
        socket.sendChatMessage(message);
    }

    public void runCommand(String command, String user) {
        chatCommands.runCommand(command, user);
    }
}
