package com.bigfishprobability;

import lombok.Getter;

@Getter
public enum FishType {
    SHARK("You catch a shark", 5000f),
    SWORDFISH("You catch a raw bass", 2500f),
    BASS("You catch a raw swordfish", 1000f);

    private final String catchMessage;
    private final float probability;

    FishType(String catchMessage, float probability) {
        this.catchMessage = catchMessage;
        this.probability = probability;
    }

    public boolean matches(String message) {
        return message.contains(catchMessage);
    }

}
