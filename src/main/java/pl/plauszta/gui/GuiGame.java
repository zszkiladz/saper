package pl.plauszta.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import pl.plauszta.game.Game;

import java.net.URL;
import java.util.Objects;

public class GuiGame extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        final boolean debug = getParameters().getNamed().containsKey("--debug");
        Game.getInstance().setDebug(debug);

        final URL homeResource = GuiGame.class.getClassLoader().getResource("gameScene.fxml");
        final Pane homePane = FXMLLoader.load(Objects.requireNonNull(homeResource));
        final Scene scene = new Scene(homePane);
        scene.getStylesheets().add("scene.css");
        stage.setScene(scene);
        stage.setTitle("Saper");
        stage.setResizable(false);
        stage.getIcons().add(new Image("saper_logo.png"));
        stage.show();
    }
}