package utils;


import Objects.Wall;
import Structure.NodeStructure;

public class MapGeneratorExtractor {

    public static int[][] extractMazeSetup(NodeStructure[][] grid){
        int[][] resultGrid = new int[grid.length][grid[grid.length-1].length];
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                if(grid[i][j] instanceof Wall){
                    resultGrid[i][j] = 1;
                }
            }
        }
        return resultGrid;
    }

    public static void displayGeneratedMaze(int[][] grid){
        System.out.println("{");
        for (int i = 0; i < grid.length; i++) {
            System.out.print("{");
            for (int j = 0; j < grid[i].length; j++) {
                if(j<grid[i].length-1){
                    System.out.print(grid[i][j]+",");
                }else {
                    System.out.print(grid[i][j]);
                }
            }
            System.out.println("},");
        }
        System.out.println("}");
    }
}
