package uk.codingbadgers.bsocial;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class Config {

	@Getter private int configVersion = bSocial.CURRENT_CONFIG_VERSION;
	
	@Getter private boolean logToConsole = true;
	
}
