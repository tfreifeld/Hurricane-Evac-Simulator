package HurricaneEvacuation;

class AStarSearchAgent extends SearchAgent {

    AStarSearchAgent(int agentNum) {
        super(agentNum);
    }

    @Override
    HeuristicSearch getSearch(Agent agent, State startState) {
        return new AStarHeuristicSearch(agent,startState, this.getCarrying());
    }

    static class AStarHeuristicSearch extends HeuristicSearch{

        AStarHeuristicSearch(Agent agent, State startState, int carrying) {
            super(agent);
            this.fringe.add(new AStarHeuristicNode(startState, carrying));
        }

        @Override
        Node createChildNode(Edge edge, Node currentNode) {
            return new AStarHeuristicNode
                    (edge.getNeighbour(currentNode.getState().getLocation()), (HeuristicNode) currentNode, edge);

        }
    }

    static private class AStarHeuristicNode extends HeuristicNode{

        AStarHeuristicNode(State startState, int carrying) {
            super(startState, carrying);
        }

        AStarHeuristicNode(Vertex location, HeuristicNode parent, Edge edge) {
            super(location, parent, edge);
        }

        @Override
        double computeEvaluationFunction() {
            return super.computeHeuristicFunction() + this.getPathCost();
        }
    }
}
