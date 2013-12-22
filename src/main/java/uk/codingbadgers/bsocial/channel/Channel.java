package uk.codingbadgers.bsocial.channel;

import java.util.ArrayList;
import java.util.List;

import uk.codingbadgers.bsocial.bSocial;
import uk.codingbadgers.bsocial.chatter.Chatter;
import uk.codingbadgers.bsocial.messaging.Messages;

import lombok.Data;
import lombok.NoArgsConstructor;

import net.md_5.bungee.api.ChatColor;

@Data
public class Channel {

	@Data
	@NoArgsConstructor
	private static class ChannelData {
		private ChatColor basecolour = ChatColor.WHITE;
		private String format = "{colour}<{player}>:";
	}
	
	private final String name;
	private final ChannelData data = new ChannelData();
	private List<Chatter> listening = new ArrayList<>();
	
	/**
	 * Broadcast a raw message to this channel
	 * 
	 * @param message the raw message to broadcast
	 */
	@SuppressWarnings("deprecation")
	public void broadcastRawMessage(String message) {
		for (Chatter chatter : listening) {
			chatter.sendMessage(message);
		}
		
		if (bSocial.getConfig().isLogToConsole()) {
			bSocial.getInstance().getLogger().info("[" + getName() + "] -> " + message);
		}
	}

	/**
	 * Format a message according to the channels formatting rules
	 * 
	 * @param player the player that sent the message
	 * @param message the message to format
	 * @return a formatted version of the message passed in
	 */
	public String formatMessage(Chatter player, String message) {
		String prefix = ChatColor.translateAlternateColorCodes('&', data.getFormat());
		prefix = prefix.replaceAll("\\{player\\}", player.getName());
		prefix = prefix.replaceAll("\\{server\\}", player.getPlayer().getServer().getInfo().getName());
		prefix = prefix.replaceAll("\\{colour\\}", data.getBasecolour().toString());
		
		return prefix + " " + ChatColor.translateAlternateColorCodes('&', message);
	}
	
	/**
	 * Format a message then broadcast it to the channel
	 * 
	 * @param player the player that sent the message
	 * @param message the message to format
	 */
	public void sendMessage(Chatter player, String message) {
		broadcastRawMessage(formatMessage(player, message));
	}

	/**
	 * See if this channel has a player in it
	 * 
	 * @param chatter the player to check
	 * @return true if the player is in the channel, false otherwise
	 */
	public boolean hasChatter(Chatter chatter) {
		if (listening == null) {
			listening = new ArrayList<>();
		}
		
		return listening.contains(chatter);
	}
	
	/**
	 * Make a chatter join this channel
	 * 
	 * @param chatter the chatter to join this channel
	 * @see Chatter#join(Channel)
	 */
	public void join(Chatter chatter) {
		if (hasChatter(chatter)) {
			return;
		}
		
		listening.add(chatter);
		chatter.sendMessage(Messages.joinedChannel(getName()));
	}
	
	/**
	 * Make a chatter leave this channel
	 * 
	 * @param chatter the chatter to leave the channel
	 * @see Chatter#leave(Channel)
	 */
	public void leave(Chatter chatter) {
		if (!hasChatter(chatter)) {
			return;
		}
		
		listening.remove(chatter);
		chatter.sendMessage(Messages.leftChannel(getName()));
	}
}
