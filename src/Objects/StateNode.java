package Objects;

import Structure.NodeStructure;

public class StateNode extends NodeStructure {
    private String name;

    public StateNode(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
