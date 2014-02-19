package uk.codingbadgers.bsocial;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import lombok.Getter;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;
import uk.codingbadgers.bsocial.channel.ChannelManager;
import uk.codingbadgers.bsocial.chatter.Chatter;
import uk.codingbadgers.bsocial.chatter.ChatterManager;
import uk.codingbadgers.bsocial.commands.ChatCommand;
import uk.codingbadgers.bsocial.commands.MuteCommand;
import uk.codingbadgers.bsocial.commands.PrivateMessageCommand;
import uk.codingbadgers.bsocial.commands.UnmuteCommand;
import uk.codingbadgers.bsocial.exception.ConfigException;
import uk.codingbadgers.bsocial.json.JsonExclusionStrategy;
import uk.codingbadgers.bsocial.listener.EventListener;
import uk.codingbadgers.bsocial.logging.LoggingHandler;

public class bSocial extends Plugin {

	public static final int CURRENT_CONFIG_VERSION = 0x01;
	@Getter
	private static final Gson gson = new GsonBuilder()
										.setExclusionStrategies(new JsonExclusionStrategy())
										.setFieldNamingPolicy(FieldNamingPolicy.IDENTITY)
										.setPrettyPrinting()
										.create();

	@Getter
	private static bSocial instance;
	@Getter
	private static Config config;
	@Getter
	private static ChatterManager chatterManager;
	@Getter
	private static ChannelManager channelManager;
	@Getter
	private static LoggingHandler logHandler;

	@Override
	public void onLoad() {
		instance = this;
	}

	@Override
	public void onEnable() {
		chatterManager = new ChatterManager();
		channelManager = new ChannelManager();

		loadConfig();

		channelManager.loadChannels();

		getProxy().getPluginManager().registerListener(this, new EventListener());

		getProxy().getPluginManager().registerCommand(this, new ChatCommand());
		getProxy().getPluginManager().registerCommand(this, new MuteCommand());
		getProxy().getPluginManager().registerCommand(this, new UnmuteCommand());
		getProxy().getPluginManager().registerCommand(this, new PrivateMessageCommand());

		for (ProxiedPlayer player : ProxyServer.getInstance().getPlayers()) {
			chatterManager.loadChatter(player);
		}
	}

	@Override
	public void onDisable() {

		for (Chatter chatter : chatterManager.getChatters()) {
			chatterManager.removeChatter(chatter);
		}

		instance = null;
		chatterManager = null;
		channelManager = null;
	}

	private void loadConfig() throws ConfigException {
		config = new Config();
		File configFile = new File(this.getDataFolder(), "config.json");

		try {
			if (!configFile.exists()) {
				if (!configFile.getParentFile().exists()) {
					if (!configFile.getParentFile().mkdirs()) {
						throw new ConfigException("Error creating datafolder");
					}
				}

				try (FileWriter writer = new FileWriter(configFile)) {
					gson.toJson(config, writer);
					writer.flush();
				}
			}

			try (FileReader reader = new FileReader(configFile)) {
				config = gson.fromJson(reader, Config.class);
			}

			if (config == null || config.getConfigVersion() != CURRENT_CONFIG_VERSION) {
				File backup = new File(this.getDataFolder(), "config.json." + config.getConfigVersion());

				getLogger().log(Level.WARNING, "----------------------------------------------------------------");
				getLogger().log(Level.WARNING, "Outdated config, regenerating.");
				getLogger().log(Level.WARNING, "The old config will be located at \"plugins/bSocial/{0}\".", backup.getName());
				getLogger().log(Level.WARNING, "Please note you will have to resetup parts of the config");
				getLogger().log(Level.WARNING, "----------------------------------------------------------------");

				if (backup.exists() && !backup.delete()) {
					throw new ConfigException("Error deleting backup config file (" + backup.getName() + ")");
				}

				if (!configFile.renameTo(backup)) {
					throw new ConfigException("Error backing up old config file");
				}

				if (configFile.exists() && !configFile.delete()) {
					throw new ConfigException("Failed to delete old config file");
				}

				try (FileWriter writer = new FileWriter(configFile)) {
					config = new Config();
					gson.toJson(config, writer);
					writer.flush();
				}
			}

		} catch (JsonIOException | IOException e) {
			throw new ConfigException(e);
		}

		try {
			logHandler = config.getLogHandler().newHandler();
		} catch (ReflectiveOperationException ex) {
			getLogger().log(Level.SEVERE, "Error setting up log handler", ex);
		}
	}
}
