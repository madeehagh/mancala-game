package com.game.mancala.model;

import com.game.mancala.enums.PlayerNumber;

import java.util.List;
import java.util.function.Predicate;

public record Player(PlayerNumber playerNumber, List<House> houses, Store store) {

    public Pit makeMove(int houseNumber) {
        House house = getHouse(houseNumber);
        if (!checkHasSeeds(house))
            return house;

        Pit pit = takeTurn(house);

        return pit;
    }

    private Pit takeTurn(House house) {
        int seeds = house.take();
        Pit pit = house;
        while (seeds > 0) {
            pit = pit.next;
            if (pit.isSowable(playerNumber)) {
                seeds--;
                pit.addSeeds();
            }
        }
        if(isEmptyHouseRuleApplicable(pit, playerNumber)) {
            house = (House) pit;
            int seedsInCurrentHouse =  house.take();
            int seedsInOppositeHouse = house.getOpposite().take();
            this.store.addSeed(seedsInCurrentHouse + seedsInOppositeHouse);
        }
        return pit;
    }

    // If the last stone you place lands in an empty pocket on your side of the
    //board, you get to take that stone— plus all of your opponent’s stones that
    //are in the opposite pocket— and place them in your own Mancala Store.
    private boolean isEmptyHouseRuleApplicable(Pit pit, PlayerNumber playerNumber) {
        // pit.seed == 1
        // pit.house === player's house
        // opponent.house numOFseed > 0
        // pit != store
        return (pit.count() == 1) && !(pit instanceof Store) && (pit.player == playerNumber);
    }

    public boolean checkHasSeeds(House house) {
        return house == null || house.count() != 0;
    }

    private House getHouse(int houseNum) {
        return this.houses.get(houseNum -1);
    }

    public int getScore(){
        return store.count();
    }

    public boolean hasNoSeeds() {
        Predicate<House> emptyHouse = house -> house.count() == 0;
        return houses.stream().allMatch(emptyHouse);
    }
}
