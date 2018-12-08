package HurricaneEvacuation;

class GreedySearchAgent extends SearchAgent {

    GreedySearchAgent(int agentNum) {
        super(agentNum);
    }

    @Override
    HeuristicSearch getSearch(Agent agent) {
        return new GreedyHeuristicSearch(agent);
    }

    static private class GreedyHeuristicSearch extends HeuristicSearch{

        GreedyHeuristicSearch(Agent agent) {
            super(agent);
            this.fringe.add(new GreedyHeuristicNode(agent.getLocation()));
        }

        @Override
        Node createChildNode(Edge edge, Node currentNode) {
            return new GreedyHeuristicNode
                    (edge.getNeighbour(currentNode.getState().getLocation()), (HeuristicNode) currentNode, edge);

        }
    }

    static private class GreedyHeuristicNode extends HeuristicNode{

        GreedyHeuristicNode(Vertex location) {
            super(location);
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
