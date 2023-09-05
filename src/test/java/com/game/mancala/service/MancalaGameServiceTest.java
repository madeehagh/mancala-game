package com.game.mancala.service;

import com.game.mancala.enums.PlayerNumber;
import com.game.mancala.enums.Status;
import com.game.mancala.model.Player;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MancalaGameServiceTest {

    MancalaGameService mancalaGameService;
    int numOfSeeds = 2;
    int numOfHouses = 4;
    @Before
    public void setUp() {
        this.mancalaGameService = new MancalaGameService(numOfSeeds, numOfHouses);
    }

    @Test
    @DisplayName("Test Player ONE should always make the first move")
    public void playerOneShouldTakeFirstTurn() {
        Player player = this.mancalaGameService.getCurrentPlayer();
        assertThat(player.playerNumber()).isEqualTo(PlayerNumber.ONE);
    }

    @Test
    public void inActivePlayerShouldNotBeAllowedForMOve() {
        MancalaGameService.Response response = this.mancalaGameService.makeMove(PlayerNumber.TWO, 1);
        assertThat(response.status()).isEqualTo(Status.INVALID_PLAYER_MOVE);
    }

    @Test
    @DisplayName("Current Player should be able to add seeds in his/her pits")
    public void playerCanSowSeed() {
        MancalaGameService.Response response = this.mancalaGameService.makeMove(PlayerNumber.ONE, 1);
        assertThat(response.board().getHouses().get(2).count()).isEqualTo(numOfSeeds+1);
        assertThat(response.status()).isEqualTo(Status.ACTIVE);
    }

    @Test
    @DisplayName("Current Player should get Bonus Chance when last seed lands up in Store")
    public void playerGetsBonusChanceWhenLandsInStore(){
        MancalaGameService.Response response = this.mancalaGameService.makeMove(PlayerNumber.ONE, 3);
        assertThat(response.nextPlayer()).isEqualTo(PlayerNumber.ONE);
        assertThat(response.status()).isEqualTo(Status.ACTIVE);
    }

    @Test
    @DisplayName("Opposite Player should get chance for next move if that last seed doesn't land in player's store")
    public void oppositePlayerShouldGetChanceForNextMove(){
        MancalaGameService.Response response = this.mancalaGameService.makeMove(PlayerNumber.ONE, 2);
        assertThat(response.status()).isEqualTo(Status.ACTIVE);
        assertThat(response.nextPlayer()).isEqualTo(PlayerNumber.TWO);
    }

 /*   @Test
    *//**If the last stone you place lands in an empty pocket on your side of the
    board, you get to take that stone— plus all of your opponent’s stones that
    are in the opposite pocket— and place them in your own Mancala Store.
                Player Two
            | 02 | 02 | 02 | 02 |
     (00)                                 (00)
            | 02 | 02 | 02 | 02 |
                Player One
    **//*
    @DisplayName("Capture Opponent's seed if next House is Empty")
    public void captureOpponentsSeedWhenEmptyHouse(){
        MancalaGameService.Response response = this.mancalaGameService.makeMove(PlayerNumber.ONE,    4); //Player 1

        System.out.println("Next turn "+ response.nextPlayer());
        response = this.mancalaGameService.makeMove(response.nextPlayer(), 3); //Player 2
        assertThat(response.board().getStores().get(0)).isEqualTo(2);
        assertThat(response.board().getHouses().get(5)).isEqualTo(0); //seeds in opponent's house
        assertThat(response.board().getHouses().get(3)).isEqualTo(0); //seeds in current player's house
        System.out.println(response);
    }
*/

}
