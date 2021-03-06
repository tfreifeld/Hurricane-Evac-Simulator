package HurricaneEvacuation;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

abstract class SearchAgent extends Agent {

    private Node node;
    private boolean finishedSearch;

    SearchAgent(int agentNum) {

        this.agentNum = agentNum;
        this.node = null;
    }

    @Override
    Move makeOperation() {

        if(finishedSearch){
            /*NoOp*/
            return new Move(this, this.getLocation(), null);

        }

        if (this.node == null) {

            State stateAtSearchStart = getSearchStartState();

            Search search = getSearch(this,stateAtSearchStart);
            this.node = search.run();


            if (this.node == null) {
                /*If no path has been found -> NoOp*/
                finishedSearch = true;
                return new Move(this, this.getLocation(), null);
            }

            while (!(this.node.getParent().getState().equals(stateAtSearchStart))) {
                this.node.getParent().setChosenChild(this.node);
                this.node = this.node.getParent();
            }
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

        if(getGoalTest().test(this.node)){
            finishedSearch = true;
        }
        else{
            this.node = this.node.getChosenChild();
        }

        return move;
    }

    State getSearchStartState() {
        return new State(Simulator.getInitialPeopleMap(), location, Simulator.getTotalPeople());
    }


    @Override
    void performanceMeasure() {

        System.out.println("f = -1: " + ((-1 * getSaved()) + getNumOfExpansions()));
        System.out.println("f = -100: " + ((-100 * getSaved()) + getNumOfExpansions()));
        System.out.println("f = -10000: " + ((-10000 * getSaved()) + getNumOfExpansions()));

    }

    private static Predicate<Node> getGoalTest() {
        return node -> (node.getPathCost() > Simulator.getDeadline()) ||
                (node.getState().getLeftToSave() == 0);
    }

    abstract HeuristicSearch getSearch(Agent agent, State startState);

    static abstract class HeuristicSearch extends Search {

        HeuristicSearch(Agent agent) {
            super(getGoalTest(), agent);
        }

    }

    static abstract class HeuristicNode extends Node {

        private int carrying;
        private double evaluationValue;

        HeuristicNode(State startState, int carrying) {
            super();

            this.carrying = carrying;

            this.state = startState;
            this.evaluationValue = computeEvaluationFunction();
        }

        HeuristicNode(Vertex location, HeuristicNode parent, Edge edge) {
            super(parent);


            HashMap<Integer, Integer> tempPeopleMap = new HashMap<>(parent.getState().getPeopleMap());
            this.carrying = parent.getCarrying();

            this.pathCost = parent.getPathCost()
                    + edge.getWeight() * (1 + Simulator.getKFactor() * this.carrying);

            if (tempPeopleMap.get(location.getId()) != null) {
                if (tempPeopleMap.get(location.getId()) > 0) {
                    this.carrying += location.getPersons();
                    tempPeopleMap.replace(location.getId(), 0);
                }
            }
            int tempLeftToSave = parent.getState().getLeftToSave();


            if (location.isShelter()) {
                tempLeftToSave -= this.carrying;
            }

            this.state = new State(tempPeopleMap, location, tempLeftToSave);
            this.evaluationValue = computeEvaluationFunction();
            if (location.isShelter()){
                this.carrying = 0;
            }
        }

        int getCarrying() {
            return carrying;
        }

        abstract double computeEvaluationFunction();

        double computeHeuristicFunction() {

            int result = 0;


            HashMap<Integer, Double> lengthsToPeopleMap = getState().getLocation().getLengthsToPeople();
            for (Map.Entry<Integer, Integer> entry : getState().getPeopleMap().entrySet()) {
                if (entry.getValue() > 0) {
                    double lengthToShelter =
                            Simulator.getGraph().getVertex(entry.getKey()).getLengthToClosestShelter()
                                 /*   * (1 + ((getCarrying() + entry.getValue()) * Simulator.getKFactor()))*/;
                    double lengthToPeople = (lengthsToPeopleMap.get(entry.getKey())
                            /* * (1 + getCarrying() * Simulator.getKFactor())*/);
                    if (getPathCost() + lengthToPeople + lengthToShelter > Simulator.getDeadline()) {
                        result += entry.getValue() * 100;
                    }
                }
            }

            if (this.getCarrying() > 0) {
                double lengthToShelter =
                        Simulator.getGraph().getVertex(getState().getLocation().getId())
                                .getLengthToClosestShelter() * (1 + (getCarrying() * Simulator.getKFactor()));
                if (getPathCost() + lengthToShelter > Simulator.getDeadline()) {
                    result += this.carrying * 100;
                }
            }

            return result;
        }

        @Override
        public int compareTo(Node o) {
            return Double.compare(this.evaluationValue, ((HeuristicNode) o).evaluationValue);

        }
    }
}
