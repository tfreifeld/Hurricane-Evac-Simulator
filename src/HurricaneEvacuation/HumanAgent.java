package HurricaneEvacuation;

import java.util.InputMismatchException;

class HumanAgent extends Agent {

    HumanAgent(int agentNum) {

        this.agentNum = agentNum;
    }

    @Override
    public Move makeOperation() {

        System.out.println("Choose a neighbour vertex from "
                        + location.getNeighboursToString() + " to move to: ");

        int targetVertex;
        Edge edge;
        while(true) {
            try {
                 targetVertex = Simulator.sc.nextInt();
                 edge = location.getNeighbour(targetVertex);
                 break;
            } catch (InputMismatchException e) {
                Simulator.sc.next();
                System.out.println("Invalid option.");
            } catch (Vertex.NotNeighbourException e) {
                System.out.println("Not a neighbour.");
            }
        }

        return new Move(this, Simulator.graph.getVertex(targetVertex), edge);
    }

    @Override
    public String toString() {
            return "{Type: Human\n" + super.toString();
    }
}
