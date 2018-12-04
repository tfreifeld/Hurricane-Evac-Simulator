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
                if (this.path != null){
                    this.path.getLocation().registerListener(() -> path = null);
                }
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

    static private class UniformSearch extends Search {

        UniformSearch(Vertex location, Predicate<Node> goalTest) {
            super(goalTest);
            this.node = new UniformSearchNode(location);
            this.fringe.add(this.node);
        }

        @Override
        Node getChildNode(Edge edge) {
            return new UniformSearchNode(edge.getNeighbour(node.getLocation()), (UniformSearchNode) node, edge);
        }
    }

    static private class UniformSearchNode extends Node {

        UniformSearchNode(Vertex location) {
            super(location);
        }

        UniformSearchNode(Vertex location, UniformSearchNode parent, Edge edge) {
            super(location, parent);
            this.pathCost = parent.getPathCost() + edge.getWeight();

        }

        @Override
        public int compareTo(Node o) {

            int result = Float.compare(this.getPathCost(), o.getPathCost());
            if (result == 0) {
                /* If nodes have the same path cost, compare according to number of people */
                return Integer.compare(o.getLocation().getPersons(), this.getLocation().getPersons());
            } else {
                return result;
            }

        }
    }
}


