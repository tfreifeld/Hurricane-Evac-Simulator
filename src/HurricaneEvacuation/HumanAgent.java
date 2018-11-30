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
        while(true) {
            try {
                 targetVertex = Simulator.sc.nextInt();
                 if (!location.hasNeighbour(targetVertex)){
                     System.out.println("Not a neighbour.");
                     continue;
                 }
                 break;
            } catch (InputMismatchException e) {
                Simulator.sc.next();
                System.out.println("Invalid option.");
            }
        }

        return new Move(this, Simulator.graph.getVertex(targetVertex));
    }
}
