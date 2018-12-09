package HurricaneEvacuation;

class RTAStarSearchAgent extends AStarSearchAgent {

    private int expansionLimit;

    RTAStarSearchAgent(int agentNum, int expansionLimit) {
        super(agentNum);
        this.expansionLimit = expansionLimit;
    }

    @Override
    HeuristicSearch getSearch(Agent agent, State startState) {
        return new RTAStarHeuristicSearch
                (agent, this.expansionLimit, startState, this.getCarrying());

    }

    @Override
    State getSearchStartState() {
        return new State(Simulator.getInitialPeopleMap(), location,
                Simulator.getTotalPeople() - getSaved());

    }

    static private class RTAStarHeuristicSearch extends AStarHeuristicSearch{

        private int expansionLimit;
        private int expansions;

        RTAStarHeuristicSearch(Agent agent, int expansionLimit, State startState, int carrying) {
            super(agent, startState, carrying);
            this.expansionLimit = expansionLimit;

        }

        @Override
        boolean checkReturnCondition(Node currentNode) {
            boolean isGoal =  super.checkReturnCondition(currentNode);
            if (isGoal)
                return true;
            else if(expansions == expansionLimit){
                //System.out.println("Expansion limit has been reached.");
                return true;
            }
            else{
                expansions++;
                return false;
            }
        }
    }
}
