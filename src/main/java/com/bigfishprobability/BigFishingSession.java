package com.bigfishprobability;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.EnumMap;

class BigFishingSession {
    @Getter
    @Setter
    private Instant lastFishCaught;

    @Setter
    @Getter
    private FishType activeFishType;
    private final EnumMap<FishType, Integer> caughtFish;

    public BigFishingSession() {
        caughtFish = new EnumMap<>(FishType.class);
        for (FishType type : FishType.values()) {
            caughtFish.put(type, 0);
        }
    }

    public void catchFish(FishType type, int amount) {
        caughtFish.put(type, caughtFish.get(type) + amount);
    }

    public void catchActiveFish(int amount) {
        if (activeFishType != null) {
            catchFish(activeFishType, amount);
        }
    }

    public int getCaughtFish(FishType type) {
        return caughtFish.get(type);
    }

    public int getCaughtActiveFish() {
        if (activeFishType == null) return 0;
        return getCaughtFish(activeFishType);
    }

    public String getActiveFishMessage() {
        if (activeFishType == null) {
            return "Nothing caught";
        }

        int amount = getCaughtActiveFish();
        String fishName = formatFishName(activeFishType, amount);

        return "Caught " + fishName;
    }

    private String formatFishName(FishType type, int amount) {
        if (activeFishType == FishType.SHARK) {
            String name = type.name().toLowerCase(); // "shark"
            name = name.substring(0, 1).toUpperCase() + name.substring(1); // "Shark"

            // Very simple plural rule (add "s" if >1)
            if (amount != 1) {
                name += "s";
            }

            return name;
        } else return type.name().toLowerCase();
    }

    public float getProbability() {
        if (activeFishType == null) {
            return 0f;
        }

        return (getCaughtActiveFish() / activeFishType.getProbability()) * 100f;
    }

}
