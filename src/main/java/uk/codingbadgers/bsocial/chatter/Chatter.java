package uk.codingbadgers.bsocial.chatter;

import java.util.Arrays;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import uk.codingbadgers.bsocial.Config.AntiSpam;
import uk.codingbadgers.bsocial.MuteData;

import uk.codingbadgers.bsocial.bSocial;
import uk.codingbadgers.bsocial.channel.Channel;
import uk.codingbadgers.bsocial.json.NonSerialized;

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
    @Setter(AccessLevel.NONE)
    private ProxiedPlayer player;
    private MuteData mutedata;
    private Channel activeChannel;
    @NonSerialized
    private AntiSpamData antispam;

    public Chatter(ProxiedPlayer player) {
        this.player = player;
        this.name = player.getName();
        this.antispam = new AntiSpamData();
        
        setActiveChannel(bSocial.getChannelManager().getDefaultChannel());
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
    }

    public final void setActiveChannel(Channel channel) {
        if (!channel.hasChatter(this)) {
            channel.join(this);
        }

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
        
        int caps = 0;
        for (char c : message.toCharArray()) {
            if (Character.isAlphabetic(c) && Character.isUpperCase(c)) {
                caps++;
            }
        }
        
        int length = message.replaceAll(" ", "").length();
        if ((((float) caps / (float) length) * 100) >= settings.getCapsPercentage()) {
            return AntiSpamReason.CAPS;
        }
        
        return AntiSpamReason.NONE;
    }
        
}
