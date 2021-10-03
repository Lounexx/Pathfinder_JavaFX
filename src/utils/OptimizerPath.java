package utils;


import Objects.NodeGrid;
import Objects.PathNode;
import Objects.StateNode;
import javafx.scene.paint.Paint;


public class OptimizerPath {


    public static PathNode getStarterNode(NodeGrid grid){
        StateNode endNode = (StateNode) grid.getStateNodePos("end");
        PathNode pathNode = null;
        int x = endNode.getX();
        int y = endNode.getY();
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if(y+i >= 0 &&  y+i < grid.getRows() && x+j >=0 && x+j < grid.getCols()){
                    if(grid.getGrid()[y+i][x+j] instanceof PathNode && grid.getGrid()[y+i][x+j].isClicked()){
                        pathNode = (PathNode) grid.getGrid()[y+i][x+j];
                    }
                }
            }
        }
        return pathNode;
    }

    public static boolean finished(int x, int y, NodeGrid grid){
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if(y+i >= 0 &&  y+i < grid.getRows() && x+j >=0 && x+j < grid.getCols()){
                    if(grid.getGrid()[y+i][x+j] instanceof StateNode && ((StateNode) grid.getGrid()[y+i][x+j]).getName().equals("start")){
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static PathNode getNextNode(PathNode node){
        PathNode parent;
        try {
            parent = (PathNode) node.getParent();
        }catch (Exception e){
            parent = null;
        }

        return parent;
    }

    public static void changeColor(PathNode node, Paint color){
        node.getRectangle().setFill(color);
    }

}
