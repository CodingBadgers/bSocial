package uk.codingbadgers.bsocial.logging;

import com.google.gson.Gson;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import lombok.AllArgsConstructor;
import lombok.Data;
import uk.codingbadgers.bsocial.bSocial;
import uk.codingbadgers.bsocial.exception.ConfigException;
import uk.codingbadgers.bsocial.json.NonSerialized;

/**
 * 
 * @author james
 */
@Data
public class JsonLogHandler implements LoggingHandler {

	private static final DateFormat LOG_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static final DateFormat FILE_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
	private static final Gson GSON = bSocial.getGson();

	@Data
	@AllArgsConstructor
	private static final class LogMessage {
		private String time;
		private String sender;
		private String target;
		private String message;
	}

	private final List<LogMessage> messages = new ArrayList<>();
	@NonSerialized
	private File log = null;

	public JsonLogHandler() {
		int i = 0;

		File logsDir = new File(bSocial.getInstance().getDataFolder(), "logs");

		if (!logsDir.exists() && !logsDir.mkdirs()) {
			throw new ConfigException("Could not create log directory");
		}

		do {
			log = new File(logsDir, FILE_DATE_FORMAT.format(new Date()) + "." + i++ + ".json");
		} while (log.exists());
	}

	@Override
	public void logMessage(String sender, String channel, String message) {
		messages.add(new LogMessage(now(), sender, channel, message));

		save();
	}

	@Override
	public void logPM(String sender, String target, String message) {
		messages.add(new LogMessage(now(), sender, target, message));

		save();
	}

	private void save() {
		try (FileWriter writer = new FileWriter(log)) {
			GSON.toJson(this, writer);
			writer.flush();
			writer.close();
		} catch (IOException ex) {
			bSocial.getInstance().getLogger().log(Level.SEVERE, "Error saving log to file", ex);
		}
	}

	private String now() {
		return LOG_DATE_FORMAT.format(new Date());
	}

}
