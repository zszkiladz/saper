package pl.plauszta.gui;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;
import pl.plauszta.game.Game;

import java.net.URL;
import java.util.ResourceBundle;

public class GameController implements Initializable {

    Game game = Game.getInstance();

    @FXML
    public GridPane grid;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        for (int i = 0; i < game.getBombs()[0].length; i++) {
            for (int j = 0; j < game.getBombs().length; j++) {
                Button button = new Button();
                button.setPrefWidth(30);
                button.setPrefHeight(30);
                button.setId("" + j + " " + i);
                button.setOnMouseClicked(event -> {
                    if (event.getButton() == MouseButton.PRIMARY) {
                        doAction(button);
                    } else if (event.getButton() == MouseButton.SECONDARY) {
                        changeFlag(button);
                    }
                });
                grid.add(button, i, j);
            }
        }
    }

    private void changeFlag(Button button) {
        button.setText(button.getText().equals("F") ? "" : "F");
    }

    private void doAction(Button button) {
        if ("F".equals(button.getText())) {
            return;
        }
        String[] coords = button.getId().split(" ");
        int xButton = Integer.parseInt(coords[0]);
        int yButton = Integer.parseInt(coords[1]);

        if (game.getBombs()[xButton][yButton]) {
            button.setText("X");
            lockButtons();
            showEndAlert("You lose!");
        } else {
            game.addHit();
            int bombs = game.getGameBoard()[xButton][yButton];
            button.setText(bombs + "");
            if (gameIsOver()) {
                lockButtons();
                showEndAlert("You win!");
            }
        }
        button.setDisable(true);
    }

    private void lockButtons() {
        for (Node child : grid.getChildren()) {
            child.setDisable(true);
        }
    }

    private boolean gameIsOver() {
        return game.isOver();
    }

    private void showEndAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("End of game");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
