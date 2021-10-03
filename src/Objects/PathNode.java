package Objects;

import Structure.NodeStructure;


public class PathNode extends NodeStructure {
    private PathNode parent;

    public PathNode(int y ,int x){
        super.isClickable = true;
        super.x = x;
        super.y = y;
    }

}
