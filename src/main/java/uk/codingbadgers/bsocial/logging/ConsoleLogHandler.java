package uk.codingbadgers.bsocial.logging;

import java.util.logging.Level;
import uk.codingbadgers.bsocial.bSocial;

/**
 *
 * @author james
 */
public class ConsoleLogHandler implements LoggingHandler {

    @Override
    public void logMessage(String sender, String channel, String message) {
        bSocial.getInstance().getLogger().log(Level.INFO, "[{0}->{1}] {2}", new Object[] {sender, channel, message});
    }

    @Override
    public void logPM(String sender, String target, String message) {
        bSocial.getInstance().getLogger().log(Level.INFO, "[{0}->{1}] {2}", new Object[] {sender, target, message});
    }
}
