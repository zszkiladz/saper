package pl.plauszta.gui.scene.component.dialog;

import javafx.scene.control.Alert;

public class EndAlert extends Alert {
    public EndAlert(String message) {
        super(AlertType.INFORMATION);
        super.setTitle("End of game");
        super.setHeaderText(null);
        super.getDialogPane().setPrefWidth(100);
        super.setContentText(message);
    }
}
