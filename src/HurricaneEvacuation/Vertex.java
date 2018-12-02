package HurricaneEvacuation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

class Vertex {

    private int id;
    private int persons;
    private boolean shelter;
    private HashMap<Integer,Edge> edges;

    Vertex(int id) {
        this.id = id;
        this.persons = 0;
        this.shelter = false;
        this.edges = new HashMap<>();
    }

    void submitEdge(Edge edge){

        this.edges.put(edge.getNeighbour(this).getId(),edge);

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

    HashMap<Integer, Edge> getEdges() {
        return edges;
    }

    int getId() {
        return id;
    }

    Set<Integer> getNeighbours(){

        return edges.keySet();

    }

    /**
     * Returns an edge to the requested neighbour.
     * @param id Requested neighbour's id.
     * @return Edge to the neighbour.
     * @throws NotNeighbourException if a neighbour with the requested id was not found.
     */
    Edge getNeighbour(int id) throws NotNeighbourException{

        if(getNeighbours().contains(id))
            return edges.get(id);
        else {
            throw new NotNeighbourException();
        }
    }

    String getNeighboursToString(){

        Iterator<Integer> iterator = getNeighbours().iterator();

        StringBuilder ans = new StringBuilder("[");

        while (iterator.hasNext()){
            ans.append(iterator.next());
            if (iterator.hasNext()){
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

    class NotNeighbourException extends Throwable {
    }
}
