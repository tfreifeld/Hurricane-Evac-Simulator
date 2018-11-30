package HurricaneEvacuation;

class VandalAgent extends Agent {

    VandalAgent(int agentNum) {
        this.agentNum = agentNum;
    }

    @Override
    Move makeOperation() {

        return null;
    }

    @Override
    public String toString() {
        return "{Type: Vandal\n" + super.toString();
    }
}
