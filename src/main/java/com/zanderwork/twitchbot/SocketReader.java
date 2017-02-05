package com.zanderwork.twitchbot;

import com.sun.istack.internal.logging.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.HashMap;
import java.util.Properties;

/**
 * Created by Zander Work on 2/1/2017.
 */
public class SocketReader implements Runnable {
    private static Logger logger = Logger.getLogger(SocketReader.class);
    private final Bot bot;

    private BufferedReader reader;

    private boolean running = false;
    private Thread myThread;
    private String commmandPrefix;

    public SocketReader(Socket socket, Properties properties, Bot bot) throws IOException {
        reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        myThread = new Thread(this);
        myThread.setName("SocketReader");
        this.commmandPrefix = properties.getProperty("command_prefix");
        this.bot = bot;
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
        logger.info("STATUS: Bot SocketReader starting up");

        running = true;
        String message;
        HashMap<String, String> parsedMessage;
        while (running) {
            try {
                message = reader.readLine();
                parsedMessage = parseIRC(message);
                if (parsedMessage.containsKey("error")) {
                    //we attempted to parse a non-twitch chat message
                    logger.info("IRC_IN:" + message);
                    /*if (message.contentEquals("PING :tmi.twitch.tv")) {
                        //avoid getting auto-disconnected
						Bot.getWriter().sendIRCMessage("PONG :tmi.twitch.tv");
					}*/
                    continue;
                }
                //we parsed a valid twitch chat message
                logger.info(String.format("%s: %s", parsedMessage.get("user"), parsedMessage.get("message")));
                if (parsedMessage.get("message").startsWith(commmandPrefix)) {
                    //user sent a command, process it
                    String command = parsedMessage.get("message").substring(commmandPrefix.length());
                    bot.runCommand(command, parsedMessage.get("user"));
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
}
