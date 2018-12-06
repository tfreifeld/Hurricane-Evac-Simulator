package HurricaneEvacuation;

import java.util.function.Predicate;

class GreedyAgent extends Agent {

    private Node path;

    GreedyAgent(int agentNum) {

        this.agentNum = agentNum;
        this.path = null;
    }

    @Override
    public Move makeOperation() {

        //TODO: add support for edges that become block along the run

        if (this.path != null) {
            if (this.path.getState().getLocation().equals(this.getLocation())) {
                /*If target has been reached, need to choose new target */
                this.path = null;
            }
        }

        if (this.path == null) {
            /*If a target has not yet been set */
            if (getCarrying() > 0) {
                this.path = new UniformSearch(getLocation(), node -> node.getState().getLocation().isShelter()).run();
            } else {
                this.path = new UniformSearch(getLocation(), node -> node.getState().getLocation().getPersons() > 0).run();
                if (this.path != null){
                    this.path.getState().getLocation().registerListener(() -> path = null);
                }
            }

            if (this.path == null){
                /*If no path has been found -> NoOp*/
                return new Move(this, this.getLocation(),null);
            }

            while (!this.path.getParent().getState().getLocation().equals(this.getLocation())) {
                this.path.getParent().setChosenChild(this.path);
                this.path = this.path.getParent();
            }
        }

        Edge edge;

        try {
            edge = this.getLocation().getNeighbour(this.path.getState().getLocation().getId());
        } catch (Vertex.NotNeighbourException e) {
            e.printStackTrace();
            this.path = null;
            return new Move(this, this.getLocation(), null);
        }

        Move move = new Move(this, this.path.getState().getLocation(), edge);

        this.path = this.path.getChosenChild();

        return move;

    }

    @Override
    public String toString() {
        return "{Type: Greedy\n" + super.toString();
    }

    static class UniformSearch extends Search {

        UniformSearch(Vertex location, Predicate<Node> goalTest) {
            super(goalTest);
            this.fringe.add(new UniformSearchNode(location));
        }

        @Override
        Node createChildNode(Edge edge, Node currentNode) {
            return new UniformSearchNode(edge.getNeighbour
                    (currentNode.getState().getLocation()), (UniformSearchNode) currentNode, edge);
        }

    }

    static private class UniformSearchNode extends Node {

        UniformSearchNode(Vertex location) {
            super();
            this.state = new State(null, location, -1);
        }

        UniformSearchNode(Vertex location, UniformSearchNode parent, Edge edge) {
            super(parent);
            this.pathCost = parent.getPathCost() + edge.getWeight();
            this.state = new State(null, location, -1);

        }

        @Override
        public int compareTo(Node o) {

            int result = Float.compare(this.getPathCost(), o.getPathCost());
            if (result == 0) {
                /* If nodes have the same path cost, compare according to number of people */
                return Integer.compare(o.getState().getLocation().getPersons(),
                                        this.getState().getLocation().getPersons());
            } else {
                return result;
            }

        }
    }
}


