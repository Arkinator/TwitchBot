package com.zanderwork.twitchbot;

/**
 * Created by Zander Work on 2/3/2017.
 */
public class ChatCommands {

	public static void runCommand(String command, boolean isModerator) {
		User user = User.valueOf(command);
		if (isModerator) {
			//chat user is a moderator
			Moderator moderator = Moderator.valueOf(command);
		} else {
			//chat user is a plebian
		}
	}

	private enum User {
		ABOUT,
		YOUTUBE,
		GITHUB;
	}

	private enum Moderator {
		STOP;
	}
}