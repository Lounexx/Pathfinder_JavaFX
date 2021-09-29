package sample;

import Objects.NodeGrid;
import Objects.PathNode;
import Objects.StateNode;
import Structure.NodeStructure;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Node;
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
import utils.PositionCalculer;

import java.awt.*;
import java.util.Arrays;

public class Main extends Application{
    private Scene scene;
    private Group root;
    private Pane pane;
    private NodeGrid grid;
    private int lineWidth = 6;
    private boolean startPlaced = false,
                    endPlaced = false,
                    startAlgo = false;
    private int count;

    public void start(Stage primaryStage){
        root = new Group();
        scene = new Scene(root,1000,720);
        primaryStage.setResizable(false);
        initt();
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void initt(){
        pane = new Pane();
        pane.setPrefSize(scene.getWidth(),scene.getHeight());
        pane.setStyle("-fx-background-color: white;");

        grid = new NodeGrid(14,14);

        drawHorizontalLines();

        drawVerticalLines();

        updateGUI();

        root.getChildren().add(pane);



        root.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                int x = (int)event.getX()/(int)(scene.getWidth()/grid.getCols());
                int y = (int)event.getY()/(int)(scene.getHeight()/grid.getRows());

                //Check if spawners are placed
                if(!startPlaced){
                    grid.addPathNode(x,y,new StateNode("start"));
                    startPlaced = true;
                }else {
                    if(!endPlaced){
                        grid.addPathNode(x,y,new StateNode("end"));
                        endPlaced = true;
                    }else {
                        startAlgo = true;
                    }
                }

                // Check if pathing is started
                if(startAlgo){
                    moveFromNode(x,y);
                }

                if(checkAction(x,y)){
                    // Set click to true only if pathing is started, else it will automatically put state nodes on true
                    if(startAlgo){
                        grid.getGrid()[y][x].setClicked(true);
                    }
                    updateGUI(x,y);
                }

            }
        });
    }


    public void drawRectangleOnPos(int x, int y,NodeStructure node){
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
        }else {
            rectangle.setFill(Color.LIGHTBLUE);
        }

        pane.getChildren().add(rectangle);

        //If node has a gCost value, show its value
        if(node.getgCost() !=0 ){
            Text text = new Text(String.valueOf(node.getgCost()));
            text.setLayoutX(rectangle.getLayoutX() + 20);
            text.setLayoutY(rectangle.getLayoutY() + 20);
            text.setFont(new Font(20));
            text.setStroke(Color.BLACK);
            text.setTextAlignment(TextAlignment.CENTER);
            pane.getChildren().add(text);
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

        for(Node node: pane.getChildren()){
            if(node instanceof Rectangle){
                count++;
            }
        }
        System.out.println(count);
        count = 0;

    }

    public void updateGUI(){
        for (int i = 0; i < grid.getRows(); i++) {
            for (int j = 0; j < grid.getCols(); j++) {
                if(grid.getGrid()[i][j] != null ){
                    drawRectangleOnPos(j,i,grid.getGrid()[i][j]);
                }
            }
        }
        drawVerticalLines();
        drawHorizontalLines();
    }

    public void moveFromNode(int x, int y){
        if(checkAction(x,y)){
            grid.createCloseNodes(x,y);
            PositionCalculer.calculateGcost(x,y,grid);
        }
    }

    public boolean checkAction(int x, int y){
        boolean verif = false;
        if(grid.isFilled(x,y) && !grid.getGrid()[y][x].isClicked()){
            verif = true;
        }
        return verif;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
