package HurricaneEvacuation;

import java.util.InputMismatchException;
import java.util.Scanner;

class HumanAgent extends Agent {

    HumanAgent(int agentNum) {

        this.agentNum = agentNum;
    }

    @Override
    public void makeOperation() {

        System.out.println("Choose a neighbour vertex from "
                        + location.getNeighboursToString() + " to move to: ");

        Scanner in = new Scanner(System.in);

        int targetVertex;
        while(true) {
            try {
                 targetVertex = in.nextInt();
                 if (!location.getNeighbours().contains(targetVertex)){
                     continue;
                 }
                 break;
            } catch (InputMismatchException e) {
                System.out.println("Invalid option.");
            }
        }


    }
}
