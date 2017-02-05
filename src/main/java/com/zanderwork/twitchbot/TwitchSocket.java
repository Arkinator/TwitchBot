package com.zanderwork.twitchbot;

import java.io.IOException;
import java.net.Socket;
import java.util.Properties;

/**
 * Created by Zander Work on 2/1/2017.
 */
public class TwitchSocket {
    private final Socket socket;
    private final SocketReader reader;
    private final SocketWriter writer;
    private final String channel;

    public TwitchSocket(Properties config, Bot bot) {
        try {
            this.socket = new Socket(config.getProperty("host"), Integer.parseInt(config.getProperty("port")));
            reader = new SocketReader(socket, config, bot);
            writer = new SocketWriter(socket);
            connect(writer, config.getProperty("oauth_token"), config.getProperty("nickname"));
            setChannel(writer, config.getProperty("channel"));
            reader.start();
            channel = config.getProperty("channel");
        } catch (IOException e) {
            throw new RuntimeException("Exception while trying to set-up communications:", e);
        }
    }

    public void connect(SocketWriter writer, String pass, String nick) {
        writer.sendIRCMessage(String.format("PASS %s", pass));
        writer.sendIRCMessage(String.format("NICK %s", nick));
    }

    public void setChannel(SocketWriter writer, String channel) {
        writer.sendIRCMessage(String.format("JOIN #%s", channel));
    }

    public void sendChatMessage(String message) {
        writer.sendChatMessage(channel, message);
    }
}
