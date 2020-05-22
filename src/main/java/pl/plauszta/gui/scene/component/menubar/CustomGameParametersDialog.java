package pl.plauszta.gui.scene.component.menubar;

import javafx.application.Platform;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import pl.plauszta.gui.CustomGameParams;

public class CustomGameParametersDialog extends Dialog<CustomGameParams> {

    private final TextField rowsNumber;
    private final TextField columnsNumber;
    private final TextField minesNumber;

    public CustomGameParametersDialog() {
        super();
        setTitle("Custom game");
        setHeaderText("Please specify…");
        DialogPane dialogPane = getDialogPane();
        dialogPane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        Label rowsLabel = new Label("Enter number of rows");
        rowsNumber = new TextField();
        Label columnsLabel = new Label("Enter number of columns");
        columnsNumber = new TextField();
        Label minesNumberLabel = new Label("Enter number of mines");
        minesNumber = new TextField();
        dialogPane.setContent(new VBox(6,
                rowsLabel, rowsNumber,
                columnsLabel, columnsNumber,
                minesNumberLabel, minesNumber));

        Platform.runLater(rowsNumber::requestFocus);

        setResultConverter((ButtonType button) -> {
            if (button == ButtonType.OK) {
                return getInputResult();
            }
            return null;
        });
    }

    private CustomGameParams getInputResult() {
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
