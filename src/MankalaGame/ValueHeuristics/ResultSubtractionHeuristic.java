package MankalaGame.ValueHeuristics;

import MankalaGame.Mankala;

public class ResultSubtractionHeuristic implements ValueHeuristic{

    @Override
    public int getValue(Mankala game) {
        int value = game.gameState.southWell - game.gameState.northWell;
        if(game.gameState.isFinished() && value > 0)
            value += 10;
        else if (game.gameState.isFinished() && value < 0)
            value -= 10;
        return value;
    }
}
