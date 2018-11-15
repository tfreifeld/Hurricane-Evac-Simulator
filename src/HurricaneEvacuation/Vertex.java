package HurricaneEvacuation;

import java.util.ArrayList;

class Vertex {

    private int persons;
    private boolean shelter ;
    private ArrayList<Edge> edges;

    Vertex(int persons, boolean shelter, ArrayList<Edge> edges) {
        this.persons = persons;
        this.shelter = shelter;
        this.edges = edges;
    }

    void submitEdge(Edge edge){
        edges.add(edge);
    }

    void setPersons(int persons) {
        this.persons = persons;
    }

    void setShelter(boolean shelter) {
        this.shelter = shelter;
    }

    int getPersons() {
        return persons;
    }

    boolean isShelter() {
        return shelter;
    }

    ArrayList<Edge> getEdges() {
        return edges;
    }
}
