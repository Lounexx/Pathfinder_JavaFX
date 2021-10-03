package Objects;

import Structure.NodeStructure;


import java.util.ArrayList;

public class NodeGrid {
    private NodeStructure[][] grid;
    private int rows, cols;
    private ArrayList<PathNode> chosenNodes = new ArrayList<>();


    public NodeGrid(int rows,int cols){
        this.rows = rows;
        this.cols = cols;
        grid = new NodeStructure[this.rows][this.cols];
    }

    public boolean checkFinish(int x, int y,String nameStateNode){
        boolean verif = false;
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if(y+i >= 0 &&  y+i < rows && x+j >=0 && x+j < cols){
                    if(grid[y+i][x+j] instanceof StateNode && ((StateNode) grid[y+i][x+j]).getName().equals(nameStateNode)){
                        verif = true;
                        break;
                    }
                }
            }
        }
        return verif;
    }

    public void createStartNode(int x, int y){
      addPathNode(x,y,new StateNode("start",x,y));
    }

    public void createEndNode(int x, int y){
        addPathNode(x,y,new StateNode("end",x,y,false));
    }

    public boolean checkStatusNodes(int x,int y){
        boolean verif = true;
        if(grid[y][x] != null){
            verif = false;
        }
        return verif;
    }

    public void generatePresetGame(int xStart, int yStart, int xEnd, int yEnd){
        createStartNode(xStart,yStart);
        createEndNode(xEnd,yEnd);
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

    public void addWall(int x, int y){
        if(y < rows && x < cols && grid[y][x] == null){
            grid[y][x] = new Wall();
        }
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
                    if (grid[y + i][x + j] != grid[y][x] && grid[y + i][x + j] == null && checkCross(x,y,x+j,y+i)) {
                        grid[y + i][x + j] = new PathNode(y+i,x+j);
                        ((PathNode) grid[y + i][x + j]).setParent(grid[y][x]);
                        updateClick(x,y);
                    }
                }
            }
        }
    }

    public void updateClick(int x,int y){
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if(y+i >= 0 &&  y+i < rows && x+j >=0 && x+j < cols) {
                    if (grid[y + i][x + j] instanceof PathNode && grid[y][x].getgCost() < ((PathNode) grid[y + i][x + j]).getParent().getgCost() && !grid[y + i][x + j].isClicked()) {
                        ((PathNode) grid[y + i][x + j]).setParent(grid[y][x]);
                        int xdif = Math.abs(x+j);
                        int ydif = Math.abs(y+i);
                        if(xdif == 1 && ydif == 1){
                            grid[y + i][x + j].setgCost(((PathNode) grid[y + i][x + j]).getParent().getgCost() + 14);
                        }else {
                            grid[y + i][x + j].setgCost(((PathNode) grid[y + i][x + j]).getParent().getgCost() + 10);
                        }
                        grid[y + i][x + j].setfCost(grid[y + i][x + j].getgCost() + grid[y + i][x + j].gethCost());

                        try{
                            ((PathNode) grid[y + i][x + j]).getTextGcost().setText(String.valueOf(grid[y + i][x + j].getgCost()));
                            ((PathNode) grid[y + i][x + j]).getTextFcost().setText(String.valueOf(grid[y + i][x + j].getfCost()));
                        }catch (Exception ignored){}
                    }
                }
            }
        }
    }

    public NodeStructure getStateNodePos(String nameSN){
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                if(grid[i][j] instanceof StateNode && ((StateNode) grid[i][j]).getName().equals(nameSN) ){
                    return grid[i][j];
                }
            }
        }
        return null;
    }

    public void addPathNode(int x, int y,NodeStructure node){
        if(y < rows && x < cols && grid[y][x] == null ){
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

    public boolean checkCross(int x, int y , int xToGo, int yToGo){
        boolean verif = true;
        if(xToGo-x < 0 && yToGo-y < 0 ){
            if(grid[y][x-1] instanceof Wall && grid[y-1][x] instanceof Wall ){
                verif = false;
            }
        }else if(xToGo-x > 0 && yToGo-y < 0 ){
            if(grid[y-1][x] instanceof Wall && grid[y][x+1] instanceof Wall ){
                verif = false;
            }
        }else if(xToGo-x < 0 && yToGo-y > 0 ){
            if(grid[y][x-1] instanceof Wall && grid[y+1][x] instanceof Wall ){
                verif = false;
            }
        }else if(xToGo-x > 0 && yToGo-y > 0 ){
            if(grid[y][x+1] instanceof Wall && grid[y+1][x] instanceof Wall ){
                verif = false;
            }
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

    public ArrayList<PathNode> getChosenNodes() {
        return chosenNodes;
    }

    public void setChosenNodes(ArrayList<PathNode> chosenNodes) {
        this.chosenNodes = chosenNodes;
    }
}
