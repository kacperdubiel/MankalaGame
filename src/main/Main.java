package main;

import MankalaGame.Mankala;
import MankalaGame.Player;
import MankalaGame.ValueHeuristics.ResultSubtractionHeuristic;
import MankalaGame.ValueHeuristics.ValueHeuristic;

public class Main {

    public static void main(String[] args) {
        int numberOfHoles = 6;
        int startingStones = 4;
        int depth = 5;

        ValueHeuristic valueHeuristic = new ResultSubtractionHeuristic();

        Player p1 = new Player("Gracz 1", false);
        Player p2 = new Player("Gracz 2", true);

        Mankala mankalaGame = new Mankala(numberOfHoles, startingStones, depth, valueHeuristic, p1, p2);
        mankalaGame.runGame();
    }
}
