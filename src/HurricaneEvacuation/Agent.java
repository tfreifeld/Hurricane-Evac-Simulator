package HurricaneEvacuation;

abstract class Agent {

    int agentNum;
    Vertex location;
    private int carrying = 0;
    private int numOfMoves = -1;

    abstract Move makeOperation();

    void setLocation(Vertex location){
        this.location = location;
    }

    int getAgentNum() {
        return agentNum;
    }

    Vertex getLocation() {
        return location;
    }

    public int getCarrying() {
        return carrying;
    }

    void setCarrying(int carrying) {

        this.carrying = carrying;
    }

    private int getNumOfMoves() {
        return numOfMoves;
    }

    void increaseMoves() {
        this.numOfMoves++;
    }

    @Override
    public String toString() {

        return "Location: " + getLocation().getId() + "\n"
                + "Carrying: " + getCarrying() + "\n"
                + "Number of moves: " + getNumOfMoves() + "}";

    }
}
