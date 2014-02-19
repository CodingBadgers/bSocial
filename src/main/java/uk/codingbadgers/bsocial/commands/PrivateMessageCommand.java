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
        bSocial.getLogHandler().logPM(sender.getName(), target.getName(), message);
    }
    
    private String join(String[] args, int i) {
        StringBuilder builder = new StringBuilder();
        
        for (String arg : Arrays.copyOfRange(args, i, args.length)) {
            builder.append(arg).append(" ");
        }
        
        return builder.toString().trim();
    }

}
