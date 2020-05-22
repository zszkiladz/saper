package pl.plauszta.gui.scene;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import pl.plauszta.gui.GuiGame;

import java.io.IOException;
import java.net.URL;

public class SceneChanger {

    private SceneChanger() {
    }

    public static void changeScene(Scene scene) {
        try {
            Stage stage = (Stage) scene.getWindow();
            final URL resource = GuiGame.class.getClassLoader().getResource("gameScene.fxml");
            if (resource == null) {
                return;
            }
            final Pane pane = FXMLLoader.load(resource);
            final Scene newScene = new Scene(pane);
            newScene.getStylesheets().add("test.css");
            stage.setScene(newScene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
