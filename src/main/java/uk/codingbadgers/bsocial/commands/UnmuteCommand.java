package uk.codingbadgers.bsocial.commands;

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
