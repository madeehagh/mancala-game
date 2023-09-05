package com.game.mancala.model;

import com.game.mancala.enums.PlayerNumber;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class Board {

    //smaller pits on each side
    @Getter
    private final List<House> houses;

    //Larger pits, one for each player
    @Getter
    private List<Store> stores;
    @Getter
    private Players players;

    public Board(int numberOfSeeds, int numberOfHouses) {
        this.houses = new LinkedList<>();
        this.stores = new LinkedList<>();
        create(numberOfSeeds, numberOfHouses);
    }

    /**
     * This method creates List<>Houses</>, Store for each Player
     * It makes sure that List<Houses>, Store for each Player for a circular chain
     * and have reference to Opponent's house/store
     * @param numberOfSeeds
     * @param numberOfHouses
     */
    private void create(int numberOfSeeds, int numberOfHouses) {
        LinkedList<House> housePlayer1 = buildHouses(PlayerNumber.ONE, numberOfSeeds, numberOfHouses);
        LinkedList<House> housePlayer2 = buildHouses(PlayerNumber.TWO, numberOfSeeds, numberOfHouses);
        Store storeOfPlayer1 = new Store(PlayerNumber.ONE, 0);
        Store storeOfPlayer2 = new Store(PlayerNumber.TWO, 0);

        formCircularHouses(housePlayer1, storeOfPlayer1, housePlayer2, storeOfPlayer2);
        setOpposites(housePlayer1, housePlayer2);

        Player player1 = new Player(PlayerNumber.ONE, housePlayer1, storeOfPlayer1);
        Player player2 = new Player(PlayerNumber.TWO, housePlayer2, storeOfPlayer2);

        this.houses.addAll(housePlayer1);
        this.houses.addAll(housePlayer2);
        this.stores = List.of(storeOfPlayer1, storeOfPlayer2);
        this.players = new Players(player1, player2);
    }

    /**
     * This method creates List of Houses for the given player.
     * @param player: Player Number
     * @param seeds: Number of Seeds
     * @param length: Number of Houses
     * @return
     */
    private LinkedList<House> buildHouses(PlayerNumber player, int seeds, int length) {
        LinkedList<House> houses = new LinkedList<>();
        House prevHouse = new House(player, seeds);
        houses.add(prevHouse);
        while (houses.size() < length) {
            House house = new House(player, seeds);
            prevHouse.setNext(house);
            houses.addLast(house);
            prevHouse = house;
        }
        return houses;
    }

    /**
     * form a circle for clockwise moves
     *
     * @param houses1
     * @param storeOfPlayer1
     * @param houses2
     * @param storeOfPlayer2
     * for example:
     *  store2 <- Player2
     *  |            ^
     *  |            |
     *  Player1 -> store1
     */
    private void formCircularHouses(LinkedList<House> houses1, Store storeOfPlayer1, LinkedList<House> houses2, Store storeOfPlayer2) {
        houses1.getLast().setNext(storeOfPlayer1);
        Collections.reverse(houses2);
        storeOfPlayer1.setNext(houses2.getLast());
        houses2.getFirst().setNext(storeOfPlayer2);
        storeOfPlayer2.setNext(houses1.getFirst());
    }

    /**
     * Set opposites of the opponents for the rule when
     * the last seed lands seed ends in empty house owned by player
     * the opposite house contains seeds
     * both the last seed and opposite seeds are taken and put to player's store
     *
     * @param houses1: owned by player 1
     * @param houses2: owned by player 2
     */
    private void setOpposites(List<House> houses1, List<House> houses2) {
        for (int i = 0; i < houses1.size(); i++) {
            House one = houses1.get(i);
            House two = houses2.get(i);
            one.setOpposite(two);
            two.setOpposite(one);
        }
    }

    /**
     * Print Board in the format for visibility
     * For example
     *        Player Two
     *        | 00 | 00 | 00 | 00 |
     * (11)                                 (02)
     *        | 00 | 03 | 00 | 00 |
     *        Player One
     * @return board details as String
     */
    public String printBoard() {
        Player player1 = this.getPlayers().player1();
        Player player2 = this.getPlayers().player2();
        List<House> player2House = player2.houses();
        List<Pit> pits = new ArrayList<>(player2House);
        pits.add(player2.store());
        pits.add(player1.store());
        pits.addAll(player1.houses());

        String template = """
                                    Player Two        
                     | %02d | %02d | %02d | %02d | %02d | %02d |
              (%02d)                                             (%02d)
                     | %02d | %02d | %02d | %02d | %02d | %02d |
                                    Player One
                """;

        return String.format(template, pits.stream().map(Pit::count).toList().toArray());
    }

    public record Players(Player player1, Player player2) {
    }
}
