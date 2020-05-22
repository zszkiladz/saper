package pl.plauszta.gui.scene.component.dialog;

import javafx.scene.control.TextInputDialog;

public class WinInputDialog extends TextInputDialog {
    public WinInputDialog(String time) {
        super("Anonim");
        super.setTitle("Game end");
        super.setHeaderText("You win!\nTime: " + time);
        super.setContentText("Please enter your name:");
    }
}
