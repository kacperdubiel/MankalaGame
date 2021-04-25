package MankalaGame;

import java.util.ArrayList;
import java.util.List;

public class GameState {
    List<Integer> board;

    public int northWell;
    public int northPlayerStrikes;
    public int northPlayerMoves;

    public int southWell;
    public int southPlayerStrikes;
    public int southPlayerMoves;

    public GameState(int numberOfHoles, int startingStones){
        this.board = new ArrayList<>(numberOfHoles*2);
        for(int i = 0; i < numberOfHoles * 2; i++){
            this.board.add(startingStones);
        }
    }

    public GameState(GameState other){
        this.board = new ArrayList<>();
        this.board.addAll(other.board);

        this.northWell = other.northWell;
        this.northPlayerStrikes = other.northPlayerStrikes;
        this.northPlayerMoves = other.northPlayerMoves;

        this.southWell = other.southWell;
        this.southPlayerStrikes = other.southPlayerStrikes;
        this.southPlayerMoves = other.southPlayerMoves;
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
