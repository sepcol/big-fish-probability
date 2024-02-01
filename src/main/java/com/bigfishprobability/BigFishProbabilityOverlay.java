/*
 * Copyright (c) 2017, Seth <Sethtroll3@gmail.com>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.bigfishprobability;

import com.google.common.collect.ImmutableSet;
import net.runelite.api.AnimationID;
import net.runelite.api.Client;
import net.runelite.api.GraphicID;
import net.runelite.client.ui.overlay.OverlayPanel;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.components.LineComponent;
import net.runelite.client.ui.overlay.components.TitleComponent;

import javax.inject.Inject;
import java.awt.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Set;

import static net.runelite.api.MenuAction.RUNELITE_OVERLAY;
import static net.runelite.api.MenuAction.RUNELITE_OVERLAY_CONFIG;
import static net.runelite.client.ui.overlay.OverlayManager.OPTION_CONFIGURE;

class BigFishProbabilityOverlay extends OverlayPanel {
    private static final String FISHING_SPOT = "Fishing spot";
    private static final String FISHING_RESET = "Reset";

    private static final Set<Integer> FISHING_ANIMATIONS = ImmutableSet.of(
            AnimationID.FISHING_BARBTAIL_HARPOON,
            AnimationID.FISHING_BAREHAND,
            AnimationID.FISHING_BAREHAND_CAUGHT_SHARK_1,
            AnimationID.FISHING_BAREHAND_CAUGHT_SHARK_2,
            AnimationID.FISHING_BAREHAND_CAUGHT_SWORDFISH_1,
            AnimationID.FISHING_BAREHAND_CAUGHT_SWORDFISH_2,
            AnimationID.FISHING_BAREHAND_CAUGHT_TUNA_1,
            AnimationID.FISHING_BAREHAND_CAUGHT_TUNA_2,
            AnimationID.FISHING_BAREHAND_WINDUP_1,
            AnimationID.FISHING_BAREHAND_WINDUP_2,
            AnimationID.FISHING_BIG_NET,
            AnimationID.FISHING_CAGE,
            AnimationID.FISHING_CRYSTAL_HARPOON,
            AnimationID.FISHING_DRAGON_HARPOON,
            AnimationID.FISHING_DRAGON_HARPOON_OR,
            AnimationID.FISHING_HARPOON,
            AnimationID.FISHING_INFERNAL_HARPOON,
            AnimationID.FISHING_TRAILBLAZER_HARPOON,
            AnimationID.FISHING_KARAMBWAN,
            AnimationID.FISHING_NET,
            AnimationID.FISHING_OILY_ROD,
            AnimationID.FISHING_POLE_CAST,
            AnimationID.FISHING_PEARL_ROD,
            AnimationID.FISHING_PEARL_FLY_ROD,
            AnimationID.FISHING_PEARL_BARBARIAN_ROD,
            AnimationID.FISHING_PEARL_ROD_2,
            AnimationID.FISHING_PEARL_FLY_ROD_2,
            AnimationID.FISHING_PEARL_BARBARIAN_ROD_2,
            AnimationID.FISHING_PEARL_OILY_ROD,
            AnimationID.FISHING_BARBARIAN_ROD);

    private final Client client;
    private final BigFishProbabilityPlugin plugin;
    private final BigFishProbabilityConfig config;

    @Inject
    public BigFishProbabilityOverlay(Client client, BigFishProbabilityPlugin plugin, BigFishProbabilityConfig config) {
        super(plugin);
        setPosition(OverlayPosition.TOP_LEFT);
        this.client = client;
        this.plugin = plugin;
        this.config = config;
        addMenuEntry(RUNELITE_OVERLAY_CONFIG, OPTION_CONFIGURE, "BigFishProbability overlay");
        addMenuEntry(RUNELITE_OVERLAY, FISHING_RESET, "BigFishProbability overlay", e -> plugin.reset());
    }

    @Override
    public Dimension render(Graphics2D graphics) {
        if (plugin.getSession().getLastFishCaught() == null) {
            return null;
        }

        if (client.getLocalPlayer().getInteracting() != null
                && client.getLocalPlayer().getInteracting().getName().contains(FISHING_SPOT)
                && client.getLocalPlayer().getInteracting().getGraphic() != GraphicID.FLYING_FISH
                && FISHING_ANIMATIONS.contains(client.getLocalPlayer().getAnimation())) {
            panelComponent.getChildren().add(TitleComponent.builder()
                    .text("Fishing")
                    .color(Color.GREEN)
                    .build());
        } else {
            panelComponent.getChildren().add(TitleComponent.builder()
                    .text("NOT fishing")
                    .color(Color.RED)
                    .build());
        }

        if (plugin.getSession().getSharksFishedAmount() > 0) {
            panelComponent.getChildren().add(LineComponent.builder()
                    .left("Caught sharks:")
                    .right(Integer.toString((int) plugin.getSession().getSharksFishedAmount()))
                    .build());

            var probability = (plugin.getSession().getSharksFishedAmount() / 5000f) * 100f;
            BigDecimal bd = new BigDecimal(probability).setScale(2, RoundingMode.HALF_UP);

            panelComponent.getChildren().add(LineComponent.builder()
                    .left("Big shark probability:")
                    .right(Double.toString(bd.doubleValue()))
                    .build());
        }

        return super.render(graphics);
    }
}
