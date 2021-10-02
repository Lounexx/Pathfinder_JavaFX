package Objects;

import Structure.NodeStructure;

public class StateNode extends NodeStructure {
    private String name;

    public StateNode(String name){
        this.name = name;
        super.isClickable = true;
    }
    public StateNode(String name , boolean clickable){
        this.name = name;
        super.isClickable = clickable;
    }

    public String getName() {
        return name;
    }
}
