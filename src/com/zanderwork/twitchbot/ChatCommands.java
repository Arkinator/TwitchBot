package com.zanderwork.twitchbot;

/**
 * Created by Zander Work on 2/3/2017.
 */
public class ChatCommands {

	public static void runCommand(String command, boolean isModerator) {
		User user = null;
		Moderator moderator = null;
		try {
			user = User.valueOf(command.toUpperCase());
			Bot.log("COMMAND", String.format("Received %s user command", user.toString()));
		} catch (IllegalArgumentException e) {
			//non-user command was received
			//intentional empty catch block
		}

		try {
			moderator = Moderator.valueOf(command.toUpperCase());
			Bot.log("COMMAND", String.format("Received %s moderator command", moderator.toString()));
		} catch (IllegalArgumentException e) {
			//non-moderator command was received
			//intentional empty catch block
		}

		if (user == null && moderator == null) {
			//an invalid command was received, don't try and run exec code
			return;
		}

		if (isModerator && moderator != null) {
			//chat user is a moderator
			switch (moderator) {
				case STOP:
					Bot.getWriter().sendChatMessage(ConfigLoader.getConfig().get("channel"),
					                                "Shutting down bot MrDestructoid");
					Bot.getReader().stop();
					break;
			}
			//command was a mod command from a mod, no need to execute user command code
			return;
		}
		switch (user) {
			case ABOUT:
				//print about blurb
				Bot.getWriter().sendChatMessage(ConfigLoader.getConfig().get("channel"),
				                                "Zander is a Terran Player on Team UnRivaled. TeamUR competes in the ChoboTeamLeague " +
				                                "(http://www.choboteamleague.com), and is one of the top teams. " +
				                                "Zander also does a lot of programming (" + ConfigLoader.getConfig().get("command_prefix") +
				                                "github).");
				break;
			case YOUTUBE:
				//print youtube info
				Bot.getWriter().sendChatMessage(ConfigLoader.getConfig().get("channel"),
				                                "You can see Zander's YouTube channel at https://www.youtube.com/channel/UCSyfavU-66FP30uKqsN9LLw");
				break;
			case GITHUB:
				//print github info
				Bot.getWriter().sendChatMessage(ConfigLoader.getConfig().get("channel"),
				                                "You can see Zander's GitHub at https://github.com/zzzanderw");
				break;
			case DISCORD:
				//print discord info
				Bot.getWriter().sendChatMessage(ConfigLoader.getConfig().get("channel"),
				                                "You can join our Discord server at https://discord.gg/trpAJ5n");
				break;
		}
	}

	private enum User {
		ABOUT,
		YOUTUBE,
		GITHUB,
		DISCORD;
	}

	private enum Moderator {
		STOP;
	}
}