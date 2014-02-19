package uk.codingbadgers.bsocial.logging;

import lombok.AllArgsConstructor;

/**
 * 
 * @author james
 */
@AllArgsConstructor
public enum LogHandlers {

	CONSOLE(ConsoleLogHandler.class), JSON(JsonLogHandler.class), SQL(null), // TODO sql logging
	;

	private Class<? extends LoggingHandler> handler;

	public LoggingHandler newHandler() throws ReflectiveOperationException {
		return handler.newInstance();
	}
}
