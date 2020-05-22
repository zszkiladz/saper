package pl.plauszta.gui.scene;

import javafx.application.Platform;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import pl.plauszta.game.DifficultyLevel;
import pl.plauszta.game.Game;
import pl.plauszta.gui.CustomGameParams;

import java.util.Optional;

public class MenuBarMaker {

    private final Game game = Game.getInstance();

    public void prepareMenuBar(MenuBar menuBar) {
        Menu gameMenu = prepareGameMenu(menuBar);
        Menu helpMenu = prepareHelpMenu();
        Menu recordsMenu = prepareRecordMenu();

        menuBar.getMenus().add(gameMenu);
        menuBar.getMenus().add(recordsMenu);
        menuBar.getMenus().add(helpMenu);
    }

    private Menu prepareRecordMenu() {
        Menu gameMenu = new Menu("Records");

        MenuItem showRecordsItem = new MenuItem("Show Records");
        showRecordsItem.setOnAction(event -> {
            //TODO showing records
        });

        MenuItem resetRecordsItem = new MenuItem("Reset Records");
        resetRecordsItem.setOnAction(event -> {
            //TODO reset records
        });

        gameMenu.getItems().add(showRecordsItem);
        gameMenu.getItems().add(resetRecordsItem);
        return gameMenu;
    }

    private Menu prepareGameMenu(MenuBar menuBar) {
        Menu gameMenu = new Menu("Game");

        MenuItem newGameItem = new MenuItem("New Game");
        newGameItem.setOnAction(event -> newGame(menuBar));

        Menu changeDifficultyItem = prepareChangeDifficultyMenu(menuBar);

        gameMenu.getItems().add(newGameItem);
        gameMenu.getItems().add(changeDifficultyItem);
        return gameMenu;
    }

    private void newGame(MenuBar menuBar) {
        game.newGame();
        SceneChanger.changeScene(menuBar.getScene());
    }

    private Menu prepareChangeDifficultyMenu(MenuBar menuBar) {
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

        setActionsOnRadioItems(easyItem, mediumItem, hardItem, customItem, menuBar);
        return changeDifficultyItem;
    }

    private void setActionsOnRadioItems(RadioMenuItem easyItem, RadioMenuItem mediumItem,
                                        RadioMenuItem hardItem, RadioMenuItem customItem, MenuBar menuBar) {
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

        setCustomGameAction(easyItem, mediumItem, hardItem, customItem, menuBar);
    }

    private void setCustomGameAction(RadioMenuItem easyItem, RadioMenuItem mediumItem, RadioMenuItem hardItem, RadioMenuItem customItem, MenuBar menuBar) {
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
}