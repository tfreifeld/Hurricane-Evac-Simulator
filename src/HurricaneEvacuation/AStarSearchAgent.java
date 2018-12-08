package HurricaneEvacuation;

class AStarSearchAgent extends SearchAgent {

    AStarSearchAgent(int agentNum) {
        super(agentNum);
    }

    @Override
    HeuristicSearch getSearch(Agent agent) {
        return new AStarHeuristicSearch(agent);
    }

    static private class AStarHeuristicSearch extends HeuristicSearch{

        AStarHeuristicSearch(Agent agent) {
            super(agent);
            this.fringe.add(new AStarHeuristicNode(agent.getLocation()));
        }

        @Override
        Node createChildNode(Edge edge, Node currentNode) {
            return new AStarHeuristicNode
                    (edge.getNeighbour(currentNode.getState().getLocation()), (HeuristicNode) currentNode, edge);

        }
    }

    static private class AStarHeuristicNode extends HeuristicNode{

        AStarHeuristicNode(Vertex location) {
            super(location);
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
