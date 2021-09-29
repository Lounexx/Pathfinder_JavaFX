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

    public void fillGrid(){
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                if(grid[i][j] == null ){
                    grid[i][j] = new PathNode();
                }
            }
        }
    }

    public void createCloseNodes(int x , int y){
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if(y+i >= 0 &&  y+i < rows && x+j >=0 && x+j < cols) {
                    if (grid[y + i][x + j] != grid[y][x] && grid[y + i][x + j] == null) {
                        grid[y + i][x + j] = new PathNode();
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
        }else {
            System.out.println("already filled");
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
