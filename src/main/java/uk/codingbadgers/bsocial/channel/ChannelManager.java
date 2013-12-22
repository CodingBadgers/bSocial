package uk.codingbadgers.bsocial.channel;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import uk.codingbadgers.bsocial.bSocial;

public class ChannelManager {

	private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
	@Getter private List<Channel> channels = new ArrayList<>();
	@Getter private Channel defaultChannel = null;
	
	public Channel getChannel(String name) {
		List<Channel> channels = new ArrayList<Channel>(this.channels);
		
		for (Channel channel : channels) {
			if (channel.getName().equalsIgnoreCase(name)) {
				return channel;
			}
		}
		
		return null;
	}
	
	public void addChannel(Channel channel) {
		channels.add(channel);
	}
	
	public void loadChannels() {
		File dir = new File(bSocial.getInstance().getDataFolder(), "channels");
		
		for (File file : dir.listFiles()) {
			try {
				Channel channel = gson.fromJson(new FileReader(file), Channel.class);
				addChannel(channel);
				bSocial.getInstance().getLogger().info("Loaded channel " + channel.getName());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		defaultChannel = channels.get(0);
	}

	public boolean exists(String name) {
		return getChannel(name) != null;
	}

	public void removeChannel(Channel channel) {
		channels.remove(channel);
	}
	
}
