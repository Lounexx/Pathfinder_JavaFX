package Objects;

import Structure.NodeStructure;
import javafx.scene.shape.Rectangle;


public class PathNode extends NodeStructure {

    private Rectangle rectangle;

    public PathNode(int y ,int x){
        super.isClickable = true;
        super.x = x;
        super.y = y;
    }

    public Rectangle getRectangle() {
        return rectangle;
    }

    public void setRectangle(Rectangle rectangle) {
        this.rectangle = rectangle;
    }
}
