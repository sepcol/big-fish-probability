package com.bigfishprobability;

import lombok.Getter;

@Getter
public enum FishType {
    SHARK(5000f),
    SWORDFISH(2500f),
    BASS(1000f);

    private final float probability;

    FishType(float probability) {
        this.probability = probability;
    }

}
