package uk.codingbadgers.bsocial.messaging;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;

public class Messages {

	public static BaseComponent[] chatUsage() {
		return new BaseComponent[] {
				createPrefix(),
				createComponent("/chat join <channel>", ChatColor.AQUA),
				newline(),
				createPrefix(),
				createComponent("/chat leave <channel>", ChatColor.AQUA),
				newline(),
				createPrefix(),
				createComponent("/chat list", ChatColor.AQUA),
				newline()
			};
	}

	public static BaseComponent[] channelDoesNotExist(String channel) {
		return new BaseComponent[] {
				createPrefix(),
				createComponent("The channel" + channel + " does not exist", ChatColor.RED)
			};
	}

	public static BaseComponent[] alreadyInChannel(String channel) {
		return new BaseComponent[] {
				createPrefix(),
				createComponent("You are already in the channel " + channel, ChatColor.RED)
			};
	}

	public static BaseComponent[] notInChannel(String channel) {
		return new BaseComponent[] {
				createPrefix(),
				createComponent("You are not in the channel " + channel, ChatColor.RED)
			};
	}

	public static BaseComponent[] noChannel() {
		return new BaseComponent[] {
				createPrefix(),
				createComponent("You are not in any channels", ChatColor.RED)
			};
	}

	public static BaseComponent[] joinedChannel(String channel) {
		return new BaseComponent[] {
				createPrefix(),
				createComponent("You have joined channel " + channel, ChatColor.AQUA)
			};
	}

	public static BaseComponent[] leftChannel(String channel) {
		return new BaseComponent[] {
				createPrefix(),
				createComponent("You have left channel " + channel, ChatColor.AQUA)
			};
	}

	public static BaseComponent[] playerOnly() {
		return new BaseComponent[] {
				createPrefix(),
				createComponent("You can only use this command as a player", ChatColor.AQUA)
			};
	}

	public static BaseComponent[] channelList() {
		return new BaseComponent[] {
				createPrefix(),
				createComponent("Channels:", ChatColor.AQUA)
			};
	}

	public static BaseComponent[] channelListElement(String name) {
		return new BaseComponent[] {
				createPrefix(),
				createComponent("- " + name, ChatColor.AQUA)
			};
	}

	public static BaseComponent[] noChannels() {
		return new BaseComponent[] {
				createPrefix(),
				createComponent("No channels exist on this server", ChatColor.RED)
			};
	}

	public static BaseComponent[] channelAlreadyExists(String name) {
		return new BaseComponent[] {
				createPrefix(),
				createComponent("The channel " + name + " already exists", ChatColor.RED)
			};
	}

	public static BaseComponent[] channelDosntExist(String name) {
		return new BaseComponent[] {
				createPrefix(),
				createComponent("The channel " + name + " does not exist", ChatColor.RED)
			};
	}

	public static BaseComponent[] channelCreated(String name) {
		return new BaseComponent[] {
				createPrefix(),
				createComponent("Channel " + name + " created successfully", ChatColor.AQUA)
			};
	}

	public static BaseComponent[] channelRemoved(String name) {
		return new BaseComponent[] {
				createPrefix(),
				createComponent("Channel " + name + " removed successfully", ChatColor.AQUA)
			};
	}

	private static BaseComponent newline() {
		return new ComponentBuilder("\n").create()[0];
	}

	private static BaseComponent createComponent(String message, ChatColor color) {
		return new ComponentBuilder(message).bold(false).color(color).create()[0];
	}
	
	private static BaseComponent createPrefix() {
		return new ComponentBuilder("[bSocial] ").bold(true).color(ChatColor.BLUE).create()[0];
	}
	
}