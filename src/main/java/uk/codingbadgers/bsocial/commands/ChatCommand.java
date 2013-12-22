package uk.codingbadgers.bsocial.commands;

import java.util.List;

import uk.codingbadgers.bsocial.bSocial;
import uk.codingbadgers.bsocial.channel.Channel;
import uk.codingbadgers.bsocial.channel.ChannelManager;
import uk.codingbadgers.bsocial.chatter.Chatter;
import uk.codingbadgers.bsocial.messaging.Messages;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class ChatCommand extends Command {

	public ChatCommand() {
		super("chat", "bsocial.command.chat", "ch");
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		
		if (!(sender instanceof ProxiedPlayer)) {
			sender.sendMessage(Messages.playerOnly());
			return;
		}
		
		ChannelManager manager = bSocial.getChannelManager();
		Chatter chatter = bSocial.getChatterManager().getChatter((ProxiedPlayer) sender);

		if (args.length < 1) {
			chatter.sendMessage(Messages.chatUsage());
			return;
		}
		
		String subcommand = args[0];
		
		switch(subcommand) {
			case "join":
				handleJoin(args, manager, chatter);
				break;
			case "leave":
				handleLeave(args, manager, chatter);
				break;
			case "create":
				handleCreate(args, manager, chatter);
				break;
			case "remove":
				handleRemove(args, manager, chatter);
				break;
			case "list":
				handleList(args, manager, chatter);
				break;
			case "focus":
			default:
				handleFocus(args, manager, chatter);
				break;
		}
	}

	private void handleFocus(String[] args, ChannelManager manager, Chatter chatter) {
		if (args.length < 1) {
			return;
		}
		
		Channel channel = manager.getChannel(args[1]);
		
		if (channel == null) {
			chatter.sendMessage(Messages.channelDoesNotExist(args[1]));
			return;
		}
		
		if (!channel.hasChatter(chatter)) {
			chatter.sendMessage(Messages.notInChannel(args[1]));
			return;
		}

		chatter.setActiveChannel(channel);
	}

	private void handleRemove(String[] args, ChannelManager manager, Chatter chatter) {
		if (args.length < 1) {
			return;
		}
		
		String name = args[1];

		Channel channel = manager.getChannel(name);
		
		if (channel == null) {
			chatter.sendMessage(Messages.channelDosntExist(name));
			return;
		}
		
		manager.removeChannel(channel);
		chatter.sendMessage(Messages.channelRemoved(name));
	}

	private void handleCreate(String[] args, ChannelManager manager, Chatter chatter) {
		if (args.length < 1) {
			return;
		}
		
		String name = args[1];
		
		if (manager.exists(name)) {
			chatter.sendMessage(Messages.channelAlreadyExists(name));
			return;
		}

		Channel channel = new Channel(name);
		manager.addChannel(channel);
		chatter.sendMessage(Messages.channelCreated(name));
	}

	private void handleList(String[] args, ChannelManager manager, Chatter chatter) {
		List<Channel> channels = manager.getChannels();
		chatter.sendMessage(Messages.channelList());
		
		if (channels.size() == 0) {
			chatter.sendMessage(Messages.noChannels());
			return;
		}
		
		for (Channel current : channels) {
			chatter.sendMessage(Messages.channelListElement(current.getName()));
		}
	}

	private void handleLeave(String[] args, ChannelManager manager, Chatter chatter) {
		if (args.length < 1) {
			return;
		}
		
		Channel channel = manager.getChannel(args[1]);
		
		if (channel == null) {
			chatter.sendMessage(Messages.channelDoesNotExist(args[1]));
			return;
		}
		
		if (!channel.hasChatter(chatter)) {
			chatter.sendMessage(Messages.notInChannel(args[1]));
			return;
		}

		chatter.leave(channel);
	}

	private void handleJoin(String[] args, ChannelManager manager, Chatter chatter) {
		if (args.length < 1) {
			return;
		}
		
		Channel channel = manager.getChannel(args[1]);
		
		if (channel == null) {
			chatter.sendMessage(Messages.channelDoesNotExist(args[1]));
			return;
		}
		
		if (channel.hasChatter(chatter)) {
			chatter.sendMessage(Messages.alreadyInChannel(args[1]));
			return;
		}
		
		chatter.join(channel);
	}

}
