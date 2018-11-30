package HurricaneEvacuation;

abstract class Agent {

    int agentNum;
    Vertex location;
    protected int carrying = 0;
    int numOfMoves = 0;

    abstract Move makeOperation();

    void setLocation(Vertex location){
        this.location = location;
    }

    public int getAgentNum() {
        return agentNum;
    }

    Vertex getLocation() {
        return location;
    }

    public int getCarrying() {
        return carrying;
    }

    public void setCarrying(int carrying) {
        this.carrying += carrying;
    }

    public int getNumOfMoves() {
        return numOfMoves;
    }

    public void increaseMoves() {
        this.numOfMoves++;
    }
}
