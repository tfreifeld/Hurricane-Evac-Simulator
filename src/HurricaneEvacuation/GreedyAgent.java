package HurricaneEvacuation;

import java.util.*;
import java.util.function.Predicate;

class GreedyAgent extends Agent {

    private Node path;

    GreedyAgent(int agentNum) {

        this.agentNum = agentNum;
        this.path = null;
    }

    @Override
    public Move makeOperation() {

        if (this.path != null) {
            if (this.path.getLocation().equals(this.getLocation())) {
                /*If target has been reached, need to choose new target */
                this.path = null;
            }
        }

        if (this.path == null) {
            /*If a target has not yet been set */
            if (getCarrying() > 0) {
                this.path = new UniformSearch(getLocation(), node -> node.getLocation().isShelter()).run();
            } else {
                this.path = new UniformSearch(getLocation(), node -> node.getLocation().getPersons() > 0).run();
            }

            if (this.path == null){
                /*If no path has been found -> NoOp*/
                return new Move(this, this.getLocation(),null);
            }

            while (!this.path.getParent().getLocation().equals(this.getLocation())) {
                this.path.getParent().setChosenChild(this.path);
                this.path = this.path.getParent();
            }
        }

        Edge edge;

        try {
            edge = this.getLocation().getNeighbour(this.path.getLocation().getId());
        } catch (Vertex.NotNeighbourException e) {
            e.printStackTrace();
            this.path = null;
            return new Move(this, this.getLocation(), null);
        }

        Move move = new Move(this, this.path.getLocation(), edge);

        this.path = this.path.getChosenChild();

        return move;

    }

    @Override
    public String toString() {
        return "{Type: Greedy\n" + super.toString();
    }

    class UniformSearch {

        Node node;
        HashSet<Node> explored;
        PriorityQueue<Node> fringe;
        Predicate<Node> goalTest;

        UniformSearch(Vertex location, Predicate<Node> goalTest) {
            this.explored = new HashSet<>();
            this.node = new Node(location);
            this.fringe = new PriorityQueue<>();
            this.fringe.add(this.node);
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

                for (Map.Entry<Integer, Edge> next : node.getLocation().getEdges().entrySet()) {

                    if (next.getValue().isBlocked()){
                        continue;
                    }

                    Node child = new Node
                            (next.getValue().getNeighbour(node.getLocation()), node, next.getValue());

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

    }

    class Node implements Comparable {

        Vertex location;
        int pathCost;
        Node parent;
        Node chosenChild;


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
            /*if (this.parent.getChosenChild() == null) {
                this.parent.setChosenChild(this);
            }
            else {
                if (this.parent.getChosenChild().getPathCost() > this.getPathCost()){
                    this.parent.setChosenChild(this);
                }
            }*/
        }

        private void setChosenChild(Node node) {
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
        public int compareTo(Object o) {
            int result =  Integer.compare(this.getPathCost(), ((Node) o).getPathCost());
            if (result  == 0){
                /*If nodes have the same path cost, compare according to number of people */
                return Integer.compare(((Node)o).getLocation().getPersons(), this.getLocation().getPersons());
            }
            else{
                return result;
            }
        }
    }
}


