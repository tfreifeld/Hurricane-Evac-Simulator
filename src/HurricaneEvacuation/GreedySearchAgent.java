package HurricaneEvacuation;

class GreedySearchAgent extends SearchAgent {

    GreedySearchAgent(int agentNum) {
        super(agentNum);
    }

    @Override
    HeuristicSearch getSearch(Agent agent, State startState) {
        return new GreedyHeuristicSearch(agent, startState);
    }

    static private class GreedyHeuristicSearch extends HeuristicSearch{

        GreedyHeuristicSearch(Agent agent, State startState) {
            super(agent);
            this.fringe.add(new GreedyHeuristicNode(startState));
        }

        @Override
        Node createChildNode(Edge edge, Node currentNode) {
            return new GreedyHeuristicNode
                    (edge.getNeighbour(currentNode.getState().getLocation()), (HeuristicNode) currentNode, edge);

        }
    }

    static private class GreedyHeuristicNode extends HeuristicNode{

        GreedyHeuristicNode(State startState) {
            super(startState, 0);
        }

        GreedyHeuristicNode(Vertex location, HeuristicNode parent, Edge edge) {
            super(location, parent, edge);
        }

        @Override
        double computeEvaluationFunction() {
            return super.computeHeuristicFunction();
        }

    }
}
