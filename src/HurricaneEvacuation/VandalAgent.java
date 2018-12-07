package HurricaneEvacuation;

class VandalAgent extends Agent {

    private int vandalCounter;

    VandalAgent(int agentNum) {
        this.agentNum = agentNum;
        this.vandalCounter = 0;
    }

    @Override
    Move makeOperation() {

        Move move;

        if (vandalCounter == Simulator.getGraph().getNumberOfVertices()){
            /* Blocks an edge */
            Edge minimalEdge = getMinEdge();

            if (minimalEdge != null) {
                minimalEdge.setBlocked();
            }

            vandalCounter++;

            move = new Move(this, this.getLocation(), null);
        }
        else if (vandalCounter == 2 * Simulator.getGraph().getNumberOfVertices() + 1){
            /* Traverse */

            Edge minimalEdge = getMinEdge();

            move = new Move(this,
                    minimalEdge.getNeighbour(this.getLocation()), minimalEdge);

            vandalCounter = 0;
        }
        else{
            /* NoOp */
            vandalCounter++;
            move = new Move(this, this.getLocation(), null);
        }

        return move;
    }

    private Edge getMinEdge() {
        int minimalWeight = Integer.MAX_VALUE;
        Edge minimalEdge = null;

        for (Edge e :getLocation().getEdges().values()) {
            if ((!e.isBlocked()) && (e.getWeight() <= minimalWeight)){
                if ((e.getWeight() < minimalWeight) ||
                        e.getNeighbour(this.getLocation()).getId()
                                < minimalEdge.getNeighbour(this.getLocation()).getId()){
                    minimalEdge = e;
                    minimalWeight = e.getWeight();
                }
            }
        }
        return minimalEdge;
    }

    @Override
    void traverse(Vertex target) {
        setLocation(target);
    }

    @Override
    public String toString() {
        return "{Type: Vandal\n" + super.toString();
    }
}
