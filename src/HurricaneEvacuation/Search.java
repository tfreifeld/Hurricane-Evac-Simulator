package HurricaneEvacuation;

import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.function.Predicate;

abstract class Search {

    Node node;
    private HashSet<Node> explored;
    PriorityQueue<Node> fringe;
    private Predicate<Node> goalTest;

    Search(Predicate<Node> goalTest) {
        this.explored = new HashSet<>();
        this.fringe = new PriorityQueue<>();
        this.goalTest = goalTest;
    }

    Node run() {

        while (true) {

            if (fringe.isEmpty()) {
                return null;
            }

            node = fringe.poll();

            if (goalTest.test(node)) {
                return node;
            }

            explored.add(node);

            for (Edge next : node.getLocation().getEdges().values()) {

                if (next.isBlocked()) {
                    continue;
                }

                Node child = getChildNode(next);

                boolean inFringe = false;

                for (Node node : fringe) {

                    if (node.getLocation().equals(child.getLocation())) {

                        inFringe = true;

                        if (node.compareTo(child) > 0) {
                            fringe.remove(node);
                            fringe.add(child);
                            break;
                        }
                    }
                }
                if (!inFringe) {

                    boolean inExplored = false;

                    for (Node node : explored) {
                        if (node.getLocation().equals(child.getLocation())) {
                            inExplored = true;
                            break;
                        }
                    }

                    if (!inExplored) {
                        fringe.add(child);
                    }
                }
            }
        }
    }

    abstract Node getChildNode(Edge edge);
}

abstract class Node implements Comparable<Node> {

    private Vertex location;
    private int pathCost;
    private Node parent;
    private Node chosenChild;


    Node(Vertex location) {
        this.location = location;
        this.pathCost = 0;
        this.parent = null;
        this.chosenChild = null;
    }

    Node(Vertex location, Node parent, Edge edge) {
        this.location = location;
        this.pathCost = parent.getPathCost() + edge.getWeight();
        this.parent = parent;

    }

    void setChosenChild(Node node) {
        this.chosenChild = node;
    }

    Vertex getLocation() {
        return location;
    }

    int getPathCost() {
        return pathCost;
    }

    Node getParent() {
        return parent;
    }

    Node getChosenChild() {
        return chosenChild;
    }

    @Override
    abstract public int compareTo(Node o);
}

