package com.game.mancala.service;

import com.game.mancala.enums.PlayerNumber;
import com.game.mancala.enums.Status;
import com.game.mancala.model.Board;
import com.game.mancala.model.Pit;
import com.game.mancala.model.Player;
import lombok.Getter;

public class MancalaGameService {
    //Improvement: assign UUID for each games

    private final Board board;
    @Getter
    private Status status;

    private Player player;

    public MancalaGameService(int numOfSeeds, int numOfHouses) {
        Board board = new Board(numOfSeeds, numOfHouses);
        this.board = board;
        this.status = Status.ACTIVE;
        this.player = board.getPlayers().player1();
    }

    public record Response(Status status, PlayerNumber nextPlayer, Board board){}

    /**
     * This method checks for validation of moves and add seeds to the respective pits (if applicable to the player)
     * The status of Game is set to non-active indicating game to end. The set of status details can be found in Status enum
     * @param playerNumber: current Player
     * @param houseNumber: House selected by Current Player
     * @return
     */
    public Response makeMove(PlayerNumber playerNumber, int houseNumber) {
        if (!player.playerNumber().equals(playerNumber)) { // check if current player selects his/her own house
            this.status = Status.INVALID_PLAYER_MOVE;
        }
        else if (player.isHouseEmpty(houseNumber)) { //check if selected house has any seed
            this.status = Status.NO_SEEDS_IN_THE_HOUSE;
        } else {
            Pit lastSowedPit = player.makeMove(houseNumber);
            if (player.areAllHousesEmpty()) { // check if all houses have 0 seeds, if yes declare winner
                this.status = declareWinner();
                //TODO: empty other player pits
                System.out.println(this.status);
            }
            player = switchTurn(lastSowedPit);
        }

        return new Response(status, player.playerNumber(), board);
    }

    /**
     * This method compares the score of each player by comparing number of seeds in the store
     * @return WINNER or DRAW status
     */
    private Status declareWinner(){
        Board.Players player = this.board.getPlayers();
        int scoreOfPlayer1 = player.player1().getScore();
        int scoreOfPlayer2 = player.player2().getScore();
        if (scoreOfPlayer1 > scoreOfPlayer2)
            return Status.PLAYER_ONE_WIN;
        else if (scoreOfPlayer2 > scoreOfPlayer1)
            return Status.PLAYER_TWO_WIN;
        else
            return Status.DRAW;
    }

    public String printBoard() {
        return this.board.printBoard();
    }

    public Player getCurrentPlayer() {
        return this.player;
    }

    /**
     * This method returns the Player to play next turn.
     * However, if the last sowed seed last in Store, the current player continues to play.
     * @param currentPit: the last sowed Pit
     * @return
     */
    public Player switchTurn(Pit currentPit){
        if (currentPit.equals(player.store()))
            return player;
        Board.Players players = board.getPlayers();
        return player.playerNumber().equals(players.player1().playerNumber()) ? players.player2() : players.player1();
    }
}
