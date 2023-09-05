package com.game.mancala.model;

import com.game.mancala.enums.PlayerNumber;
import lombok.Getter;
import lombok.Setter;

public class House extends Pit{

    @Getter
    @Setter
    private House opposite;

    House(PlayerNumber player, int seeds){
        super(player, seeds);
    }

    public int take(){
        int seed = this.count();
        this.setSeeds(0);
        return seed;
    }

    @Override
    boolean isSowable(PlayerNumber player) {
        return true;
    }
}

