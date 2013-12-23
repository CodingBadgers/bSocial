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
import uk.codingbadgers.bsocial.chatter.Chatter.AntiSpamReason;

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
        
        String message = event.getMessage();
        
        AntiSpamReason spam = chatter.checkSpam(message);
        
        if (spam != AntiSpamReason.NONE) {
            switch (spam) {
                case JOIN_TIME:
                    chatter.sendMessage(Messages.spamJoinTime(chatter.getName()));
                    return;
                case MESSAGE_TIME:
                    chatter.sendMessage(Messages.spamMessageTime(chatter.getName()));
                    return;
                case MESSAGE:
                    chatter.sendMessage(Messages.spamMessage(chatter.getName()));
                    return;
                case CAPS:
                    message = message.toLowerCase();
                    break;
            }
        }

        active.sendMessage(chatter, message);
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
