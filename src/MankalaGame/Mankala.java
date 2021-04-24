package MankalaGame;

import java.io.IOException;
import java.util.Random;
import java.util.Scanner;

public class Mankala {
    private GameState gameState;
    private final int numberOfHoles;
    private final int startingStones;

    private Player northPlayer;
    private Player southPlayer;
    private Player currentPlayer;

    private Scanner inputScanner;
    private String message;

    public Mankala(int numberOfHoles, int startingStones, Player p1, Player p2){
        this.numberOfHoles = numberOfHoles > 0 ? numberOfHoles : 1;
        this.startingStones = startingStones > 0 ? startingStones : 1;

        southPlayer = p1;
        northPlayer = p2;

        inputScanner = new Scanner(System.in);

        gameState = new GameState(numberOfHoles, startingStones);
    }

    public void runGame(){
        drawFirstPlayer();

        while(!gameState.isFinished()) {
            currentPlayerPlay();
        }

        printGameState();
        System.out.println("GAME FINISHED!");
        Player winner = getWinner();
        currentPlayer = winner;
        if(winner == null)
            System.out.println("It's a draw!");
        else
            System.out.println("Player " + winner.name + " has won!");

        inputScanner.nextLine();
    }

    private void drawFirstPlayer(){
        Random rand = new Random();
        if(rand.nextBoolean())
            currentPlayer = southPlayer;
        else
            currentPlayer = northPlayer;
    }

    private void currentPlayerPlay(){
        if(currentPlayer.useAI){
            // TODO
            System.out.println("AI MOVE!");
        } else {
            int holeInput = getUserInput();
            moveStones(holeInput);
        }

        if(areSouthHolesEmpty())
            fillNorthWell();

        if(areNorthHolesEmpty())
            fillSouthWell();

        printGameState();
    }

    private int getUserInput(){
        boolean correctUserInput = false;
        int holeInput = -1;

        message = "It's your turn " + currentPlayer.name + "! \nEnter hole ID: ";
        printGameState();

        while(!correctUserInput){
            String userInput = this.inputScanner.nextLine();
            try{
                holeInput = Integer.parseInt(userInput);
                if(holeInput >= 0 && holeInput < numberOfHoles)
                    if(isSouthTurn() && gameState.board.get(holeInput) > 0)
                        correctUserInput = true;
                    else if(isNorthTurn() && gameState.board.get(holeInput+numberOfHoles) > 0)
                        correctUserInput = true;
                    else{
                        String warning = "That hole is empty!";
                        message = "It's your turn " + currentPlayer.name + "! \n" + warning + "\nEnter hole ID: ";
                        printGameState();
                    }
                else {
                    String warning = "Wrong hole ID!";
                    message = "It's your turn " + currentPlayer.name + "! \n" + warning + "\nEnter hole ID: ";
                    printGameState();
                }
            }
            catch (Exception e){
                String warning = "Wrong hole ID!";
                message = "It's your turn " + currentPlayer.name + "! \n" + warning + "\nEnter hole ID: ";
                printGameState();
            }
        }
        message = "";
        return holeInput;
    }

    private boolean isSouthTurn(){
        return currentPlayer.equals(southPlayer);
    }

    private boolean isNorthTurn(){
        return currentPlayer.equals(northPlayer);
    }

    private void moveStones(int holeInput){
        int currentHoleId = isSouthTurn() ? holeInput : holeInput + numberOfHoles;
        int stonesRemaining = gameState.board.get(currentHoleId);
        gameState.board.set(currentHoleId, 0);
        currentHoleId++;

        boolean changePlayerTurn = true;
        while(stonesRemaining > 0){
            if(currentHoleId == numberOfHoles && isSouthTurn()){
                stonesRemaining--;
                gameState.southWell++;
                if(stonesRemaining == 0)
                    changePlayerTurn = false;
                else{
                    stonesRemaining--;
                    gameState.addStones(currentHoleId, 1);
                    currentHoleId++;
                }
            }
            else if(currentHoleId == numberOfHoles * 2){
                if(isNorthTurn()){
                    stonesRemaining--;
                    gameState.northWell++;
                    if(stonesRemaining == 0)
                        changePlayerTurn = false;
                }
                currentHoleId = 0;
            }else{
                stonesRemaining--;
                gameState.addStones(currentHoleId, 1);

                // Strike
                if(stonesRemaining == 0){
                    int oppositeHoleId = 2*numberOfHoles - 1 - currentHoleId;
                    if(isSouthTurn() && currentHoleId < numberOfHoles ||
                            isNorthTurn() && currentHoleId >= numberOfHoles && currentHoleId < numberOfHoles*2) {
                        // Ended on one of current player holes - strike if possible!
                        strike(currentHoleId, oppositeHoleId);
                    }
                }

                currentHoleId++;
            }
        }

        if(changePlayerTurn){
            changeTurn();
        }
    }

