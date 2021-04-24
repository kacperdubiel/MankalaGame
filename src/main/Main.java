package main;

import MankalaGame.Mankala;
import MankalaGame.Player;

public class Main {

    public static void main(String[] args) {
        int numberOfHoles = 6;
        int startingStones = 4;

        Player p1 = new Player("Kacper", false);
        Player p2 = new Player("JeszczeNieAI", false);

        Mankala mankalaGame = new Mankala(numberOfHoles, startingStones, p1, p2);
        mankalaGame.runGame();
    }
}
