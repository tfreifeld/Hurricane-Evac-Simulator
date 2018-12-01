package HurricaneEvacuation;

class Edge {

    private Vertex in;
    private Vertex out;
    private int weight;
    private boolean blocked;

    Edge(Vertex in, Vertex out, int weight) {
        this.in = in;
        this.out = out;
        this.weight = weight;

        in.submitEdge(this);
        out.submitEdge(this);
    }

    boolean isBlocked() {
        return blocked;
    }

    Vertex getNeighbour(Vertex vertex) {
        if (vertex.equals(in)) {
            return out;
        } else {
            return in;
        }
    }

    public int getWeight() {
        return weight;
    }
}
