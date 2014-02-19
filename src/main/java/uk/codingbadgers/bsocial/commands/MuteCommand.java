package uk.codingbadgers.bsocial.commands;

import static java.lang.Long.parseLong;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import uk.codingbadgers.bsocial.MuteData;
import uk.codingbadgers.bsocial.bSocial;
import uk.codingbadgers.bsocial.chatter.Chatter;
import uk.codingbadgers.bsocial.messaging.Messages;

/**
 *
 * @author james
 */
public class MuteCommand extends Command {

    private static final Pattern TIME_PATTERN = Pattern.compile("([0-9]+)([mhd])");

    public MuteCommand() {
        super("mute", "bsocial.admin.mute");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {

        if (!(sender instanceof ProxiedPlayer)) {
            sender.sendMessage(Messages.playerOnly());
            return;
        }

        Chatter chatter = bSocial.getChatterManager().getChatter((ProxiedPlayer) sender);

        if (args.length < 2) {
            chatter.sendMessage(Messages.muteUsage());
            return;
        }

        // Get the player 
        
        String player = args[0];
        
        Chatter target = bSocial.getChatterManager().getChatter(player);
        
        if (target == null) {
            chatter.sendMessage(Messages.noPlayer(player));
            return;
        }
        
        if (target.isMuted()) {
            chatter.sendMessage(Messages.alreadyMuted(player));
            return;
        }
        
        // Get if a timeframe has been added
        long mutetime = -1;

        if (args.length > 1 && TIME_PATTERN.matcher(args[1]).matches()) {
            mutetime = System.currentTimeMillis();

            Matcher matcher = TIME_PATTERN.matcher(args[1]);
            long time = parseLong(matcher.group(1));
            String timescale = matcher.group(2);

            switch (timescale.toLowerCase()) {
                case "m":
                    mutetime += time * 60 * 1000;
                    break;
                case "h":
                    mutetime += time * 60 * 60 * 1000;
                    break;
                case "d":
                    mutetime += time * 60 * 60 * 24 * 1000;
                    break;
            }
        }
        
        // get the reason
        String reason = join(args, mutetime == -1 ? 1 : 2);
        
        MuteData data = new MuteData(mutetime, sender.getName(), reason);
        target.setMutedata(data);
        
        target.sendMessage(Messages.muted(data));
        chatter.sendMessage(Messages.mutedSuccess(target.getName(), data));
    }

    private String join(String[] args, int i) {
        StringBuilder builder = new StringBuilder();
        
        for (String arg : Arrays.copyOfRange(args, i, args.length)) {
            builder.append(arg).append(" ");
        }
        
        return builder.toString().trim();
    }

}
