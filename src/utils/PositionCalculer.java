package utils;

import Objects.NodeGrid;
import Objects.PathNode;
import Structure.NodeStructure;

public class PositionCalculer {

    /**
     * Calculate every gCost (Distance between the clicked node and the next node) around the clicked node
     * @param x is the x position of our clicked case
     * @param y is the y position of our clicked case
     * @param grid is the grid with every node
     */
    public static void calculateGcost(int x, int y, NodeGrid grid){
        int xDif,yDif;
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if(y+i >= 0 &&  y+i < grid.getRows() && x+j >=0 && x+j < grid.getCols()){
                    if(grid.getGrid()[y+i][x+j] != grid.getGrid()[y][x] && grid.getGrid()[y+i][x+j] instanceof PathNode && !(grid.getGrid()[y+i][x+j]).isGenerated()){
                        xDif = Math.abs(x+j-x);
                        yDif = Math.abs(y+i-y);
                        if(xDif == 1 && yDif == 1){
                            grid.getGrid()[y + i][x + j].setgCost(((PathNode) grid.getGrid()[y + i][x + j]).getParent().getgCost() + 14);
                        }else {
                            grid.getGrid()[y + i][x + j].setgCost(((PathNode) grid.getGrid()[y + i][x + j]).getParent().getgCost() + 10);
                        }
                    }
                }
            }
        }
    }


    public static void calculateHcost(int x, int y,NodeStructure node, NodeGrid grid){
        int xEnd = node.getX();
        int yEnd = node.getY();
        int xDif,yDif;
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if(y+i >= 0 &&  y+i < grid.getRows() && x+j >=0 && x+j < grid.getCols()){
                    if(grid.getGrid()[y+i][x+j] != grid.getGrid()[y][x] && grid.getGrid()[y+i][x+j] instanceof PathNode && !(grid.getGrid()[y+i][x+j]).isGenerated()){
                        xDif = Math.abs(x+j-xEnd);
                        yDif = Math.abs(y+i-yEnd);
                        while(xDif!=0 && yDif!=0){
                            grid.getGrid()[y+i][x+j].sethCost(grid.getGrid()[y+i][x+j].gethCost() + 14);
                            xDif--;
                            yDif--;

                        }
                        grid.getGrid()[y+i][x+j].sethCost(grid.getGrid()[y+i][x+j].gethCost() + xDif * 10 + yDif * 10);
                    }
                }
            }
        }
    }


    public static void calculateFcost(int x, int y, NodeGrid grid){
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if(y+i >= 0 &&  y+i < grid.getRows() && x+j >=0 && x+j < grid.getCols()){
                    if(grid.getGrid()[y+i][x+j] != grid.getGrid()[y][x] && grid.getGrid()[y+i][x+j] instanceof PathNode && !(grid.getGrid()[y+i][x+j]).isGenerated()){
                        grid.getGrid()[y+i][x+j].setfCost(grid.getGrid()[y+i][x+j].getgCost() + grid.getGrid()[y+i][x+j].gethCost());
                    }
                }
            }
        }
    }


}
