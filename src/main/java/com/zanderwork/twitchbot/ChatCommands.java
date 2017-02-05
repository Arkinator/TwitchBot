package com.zanderwork.twitchbot;

import com.sun.istack.internal.logging.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

/**
 * Created by Zander Work on 2/3/2017.
 */
public class ChatCommands {
	private static Logger logger = Logger.getLogger(ChatCommands.class);

	private final Bot bot;
	private final List<String> moderators;
	private final String commandPrefix;

	ChatCommands(Bot bot, Properties properties) {
		this.bot = bot;
		moderators = new ArrayList<>(Arrays.asList(properties.getProperty("moderators").split(",")));
		moderators.add(properties.getProperty("channel"));
		commandPrefix = properties.getProperty("command_prefix");
	}

	public void runCommand(String command, String username) {
		CommandType commandType = null;
		Moderator moderator = null;
		try {
			commandType = CommandType.valueOf(command.toUpperCase());
			logger.info("COMMAND: " + String.format("Received %s commandType command", commandType.toString()));
		} catch (IllegalArgumentException e) {
			//non-commandType command was received
			//intentional empty catch block
		}

		try {
			moderator = Moderator.valueOf(command.toUpperCase());
			logger.info("COMMAND:" + String.format("Received %s moderator command", moderator.toString()));
		} catch (IllegalArgumentException e) {
			//non-moderator command was received
			//intentional empty catch block
		}

		if (commandType == null && moderator == null) {
			//an invalid command was received, don't try and run exec code
			return;
		}

		if (userIsModerator(username) && moderator != null) {
			//chat commandType is a moderator
			switch (moderator) {
				case STOP:
					bot.sendChatMessage("Shutting down bot MrDestructoid");
					break;
			}
			//command was a mod command from a mod, no need to execute commandType command code
			return;
		}
		switch (commandType) {
			case ABOUT:
				//print about blurb
				bot.sendChatMessage("Zander is a Terran Player on Team UnRivaled. TeamUR competes in the ChoboTeamLeague " +
						"(http://www.choboteamleague.com), and is one of the top teams. " +
						"Zander also does a lot of programming (" + commandPrefix + "github).");
				break;
			case YOUTUBE:
				//print youtube info
				bot.sendChatMessage("You can see Zander's YouTube channel at https://www.youtube.com/channel/UCSyfavU-66FP30uKqsN9LLw");
				break;
			case GITHUB:
				//print github info
				bot.sendChatMessage("You can see Zander's GitHub at https://github.com/zzzanderw");
				break;
			case DISCORD:
				//print discord info
				bot.sendChatMessage("You can join our Discord server at https://discord.gg/trpAJ5n");
				break;
		}
	}

	private boolean userIsModerator(String username) {
		return moderators.contains(username);
	}

	private enum CommandType {
		ABOUT,
		YOUTUBE,
		GITHUB,
		DISCORD
	}

	private enum Moderator {
		STOP
	}
}