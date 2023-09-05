package com.game.mancala;

import com.game.mancala.enums.Status;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Scanner;

@SpringBootApplication
public class MancalaApplication {
    private static final int NUM_HOUSES = 6;

    public static void main(String[] args) {
        SpringApplication.run(MancalaApplication.class, args);
        //TODO: ADD improvements for multiple games
        //TODO: Add more validation on Input
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter Number of Houses, should be either 4  or 6");
        int numOfHouses = scanner.nextInt();
        System.out.println("Enter Number of Seeds");
        int numOfSeeds = scanner.nextInt();
        MancalaGame mancalaGame = new MancalaGame(numOfSeeds, numOfHouses);
        MancalaGame.Response response = null;
        while (mancalaGame.getStatus().equals(Status.ACTIVE) || response.status() == Status.INVALID_PLAYER_MOVE || response.status() == Status.NO_SEEDS_IN_THE_HOUSE) {
            printBoard(mancalaGame);
            String TEXT_RED = "\u001B[31m";
            System.out.println(TEXT_RED + "Player " + mancalaGame.getCurrentPlayer().playerNumber() + " playing");
            int selectedPitNumber;
            do {
                System.out.println("Enter pit number from 1-: " + NUM_HOUSES);
                selectedPitNumber = scanner.nextInt();
            } while (selectedPitNumber < 0 || selectedPitNumber > NUM_HOUSES);
            response = mancalaGame.makeMove(mancalaGame.getCurrentPlayer().playerNumber(), selectedPitNumber);
        }
        System.out.println(" GAME END " + response.status());
    }

    private static void printBoard(MancalaGame mancalaGame) {
        String TEXT_RESET = "\u001B[0m";
        System.out.print(TEXT_RESET);
        final String GREEN_TEXT_CODE = "\033[0;32m";
        System.out.println("Mancala Board:::");
        String printBoard = mancalaGame.printBoard();
        System.out.println(GREEN_TEXT_CODE + printBoard);
        System.out.println();
    }
}
