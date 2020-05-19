package pl.plauszta.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Control;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class SceneChanger {

    private SceneChanger() {
    }

    static void changeScene(Scene scene) throws IOException {
        Stage stage = (Stage) scene.getWindow();
        final URL resource = GuiGame.class.getClassLoader().getResource("gameScene.fxml");
        final Pane pane = FXMLLoader.load(resource);
        final Scene newScene = new Scene(pane);
        stage.setScene(newScene);
        stage.show();
    }
}
