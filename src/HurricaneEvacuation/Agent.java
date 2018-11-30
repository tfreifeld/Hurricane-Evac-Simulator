package HurricaneEvacuation;

abstract class Agent {

    int agentNum;
    Vertex location;

    abstract void makeOperation();

    void setLocation(Vertex location){
        this.location = location;
    }
}
