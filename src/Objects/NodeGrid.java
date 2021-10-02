package Objects;

import Structure.NodeStructure;

public class NodeGrid {
    private NodeStructure[][] grid;
    private int rows, cols;


    public NodeGrid(int rows,int cols){
        this.rows = rows;
        this.cols = cols;
        grid = new NodeStructure[this.rows][this.cols];
    }

    public boolean checkEnd(int x,int y){
        boolean verif = false;
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if(y+i >= 0 &&  y+i < rows && x+j >=0 && x+j < cols){
                    if(grid[y+i][x+j] instanceof StateNode && ((StateNode) grid[y+i][x+j]).getName().equals("end")){
                        verif = true;
                        break;
                    }
                }
            }
        }
        return verif;
    }

    public void createStartNode(int x, int y){
      addPathNode(x,y,new StateNode("start"));
    }

    public void createEndNode(int x, int y){
        addPathNode(x,y,new StateNode("end",false));
    }

    public boolean checkStatusNodes(int x,int y){
        boolean verif = true;
        if(grid[y][x] != null){
            verif = false;
        }
        return verif;
    }

    public void generatePresetGame(int xStart, int yStart, int xEnd, int yEnd){
        grid[yStart][xStart] = new StateNode("start");
        grid[yEnd][xEnd] = new StateNode("end");
        generateWalls();
    }

    public PathNode chooseNextNode(){
        int lowestValue = 10000;
        PathNode pathNodeToChoose = null;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if(grid[i][j] instanceof PathNode && grid[i][j].isGenerated() && !grid[i][j].isClicked()){
                    if(grid[i][j].getfCost() < lowestValue){
                        pathNodeToChoose = (PathNode) grid[i][j];
                        lowestValue = pathNodeToChoose.getfCost();
                    }
                }
            }
        }
        return pathNodeToChoose;
    }

    public void generateWalls(){
        grid[2][1] = new Wall();
        grid[2][2] = new Wall();
        grid[2][3] = new Wall();


        grid[2][1] = new Wall();
        grid[4][5] = new Wall();
        grid[2][10] = new Wall();


        grid[8][1] = new Wall();
        grid[5][2] = new Wall();
        grid[4][10] = new Wall();

    }

    public void fillGrid(){
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                if(grid[i][j] == null ){
                    grid[i][j] = new PathNode(i,j);
                }
            }
        }
    }

    public void createCloseNodes(int x , int y){
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if(y+i >= 0 &&  y+i < rows && x+j >=0 && x+j < cols) {
                    if (grid[y + i][x + j] != grid[y][x] && grid[y + i][x + j] == null) {
                        grid[y + i][x + j] = new PathNode(y+i,x+j);
                    }
                }
            }
        }
    }

    public int[] getStateNodePos(String nameSN){
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                if(grid[i][j] instanceof StateNode && ((StateNode) grid[i][j]).getName().equals(nameSN) ){
                    return new int[]{i,j};
                }
            }
        }
        return null;
    }

    public void addPathNode(int x, int y,NodeStructure node){
        if(grid[y][x] == null){
            grid[y][x] = node;
        }
    }

    public boolean isFilled(int x, int y){
        boolean verif = false;
        if(grid[y][x] != null){
            verif = true;
        }
        return verif;
    }

    public NodeStructure[][] getGrid() {
        return grid;
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }
}
