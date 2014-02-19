package uk.codingbadgers.bsocial.channel;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileNotFoundException;
import java.util.logging.Level;

import net.md_5.bungee.api.ProxyServer;

import uk.codingbadgers.bsocial.bSocial;
import uk.codingbadgers.bsocial.commands.QuickMessageCommand;

public class ChannelManager {

    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    @Getter
    private final List<Channel> channels;
    @Getter
    private Channel defaultChannel = null;

    public ChannelManager() {
        this.channels = new ArrayList<>();
    }

    public Channel getChannel(String name) {
        List<Channel> channels = new ArrayList<>(this.channels);

        for (Channel channel : channels) {
            if (channel.getName().equalsIgnoreCase(name)) {
                return channel;
            }
        }

        return null;
    }

    public void addChannel(Channel channel) {
    	channel.setup();
        channel.save();
        channels.add(channel);
        
        ProxyServer.getInstance().getPluginManager().registerCommand(bSocial.getInstance(), new QuickMessageCommand(channel));
    }

    public void loadChannels() {
        File dir = new File(bSocial.getInstance().getDataFolder(), "channels");

        for (File file : dir.listFiles()) {
            try {
                Channel channel = gson.fromJson(new FileReader(file), Channel.class);
                addChannel(channel);
                bSocial.getInstance().getLogger().log(Level.INFO, "Loaded channel {0}", channel.getName());
            } catch (FileNotFoundException e) {
                bSocial.getInstance().getLogger().log(Level.INFO, String.format("Error loading channel %1$s", file.getName()), e);
            }
        }

        defaultChannel = getChannel(bSocial.getConfig().getDefaultChannel());
    }
    
    public void saveChannels() {
        for (Channel channel : channels) {
        	channel.save();
        }
    }

    public boolean exists(String name) {
        return getChannel(name) != null;
    }

    public void removeChannel(Channel channel) {
        channels.remove(channel);
        channel.delete();
    }
}
