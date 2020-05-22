package pl.plauszta.gui.scene;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import pl.plauszta.game.DifficultyLevel;
import pl.plauszta.game.Game;
import pl.plauszta.game.Record;
import pl.plauszta.game.Records;
import pl.plauszta.gui.scene.component.button.BoardButton;
import pl.plauszta.gui.scene.component.button.Status;
import pl.plauszta.gui.scene.component.dialog.EndAlert;
import pl.plauszta.gui.scene.component.dialog.WinInputDialog;

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
        final int rows = game.getMines().length;
        final int cols = game.getMines()[0].length;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                BoardButton button = gameButton(i, j);
                grid.add(button, i, j);

                if (game.getMines()[i][j]) {
                    button.getStyleClass().add("debug-mine");
                }
            }
        }
    }

    private BoardButton gameButton(int i, int j) {
        BoardButton button = new BoardButton(i, j);
        button.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                updateBoard(button);
            } else if (event.getButton() == MouseButton.SECONDARY) {
                if (Status.CLICKED == button.getStatus()) {
                    clickedButtonAlreadyClicked(button);
                } else {
                    changeFlag(button);
                }
            }
            if (game.isOver()) {
                winGame();
            }
        });
        return button;
    }

    private void clickedButtonAlreadyClicked(BoardButton button) {
        int minesButton = Integer.parseInt(button.getText() == null ? "0" : button.getText());
        String[] coordsButton = button.getId().split(" ");
        int x = Integer.parseInt(coordsButton[0]);
        int y = Integer.parseInt(coordsButton[1]);
        int flags = countFlags(x, y);
        System.out.println(flags);
        if (minesButton == flags) {
            expandNeighbours(x, y);
        }
    }

    private int countFlags(int x, int y) {
        int flags = 0;
        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                System.out.println("neighbour " + (x + i) + " " + (y + j));
                boolean inBounds = x + i >= 0 && x + i < game.getMines().length && y + j >= 0
                        && y + j < game.getMines()[0].length;
                boolean isCurrentButton = i == 0 && j == 0;
                BoardButton neighbourButton = (BoardButton) getNodeFromGridPane(x + i, y + j);
                if (inBounds && !isCurrentButton && neighbourButton != null
                        && neighbourButton.getStatus() == Status.MARKED) {
                    flags++;
                }
            }
        }
        return flags;
    }

    private Node getNodeFromGridPane(int x, int y) {
        for (Node node : grid.getChildren()) {
            BoardButton but = (BoardButton) node;
            if (Integer.parseInt(but.getId().split(" ")[0]) == x && Integer.parseInt(but.getId().split(" ")[1]) == y) {
                return node;
            }
        }
        return null;
    }

    private void expandNeighbours(int xButton, int yButton) {
        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                boolean inBounds = xButton + i >= 0 && xButton + i < game.getMines().length
                        && yButton + j >= 0 && yButton + j < game.getMines()[0].length;
                boolean isCurrentButton = i == 0 && j == 0;
                if (inBounds && !isCurrentButton) {
                    updateNeighbouringButton(xButton + i, yButton + j);
                }
            }
        }
    }

    private void updateNeighbouringButton(int xButton, int yButton) {
        BoardButton neighbourButton = (BoardButton) getNodeFromGridPane(xButton, yButton);
        if (neighbourButton != null && neighbourButton.getStatus() == Status.UNMARKED) {
            updateButton(neighbourButton, xButton, yButton);
        }
    }

    private void updateBoard(BoardButton button) {
        if (Status.MARKED == button.getStatus() || button.getStatus() == Status.CLICKED) {
            return;
        }

        String[] coords = button.getId().split(" ");
        int xButton = Integer.parseInt(coords[0]);
        int yButton = Integer.parseInt(coords[1]);

        if (game.getMines()[xButton][yButton]) {
            buttonIsMine();
        } else {
            updateButton(button, xButton, yButton);
        }
    }

    private void buttonIsMine() {
        if (game.getCountHits() == 0) {
            newGame();
            return;
        }
        String timeString = time.getText();
        sceneAfterGameOver(timeString);

        EndAlert endAlert = new EndAlert("You lose!");
        endAlert.showAndWait();
    }

    private void updateButton(BoardButton button, int xButton, int yButton) {
        if (game.getMines()[xButton][yButton]) {
            String timeString = time.getText();
            sceneAfterGameOver(timeString);
            EndAlert endAlert = new EndAlert("You lose!");
            endAlert.showAndWait();
        }
        game.addHit();
        int mines = game.getGameBoard()[xButton][yButton];
        button.setClicked();
        button.setText(mines + "");
        button.setStyle("-fx-background-color: #ffffff");
        if (mines == 0) {
            button.setText(null);
        } else if (mines < 4) {
            button.setTextFill(Paint.valueOf("GREEN"));
        } else if (mines < 6) {
            button.setTextFill(Paint.valueOf("ORANGE"));
        } else {
            button.setTextFill(Paint.valueOf("RED"));
        }

        if (mines == 0) {
            expandNeighbours(xButton, yButton);
        }
    }

    private void sceneAfterGameOver(String timeString) {
        statisticsText.getChildren().remove(2);
        statisticsText.getChildren().add(new Text(timeString));
        lockButtons();
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

    private void changeFlag(BoardButton button) {
        button.changeStatus();
        System.out.println("button " + button.getId() + " set " + button.getStatus());
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

    private void winGame() {
        String timeString = time.getText();
        int stopTime = LocalTime.parse(timeString).toSecondOfDay();
        sceneAfterGameOver(timeString);
        if (game.getDifficultyLevel() == DifficultyLevel.CUSTOM) {
            EndAlert endAlert = new EndAlert("You win!\nTime: " + timeString);
            endAlert.showAndWait();
        } else {
            TextInputDialog dialog = new WinInputDialog(timeString);
            Optional<String> result = dialog.showAndWait();
            result.ifPresent(name -> Records.getInstance().addRecord(new Record(name, stopTime), game.getDifficultyLevel()));
        }
    }

    private void newGame() {
        game.newGame();
        SceneChanger.changeScene(menuBar.getScene());
    }

}
