package uk.codingbadgers.bsocial.messaging;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import uk.codingbadgers.bsocial.MuteData;
import uk.codingbadgers.bsocial.bSocial;

public class Messages {

    private static BaseComponent newline() {
        return new ComponentBuilder("\n").create()[0];
    }

    private static BaseComponent createComponent(String message, ChatColor color) {
        return new ComponentBuilder(message).color(color).create()[0];
    }

    private static BaseComponent createPrefix() {
        return new ComponentBuilder("[bSocial] ").color(ChatColor.BLUE).create()[0];
    }
    
    public static BaseComponent[] chatUsage() {
        return new BaseComponent[]{
            createPrefix(),
            createComponent("/chat join <channel>", ChatColor.AQUA),
            newline(),
            createPrefix(),
            createComponent("/chat leave <channel>", ChatColor.AQUA),
            newline(),
            createPrefix(),
            createComponent("/chat list", ChatColor.AQUA)
        };
    }

    public static BaseComponent[] muteUsage() {
        return new BaseComponent[]{
            createPrefix(),
            createComponent("/mute <player> [time<frame>] <reason>", ChatColor.AQUA)
        };
    }
    
    public static BaseComponent[] unmuteUsage() {
        return new BaseComponent[]{
            createPrefix(),
            createComponent("/unmute <player>", ChatColor.AQUA)
        };
    }
    
    public static BaseComponent[] pmUsage() {
        return new BaseComponent[]{
            createPrefix(),
            createComponent("/pm <player> <message>", ChatColor.AQUA)
        };
    }
    
    public static BaseComponent[] channelDoesNotExist(String channel) {
        return new BaseComponent[]{
            createPrefix(),
            createComponent("The channel " + channel + " does not exist", ChatColor.RED)
        };
    }

    public static BaseComponent[] alreadyInChannel(String channel) {
        return new BaseComponent[]{
            createPrefix(),
            createComponent("You are already in the channel " + channel, ChatColor.RED)
        };
    }

    public static BaseComponent[] notInChannel(String channel) {
        return new BaseComponent[]{
            createPrefix(),
            createComponent("You are not in the channel " + channel, ChatColor.RED)
        };
    }

    public static BaseComponent[] noChannel() {
        return new BaseComponent[]{
            createPrefix(),
            createComponent("You are not in any channels", ChatColor.RED)
        };
    }

    public static BaseComponent[] joinedChannel(String channel) {
        return new BaseComponent[]{
            createPrefix(),
            createComponent("You have joined channel " + channel, ChatColor.AQUA)
        };
    }

    public static BaseComponent[] leftChannel(String channel) {
        return new BaseComponent[]{
            createPrefix(),
            createComponent("You have left channel " + channel, ChatColor.AQUA)
        };
    }

    public static BaseComponent[] playerOnly() {
        return new BaseComponent[]{
            createPrefix(),
            createComponent("You can only use this command as a player", ChatColor.AQUA)
        };
    }

    public static BaseComponent[] channelList() {
        return new BaseComponent[]{
            createPrefix(),
            createComponent("Channels:", ChatColor.AQUA)
        };
    }

    public static BaseComponent[] channelListElement(String name, boolean inChannel) {
        return new BaseComponent[]{
            createPrefix(),
            createComponent("- " + name, inChannel ? ChatColor.GREEN : ChatColor.RED)
        };
    }

    public static BaseComponent[] noChannels() {
        return new BaseComponent[]{
            createPrefix(),
            createComponent("No channels exist on this server", ChatColor.RED)
        };
    }

    public static BaseComponent[] channelAlreadyExists(String name) {
        return new BaseComponent[]{
            createPrefix(),
            createComponent("The channel " + name + " already exists", ChatColor.RED)
        };
    }

    public static BaseComponent[] channelDosntExist(String name) {
        return new BaseComponent[]{
            createPrefix(),
            createComponent("The channel " + name + " does not exist", ChatColor.RED)
        };
    }

    public static BaseComponent[] channelCreated(String name) {
        return new BaseComponent[]{
            createPrefix(),
            createComponent("Channel " + name + " created successfully", ChatColor.AQUA)
        };
    }

    public static BaseComponent[] channelRemoved(String name) {
        return new BaseComponent[]{
            createPrefix(),
            createComponent("Channel " + name + " removed successfully", ChatColor.AQUA)
        };
    }

    public static BaseComponent[] muted() {
        return new BaseComponent[]{
            createPrefix(),
            createComponent("You are currently muted", ChatColor.RED)
        };
    }

    public static BaseComponent[] noPlayer(String player) {
        return new BaseComponent[]{
            createPrefix(),
            createComponent("Cannot find a player by the name of " + player, ChatColor.RED)
        };
    }

    public static BaseComponent[] muted(MuteData data) {
        return new BaseComponent[]{
            createPrefix(),
            createComponent("You have been muted by " + data.getAdmin() + " for " + data.getReason(), ChatColor.AQUA)
        };
    }

    public static BaseComponent[] unmuted(String admin) {
        return new BaseComponent[]{
            createPrefix(),
            createComponent("You have been unmuted by " + admin, ChatColor.AQUA)
        };
    }

    public static BaseComponent[] mutedSuccess(String target, MuteData data) {
        return new BaseComponent[]{
            createPrefix(),
            createComponent("You have muted " + target + " for " + data.getReason(), ChatColor.AQUA)
        };
    }

    public static BaseComponent[] unmuteSuccess(String target) {
        return new BaseComponent[]{
            createPrefix(),
            createComponent("You have unmuted " + target, ChatColor.AQUA)
        };
    }

    public static BaseComponent[] alreadyMuted(String player) {
        return new BaseComponent[]{
            createPrefix(),
            createComponent(player + " is already muted", ChatColor.RED)
        };
    }

    public static BaseComponent[] notMuted(String player) {
        return new BaseComponent[]{
            createPrefix(),
            createComponent(player + " is not muted", ChatColor.RED)
        };
    }

    public static BaseComponent[] formatPM(String sender, String target, String message) {
        String output = ChatColor.translateAlternateColorCodes('&', bSocial.getConfig().getPmFormat());
        output = output.replaceAll("\\{sender\\}", sender);
        output = output.replaceAll("\\{target\\}", target);
        output = output.replaceAll("\\{message\\}", message);
        
        return new BaseComponent[]{
            createComponent(output, ChatColor.RESET)
        };
    }

    public static BaseComponent[] spamJoinTime(String player) {
        return new BaseComponent[]{
            createPrefix(),
            createComponent("You cannot talk in the first " + (bSocial.getConfig().getAntiSpamSettings().getLoginDelay() / 1000f) + " seconds of logging in", ChatColor.RED)
        };
    }

    public static BaseComponent[] spamMessageTime(String player) {
        return new BaseComponent[]{
            createPrefix(),
            createComponent("Please do not spam the chat", ChatColor.RED)
        };
    }

    public static BaseComponent[] spamMessage(String player) {
        return new BaseComponent[]{
            createPrefix(),
            createComponent("Please do not spam the chat", ChatColor.RED)
        };
    }

	public static BaseComponent[] activeChanged(String name) {
        return new BaseComponent[]{
                createPrefix(),
                createComponent("Your active channel has been changed to " + name, ChatColor.RED)
        };
	}

	public static BaseComponent[] nullChatter() {
        return new BaseComponent[]{
                createPrefix(),
                createComponent("A unexpected error has occured, please tell staff", ChatColor.RED)
        };
	}

}
