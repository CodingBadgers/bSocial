package uk.codingbadgers.bsocial.listener;

import uk.codingbadgers.bsocial.bSocial;
import uk.codingbadgers.bsocial.channel.Channel;
import uk.codingbadgers.bsocial.chatter.Chatter;
import uk.codingbadgers.bsocial.messaging.Messages;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class EventListener implements Listener {

    @EventHandler
    public void onPlayerChat(ChatEvent event) {
        final ProxiedPlayer player = (ProxiedPlayer) event.getSender();

        if (event.isCommand()) {
            return;
        }

        event.setCancelled(true);

        Chatter chatter = bSocial.getChatterManager().getChatter(player);
        Channel active = chatter.getActiveChannel();

        if (active == null) {
            chatter.sendMessage(Messages.noChannel());
            return;
        }

        if (chatter.isMuted()) {
            chatter.sendMessage(Messages.muted());
            return;
        }

        active.sendMessage(chatter, event.getMessage());
    }

    @EventHandler
    public void onPlayerJoin(PostLoginEvent event) {
        bSocial.getChatterManager().loadChatter(event.getPlayer());
    }

    @EventHandler
    public void onPlayerJoin(PlayerDisconnectEvent event) {
        bSocial.getChatterManager().removeChatter(event.getPlayer());
    }
}
