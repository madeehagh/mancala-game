package com.game.mancala.model;

import com.game.mancala.enums.PlayerNumber;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BoardTest {
    int NUM_HOUSES = 6;
    int NUM_SEEDS = 2;

    Board mancalaBoard;

    @Before
    public void setUp() {
        mancalaBoard = new Board(NUM_SEEDS, NUM_HOUSES);
    }

    @Test
    public void boardShouldHaveTwoStores() {
        List<Store> stores = this.mancalaBoard.getStores();
        assertThat(stores.size()).isEqualTo(2);
        Map<PlayerNumber, List<Store>> playerStore = stores.stream().collect(Collectors.groupingBy(Store::getOwner));
        assertThat (playerStore.get(PlayerNumber.ONE).size()).isEqualTo(1);
        assertThat (playerStore.get(PlayerNumber.TWO).size()).isEqualTo(1);
    }

    @Test
    public void housesShouldBeMutuallyOpposite(){
        Board.Players players = this.mancalaBoard.getPlayers();
        List<House> houseOfPLayer1 = players.player1().houses();
        List<House> houseOfPLayer2 = players.player2().houses();

        assertThat(houseOfPLayer1.get(0).getOpposite()).isEqualTo(houseOfPLayer2.get(0));
        assertThat(houseOfPLayer1.get(1).getOpposite()).isEqualTo(houseOfPLayer2.get(1));
        assertThat(houseOfPLayer1.get(2).getOpposite()).isEqualTo(houseOfPLayer2.get(2));
        assertThat(houseOfPLayer1.get(3).getOpposite()).isEqualTo(houseOfPLayer2.get(3));

        assertThat(houseOfPLayer2.get(0).getOpposite()).isEqualTo(houseOfPLayer1.get(0));
        assertThat(houseOfPLayer2.get(1).getOpposite()).isEqualTo(houseOfPLayer1.get(1));
        assertThat(houseOfPLayer2.get(2).getOpposite()).isEqualTo(houseOfPLayer1.get(2));
        assertThat(houseOfPLayer2.get(3).getOpposite()).isEqualTo(houseOfPLayer1.get(3));
    }

    @Test
    public void allPitsShouldFormCircle(){
        Pit first = this.mancalaBoard.getHouses().get(0);
        int count = (NUM_HOUSES * 2) + 2 ;
        Pit pit = first;
        while (count > 0) {
            pit = pit.getNext();
            count--;
        }
        assertThat(pit).isEqualTo(first);
    }
}
