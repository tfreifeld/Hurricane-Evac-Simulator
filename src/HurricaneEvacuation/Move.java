package HurricaneEvacuation;

class Move {

    private final Agent agent;
    private final Vertex target;
    private final Edge edge;

    Move(Agent agent, Vertex target, Edge edge) {
        this.agent = agent;
        this.target = target;
        this.edge = edge;
    }

    Agent getAgent() {
        return agent;
    }

    Vertex getTarget() {
        return target;
    }

    Edge getEdge() {
        return edge;
    }
}
