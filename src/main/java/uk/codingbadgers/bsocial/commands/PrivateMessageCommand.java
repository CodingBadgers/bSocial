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

import java.util.Arrays;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;
import uk.codingbadgers.bsocial.bSocial;
import uk.codingbadgers.bsocial.chatter.Chatter;
import uk.codingbadgers.bsocial.messaging.Messages;

/**
 *
 * @author james
 */
public class PrivateMessageCommand extends Command {

    public PrivateMessageCommand() {
        super("pm", "bsocial.command.pm");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        
        if (args.length < 2) {
            sender.sendMessage(Messages.pmUsage());
            return;
        }
        
        String player = args[0];
        String message = join(args, 1);
        
        Chatter target = bSocial.getChatterManager().getChatter(player);
        
        if (target == null) {
            sender.sendMessage(Messages.noPlayer(player));
            return;
        }
        
        sender.sendMessage(Messages.formatPM("You", target.getName(), message));
        target.sendMessage(Messages.formatPM(sender.getName(), "You", message));
    }
    
    private String join(String[] args, int i) {
        StringBuilder builder = new StringBuilder();
        
        for (String arg : Arrays.copyOfRange(args, i, args.length)) {
            builder.append(arg).append(" ");
        }
        
        return builder.toString().trim();
    }

}
