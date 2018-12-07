package HurricaneEvacuation;

class AStarSearchAgent extends SearchAgent {

    AStarSearchAgent(int agentNum) {
        super(agentNum);
    }

    @Override
    HeuristicSearch getSearch(Vertex location) {
        return new AStarHeuristicSearch(location);
    }

    static private class AStarHeuristicSearch extends HeuristicSearch{

        AStarHeuristicSearch(Vertex location) {
            super();
            this.fringe.add(new AStarHeuristicNode(location));
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
