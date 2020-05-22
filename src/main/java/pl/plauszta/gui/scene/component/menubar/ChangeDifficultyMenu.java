package pl.plauszta.gui.scene.component.menubar;

import javafx.application.Platform;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import pl.plauszta.game.DifficultyLevel;
import pl.plauszta.game.Game;
import pl.plauszta.gui.CustomGameParams;
import pl.plauszta.gui.scene.SceneChanger;

import java.util.Optional;

public class ChangeDifficultyMenu extends Menu {

    private final Game game = Game.getInstance();

    public ChangeDifficultyMenu(MenuBar menuBar) {
        super("Change difficulty");
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

        getItems().add(easyItem);
        getItems().add(mediumItem);
        getItems().add(hardItem);
        getItems().add(new SeparatorMenuItem());
        getItems().add(customItem);

        setActionsOnRadioItems(easyItem, mediumItem, hardItem, customItem, menuBar);
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
}
