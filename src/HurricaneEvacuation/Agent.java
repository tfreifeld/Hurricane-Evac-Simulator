package HurricaneEvacuation;

abstract class Agent {

    int agentNum;
    Vertex location;
    private int carrying = 0;
    private int numOfMoves = 0;

    abstract Move makeOperation();

    void setLocation(Vertex location) {
        this.location = location;
    }

    void traverse(Vertex target) {

        setLocation(target);
        if (target.isShelter()) {
            Simulator.safeCount += getCarrying();
            setCarrying(0);
        } else {
            setCarrying(getCarrying() + target.getPersons());
            target.setPersons(0);
        }

    }

    int getAgentNum() {
        return agentNum;
    }

    Vertex getLocation() {
        return location;
    }

    int getCarrying() {
        return carrying;
    }

    void setCarrying(int carrying) {

        this.carrying = carrying;
    }

    int getNumOfMoves() {
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
