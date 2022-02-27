package utils;

import Objects.NodeGrid;
import Objects.Wall;

public class MapGenerator {

    public static boolean isLastWallPut = false;

    public static int wallSpammer = 0;

    /**
     * Generate a random maze according to a system of randomization.
     * Randomization is protected by an anti-spam, checks if a wall have been placed earlier close to this one
     * and reduce the chance of spawning another.
     * @param grid is a given grid to apply the walls
     */
    public static void generateRandomWalls(NodeGrid grid){
        for(int i = 0; i<grid.getGrid().length;i++){
            for (int j = 0; j < grid.getGrid()[i].length; j++) {
                double random = Math.floor(Math.random() * 100);
                if(isLastWallPut){
                    if(random<=(25 - 10*wallSpammer) && grid.getGrid()[i][j] == null){
                        grid.getGrid()[i][j] = new Wall();
                        isLastWallPut = true;
                        wallSpammer++;
                    }else{
                        isLastWallPut = false;
                        wallSpammer = 0;
                    }
                }else{
                    if(random<=50 && grid.getGrid()[i][j] == null){
                        grid.getGrid()[i][j] = new Wall();
                        isLastWallPut = true;
                        wallSpammer++;
                    }else{
                        isLastWallPut = false;
                        wallSpammer=0;
                    }
                }
            }
        }
    }

}
