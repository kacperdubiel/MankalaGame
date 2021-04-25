package MankalaGame;

public class Player {
    String name;
    boolean useAI;

    public Player(String name, boolean useAI){
        this.name = name;
        this.useAI = useAI;
    }

    public Player(Player other){
        this.name = other.name;
        this.useAI = other.useAI;
    }
}
