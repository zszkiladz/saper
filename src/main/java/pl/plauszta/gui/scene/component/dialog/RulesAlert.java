package pl.plauszta.gui.scene.component.dialog;

import javafx.scene.control.Alert;

public class RulesAlert extends Alert {

    public RulesAlert() {
        super(AlertType.INFORMATION);
        this.setTitle("How to play");
        this.setHeaderText(null);
        final String gameRules = "Clear board without detonating mines. Each of the discovered fields has the number of mines" +
                "that are in direct contact with the field (from zero to eight)." +
                "\n\nYou can mark the position of the mine with the flag by clicking the right mouse button." +
                "\n\nIf field containing a mine is revealed, the player loses the game.";
        this.setContentText(gameRules);
    }
}
