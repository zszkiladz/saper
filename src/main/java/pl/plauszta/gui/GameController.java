package pl.plauszta.gui;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;
import javafx.util.Duration;
import pl.plauszta.game.DifficultyLevel;
import pl.plauszta.game.Game;

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
    public void initialize(URL url, ResourceBundle resourceBundle) { //TODO add timer and progress
        prepareGridOfGameBoard();
        prepareMenuBar();
        initTimer();
        prepareStatistics();
    }

    private void initTimer() {
        LocalTime startTime = LocalTime.now();
        Timeline clock = new Timeline(new KeyFrame(Duration.ZERO, e -> {
            LocalTime currentTime = LocalTime.now().minusSeconds(startTime.toSecondOfDay());
            time.setText(currentTime.getHour() + ":" + currentTime.getMinute() + ":" + currentTime.getSecond());
        }),
                new KeyFrame(Duration.seconds(1))
        );
        clock.setCycleCount(Animation.INDEFINITE);
        clock.play();
    }

    private void prepareMenuBar() {
        Menu gameMenu = prepareGameMenu();
        Menu helpMenu = prepareHelpMenu();

        menuBar.getMenus().add(gameMenu);
        menuBar.getMenus().add(helpMenu);
    }

    private Menu prepareGameMenu() {
        Menu gameMenu = new Menu("Game");

        MenuItem newGameItem = new MenuItem("New Game");
        newGameItem.setOnAction(event -> {
            game.newGame();
            SceneChanger.changeScene(menuBar.getScene());
        });

        Menu changeDifficultyItem = prepareChangeDifficultyMenu();

        gameMenu.getItems().add(newGameItem);
        gameMenu.getItems().add(changeDifficultyItem);
        return gameMenu;
    }

    private Menu prepareChangeDifficultyMenu() {
        Menu changeDifficultyItem = new Menu("Change difficulty");
        RadioMenuItem easyItem = new RadioMenuItem("Easy");
        RadioMenuItem mediumItem = new RadioMenuItem("Medium");
        RadioMenuItem hardItem = new RadioMenuItem("Hard");
        RadioMenuItem customItem = new RadioMenuItem("Custom");

        setSelectedRadioItem(easyItem, mediumItem, hardItem, customItem);

        ToggleGroup toggleGroup = new ToggleGroup();
        toggleGroup.getToggles().add(easyItem);
        toggleGroup.getToggles().add(mediumItem);
        toggleGroup.getToggles().add(hardItem);
        toggleGroup.getToggles().add(customItem);

        changeDifficultyItem.getItems().add(easyItem);
        changeDifficultyItem.getItems().add(mediumItem);
        changeDifficultyItem.getItems().add(hardItem);
        changeDifficultyItem.getItems().add(new SeparatorMenuItem());
        changeDifficultyItem.getItems().add(customItem);

        setActionsOnRadioItems(easyItem, mediumItem, hardItem, customItem);
        return changeDifficultyItem;
    }

    private void setActionsOnRadioItems(RadioMenuItem easyItem, RadioMenuItem mediumItem,
                                        RadioMenuItem hardItem, RadioMenuItem customItem) {
        easyItem.setOnAction(event -> {
            game.setDifficultyLevel(DifficultyLevel.EASY);
            SceneChanger.changeScene(menuBar.getScene());
        });

        mediumItem.setOnAction(event -> {
            game.setDifficultyLevel(DifficultyLevel.MEDIUM);
            SceneChanger.changeScene(menuBar.getScene());
        });

        hardItem.setOnAction(event -> {
            game.setDifficultyLevel(DifficultyLevel.HARD);
            SceneChanger.changeScene(menuBar.getScene());
        });

        setCustomGameAction(easyItem, mediumItem, hardItem, customItem);
    }

    private void setCustomGameAction(RadioMenuItem easyItem, RadioMenuItem mediumItem, RadioMenuItem hardItem, RadioMenuItem customItem) {
        customItem.setOnAction(event -> {
            Dialog<CustomGameParams> dialog = makeCustomInputDialog();
            Optional<CustomGameParams> parameters = dialog.showAndWait();
            if (parameters.isPresent()) {
                CustomGameParams customGameParams = parameters.get();
                while (customGameParams.getX() == 0) {
                    parameters = dialog.showAndWait();
                    if (parameters.isEmpty()) {
                        setSelectedRadioItem(easyItem, mediumItem, hardItem, customItem);
                        return;
                    }
                    customGameParams = parameters.get();
                }
                game.setCustomGame(customGameParams.getX(), customGameParams.getY(), customGameParams.getNumber());
                SceneChanger.changeScene(menuBar.getScene());
            }
            setSelectedRadioItem(easyItem, mediumItem, hardItem, customItem);
        });
    }

    private void setSelectedRadioItem(RadioMenuItem easyItem, RadioMenuItem mediumItem, RadioMenuItem hardItem, RadioMenuItem customItem) {
        if (game.getDifficultyLevel() == DifficultyLevel.EASY) {
            easyItem.setSelected(true);
        } else if (game.getDifficultyLevel() == DifficultyLevel.MEDIUM) {
            mediumItem.setSelected(true);
        } else if (game.getDifficultyLevel() == DifficultyLevel.HARD) {
            hardItem.setSelected(true);
        } else {
            customItem.setSelected(true);
        }
    }

    private Dialog<CustomGameParams> makeCustomInputDialog() {
        Dialog<CustomGameParams> dialog = new Dialog<>();
        dialog.setTitle("Custom game");
        dialog.setHeaderText("Please specify…");
        DialogPane dialogPane = dialog.getDialogPane();
        dialogPane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        Label rowsLabel = new Label("Enter number of rows");
        TextField rowsNumber = new TextField();
        Label columnsLabel = new Label("Enter number of columns");
        TextField columnsNumber = new TextField();
        Label minesNumberLabel = new Label("Enter number of mines");
        TextField minesNumber = new TextField();
        dialogPane.setContent(new VBox(6,
                rowsLabel, rowsNumber,
                columnsLabel, columnsNumber,
                minesNumberLabel, minesNumber));

        Platform.runLater(rowsNumber::requestFocus);
        dialog.setResultConverter((ButtonType button) -> {
            if (button == ButtonType.OK) {
                return getInputResult(rowsNumber, columnsNumber, minesNumber);
            }
            return null;
        });
        return dialog;
    }

    private CustomGameParams getInputResult(TextField rowsNumber, TextField columnsNumber, TextField minesNumber) {
        int rows = Integer.parseInt(rowsNumber.getText());
        int columns = Integer.parseInt(columnsNumber.getText());
        int mines = Integer.parseInt(minesNumber.getText());
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Wrong numbers");
        return validateInput(rows, columns, mines, alert);
    }

    private CustomGameParams validateInput(int rows, int columns, int mines, Alert alert) {
        if (rows < 1 || columns < 1 || mines < 1) {
            alert.setContentText("These numbers should be greater than 0!");
            alert.showAndWait();
            return new CustomGameParams(0, 0, 0);
        }
        if (!checkInput(rows, columns, mines)) {
            int maxMines = (rows - 1) * (columns - 1);

            alert.setContentText("With that board sizes, the maximum number of mines is " + maxMines);
            alert.showAndWait();
            return new CustomGameParams(0, 0, 0);
        } else {
            return new CustomGameParams(rows, columns, mines);
        }
    }

    private boolean checkInput(int rowsNumber, int columnsNumber, int minesNumber) {
        int maxMines = (rowsNumber - 1) * (columnsNumber - 1);
        return minesNumber <= maxMines;
    }

    private Menu prepareHelpMenu() {
        Menu helpMenu = new Menu("Help");
        MenuItem howToPlayItem = new MenuItem("How to play...");
        MenuItem aboutAuthorItem = new MenuItem("About author");

        howToPlayItem.setOnAction(event -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("How to play");
            alert.setHeaderText(null);
            alert.setContentText("Clear board without detonating mines. Each of the discovered fields has the number of mines that are in direct contact with the field (from zero to eight)."
                    + "\n\nYou can mark the position of the mine with the flag by clicking the right mouse button."
                    + "\n\nIf field containing a mine is revealed, the player loses the game.");
            alert.showAndWait();
        });
        aboutAuthorItem.setOnAction(event -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("About author");
            alert.setHeaderText(null);
            alert.setContentText("Plauszta Zuzanna\nhttps://github.com/zplauszta");
            alert.showAndWait();
        });

        helpMenu.getItems().add(howToPlayItem);
        helpMenu.getItems().add(aboutAuthorItem);
        return helpMenu;
    }

    private void prepareGridOfGameBoard() { //TODO make first reavel cant be mine
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
            endGame();
        } else {
            updateButton(button, xButton, yButton);
            if (gameIsOver()) {
                lockButtons();
                showEndAlert("You win!");
            }
        }
    }

    private void endGame() {
        lockButtons();
        showEndAlert("You lose!");
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
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void prepareStatistics() {
        ObservableList<Node> children = statisticsText.getChildren();
        String minesStat = String.format("Mines: %d/%d%n", numberOfFlags, game.getMinesNumber());
        children.add(new Text(minesStat));
        Text t = new Text("Time: ");
        t.setTextAlignment(TextAlignment.RIGHT);
        children.add(t);
        time.setTextAlignment(TextAlignment.RIGHT);
        children.add(time);
    }
}
