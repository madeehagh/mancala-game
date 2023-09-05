package com.game.mancala.model;

import com.game.mancala.enums.PlayerNumber;
import lombok.Getter;

public class Store extends Pit {

    //property to make sure the player can add seed only to his own Store
    @Getter
    private final PlayerNumber owner;

    Store(PlayerNumber playerNumber) {
        super(playerNumber);
        this.owner = playerNumber;
    }
    Store(PlayerNumber playerNumber, int seeds) {
        super(playerNumber, seeds);
        this.owner = playerNumber;
    }

    @Override
    boolean isSowable(PlayerNumber playerNumber) {
        return playerNumber == this.owner;
    }

    public void addSeed(int num){
        addSeeds(num);
    }
}
