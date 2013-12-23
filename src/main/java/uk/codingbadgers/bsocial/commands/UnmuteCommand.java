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

import static java.lang.Long.parseLong;
import java.util.Arrays;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import uk.codingbadgers.bsocial.bSocial;
import uk.codingbadgers.bsocial.chatter.Chatter;
import uk.codingbadgers.bsocial.messaging.Messages;

/**
 *
 * @author james
 */
public class UnmuteCommand extends Command {

    public UnmuteCommand() {
        super("unmute", "bsocial.admin.unmute");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {

        if (!(sender instanceof ProxiedPlayer)) {
            sender.sendMessage(Messages.playerOnly());
            return;
        }

        Chatter chatter = bSocial.getChatterManager().getChatter((ProxiedPlayer) sender);

        if (args.length != 1) {
            chatter.sendMessage(Messages.unmuteUsage());
            return;
        }
        
        // Get the player 
        
        String player = args[0];
        
        Chatter target = bSocial.getChatterManager().getChatter(player);
        
        if (target == null) {
            chatter.sendMessage(Messages.noPlayer(player));
            return;
        }

        if (!target.isMuted()) {
            chatter.sendMessage(Messages.notMuted(player));
            return;
        }
        
        target.setMutedata(null);
        
        target.sendMessage(Messages.unmuted(sender.getName()));
        chatter.sendMessage(Messages.unmuteSuccess(target.getName()));
    }

}
