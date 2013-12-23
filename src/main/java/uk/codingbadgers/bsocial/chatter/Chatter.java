package uk.codingbadgers.bsocial.chatter;

import java.util.Arrays;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import uk.codingbadgers.bsocial.bSocial;
import uk.codingbadgers.bsocial.channel.Channel;

@Data
public class Chatter {

    private final String name;
    @Setter(AccessLevel.NONE)
    private ProxiedPlayer player;
    private boolean muted;
    private Channel activeChannel;

    public Chatter(ProxiedPlayer player) {
        this.player = player;
        this.name = player.getName();
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
}
