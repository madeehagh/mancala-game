package com.game.mancala.model;

import com.game.mancala.enums.PlayerNumber;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThrows;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PlayerTest {

    House houseOfPlayer1;

    @Before
    public void setUp() {
        houseOfPlayer1 = new House(PlayerNumber.ONE, 0);
    }

    @Test
    @DisplayName("Test Player should not be able to choose empty House")
    public void playerCanNotChooseEmptyHouse(){
        Player player = new Player(PlayerNumber.ONE, List.of(houseOfPlayer1), new Store(PlayerNumber.ONE));
        assertThat (player.makeMove(1)).isEqualTo(houseOfPlayer1);
    }

    @Test
    @DisplayName("Test Player should not be allowed to choose house beyond range")
    public void playerCanNotChooseHouseBeyondRange() {
        Player player = new Player(PlayerNumber.ONE, List.of(houseOfPlayer1), new Store(PlayerNumber.ONE));
        assertThrows(IndexOutOfBoundsException.class, () -> {
            player.makeMove(0);
        });
    }

    @Test
    @DisplayName("Test Player should only be allowed to add seed on his/her turn")
    public void playerShouldAddSeedOnTurn() {
        House first = new House(PlayerNumber.ONE, 2);
        House second = new House(PlayerNumber.ONE, 0);
        Store store = new Store(PlayerNumber.ONE);
        first.setNext(second).setNext(store);
        Player player = new Player(PlayerNumber.ONE, List.of(first, second), store);
        Pit lastSowedPit = player.makeMove(1);
        assertThat(lastSowedPit).isEqualTo(store);
        assertThat(first.count()).isZero();
        assertThat(second.count()).isEqualTo(1);
        assertThat(store.count()).isEqualTo(1);
    }

    @Test
    @DisplayName("Test Player should not be allowed to add seed in opponent's store")
    /**
     * store2 <- Player2 2
     * |                |
     * Player1 ,1 -> store1 1
     */
    public void playerShouldNotAddInOpponentsStore(){
        House currentPlayerHouse = new House(PlayerNumber.ONE, 3);
        Store currentPlayerStore = new Store(PlayerNumber.ONE);
        House opponentHouse = new House(PlayerNumber.TWO, 1);
        Store opponentStore = new Store(PlayerNumber.TWO);

        currentPlayerHouse.setOpposite(opponentHouse);
        currentPlayerHouse.setNext(currentPlayerStore).setNext(opponentHouse).setNext(opponentStore).setNext(currentPlayerHouse);

        Player currentPlayer = new Player(PlayerNumber.ONE, List.of(currentPlayerHouse), currentPlayerStore);

        Pit lastSowedPit = currentPlayer.makeMove(1);

        assertThat(lastSowedPit).isEqualTo(currentPlayerHouse);
        assertThat(opponentStore.count()).isZero();
    }

    @Test
    @DisplayName("Test Player should get bonus chance if the last seed land in the store")
    public void playerGetBonusChanceLastMoveStore() {
        House first = new House(PlayerNumber.ONE, 2);
        House second = new House(PlayerNumber.ONE, 0);
        Store store = new Store(PlayerNumber.ONE);
        first.setNext(second).setNext(store);
        Player player = new Player(PlayerNumber.ONE, List.of(first, second), store);
        Pit lastSowedPit = player.makeMove(1);
        assertThat (lastSowedPit.player).isEqualTo(PlayerNumber.ONE);
    }
}
