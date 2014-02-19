package uk.codingbadgers.bsocial.chatter;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.apache.commons.io.FileUtils;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;

import uk.codingbadgers.bsocial.Config.AntiSpam;
import uk.codingbadgers.bsocial.MuteData;

import uk.codingbadgers.bsocial.bSocial;
import uk.codingbadgers.bsocial.channel.Channel;
import uk.codingbadgers.bsocial.exception.ConfigException;
import uk.codingbadgers.bsocial.json.NonSerialized;
import uk.codingbadgers.bsocial.messaging.Messages;

@Data
public class Chatter {

    @Data
    public static class AntiSpamData {
        private long logintime = System.currentTimeMillis();
        private long lastmessagetime = System.currentTimeMillis();
        private String lastmessage = "";
    }
   
    public static enum AntiSpamReason {
        JOIN_TIME,
        MESSAGE_TIME,
        MESSAGE,
        CAPS,
        NONE,
        ;
    }
    
    private final String name;
    @Setter(AccessLevel.PACKAGE) 
    @NonSerialized
    private ProxiedPlayer player;
    private MuteData mutedata;
    private Channel activeChannel;
    @NonSerialized
    private AntiSpamData antispam;

    public Chatter(ProxiedPlayer player) {
        this.player = player;
        this.name = player.getName();
        this.antispam = new AntiSpamData();
        
        load();
    }
    
    public void save() {        
        JsonObject root = new JsonObject();
        root.add("name", new JsonPrimitive(name));
        root.add("active", new JsonPrimitive(this.activeChannel.getName()));
        
        JsonArray channels = new JsonArray();
        for (Channel channel : getChannels()) {
        	channels.add(new JsonPrimitive(channel.getName()));
        }
        root.add("channels", channels);
        
        if (this.mutedata != null) {
            JsonObject mute = new JsonObject();
	        mute.add("endtime", new JsonPrimitive(this.mutedata.getEndtime()));
	        mute.add("admin", new JsonPrimitive(this.mutedata.getAdmin()));
	        mute.add("reason", new JsonPrimitive(this.mutedata.getReason()));
	        root.add("mute", mute);
        }
        
        try (FileWriter writer = new FileWriter(getSaveFile())) {
            writer.write(root.toString());
            writer.flush();
        } catch (IOException ex) {
            throw new ConfigException("Error saving player save data for " + player.getName(), ex);
        }
    }
    
    public void load() {
    	File savefile = getSaveFile();
    	
    	if (!savefile.exists()) {
            setActiveChannel(bSocial.getChannelManager().getDefaultChannel());
            return;
    	}
    	
    	JsonObject root = null;
    	
    	try (FileReader reader = new FileReader(getSaveFile())) {
    		JsonParser parser = new JsonParser();
    		root = parser.parse(reader).getAsJsonObject();
    	} catch (IOException ex) {
            throw new ConfigException("Error loading player save data for " + player.getName(), ex);
        } catch (Exception ex) {
            throw new ConfigException("Unexpected Error loading player save data for " + player.getName(), ex);
        }
    	
    	for (JsonElement element : root.get("channels").getAsJsonArray()) {
    		Channel channel = bSocial.getChannelManager().getChannel(element.getAsString());
    		
    		if (channel != null) {
    			channel.playerJoined(this);
    		}
    	}
    	
    	this.activeChannel = bSocial.getChannelManager().getChannel(root.get("active").getAsString());
    	
    	if (root.has("mute")) {
	    	JsonObject mute = root.get("mute").getAsJsonObject();
	    	this.mutedata = new MuteData(mute.get("endtime").getAsLong(),
	    									mute.get("admin").getAsString(),
	    									mute.get("reason").getAsString());
    	}
    }
    
    public File getSaveFile() {
        String id = player.getUUID().replace("-", "");
        File savefile = FileUtils.getFile(bSocial.getInstance().getDataFolder(), "players", id.substring(0, 1), id.substring(1, 2), id + ".json");
        
        if (!savefile.getParentFile().exists() && !savefile.getParentFile().mkdirs()) {
            throw new ConfigException("Error creating player save folder for " + player.getName());
        }
        
        return savefile;
    }

    @Deprecated
    public void sendMessage(String message) {
        player.sendMessage(message);
    }

    public void sendMessage(BaseComponent... components) {
        BaseComponent[][] message = new BaseComponent[1][1];

        int i = 0; // message index
        int j = 0; // line index

        for (BaseComponent component : components) {

            if (component.toPlainText().equals("\n")) {
                j++;
                i = 0;

                if (j >= message.length) {
                    message = Arrays.copyOf(message, j + 1);
                }

                continue;
            }

            if (message[j] == null) {
                message[j] = new BaseComponent[1];
            }

            if (i >= message[j].length) {
                message[j] = Arrays.copyOf(message[j], i + 1);
            }

            message[j][i++] = component;
        }

        for (BaseComponent[] component : message) {

            if (component == null) {
                continue;
            }

            player.sendMessage(component);
        }
    }

    public void destroy() {
        player = null;
        for (Channel channel : getChannels()) {
        	channel.playerLeft(this);
        }
    }

    public final void setActiveChannel(Channel channel) {
        if (!channel.hasChatter(this)) {
            channel.join(this);
        }

        this.sendMessage(Messages.activeChanged(channel.getName()));
        this.activeChannel = channel;
    }

    public void join(Channel channel) {
        if (activeChannel == null) {
            activeChannel = channel;
        }

        channel.join(this);
    }

    public void leave(Channel channel) {
        if (activeChannel == channel) {
            activeChannel = null; // TODO put into other joined channels
        }

        channel.leave(this);
    }
    
    public List<Channel> getChannels() {
    	List<Channel> channels = new ArrayList<Channel>();
    	
    	for (Channel channel : bSocial.getChannelManager().getChannels()) {
    		if (channel.hasChatter(this)) {
    			channels.add(channel);
    		}
    	}
    	
    	return channels;
    }
    
    public boolean isMuted() {
        return mutedata != null;   
    }
    
    public AntiSpamReason checkSpam(String message) {
        if (player.hasPermission("bsocial.antispam.override")) {
            return AntiSpamReason.NONE;
        }
        
        AntiSpam settings = bSocial.getConfig().getAntiSpamSettings();
        String lastmessage = antispam.getLastmessage();
        long lastmessagetime = antispam.getLastmessagetime();
        long curtime = System.currentTimeMillis();
        
        antispam.setLastmessagetime(curtime);
        antispam.setLastmessage(message);
        
        if ((curtime - antispam.getLogintime()) < settings.getLoginDelay()) {
            return AntiSpamReason.JOIN_TIME;
        }
        
        if ((curtime - lastmessagetime) < settings.getMessageDelay()) {
            return AntiSpamReason.MESSAGE_TIME;
        }
        
        if (message.equalsIgnoreCase(lastmessage)) {
            return AntiSpamReason.MESSAGE;
        }
        
        float caps = 0;
        float length = 0;
        for (char c : message.toCharArray()) {
        	if (Character.isAlphabetic(c)) {
        		length++;
        		if (Character.isUpperCase(c)) {
        			caps++;
        		}
        	}
        }
        
        if (((caps / length) * 100) >= settings.getCapsPercentage()) {
            return AntiSpamReason.CAPS;
        }
        
        return AntiSpamReason.NONE;
    }
        
}
