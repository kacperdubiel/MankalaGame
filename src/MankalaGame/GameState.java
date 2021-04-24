package MankalaGame;

import java.util.ArrayList;
import java.util.List;

public class GameState {
    List<Integer> board;
    int southWell;
    int northWell;

    public GameState(int numberOfHoles, int startingStones){
        this.board = new ArrayList<>(numberOfHoles*2);
        for(int i = 0; i < numberOfHoles * 2; i++){
            this.board.add(startingStones);
        }
    }

    public boolean isFinished(){
        for(int stonesInHole : board){
            if (stonesInHole > 0)
                return false;
        }
        return true;
    }

    public void addStones(int holeId, int numberOfStones){
        int currentStonesAmount = board.get(holeId);
        board.set(holeId, currentStonesAmount + numberOfStones);
    }
}
