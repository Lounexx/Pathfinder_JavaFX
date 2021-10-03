package Objects;

import Structure.NodeStructure;

public class StateNode extends NodeStructure {
    private String name;

    public StateNode(String name, int x, int y){
        this.name = name;
        super.x = x;
        super.y = y;
        super.isClickable = true;
    }
    public StateNode(String name , int x,int y ,boolean clickable){
        this.name = name;
        super.x = x;
        super.y = y;
        super.isClickable = clickable;
    }

    public String getName() {
        return name;
    }
}
