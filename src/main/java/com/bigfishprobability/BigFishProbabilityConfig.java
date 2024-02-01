package com.bigfishprobability;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.Units;

@ConfigGroup("Big Fish Probability")
public interface BigFishProbabilityConfig extends Config {
    @ConfigItem(
            position = 7,
            keyName = "statTimeout",
            name = "Reset stats",
            description = "The time until fishing session data is reset in minutes."
    )
    @Units(Units.MINUTES)
    default int statTimeout() {
        return 5;
    }
}
