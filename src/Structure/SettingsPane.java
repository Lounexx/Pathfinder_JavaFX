package Structure;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import sample.Main;

public class SettingsPane {
    private Stage stage;
    private Main main;
    private ToggleButton addWalls, setupState;

    public SettingsPane(Main main){
        this.main = main;
        initStage();
        addWalls = new ToggleButton("Walls");
        addWalls.setPrefSize(stage.getWidth(),40);
        addWalls.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                main.setPlacingWalls(addWalls.isSelected());
            }
        });
        setupState = new ToggleButton("States");
        setupState.setPrefSize(stage.getWidth(),40);

        VBox container = new VBox(addWalls,setupState);
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

}
