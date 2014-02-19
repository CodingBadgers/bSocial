package uk.codingbadgers.bsocial.channel;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;

import com.google.gson.JsonIOException;

import lombok.Data;
import lombok.NoArgsConstructor;
import net.md_5.bungee.api.ChatColor;
import uk.codingbadgers.bsocial.bSocial;
import uk.codingbadgers.bsocial.chatter.Chatter;
import uk.codingbadgers.bsocial.json.NonSerialized;
import uk.codingbadgers.bsocial.messaging.Messages;

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
	@NonSerialized
	private List<Chatter> listening = new ArrayList<>();

	/**
	 * Delete this channel completely from disk.
	 */
	void delete() {
		getSaveFile().delete();
	}

	/**
	 * Setup the channel ready for use
	 */
	void setup() {
		listening = new ArrayList<>();
	}

	/**
	 * Save this channel to disk in its current status
	 */
	public void save() {
		try (FileWriter writer = new FileWriter(getSaveFile())) {
			bSocial.getGson().toJson(this, writer);
			writer.flush();
		} catch (JsonIOException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Get the file that this channel saves to on disk.
	 * 
	 * @return this channel's save file.
	 */
	public File getSaveFile() {
		return FileUtils.getFile(bSocial.getInstance().getDataFolder(), "channels", name + ".json");
	}

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
		bSocial.getLogHandler().logMessage(player.getName(), getName(), message);
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

	/**
	 * A player that was listening to this channel has left the game.
	 * 
	 * @param chatter the chatter that has left the game.
	 */
	public void playerLeft(Chatter chatter) {
		listening.remove(chatter);
	}

	/**
	 * A player that was listening to this channel has joined the game.
	 * 
	 * @param chatter the chatter that has joined the game.
	 */
	public void playerJoined(Chatter chatter) {
		listening.add(chatter);
	}
}
