package uk.codingbadgers.bsocial;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class Config {

    @Getter
    private final int configVersion = bSocial.CURRENT_CONFIG_VERSION;

    @Getter
    private final boolean logToConsole = true;

}
