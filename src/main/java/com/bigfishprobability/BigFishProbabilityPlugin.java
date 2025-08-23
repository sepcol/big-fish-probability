package com.bigfishprobability;

import com.google.inject.Provides;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.events.ChatMessage;
import net.runelite.api.events.GameTick;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.game.FishingSpot;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;

import javax.inject.Inject;
import java.time.Duration;
import java.time.Instant;

@Slf4j
@PluginDescriptor(
        name = "Big Fish Probability"
)
public class BigFishProbabilityPlugin extends Plugin {

    @Inject
    private Client client;

    @Inject
    private BigFishProbabilityConfig config;

    @Inject
    private BigFishProbabilityOverlay overlay;

    private int sharksCaught = 0;
    private int swordfishCaught = 0;
    private int bassCaught = 0;

    @Getter(AccessLevel.PACKAGE)
    private final BigFishingSession session = new BigFishingSession();

    @Getter(AccessLevel.PACKAGE)
    private FishingSpot currentSpot;

    @Inject
    private OverlayManager overlayManager;

    @Override
    protected void startUp() throws Exception {
        overlayManager.add(overlay);
    }

    @Override
    protected void shutDown() throws Exception {
        overlayManager.remove(overlay);
    }

    void reset() {
        session.setLastFishCaught(null);
    }

    @Subscribe
    public void onChatMessage(ChatMessage event) {
        if (event.getType() != ChatMessageType.SPAM) {
            return;
        }

        var message = event.getMessage();
        if (message.contains("You catch a shark!")) {
            session.setActiveFishType(FishType.SHARK);
            session.catchActiveFish(1);
            session.setLastFishCaught(Instant.now());
        } else if (message.contains("You catch a raw bass.")) {
            session.setActiveFishType(FishType.BASS);
            session.catchActiveFish(1);
            session.setLastFishCaught(Instant.now());
        } else if (message.contains("You catch a raw swordfish.")) {
            session.setActiveFishType(FishType.SWORDFISH);
            session.catchActiveFish(1);
            session.setLastFishCaught(Instant.now());
        }
    }

    @Subscribe
    public void onGameTick(GameTick event) {
        // Reset fishing session
        if (session.getLastFishCaught() != null) {
            final Duration statTimeout = Duration.ofMinutes(config.statTimeout());
            final Duration sinceCaught = Duration.between(session.getLastFishCaught(), Instant.now());

            if (sinceCaught.compareTo(statTimeout) >= 0) {
                currentSpot = null;
                session.setLastFishCaught(null);
            }
        }
    }

    @Provides
    BigFishProbabilityConfig provideConfig(ConfigManager configManager) {
        return configManager.getConfig(BigFishProbabilityConfig.class);
    }
}
