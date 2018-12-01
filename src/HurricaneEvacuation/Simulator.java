package HurricaneEvacuation;

import java.io.File;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Simulator {

    private static ArrayList<Agent> agents = new ArrayList<>();
    private static float kFactor;
    static Graph graph;
    private static int safeCount = 0;
    private static double time = 0;
    static Scanner sc = new Scanner(System.in);


    public static void main(String[] args) {

        graph = new Graph(new File(args[0]));
        graph.constructGraph();

        readInputFromUser();

        displayWorldState();

        while(time < graph.getDeadline()) {
            makeMove(agents.get(0).makeOperation());
            displayWorldState();
        }


        //sc.close();


    }

    private static void makeMove(Move move){

        if (move.getEdge().isBlocked()){
            time++;
        }
        else {
            double tempTime =
                    time + move.getEdge().getWeight() * (1 + kFactor * move.getAgent().getCarrying());
            if (!(tempTime > graph.getDeadline())) {
                traverse(move);
                time = tempTime;
            }
            else{
                time++;
            }
        }

        move.getAgent().increaseMoves();

    }

    private static void traverse(Move move) {
        move.getAgent().setLocation(move.getTarget());
        if (move.getTarget().isShelter()) {
            safeCount += move.getAgent().getCarrying();
            move.getAgent().setCarrying(0);
        } else {
            move.getAgent().setCarrying(move.getAgent().getCarrying() +
                    move.getTarget().getPersons());
            move.getTarget().setPersons(0);
        }
    }

    private static void readInputFromUser() {

        System.out.println("Please enter the number of agents: ");
        int numOfAgents = sc.nextInt();
        for (int i = 0; i < numOfAgents; i++) {

            System.out.println
                    ("Please enter the type of the agent " + (i + 1) +
                            ". Type 'h' for human, 'g' for greedy or" +
                            " 'v' for vandal: ");
            String agentType = sc.next();
            while (!agentType.matches("[h|g|v]")) {
                System.out.println("Invalid option.");
                agentType = sc.next();
            }

            switch (agentType) {

                case "h": {
                    agents.add(new HumanAgent(i + 1));
                    break;
                }
                case "g": {
                    agents.add(new GreedyAgent(i + 1));
                    break;
                }
                case "v": {
                    agents.add(new VandalAgent(i + 1));
                    break;
                }
            }

            System.out.println
                    ("Please enter the number of start" +
                            " vertex for agent " + (i + 1) + ": ");

            int startVertex;
            while(true){

                try {
                    startVertex = sc.nextInt();
                    if (startVertex > graph.getNumberOfVertices()){
                        System.out.println("There are only " + graph.getNumberOfVertices()
                                        + " vertices.");
                        continue;
                    }
                    else if (startVertex <= 0){
                        System.out.println("Invalid option.");
                        continue;
                    }
                    traverse(new Move(agents.get(i),graph.getVertex(startVertex), null));
                    //agents.get(i).setLocation(graph.getVertex(startVertex));
                    break;
                } catch (InputMismatchException e) {
                    sc.next();
                    System.out.println("Invalid option.");
                }
            }

        }

        System.out.println("Please enter the \"slow-down\" constant: ");
        while(true){
            try{
                kFactor = sc.nextFloat();
                if (kFactor < 0){
                    System.out.println("K must be non-negative.");
                    continue;
                }
                break;
            } catch (InputMismatchException e){
                sc.next();
                System.out.println("Invalid option.");
            }
        }
        System.out.println();
    }

    static void displayWorldState(){

        graph.displayGraphState();

        for (Agent agent:agents) {
            System.out.println("Agent " + agent.getAgentNum() + ":");
            System.out.println(agent.toString());
            System.out.println();
        }

        System.out.println(safeCount + " people are safe");

        System.out.println("Time: " + time);
        System.out.println();

    }
}
