package pl.plauszta.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.net.URL;

public class GuiGame extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        final URL homeResource = GuiGame.class.getClassLoader().getResource("gameScene.fxml");
        final Pane homePane = FXMLLoader.load(homeResource);
        final Scene scene = new Scene(homePane);
        stage.setScene(scene);
        stage.setTitle("Saper");
        stage.setResizable(false);

        stage.show();
    }
}