package uk.codingbadgers.bsocial.exception;

@SuppressWarnings("serial")
public class ConfigException extends RuntimeException {

	public ConfigException(Throwable e) {
		super(e);
	}

	public ConfigException(String e) {
		super(e);
	}

	public ConfigException(String e, Throwable cause) {
		super(e, cause);
	}
}
