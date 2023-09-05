package com.game.mancala.model;

import com.game.mancala.enums.PlayerNumber;
import lombok.Getter;
import lombok.Setter;

public abstract class Pit {
    @Setter
    protected int seeds;

    @Getter
    public Pit next;

    protected PlayerNumber player;


    Pit ( PlayerNumber player, int seeds) {
        this.player = player;
        this.seeds = seeds;
    }

    public Pit(PlayerNumber player) {
        this.player = player;
        this.seeds = 0;
    }

    public Pit setNext(Pit next) {
        this.next = next;
        return this.next;
    }

    public int count(){
        return this.seeds;
    }

    public void addSeeds(){
        this.seeds++;
    }

    public void addSeeds(int num) {
        this.seeds += num;
    }


    abstract boolean isSowable(PlayerNumber player);
}
