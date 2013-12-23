package uk.codingbadgers.bsocial;

import lombok.Getter;

public class Config {

    public static class AntiSpam {
        @Getter
        private final long loginDelay = 3000;
        @Getter
        private final long messageDelay = 1000;
        @Getter
        private final int capsPercentage = 75;
    }
    
    @Getter
    private final int configVersion = bSocial.CURRENT_CONFIG_VERSION;

    @Getter
    private final boolean logToConsole = true;
    
    @Getter
    private final String pmFormat = "&9{sender}->{target}: &b{message}";

    @Getter
    private final AntiSpam antiSpamSettings = new AntiSpam();
    
}
