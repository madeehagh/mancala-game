package com.game.mancala;

import com.game.mancala.enums.Status;
import com.game.mancala.service.MancalaGameService;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Scanner;

@SpringBootApplication
public class MancalaApplication {
    private static final int NUM_HOUSES = 6;

    public static void main(String[] args) {
        SpringApplication.run(MancalaApplication.class, args);
        //TODO: Add more validation on Input
        Scanner scanner = new Scanner(System.in);

        //Get user input for seed and num of houses
        System.out.println();
        System.out.println("Enter Number of Houses, should be either 4  or 6");
        int numOfHouses = scanner.nextInt();
        System.out.println("Enter Number of Seeds");
        int numOfSeeds = scanner.nextInt();

        //TODO: Move this part to rest controller in future
        MancalaGameService mancalaGameService = new MancalaGameService(numOfSeeds, numOfHouses);

        String response = getResponseOfMancala(scanner, mancalaGameService, null);

        System.out.println(" GAME END " + response);
    }

    @NotNull
    private static String getResponseOfMancala(Scanner scanner, MancalaGameService mancalaGameService, MancalaGameService.Response response) {
        while (isGameActive(mancalaGameService, response)) {

            printBoard(mancalaGameService);

            final String TEXT_RED_CODE = "\u001B[31m";
            System.out.println(TEXT_RED_CODE + "Player "
                    + mancalaGameService.getCurrentPlayer().playerNumber()
                    + " playing");

            int selectedPitNumber;
            do {
                System.out.println("Enter pit number from 1-: " + NUM_HOUSES);
                selectedPitNumber = scanner.nextInt();
            } while (selectedPitNumber < 0 || selectedPitNumber > NUM_HOUSES);
            response = mancalaGameService.makeMove(
                    mancalaGameService.getCurrentPlayer().playerNumber(),
                    selectedPitNumber);
        }
        return response.status().toString();
    }

    private static boolean isGameActive(
            MancalaGameService mancalaGameService,
            MancalaGameService.Response response) {

        return mancalaGameService.getStatus().equals(Status.ACTIVE)
                || response.status() == Status.INVALID_PLAYER_MOVE
                || response.status() == Status.NO_SEEDS_IN_THE_HOUSE;
    }

    private static void printBoard(MancalaGameService mancalaGameService) {
        String TEXT_RESET = "\u001B[0m";
        System.out.print(TEXT_RESET);
        final String GREEN_TEXT_CODE = "\033[0;32m";
        System.out.println("Mancala Board:::");
        String printBoard = mancalaGameService.printBoard();
        System.out.println(GREEN_TEXT_CODE + printBoard);
        System.out.println();
    }
}
