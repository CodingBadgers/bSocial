package uk.codingbadgers.bsocial.logging;

/**
 * 
 * @author james
 */
public interface LoggingHandler {

	public void logMessage(String sender, String channel, String message);

	public void logPM(String sender, String target, String message);

}
