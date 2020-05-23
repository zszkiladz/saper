package pl.plauszta.gui.scene.component.dialog;

import javafx.scene.control.Alert;

public class InfoAlert extends Alert {

    public InfoAlert() {
        super(AlertType.INFORMATION);
        this.setTitle("About author");
        this.setHeaderText(null);
        this.setContentText("Plauszta Zuzanna\nhttps://github.com/zplauszta");
    }
}
