package HurricaneEvacuation;

import java.util.HashMap;

class GreedyBestFirstAgent extends Agent {

    private Node searchTree;

    GreedyBestFirstAgent(int agentNum) {

        this.agentNum = agentNum;
        this.searchTree = null;
    }

    @Override
    Move makeOperation() {


        if (this.searchTree == null){
            this.searchTree = new GreedyBestFirstSearch(this.getLocation()).run();
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
                    .getNeighbour(this.searchTree.getState().getLocation().getId());
        } catch (Vertex.NotNeighbourException e) {
            e.printStackTrace();
            this.searchTree = null;
            return new Move(this, this.getLocation(), null);
        }
        Move move = new Move(this, searchTree.getState().getLocation(), edge);
        return move;
    }

    static private class GreedyBestFirstSearch extends Search{

        GreedyBestFirstSearch(Vertex location) {
            super(node -> node.getPathCost() > Simulator.getDeadline());
            this.node = new GreedyBestFirstNode(location);
            this.fringe.add(this.node);
        }

        @Override
        Node createChildNode(Edge edge) {
            return new GreedyBestFirstNode
                    (edge.getNeighbour(node.getState().getLocation()), (GreedyBestFirstNode) node, edge);

        }
    }

    static private class GreedyBestFirstNode extends Node{

        private int carrying;

        GreedyBestFirstNode(Vertex location) {
            super();

            this.carrying = 0;

            this.state = new State(Simulator.getInitialPeopleMap(), location, Simulator.getTotalPeople());
        }

        GreedyBestFirstNode(Vertex location, GreedyBestFirstNode parent, Edge edge) {
            super(parent);

            this.pathCost = parent.getPathCost()
                    + edge.getWeight() * (1 + Simulator.getKFactor() /*TODO: need to multiply in vehicle load*/);

            this.carrying = parent.getCarrying();

            HashMap<Integer,Integer> tempPeopleMap = new HashMap<>(parent.getState().getPeopleMap());
            int tempLeftToSave = parent.getState().getLeftToSave();

            if(location.getPersons() > 0){
                this.carrying += location.getPersons();
                tempPeopleMap.replace(location.getId(), 0);
            }

            if(location.isShelter()){
                tempLeftToSave -= this.carrying;
                this.carrying = 0;
            }

            this.state = new State(tempPeopleMap,location, tempLeftToSave);
        }

        int getCarrying() {
            return carrying;
        }

        @Override
        public int compareTo(Node o) {
//            return Integer.compare(this.getLeftToSave(), ((GreedyBestFirstNode)o).getLeftToSave());

            /*TODO*/
            return 0;
        }
    }
}
