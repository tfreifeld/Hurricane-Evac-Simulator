package HurricaneEvacuation;

import java.io.File;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Simulator {

    private static ArrayList<Agent> agents = new ArrayList<>();
    private static float kfactor;
    static Graph graph;
    private static int safeCount = 0;
    static double time = 0;
    static Scanner sc = new Scanner(System.in);


    public static void main(String[] args) {

        graph = new Graph(new File(args[0]));
        graph.constructGraph();

        readInputFromUser();


        graph.displayWorldState();
        makeMove(agents.get(0).makeOperation());
        graph.displayWorldState();


        sc.close();


    }

    private static void makeMove(Move move){

        move.getAgent().setLocation(move.getTarget());
        if (move.getTarget().isShelter()) {
            safeCount += move.getAgent().getCarrying();
            move.getAgent().setCarrying(0);
        }
        else{
            move.getAgent().setCarrying(move.getTarget().getPersons());
            move.getTarget().setPersons(0);
        }

        //TODO: increase time

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
                    agents.get(i).setLocation(graph.getVertex(startVertex));
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
                kfactor = sc.nextFloat();
                if (kfactor < 0){
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
}
