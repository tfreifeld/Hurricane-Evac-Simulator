package HurricaneEvacuation;

import java.io.File;
import java.util.*;

public class Simulator {

    private static ArrayList<Agent> agents = new ArrayList<>();
    private static Graph graph;
    private static int deadline;
    private static double time = 0;
    private static int safeCount = 0;
    private static int totalPeople = 0;
    private static float kFactor;

    static Scanner sc = new Scanner(System.in);


    public static void main(String[] args) {

        graph = new Graph(new File(args[0]));
        graph.constructGraph();

        readInputFromUser();
        for (int i = 1; i <= graph.getNumberOfVertices(); i++) {
            graph.getVertex(i).runLengthsSearch();
        }

        getGraph().displayGraphState();
        System.out.println();

        while (time < getDeadline()) {
            for (int i = 0; i < agents.size() && time < getDeadline(); i++) {
                Agent agent = agents.get(i);
                Move move = agent.makeOperation();
                System.out.print("Agent " + (i + 1) + "'s turn: ");
                makeMove(move);
                displayWorldState();
            }
        }

        System.out.println("Deadline has been reached!");

        System.out.println("Performance measure:");
        for (Agent agent : agents) {
            System.out.println("Agent " + agent.agentNum + ":");
            agent.performanceMeasure();

        }

        sc.close();


    }

    private static void makeMove(Move move) {

        if (move.getEdge() == null) {
            /*NoOp*/
            System.out.println("NoOp");
            time++;
        } else if (move.getEdge().isBlocked()) {
            System.out.println("traverse failed - edge blocked");
            /*Edge is blocked*/
            time++;
        } else {
            double tempTime =
                    time + move.getEdge().getWeight() * (1 + kFactor * move.getAgent().getCarrying());
            if (!(tempTime > getDeadline())) {
                /*If deadline isn't breached*/
                System.out.println("traverse - " + move.getAgent().getLocation().getId()
                        + " to " + move.getTarget().getId());
                move.getAgent().traverse(move.getTarget());
                time = tempTime;
            } else {
                System.out.println("traverse failed - will breach deadline");
                /*If deadline is breached, traverse fails*/
                time++;
            }
        }

        move.getAgent().increaseMoves();

    }

    private static void readInputFromUser() {

        System.out.println("Please enter the number of agents: ");
        int numOfAgents = sc.nextInt();
        for (int i = 0; i < numOfAgents; i++) {

            System.out.println
                    ("Please enter the type of agent " + (i + 1) +
                            ".\nType 'h' for human, 'g' for greedy," +
                            " 'v' for vandal,\n's' for greedy best-first," +
                            " 'a' for A* or 'r' for RTA*:");
            String agentType = sc.next();
            while (!agentType.matches("[h|g|v|s|a|r]")) {
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
                case "s": {
                    agents.add(new GreedySearchAgent(i + 1));
                    break;
                }
                case "a": {
                    agents.add(new AStarSearchAgent(i + 1));
                    break;
                }
                case "r": {
                    System.out.println("Please enter expansions limit:");
                    int limit;
                    while(true) {
                        try {
                            limit = sc.nextInt();
                            break;
                        } catch (InputMismatchException e) {
                            sc.next();
                            System.out.println("Invalid option.");
                        }
                    }

                    agents.add(new RTAStarSearchAgent(i + 1, limit));
                    break;
                }
            }

            System.out.println
                    ("Please enter the number of start" +
                            " vertex for agent " + (i + 1) + ": ");

            int startVertex;
            while (true) {

                try {
                    startVertex = sc.nextInt();
                    if (startVertex > graph.getNumberOfVertices()) {
                        System.out.println("There are only " + graph.getNumberOfVertices()
                                + " vertices.");
                        continue;
                    } else if (startVertex <= 0) {
                        System.out.println("Invalid option.");
                        continue;
                    }
                    agents.get(i).traverse(graph.getVertex(startVertex));
                    //agents.get(i).setLocation(graph.getVertex(startVertex));
                    break;
                } catch (InputMismatchException e) {
                    sc.next();
                    System.out.println("Invalid option.");
                }
            }

        }

        System.out.println("Please enter the \"slow-down\" constant: ");
        while (true) {
            try {
                kFactor = sc.nextFloat();
                if (kFactor < 0) {
                    System.out.println("K must be non-negative.");
                    continue;
                }
                break;
            } catch (InputMismatchException e) {
                sc.next();
                System.out.println("Invalid option.");
            }
        }
        System.out.println();
    }

    private static void displayWorldState() {

       /* System.out.println("Graph State:");
        System.out.println("----------------");
        graph.displayGraphState();*/

        System.out.println("Agents State:");
        System.out.println("----------------");

        for (Agent agent : agents) {
            System.out.println("Agent " + agent.getAgentNum() + ":");
            System.out.println(agent.toString());
            System.out.println();
        }
        System.out.println("----------------");

        if (safeCount == 1) {
            System.out.println(safeCount + " person is safe");
        } else {
            System.out.println(safeCount + " people are safe");
        }

        System.out.println("Time: " + time);
        System.out.println("\n\n");

    }

    static int getDeadline() {
        return deadline;
    }

    static void setDeadline(int deadline) {
        Simulator.deadline = deadline;
    }

    static float getKFactor() {
        return kFactor;
    }

    static int getSafeCount() {
        return safeCount;
    }

    static void setSafeCount(int safeCount) {
        Simulator.safeCount = safeCount;
    }

    static Graph getGraph() {
        return graph;
    }

    static int getTotalPeople() {
        return totalPeople;
    }

    static void increaseTotalPeople(int people) {
        Simulator.totalPeople += people;
    }

    static HashMap<Integer, Integer> getInitialPeopleMap() {
        HashMap<Integer, Integer> peopleMap = new HashMap<>();
        for (Map.Entry<Integer, Vertex> v : getGraph().getVertices().entrySet()) {
            if (v.getValue().getPersons() > 0) {
                peopleMap.put(v.getKey(), v.getValue().getPersons());
            }
        }
        return peopleMap;
    }
}
