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
import utils.MapGenerator;
import utils.MapGeneratorExtractor;
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
    private int xStart, yStart, xEnd ,yEnd;
    private SettingsPane settingsPane;
    private boolean startPlaced = false;
    private boolean endPlaced = false;
    private boolean isPlacingWalls = false;
    private boolean isPlacingState = false;
    private boolean hasFailed = false;
    public boolean hasStarted = false;

    private boolean isShowingParameters = false;

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
        pane.setPrefSize(scene.getWidth(),scene.getHeight());
        pane.setStyle("-fx-background-color: white;");
        settingsPane = new SettingsPane(this);

        secondPane = new Pane();
        secondPane.setPrefSize(scene.getWidth(),200);
        secondPane.setLayoutX(0);
        secondPane.setLayoutY(720);

        grid = new NodeGrid(30,30);

        initRandomMaze();
        updateGUI();

        root.getChildren().add(pane);
        root.getChildren().add(secondPane);

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

    /**
     * Delete everything from the GUI except lines.
     */
    public void deleteAll(){
        pane.getChildren().removeIf(node -> node instanceof Rectangle);
        pane.getChildren().removeIf(node -> node instanceof Text);
    }

    /**
     * Delete only the needed information to restart a pathfinder without resetting the table.
     * Removes pathnodes and statenodes.
     * Removes text from pathnodes.
     * Keep all the walls.
     * Does not delete lines
     */
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

    /**
     * Draw a rectangle following the node type and it's position
     * @param x coordinate of the node
     * @param y coordinate of the node
     * @param node is the node we want to display
     */
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
            rectangle.setFill(Color.BLACK);
        }else {
            rectangle.setFill(Color.LIGHTBLUE);
        }

        pane.getChildren().add(rectangle);

        if(node instanceof PathNode || node instanceof StateNode){
             node.setRectangle(rectangle);
        }
        if(isShowingParameters){
            //If node has a gCost value, show its value
            if(node instanceof PathNode ){

                // Setup gCost number in PathNode
                Text gCost = new Text(String.valueOf(node.getgCost()));
                gCost.setLayoutX(rectangle.getLayoutX() + 20);
                gCost.setLayoutY(rectangle.getLayoutY() + 20);
                gCost.setFont(new Font(12));
                gCost.setStroke(Color.BLACK);
                gCost.setTextAlignment(TextAlignment.CENTER);
                pane.getChildren().add(gCost);
                ((PathNode) node).setTextGcost(gCost);

                //Setup hCost number in PathNode
                Text hCost = new Text(String.valueOf(node.gethCost()));
                hCost.setLayoutX(rectangle.getLayoutX() + rectangle.getWidth() - 40);
                hCost.setLayoutY(rectangle.getLayoutY() + 20);
                hCost.setFont(new Font(12));
                hCost.setStroke(Color.BLACK);
                hCost.setTextAlignment(TextAlignment.CENTER);
                pane.getChildren().add(hCost);

                //Setup fCost number in PathNode
                Text fCost = new Text(String.valueOf(node.getfCost()));
                fCost.setLayoutX(rectangle.getLayoutX() + rectangle.getWidth()/2 - 10);
                fCost.setLayoutY(rectangle.getLayoutY() + rectangle.getHeight()/2);
                fCost.setFont(new Font(16));
                fCost.setStroke(Color.BLACK);
                fCost.setTextAlignment(TextAlignment.CENTER);
                pane.getChildren().add(fCost);
                ((PathNode) node).setTextFcost(fCost);
            }
        }
        node.setGenerated(true);
    }

    /**
     * Draw the vertical lines of the table
     */
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

    /**
     * Draw the horizontal lines of the table
     */
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
     * Updates the GUI on a defined range by the x and y parameters
     * @param x position where to update.
     * @param y position where to update.
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

    /**
     * Updating the GUI for the entire table
     */
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

    /**
     * Jumping to the node where we clicked. Changing the node state to clicked.
     * @param x position of the x mouse click event.
     * @param y position of the y mouse click event.
     */
    public void moveFromNode(int x, int y){
        if(checkAction(x,y) && grid.getGrid()[y][x].isClickable()){
            grid.createCloseNodes(x,y);
            PositionCalculer.calculateGcost(x,y,grid);
            PositionCalculer.calculateHcost(x,y,grid.getStateNodePos("end"),grid);
            PositionCalculer.calculateFcost(x,y,grid);
            changeRectangleColorOnClick(x,y);
            grid.getGrid()[y][x].setClicked(true);

        }
        updateGUI(x,y);
    }


    /**
     * Checks if the user action is correct.
     * Prevents from getting out of bounds of the table.
     * @param x position of the x mouse click event.
     * @param y position of the y mouse click event.
     * @return if the action is correct or not
     */
    public boolean checkAction(int x, int y){
        boolean verif = false;
        // Verify if x and y coordinates are on bounds.
        if(x < grid.getCols() && y < grid.getRows()){
            if(grid.isFilled(x,y) && !grid.getGrid()[y][x].isClicked()){
                verif = true;
            }
        }
        return verif;
    }


    /**
     *
     * @param x
     * @param y
     */
    public void changeRectangleColorOnClick(int x,int y){
        if(grid.getGrid()[y][x] instanceof PathNode){
            grid.getGrid()[y][x].getRectangle().setFill(Color.RED);
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
        hasStarted = true;
        run = new Timeline(new KeyFrame(Duration.seconds(0.005), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                PathNode nextNode = grid.chooseNextNode();
                final boolean[] verif = {false};
                try {
                    moveFromNode(nextNode.getX(),nextNode.getY());
                    grid.getChosenNodes().add(nextNode);
                    verif[0] = grid.checkFinish(nextNode.getX(),nextNode.getY(),"end");
                }catch (Exception E){
                    run.stop();
                    for(NodeStructure[] node : grid.getGrid()){
                        for (NodeStructure aNode:node) {
                            if(aNode instanceof PathNode){
                                aNode.getRectangle().setFill(Color.DARKGREY);
                            }
                        }
                    }
                    hasFailed = true;
                    if(hasFailed){
                        restartRandomMaze();
                    }
                }
                if(verif[0]){
                    run.stop();
                    verif[0] = false;
                    final PathNode[] node = {OptimizerPath.getStarterNode(grid)};
                    optimized = new Timeline(new KeyFrame(Duration.seconds(0.1), new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent event) {
                            OptimizerPath.changeColor(node[0],Color.FUCHSIA);
                            verif[0] = OptimizerPath.finished(node[0].getX(), node[0].getY(),grid);
                            node[0] = OptimizerPath.getNextNode(node[0]);
                            if(verif[0]){
                                optimized.stop();
                                int[][] result = MapGeneratorExtractor.extractMazeSetup(grid.getGrid());
                                MapGeneratorExtractor.displayGeneratedMaze(result);
                            }
                        }
                    }));
                    optimized.setCycleCount(Animation.INDEFINITE);
                    optimized.play();
                }
            }
        }));
        run.setCycleCount(Animation.INDEFINITE);
        run.play();
    }


    public void restart(){
        hasStarted = false;
        if(run != null){
           run.stop();
        }
        if(optimized != null){
            optimized.stop();
        }
        deleteAll();
        grid = new NodeGrid(grid.getRows(),grid.getCols());
        startPlaced = false;
        endPlaced = false;
        updateGUI();
        hasFailed = false;
    }


    public void restartOnlyPath(){
        if(run != null){
            run.stop();
        }

        if(optimized != null){
            optimized.stop();
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


    public void initRandomMaze(){
        grid.createStartNode(0,0);
        startPlaced = true;
        xStart = 0;
        yStart = 0;
        updateGUI(0,0);

        grid.createEndNode(grid.getRows()-1,grid.getCols()-1);
        endPlaced = true;
        xEnd = grid.getRows()-1;
        yEnd = grid.getCols()-1;
        updateGUI(grid.getRows()-1,grid.getCols()-1);


        MapGenerator.generateRandomWalls(grid);
        updateGUI();
        play();
    }


    public void restartRandomMaze(){
        restart();
        initRandomMaze();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
