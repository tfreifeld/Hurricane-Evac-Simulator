package HurricaneEvacuation;

public class Move {

    final Agent agent;
    final Vertex target;

    public Move(Agent agent, Vertex target) {
        this.agent = agent;
        this.target = target;

        try {
            if (!agent.getLocation().hasNeighbour(target.getId())) {
                throw new Exception("Illegal move.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Agent getAgent() {
        return agent;
    }

    public Vertex getTarget() {
        return target;
    }
}
