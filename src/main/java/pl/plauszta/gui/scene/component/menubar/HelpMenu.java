package pl.plauszta.gui.scene.component.menubar;

import javafx.scene.control.Alert;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import pl.plauszta.gui.scene.component.dialog.InfoAlert;
import pl.plauszta.gui.scene.component.dialog.RulesAlert;

public class HelpMenu extends Menu {

    public HelpMenu() {
        super("Help");
        MenuItem howToPlayItem = new MenuItem("How to play...");
        MenuItem aboutAuthorItem = new MenuItem("About the author");

        howToPlayItem.setOnAction(event -> {
            Alert alert = new RulesAlert();
            alert.showAndWait();
        });

        aboutAuthorItem.setOnAction(event -> {
            Alert alert = new InfoAlert();
            alert.showAndWait();
        });

        super.getItems().add(howToPlayItem);
        super.getItems().add(aboutAuthorItem);
    }
}
