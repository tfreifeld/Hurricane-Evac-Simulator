package HurricaneEvacuation;

import java.util.ArrayList;
import java.util.Comparator;

class Vertex {

    private int id;
    private int persons;
    private boolean shelter;
    private ArrayList<Edge> edges;
    private ArrayList<Vertex> neighbours;


    Vertex(int id) {
        this.id = id;
        this.persons = 0;
        this.shelter = false;
        this.edges = new ArrayList<>();
        this.neighbours = new ArrayList<>();
    }

    void submitEdge(Edge edge){
        this.edges.add(edge);
        this.neighbours.add(edge.getNeighbour(this));
    }

    void setPersons(int persons) {
        this.persons = persons;
    }

    void setShelter() {
        this.shelter = true;
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

    int getId() {
        return id;
    }

    ArrayList<Vertex> getNeighbours(){

        neighbours.sort((o1, o2) -> {
            if (o1.getId() < o2.getId())
                return -1;
            else
                return 1;
        });

        return neighbours;
    }

    String getNeighboursToString(){

        ArrayList<Vertex> temp = getNeighbours();

        StringBuilder ans = new StringBuilder("[");
        for (int i = 0; i < temp.size(); i++) {
            Vertex v = temp.get(i);
            ans.append(v.toString());
            if (i < temp.size() - 1){
                ans.append(", ");
            }
        }
        ans.append("]");
        return ans.toString();
    }

    @Override
    public String toString() {
        return String.valueOf(this.id);
    }
}
