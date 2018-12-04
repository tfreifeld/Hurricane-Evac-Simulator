package HurricaneEvacuation;

import java.util.function.Predicate;

class GreedyBestFirstAgent extends Agent {

    private Node searchTree;

    GreedyBestFirstAgent(int agentNum) {

        this.agentNum = agentNum;
        this.searchTree = null;
    }

    @Override
    Move makeOperation() {


        if (this.searchTree == null){
            this.searchTree = new GreedyBestFirstSearch(this.getLocation(),
                    node -> node.getPathCost() > Simulator.getDeadline()).run();
        }
        else{

            this.searchTree = searchTree.getChosenChild();

        }

        if (this.searchTree == null){
            /*If no path has been found -> NoOp*/
            return new Move(this, this.getLocation(),null);
        }

        Edge edge;

        try {
            edge = this.getLocation()
                    .getNeighbour(this.searchTree.getLocation().getId());
        } catch (Vertex.NotNeighbourException e) {
            e.printStackTrace();
            this.searchTree = null;
            return new Move(this, this.getLocation(), null);
        }
        Move move = new Move(this, searchTree.getLocation(), edge);
        return move;
    }

    static private class GreedyBestFirstSearch extends Search{

        GreedyBestFirstSearch(Vertex location, Predicate<Node> goalTest) {
            super(goalTest);
            this.node = new GreedyBestFirstNode(location);
            this.fringe.add(this.node);
        }

        @Override
        Node getChildNode(Edge edge) {
            return new GreedyBestFirstNode
                    (edge.getNeighbour(node.getLocation()), (GreedyBestFirstNode) node, edge);

        }
    }

    static private class GreedyBestFirstNode extends Node{

        private int leftToSave;

        GreedyBestFirstNode(Vertex location) {
            super(location);
            this.leftToSave = Simulator.getTotalPeople();
        }

        GreedyBestFirstNode(Vertex location, GreedyBestFirstNode parent, Edge edge) {
            super(location, parent);
            this.pathCost = parent.getPathCost()
                    + edge.getWeight() * (1 + Simulator.getKFactor());
            this.leftToSave = parent.getLeftToSave() - location.getPersons();
        }

        int getLeftToSave() {
            return leftToSave;
        }

        @Override
        public int compareTo(Node o) {
            return Integer.compare(this.getLeftToSave(), ((GreedyBestFirstNode)o).getLeftToSave());
        }
    }
}
