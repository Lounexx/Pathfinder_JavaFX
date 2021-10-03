package sample;

import Objects.NodeGrid;
import Objects.PathNode;
import Objects.StateNode;
import Objects.Wall;
import Structure.NodeStructure;
import Structure.SettingsPane;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;
import utils.OptimizerPath;
import utils.PositionCalculer;

import java.util.ArrayList;


public class Main extends Application{
    private Scene scene;
    private Group root;
    private Pane pane, secondPane;
    private NodeGrid grid;
    private Timeline run,optimized;
    private int lineWidth = 6;
    private boolean startPlaced = false,
                    endPlaced = false;
    private int xStart, yStart, xEnd ,yEnd;
    private SettingsPane settingsPane;
    private boolean isPlacingWalls = false;
    private boolean isPlacingState = false;

    public void start(Stage primaryStage){
        root = new Group();
        scene = new Scene(root,1080,720);
        primaryStage.setResizable(false);
        initt();
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void initt(){
        pane = new Pane();
        pane.setPrefSize(1000,720);
        pane.setStyle("-fx-background-color: white;");
        settingsPane = new SettingsPane(this);

        secondPane = new Pane();
        secondPane.setPrefSize(scene.getWidth(),200);
        secondPane.setLayoutX(0);
        secondPane.setLayoutY(720);

        grid = new NodeGrid(5,5);

        updateGUI();

        root.getChildren().add(pane);
        root.getChildren().add(secondPane);

        //play();

        root.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                int x = (int)event.getX()/(int)(scene.getWidth()/grid.getCols());
                int y = (int)event.getY()/(int)(scene.getHeight()/grid.getRows());

                if(isPlacingWalls){
                    addWall(x,y);
                }

                //Check if spawners are placed
                if(isPlacingState){
                    if(!startPlaced){
                        grid.createStartNode(x,y);
                        startPlaced = true;
                        xStart = x;
                        yStart = y;
                        updateGUI(x,y);
                    }else {
                        if(!endPlaced && grid.checkStatusNodes(x,y)){ // Has to place end node else it won't play
                            grid.createEndNode(x,y);
                            endPlaced = true;
                            xEnd = x;
                            yEnd = y;
                            updateGUI(x,y);
                        }
                    }
                }
                if(startPlaced && endPlaced){
                    moveFromNode(x,y);
                }
            }

        });

    }


    public boolean isStartPlaced() {
        return startPlaced;
    }

    public boolean isEndPlaced() {
        return endPlaced;
    }

    public void setPlacingWalls(boolean placingWalls) {
        isPlacingWalls = placingWalls;
    }

    public void setPlacingState(boolean placingState) {
        isPlacingState = placingState;
    }

    public void deleteAll(){
        pane.getChildren().removeIf(node -> node instanceof Rectangle);
        pane.getChildren().removeIf(node -> node instanceof Text);
    }

    public void deleteOnlyPath(){
        for(NodeStructure[] nodeStructure : grid.getGrid()){
            for(NodeStructure node : nodeStructure){
                if(node instanceof PathNode || node instanceof StateNode){
                    pane.getChildren().remove( node.getRectangle());
                }
            }
        }
        pane.getChildren().removeIf(node -> node instanceof Text);
        grid.setChosenNodes(new ArrayList<>());
    }

    public void drawRectangleOnPos(int x, int y, NodeStructure node){
        Rectangle rectangle = new Rectangle();
        rectangle.setWidth((scene.getWidth()/grid.getCols()));
        rectangle.setHeight((scene.getHeight()/grid.getRows()));
        rectangle.setLayoutX((scene.getWidth()/grid.getCols())*x);
        rectangle.setLayoutY((scene.getHeight()/grid.getRows())*y);


        // Change color following node class
        if(node instanceof StateNode){
            if(((StateNode)node).getName().equals("start")){
                rectangle.setFill(Color.GREEN);
            }else {
                rectangle.setFill(Color.YELLOW);
            }
        }else if(node instanceof Wall){
            rectangle.setFill(Color.DARKGREY);
        }else {
            rectangle.setFill(Color.LIGHTBLUE);
        }

        pane.getChildren().add(rectangle);

        if(grid.getGrid()[y][x] instanceof PathNode || grid.getGrid()[y][x] instanceof StateNode){
             grid.getGrid()[y][x].setRectangle(rectangle);
        }

        //If node has a gCost value, show its value
        if(node instanceof PathNode ){
            //
            Text gCost = new Text(String.valueOf(node.getgCost()));
            gCost.setLayoutX(rectangle.getLayoutX() + 20);
            gCost.setLayoutY(rectangle.getLayoutY() + 20);
            gCost.setFont(new Font(12));
            gCost.setStroke(Color.BLACK);
            gCost.setTextAlignment(TextAlignment.CENTER);
            pane.getChildren().add(gCost);
            //
            Text hCost = new Text(String.valueOf(node.gethCost()));
            hCost.setLayoutX(rectangle.getLayoutX() + rectangle.getWidth() - 40);
            hCost.setLayoutY(rectangle.getLayoutY() + 20);
            hCost.setFont(new Font(12));
            hCost.setStroke(Color.BLACK);
            hCost.setTextAlignment(TextAlignment.CENTER);
            pane.getChildren().add(hCost);

            Text fCost = new Text(String.valueOf(node.getfCost()));
            fCost.setLayoutX(rectangle.getLayoutX() + rectangle.getWidth()/2 - 10);
            fCost.setLayoutY(rectangle.getLayoutY() + rectangle.getHeight()/2);
            fCost.setFont(new Font(16));
            fCost.setStroke(Color.BLACK);
            fCost.setTextAlignment(TextAlignment.CENTER);
            pane.getChildren().add(fCost);
        }

        node.setGenerated(true);

    }

    public void drawVerticalLines(){
        for (int j = 1; j <= grid.getCols(); j++) {
            Line line = new Line();
            line.setStartX((scene.getWidth()/grid.getCols())*j);
            line.setEndX(line.getStartX());
            line.setStartY(0);
            line.setEndY(scene.getHeight());
            line.setStroke(Color.BLACK);
            line.setStrokeWidth(lineWidth);
            pane.getChildren().add(line);
        }
    }

    public void drawHorizontalLines(){
        for (int i = 1; i <= grid.getRows(); i++) {
            Line line = new Line();
            line.setStartX(0);
            line.setStartY((scene.getHeight()/grid.getRows())*i);
            line.setEndX(scene.getWidth());
            line.setEndY(line.getStartY());
            line.setStroke(Color.BLACK);
            line.setStrokeWidth(lineWidth);
            pane.getChildren().add(line);
        }
    }

    /**
     * Update the GUI by getting informations from the NodeGrid
     */
    public void updateGUI(int x, int y){
        pane.getChildren().removeIf(node -> node instanceof Line);
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if(y+i >= 0 &&  y+i < grid.getRows() && x+j >=0 && x+j < grid.getCols()){
                    if(grid.getGrid()[y+i][x+j] != null && !grid.getGrid()[y+i][x+j].isGenerated()){
                        NodeStructure node = grid.getGrid()[y+i][x+j];
                        drawRectangleOnPos(x+j,y+i,node);
                    }
                }
            }
        }

        drawVerticalLines();
        drawHorizontalLines();

    }

    public void updateGUI(){
        for (int i = 0; i < grid.getRows(); i++) {
            for (int j = 0; j < grid.getCols(); j++) {
                if(grid.getGrid()[i][j] != null ){
                    drawRectangleOnPos(j,i,grid.getGrid()[i][j]);
                }
            }
        }
        drawHorizontalLines();
        drawVerticalLines();
    }

    public void moveFromNode(int x, int y){
        if(checkAction(x,y) && grid.getGrid()[y][x].isClickable()){
            grid.createCloseNodes(x,y);
            PositionCalculer.calculateGcost(x,y,grid.getStateNodePos("start"),grid);
            PositionCalculer.calculateHcost(x,y,grid.getStateNodePos("end"),grid);
            PositionCalculer.calculateFcost(x,y,grid);
            changeRectangleColorOnClick(x,y);
            grid.getGrid()[y][x].setClicked(true);
        }
        updateGUI(x,y);
    }

    public boolean checkAction(int x, int y){
        boolean verif = false;
        // Verify if x and y coordinates are on bounds.
        if(x < grid.getRows() && y < grid.getCols()){
            if(grid.isFilled(x,y) && !grid.getGrid()[y][x].isClicked()){
                verif = true;
            }
        }
        return verif;
    }

    /**
     * Change a pathnode color when it gets clicked.
     * @param x coordinate of the node
     * @param y coordinate of the node
     */
    public void changeRectangleColorOnClick(int x,int y){
        if(grid.getGrid()[y][x] instanceof PathNode){
            ((PathNode) grid.getGrid()[y][x]).getRectangle().setFill(Color.RED);
        }
    }

    public void start(){
        moveFromNode(xStart,yStart);
    }

    /**
     * Plays automatically the pathfinder.
     * Using a timeline to animate the search.
     */
    public void play(){
        start();
        run = new Timeline(new KeyFrame(Duration.seconds(0.05), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                PathNode nextNode = grid.chooseNextNode();
                boolean verif = false;
                try {
                    moveFromNode(nextNode.getX(),nextNode.getY());
                    grid.getChosenNodes().add(nextNode);
                    verif = grid.checkEnd(nextNode.getX(),nextNode.getY());
                }catch (Exception E){
                    run.stop();
                }
                if(verif){
                    run.stop();
                }
            }
        }));
        run.setCycleCount(Animation.INDEFINITE);
        run.play();
    }

    public void restart(){
        if(run != null){
           run.stop();
        }

        deleteAll();

        grid = new NodeGrid(grid.getRows(),grid.getCols());

        startPlaced = false;

        endPlaced = false;

        updateGUI();
    }

    public void restartOnlyPath(){
        if(run != null){
            run.stop();
        }

        deleteOnlyPath();

        for (int i = 0; i < grid.getRows(); i++) {
            for (int j = 0; j < grid.getCols(); j++) {
                if(grid.getGrid()[i][j] instanceof PathNode || grid.getGrid()[i][j] instanceof StateNode){
                    grid.getGrid()[i][j] = null;
                }
            }
        }

        startPlaced = false;

        endPlaced = false;

        updateGUI();
    }

    public void addWall(int x, int y){
        grid.addWall(x,y);
        updateGUI(x,y);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
