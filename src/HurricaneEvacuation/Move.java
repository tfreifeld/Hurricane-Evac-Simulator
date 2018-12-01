package HurricaneEvacuation;

public class Move {

    final Agent agent;
    final Vertex target;

    public Move(Agent agent, Vertex target) {
        this.agent = agent;
        this.target = target;
    }

    public Agent getAgent() {
        return agent;
    }

    public Vertex getTarget() {
        return target;
    }
}
