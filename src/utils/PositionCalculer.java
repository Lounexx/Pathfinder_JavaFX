package utils;

import Objects.NodeGrid;
import Objects.PathNode;

public class PositionCalculer {

    /**
     * Calculate every gCost (Distance between the clicked node and the next node) around the clicked node
     * @param x is the x position of our clicked case
     * @param y is the y position of our clicked case
     * @param grid is the grid with every node
     */
    public static void calculateGcost(int x, int y, NodeGrid grid){
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if(y+i >= 0 &&  y+i < grid.getRows() && x+j >=0 && x+j < grid.getCols()){
                    if(grid.getGrid()[y+i][x+j] != grid.getGrid()[y][x] && grid.getGrid()[y+i][x+j] instanceof PathNode && !((PathNode) grid.getGrid()[y+i][x+j]).isGenerated()){
                        if((i==-1 && j==-1) || (i == -1 && j == 1) || (i == 1 && j == -1) || (i == 1 && j == 1)){
                            grid.getGrid()[y+i][x+j].setgCost(grid.getGrid()[y][x].getgCost() + 14);
                        }else {
                            grid.getGrid()[y+i][x+j].setgCost(grid.getGrid()[y][x].getgCost() + 10);
                        }
                    }
                }
            }
        }
    }


}
