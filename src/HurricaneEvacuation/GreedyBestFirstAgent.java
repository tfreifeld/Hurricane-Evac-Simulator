package HurricaneEvacuation;

import java.util.HashMap;
import java.util.Map;

class GreedyBestFirstAgent extends Agent {

    private Node node;

    GreedyBestFirstAgent(int agentNum) {

        this.agentNum = agentNum;
        this.node = null;
    }

    @Override
    Move makeOperation() {


        if (this.node == null){

            State currentState = new State(Simulator.getInitialPeopleMap(), location, Simulator.getTotalPeople());

            this.node = new GreedyBestFirstSearch(this.getLocation()).run();
            while (!(this.node.getParent().getState().equals(currentState))){
                this.node.getParent().setChosenChild(this.node);
                this.node = this.node.getParent();
            }
        }
        else{

            this.node = node.getChosenChild();

        }

        if (this.node == null){
            /*If no path has been found -> NoOp*/
            return new Move(this, this.getLocation(),null);
        }

        Edge edge;

        try {
            edge = this.getLocation()
                    .getNeighbour(this.node.getState().getLocation().getId());
        } catch (Vertex.NotNeighbourException e) {
            e.printStackTrace();
            this.node = null;
            return new Move(this, this.getLocation(), null);
        }
        Move move = new Move(this, this.node.getState().getLocation(), edge);

        this.node = this.node.getChosenChild();

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
        private int heuristicValue;

        GreedyBestFirstNode(Vertex location) {
            super();

            this.carrying = 0;

            this.state = new State(Simulator.getInitialPeopleMap(), location, Simulator.getTotalPeople());
            this.heuristicValue = computeEvaluationFunction();
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
            this.heuristicValue = computeEvaluationFunction();
        }

        int getCarrying() {
            return carrying;
        }

        int computeEvaluationFunction(){

            int result = 0;

            /*TODO: consider adding a condition if this a goal test then return zero*/

            HashMap<Integer,Double> lengthsToPeople = getState().getLocation().getLengthsToPeople();
            for (Map.Entry<Integer,Integer> entry: getState().getPeopleMap().entrySet()){
                if (entry.getValue() > 0){
                    double lengthToShelter =
                            Simulator.getGraph().getVertex(entry.getKey()).getLengthToClosestShelter();
                    if (getPathCost() + lengthsToPeople.get(entry.getKey())
                            + lengthToShelter > Simulator.getDeadline()){
                        result += entry.getValue() * 100;
                    }
                }
            }

            return result;
        }

        @Override
        public int compareTo(Node o) {
            return Integer.compare(this.heuristicValue, ((GreedyBestFirstNode)o).heuristicValue);

        }
    }
}
