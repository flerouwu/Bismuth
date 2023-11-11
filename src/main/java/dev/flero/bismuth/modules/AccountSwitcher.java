package dev.flero.bismuth.modules;

import dev.flero.bismuth.config.ConfigHolder;
import dev.flero.bismuth.config.ConfigValue;

@ConfigHolder(name = "modules/account_switcher")
public class AccountSwitcher {
    @ConfigValue
    public static boolean isEnabled = true;
}