    private boolean strike(int currentHoleId, int oppositeHoleId){
        int currentStones = gameState.board.get(currentHoleId);
        if(currentStones == 1){
            int oppositeStones = gameState.board.get(oppositeHoleId);
            if(oppositeStones > 0){
                if(isSouthTurn()) {
                    gameState.southWell += oppositeStones;
                    gameState.southWell += currentStones;
                } else {
                    gameState.northWell += oppositeStones;
                    gameState.northWell += currentStones;
                }

                gameState.board.set(currentHoleId, 0);
                gameState.board.set(oppositeHoleId, 0);
            }
        }
        return true;
    }

    private void changeTurn(){
        if(isSouthTurn())
            currentPlayer = northPlayer;
        else
            currentPlayer = southPlayer;
    }

    private boolean areSouthHolesEmpty(){
        for(int i = 0; i < numberOfHoles; i++){
            if(gameState.board.get(i) > 0)
                return false;
        }
        return true;
    }

    private boolean areNorthHolesEmpty(){
        for(int i = numberOfHoles; i < numberOfHoles*2; i++){
            if(gameState.board.get(i) > 0)
                return false;
        }
        return true;
    }

    private void fillSouthWell(){
        for(int i = 0; i < numberOfHoles; i++){
            gameState.southWell += gameState.board.get(i);
            gameState.board.set(i, 0);
        }
    }

    private void fillNorthWell(){
        for(int i = numberOfHoles; i < numberOfHoles * 2; i++){
            gameState.northWell += gameState.board.get(i);
            gameState.board.set(i, 0);
        }
    }

    private Player getWinner(){
        if(gameState.southWell == gameState.northWell)
            return null;
        else if(gameState.southWell > gameState.northWell)
            return southPlayer;
        else
            return northPlayer;
    }

    private void printGameState(){
        // Uncomment if running from IDE for clearer output
        // System.out.println(new String(new char[50]).replace("\0", "\r\n"));
        try {
            new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
        System.out.println();

        if(isNorthTurn()) {
            printSpaces(11 + (numberOfHoles * 2) - (northPlayer.name.length()/2));
            System.out.print("> " + northPlayer.name + " <");
        } else {
            printSpaces(13 + (numberOfHoles * 2) - (northPlayer.name.length()/2));
            System.out.print(northPlayer.name);

        }
        System.out.println("\n");

        System.out.print(" Hole ID:    ");
        for(int i = numberOfHoles - 1; i >= 0; i--)
            System.out.print(" " + i + "  ");
        System.out.println();

        printSpaces(13);
        for(int i = numberOfHoles * 2 - 1; i >= numberOfHoles; i--)
            System.out.print("[" + gameState.board.get(i) + "] ");
        System.out.println("\n");

        printSpaces(10);
        System.out.printf("%02d", gameState.northWell);
        printSpaces(numberOfHoles * 4 + 1);
        System.out.printf("%02d", gameState.southWell);
        System.out.println("\n");

        printSpaces(13);
        for(int i = 0; i < numberOfHoles; i++)
            System.out.print("[" + gameState.board.get(i) + "] ");
        System.out.println();

        System.out.print(" Hole ID:    ");
        for(int i = 0; i < numberOfHoles; i++)
            System.out.print(" " + i + "  ");
        System.out.println("\n");

        if(isSouthTurn()) {
            printSpaces(11 + (numberOfHoles * 2) - (southPlayer.name.length()/2));
            System.out.print("> " + southPlayer.name + " <");
        }
        else {
            printSpaces(13 + (numberOfHoles * 2) - (southPlayer.name.length()/2));
            System.out.print(southPlayer.name);
        }
        System.out.println("\n");

        if(message.length() > 0)
            System.out.println(message);
    }

    static void printSpaces(int amount){
        System.out.print(new String(new char[amount]).replace("\0", " "));
    }
}
