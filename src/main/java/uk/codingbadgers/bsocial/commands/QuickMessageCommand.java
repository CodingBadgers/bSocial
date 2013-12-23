/*
 * Copyright (C) 2013 james
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package uk.codingbadgers.bsocial.commands;

import lombok.Getter;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import uk.codingbadgers.bsocial.bSocial;
import uk.codingbadgers.bsocial.channel.Channel;
import uk.codingbadgers.bsocial.chatter.Chatter;
import uk.codingbadgers.bsocial.messaging.Messages;

/**
 *
 * @author james
 */
public class QuickMessageCommand extends Command {
    
    @Getter private final Channel channel;

    public QuickMessageCommand(Channel channel) {
        super(channel.getName(), "bsocial.command.qm." + channel.getName());
        
        this.channel = channel;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!bSocial.getChannelManager().exists(getName())) { // Channel has been removed
            sender.sendMessage("Unknown command. Type \"/help\" for help");
            return;
        }
        
        if (!sender.hasPermission("bsocial.channel." + this.getName())) {
            sender.sendMessage(ProxyServer.getInstance().getTranslation("no_permission"));
            return;
        }
        
        if (!(sender instanceof ProxiedPlayer)) {
            sender.sendMessage(Messages.playerOnly());
            return;
        }
        
        String message = join(args);
        Chatter chatter = bSocial.getChatterManager().getChatter((ProxiedPlayer)sender);
        
        if (!channel.hasChatter(chatter)) {
            chatter.sendMessage(Messages.notInChannel(this.getName()));
            return;
        }
        
        channel.sendMessage(chatter, message);
    }

    private String join(String[] args) {
        StringBuilder builder = new StringBuilder();
        
        for (String arg : args) {
            builder.append(arg).append(" ");
        }
        
        return builder.toString().trim();
    }
}
