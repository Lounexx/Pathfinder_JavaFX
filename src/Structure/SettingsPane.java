package Structure;

import Objects.NodeGrid;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import sample.Main;

import java.util.ArrayList;

public class SettingsPane {
    private Stage stage;
    private Main main;
    private ToggleButton addWalls, setupState, play, reset, resetOnlyPath;
    private ArrayList<ToggleButton> list;

    public SettingsPane(Main main){
        this.main = main;
        initStage();
        addWalls = new ToggleButton("Walls");
        addWalls.setPrefSize(stage.getWidth(),40);
        addWalls.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                toggleButtonsOff(addWalls);

                main.setPlacingWalls(addWalls.isSelected());

                main.setPlacingState(setupState.isSelected());

            }
        });
        setupState = new ToggleButton("States");
        setupState.setPrefSize(stage.getWidth(),40);
        setupState.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                toggleButtonsOff(setupState);

                main.setPlacingWalls(addWalls.isSelected());

                main.setPlacingState(setupState.isSelected());
            }
        });

        play = new ToggleButton("Play");
        play.setPrefSize(stage.getWidth(),40);
        play.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                toggleButtonsOff(play);

                main.setPlacingWalls(addWalls.isSelected());

                main.setPlacingState(setupState.isSelected());

                if(main.isEndPlaced() && main.isStartPlaced()){
                    main.play();
                }

            }
        });

        reset = new ToggleButton("Reset");
        reset.setPrefSize(stage.getWidth(),40);
        reset.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                toggleButtonsOff(reset);

                main.setPlacingWalls(addWalls.isSelected());

                main.setPlacingState(setupState.isSelected());

                main.restart();

            }
        });

        resetOnlyPath = new ToggleButton("Reset only path");
        resetOnlyPath.setPrefSize(stage.getWidth(),40);
        resetOnlyPath.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                toggleButtonsOff(resetOnlyPath);

                main.setPlacingWalls(addWalls.isSelected());

                main.setPlacingState(setupState.isSelected());

                main.restartOnlyPath();

            }
        });

        initList();

        VBox container = new VBox(addWalls,setupState, play, reset,resetOnlyPath);
        container.setSpacing(4);
        container.setPadding(new Insets(5,5,5,5));
        container.setFillWidth(true);
        container.setAlignment(Pos.CENTER);
        stage.setScene(new Scene(container));
        stage.show();
    }

    public void initStage(){
        stage = new Stage();
        stage.setTitle("Settings");
        stage.setResizable(false);
        stage.setWidth(350);
        stage.setHeight(400);
        stage.setX(1500);
        stage.setY(200);
    }

    public void toggleButtonsOff(ToggleButton actualButton){
        for(ToggleButton button: list){
            if(button != actualButton){
                button.setSelected(false);
            }
        }
    }
    public void initList(){
        list = new ArrayList<>();
        list.add(addWalls);
        list.add(setupState);
        list.add(play);
        list.add(reset);
    }

}
