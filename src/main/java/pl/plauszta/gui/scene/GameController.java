package pl.plauszta.gui.scene;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import pl.plauszta.game.Game;
import pl.plauszta.game.Record;
import pl.plauszta.game.Records;
import pl.plauszta.gui.scene.button.BoardButton;
import pl.plauszta.gui.scene.button.Status;

import java.net.URL;
import java.time.LocalTime;
import java.util.Optional;
import java.util.ResourceBundle;

public class GameController implements Initializable {

    @FXML
    public MenuBar menuBar;

    @FXML
    public GridPane grid;

    @FXML
    public TextFlow statisticsText;

    Game game = Game.getInstance();
    int numberOfFlags = 0;
    Text time = new Text();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        (new SceneMaker()).prepareScene(menuBar, statisticsText, time, numberOfFlags);

        prepareGridOfGameBoard();
    }

    private void prepareGridOfGameBoard() {
        grid.getChildren().removeAll();
        for (int i = 0; i < game.getMines()[0].length; i++) {
            for (int j = 0; j < game.getMines().length; j++) {
                BoardButton button = new BoardButton();
                button.setPrefWidth(30);
                button.setPrefHeight(30);
                button.setId("" + j + " " + i);
                button.getStyleClass().add("my");
                button.setOnMouseClicked(event -> {
                    if (event.getButton() == MouseButton.PRIMARY) {
                        updateBoard(button);
                    } else if (event.getButton() == MouseButton.SECONDARY) {
                        changeFlag(button);
                    }
                });
                grid.add(button, i, j);
            }
        }
    }

    private void changeFlag(BoardButton button) {
        button.changeStatus();
        if (button.getStatus() == Status.UNMARKED) {
            button.setGraphic(null);
            numberOfFlags--;
        } else {
            ImageView flagImage = new ImageView("saper_flaga.png");
            flagImage.setFitHeight(12);
            flagImage.setFitWidth(12);
            button.setGraphic(flagImage);
            numberOfFlags++;
        }
        String minesStat = String.format("Mines: %d/%d%n", numberOfFlags, game.getMinesNumber());
        statisticsText.getChildren().set(0, new Text(minesStat));
    }

    private void updateBoard(BoardButton button) {
        if (Status.MARKED == button.getStatus()) {
            return;
        }
        String[] coords = button.getId().split(" ");
        int xButton = Integer.parseInt(coords[0]);
        int yButton = Integer.parseInt(coords[1]);

        if (game.getMines()[xButton][yButton]) {
            if (game.getCountHits() == 0) {
                newGame();
                return;
            }
            String timeString = time.getText();
            endGame("You lose!", timeString);
        } else {
            updateButton(button, xButton, yButton);
            if (gameIsOver()) {
                String timeString = time.getText();
                int stopTime = LocalTime.parse(timeString).toSecondOfDay();
                endGame("You win!\nTime: " + timeString, timeString);

                TextInputDialog dialog = new TextInputDialog("Anonim");
                dialog.setTitle("Game end");
                dialog.setHeaderText("You win!\nTime: " + timeString);
                dialog.setContentText("Please enter your name:");
                Optional<String> result = dialog.showAndWait();
                result.ifPresent(name -> {
                    Records.getInstance().addRecord(new Record(name, stopTime));
                });

            }
        }
    }

    private void newGame() {
        game.newGame();
        SceneChanger.changeScene(menuBar.getScene());
    }

    private void endGame(String message, String timeString) {
        statisticsText.getChildren().remove(2);
        statisticsText.getChildren().add(new Text(timeString));
        lockButtons();
        showEndAlert(message);
    }

    private void updateButton(Button button, int xButton, int yButton) {
        game.addHit();
        int mines = game.getGameBoard()[xButton][yButton];
        button.setText(mines + "");
        if (mines == 0) {
            button.setText(null);
        } else if (mines < 4) {
            button.setTextFill(Paint.valueOf("GREEN"));
        } else if (mines < 6) {
            button.setTextFill(Paint.valueOf("ORANGE"));
        } else {
            button.setTextFill(Paint.valueOf("RED"));
        }
        button.setDisable(true);

        if (mines == 0) {
            expandAllBlanks(xButton, yButton);
        }
    }

    private void expandAllBlanks(int xButton, int yButton) {
        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                boolean inBounds = xButton + i >= 0 && xButton + i < game.getMines()[0].length
                        && yButton + j >= 0 && yButton + j < game.getMines().length;
                boolean isCurrentButton = i == 0 && j == 0;
                if (inBounds && !isCurrentButton) {
                    updateNeighbouringButton(xButton + i, yButton + j);
                }
            }
        }
    }

    private void updateNeighbouringButton(int xButton, int yButton) {
        Button neighbourButton = (Button) getNodeFromGridPane(xButton, yButton);
        if (neighbourButton != null && !neighbourButton.isDisable()) {
            updateButton(neighbourButton, xButton, yButton);
        }
    }

    private Node getNodeFromGridPane(int col, int row) {
        for (Node node : grid.getChildren()) {
            if (Integer.parseInt(node.getId().split(" ")[0]) == col && Integer.parseInt(node.getId().split(" ")[1]) == row) {
                return node;
            }
        }
        return null;
    }

    private void lockButtons() {
        for (Node child : grid.getChildren()) {
            Button button = (Button) child;
            button.setDisable(true);
            String[] choord = button.getId().split(" ");
            if (game.getMines()[Integer.parseInt(choord[0])][Integer.parseInt(choord[1])]) {
                ImageView mineImage = new ImageView("saper_mina.png");
                mineImage.setFitWidth(12);
                mineImage.setFitHeight(12);
                button.setGraphic(mineImage);
            }
        }
    }

    private boolean gameIsOver() {
        return game.isOver();
    }

    private void showEndAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("End of game");
        alert.setHeaderText(null);
        alert.getDialogPane().setPrefWidth(100);
        alert.setContentText(message);
        alert.showAndWait();
    }

}
