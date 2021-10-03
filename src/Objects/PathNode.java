package Objects;

import Structure.NodeStructure;
import javafx.scene.text.Text;


public class PathNode extends NodeStructure {
    private NodeStructure parent;
    private Text textGcost, textFcost;

    public PathNode(int y ,int x){
        super.isClickable = true;
        super.x = x;
        super.y = y;
    }

    public NodeStructure getParent() {
        return parent;
    }

    public void setParent(NodeStructure parent) {
        this.parent = parent;
    }

    public Text getTextGcost() {
        return textGcost;
    }

    public void setTextGcost(Text textGcost) {
        this.textGcost = textGcost;
    }

    public Text getTextFcost() {
        return textFcost;
    }

    public void setTextFcost(Text textFcost) {
        this.textFcost = textFcost;
    }
}
