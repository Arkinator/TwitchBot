package com.zanderwork.twitchbot;

import com.sun.istack.internal.logging.Logger;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by Zander Work on 2/1/2017.
 */
public class SocketWriter {
    private static Logger logger = Logger.getLogger(SocketWriter.class);
    private PrintWriter writer;

    public SocketWriter(Socket socket) throws IOException {
        writer = new PrintWriter(socket.getOutputStream());
    }

    public void sendIRCMessage(String message) {
        logger.info("IRC_OUT:" + message);
        writer.println(message);
        writer.flush();
    }

    public void sendChatMessage(String channel, String message) {
        logger.info("CHAT_OUT:" + channel + " => " + message);
        writer.println(String.format("PRIVMSG #%s :%s", channel, message));
        writer.flush();
    }
}
