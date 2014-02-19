package uk.codingbadgers.bsocial.commands;

import java.util.List;

import uk.codingbadgers.bsocial.bSocial;
import uk.codingbadgers.bsocial.channel.Channel;
import uk.codingbadgers.bsocial.channel.ChannelManager;
import uk.codingbadgers.bsocial.chatter.Chatter;
import uk.codingbadgers.bsocial.messaging.Messages;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

@SuppressWarnings("deprecation")
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

		if (chatter == null) {
			sender.sendMessage(Messages.nullChatter());
			return;
		}

		if (args.length < 1) {
			chatter.sendMessage(Messages.chatUsage());
			return;
		}

		String subcommand = args[0];

		switch (subcommand) {
			case "join":
				handleJoin(sender, args, manager, chatter);
				break;
			case "leave":
				handleLeave(sender, args, manager, chatter);
				break;
			case "create":
				handleCreate(sender, args, manager, chatter);
				break;
			case "remove":
				handleRemove(sender, args, manager, chatter);
				break;
			case "list":
				handleList(sender, args, manager, chatter);
				break;
			case "focus":
			default:
				handleFocus(sender, args, manager, chatter);
				break;
		}
	}

	private void handleFocus(CommandSender sender, String[] args, ChannelManager manager, Chatter chatter) {
		if (!sender.hasPermission("bsocial.command.focus")) {
			sender.sendMessage(ProxyServer.getInstance().getTranslation("no_permission"));
			return;
		}

		if (args.length < 1) {
			return;
		}

		String channelName = args.length >= 2 ? args[1] : args[0];

		Channel channel = manager.getChannel(channelName);

		if (channel == null) {
			chatter.sendMessage(Messages.channelDoesNotExist(channelName));
			return;
		}

		if (!channel.hasChatter(chatter)) {
			chatter.sendMessage(Messages.notInChannel(channelName));
			return;
		}

		chatter.setActiveChannel(channel);
	}

	private void handleRemove(CommandSender sender, String[] args, ChannelManager manager, Chatter chatter) {
		if (!sender.hasPermission("bsocial.admin.remove")) {
			sender.sendMessage(ProxyServer.getInstance().getTranslation("no_permission"));
			return;
		}

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

	private void handleCreate(CommandSender sender, String[] args, ChannelManager manager, Chatter chatter) {
		if (!sender.hasPermission("bsocial.admin.create")) {
			sender.sendMessage(ProxyServer.getInstance().getTranslation("no_permission"));
			return;
		}

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

	private void handleList(CommandSender sender, String[] args, ChannelManager manager, Chatter chatter) {
		if (!sender.hasPermission("bsocial.command.list")) {
			sender.sendMessage(ProxyServer.getInstance().getTranslation("no_permission"));
			return;
		}

		List<Channel> channels = manager.getChannels();
		chatter.sendMessage(Messages.channelList());

		boolean empty = channels.isEmpty();

		for (Channel current : channels) {
			if (!sender.hasPermission("bsocial.channel." + current.getName())) {
				sender.sendMessage(ProxyServer.getInstance().getTranslation("no_permission"));
				return;
			}

			chatter.sendMessage(Messages.channelListElement(current.getName(), current.hasChatter(chatter)));
			empty = false;
		}

		if (empty) {
			chatter.sendMessage(Messages.noChannels());
		}
	}

	private void handleLeave(CommandSender sender, String[] args, ChannelManager manager, Chatter chatter) {
		if (!sender.hasPermission("bsocial.command.focus")) {
			sender.sendMessage(ProxyServer.getInstance().getTranslation("no_permission"));
			return;
		}

		if (args.length < 1) {
			return;
		}

		Channel channel = manager.getChannel(args[1]);

		if (channel == null) {
			chatter.sendMessage(Messages.channelDoesNotExist(args[1]));
			return;
		}

		if (!sender.hasPermission("bsocial.channel." + channel.getName())) {
			sender.sendMessage(ProxyServer.getInstance().getTranslation("no_permission"));
			return;
		}

		if (!channel.hasChatter(chatter)) {
			chatter.sendMessage(Messages.notInChannel(args[1]));
			return;
		}

		chatter.leave(channel);
	}

	private void handleJoin(CommandSender sender, String[] args, ChannelManager manager, Chatter chatter) {
		if (!sender.hasPermission("bsocial.command.join")) {
			sender.sendMessage(ProxyServer.getInstance().getTranslation("no_permission"));
			return;
		}

		if (args.length < 1) {
			return;
		}

		Channel channel = manager.getChannel(args[1]);

		if (channel == null) {
			chatter.sendMessage(Messages.channelDoesNotExist(args[1]));
			return;
		}

		if (!sender.hasPermission("bsocial.channel." + channel.getName())) {
			sender.sendMessage(ProxyServer.getInstance().getTranslation("no_permission"));
			return;
		}

		if (channel.hasChatter(chatter)) {
			chatter.sendMessage(Messages.alreadyInChannel(args[1]));
			return;
		}

		chatter.join(channel);
	}

}
