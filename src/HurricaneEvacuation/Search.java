package HurricaneEvacuation;

import java.util.*;
import java.util.function.Predicate;

abstract class Search {

    PriorityQueue<Node> fringe;
    private Predicate<Node> goalTest;
    private HashMap<State, Node> explored;
    private Agent agent;

    Search(Predicate<Node> goalTest, Agent agent) {
        this.fringe = new PriorityQueue<>();
        this.goalTest = goalTest;
        this.explored = new HashMap<>();
        this.agent = agent;
    }

    Node run() {

        while (true) {

            if (fringe.isEmpty()) {

                return null;
            }

            Node currentNode = fringe.poll();

            if (goalTest.test(currentNode)) {
                if (agent != null) {
                    /* This search class is also used to find shortest paths
                    * for vertices in class Vertex. Then agent will be null */
                    agent.setNumOfExpansions(explored.size());
                }
                return currentNode;
            }

            explored.put(currentNode.getState(), currentNode);

            for (Edge nextEdge : currentNode.getState().getLocation().getEdges().values()) {

                if (nextEdge.isBlocked()) {
                    continue;
                }

                Node child = createChildNode(nextEdge, currentNode);

                boolean inFringe = false;

                for (Node node : fringe) {

                    if (node.getState().equals(child.state)) {

                        inFringe = true;

                        if (node.compareTo(child) > 0) {
                            fringe.remove(node);
                            fringe.add(child);
                            break;
                        }
                    }
                }
                if (!inFringe) {
                    if (!explored.containsKey(child.getState())) {
                        fringe.add(child);
                    }
                }
            }
        }
    }

    abstract Node createChildNode(Edge edge, Node currentNode);
}

abstract class Node implements Comparable<Node> {

    float pathCost;
    private Node parent;
    private ArrayList<Node> children;
    private Node chosenChild;
    State state;


    Node() {

        this.pathCost = 0;
        this.children = new ArrayList<>();
        this.parent = null;
        this.chosenChild = null;
    }

    Node(Node parent) {
        this.parent = parent;
        this.parent.getChildren().add(this);
        this.children = new ArrayList<>();

    }

    void setChosenChild(Node node) {
        this.chosenChild = node;
    }

    float getPathCost() {
        return pathCost;
    }

    Node getParent() {
        return parent;
    }

    Node getChosenChild() {
        return chosenChild;
    }

    private ArrayList<Node> getChildren() {
        return children;
    }

    State getState() {
        return state;
    }

    @Override
    abstract public int compareTo(Node o);
}

class State {

    final private HashMap<Integer, Integer> peopleMap; /* Null if irrelevant */
    final private int leftToSave; /* -1 if irrelevant */
    final private Vertex location;

    State(HashMap<Integer, Integer> peopleMap, Vertex location, int leftToSave/*, int carrying*/) {
        this.peopleMap = peopleMap;
        this.location = location;
        this.leftToSave = leftToSave;
    }

    HashMap<Integer, Integer> getPeopleMap() {
        return peopleMap;
    }

    Vertex getLocation() {
        return location;
    }

    int getLeftToSave() {
        return leftToSave;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof State)) {
            return false;
        } else {
            return ((this.getPeopleMap() == null && ((State) obj).getPeopleMap() == null)
                    || (this.getPeopleMap().equals(((State) obj).getPeopleMap())))
                    && (this.getLocation().equals(((State) obj).getLocation()))
                    && (this.getLeftToSave() == ((State) obj).getLeftToSave());
                  //  && (this.getCarrying() == ((State) obj).getCarrying());

        }
    }

    @Override
    public int hashCode() {

        return Objects.hash(peopleMap, leftToSave, location/*, carrying*/);
    }
}

