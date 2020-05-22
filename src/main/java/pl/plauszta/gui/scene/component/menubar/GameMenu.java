package pl.plauszta.gui.scene.component.menubar;


import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import pl.plauszta.game.Game;
import pl.plauszta.gui.scene.SceneChanger;

public class GameMenu extends Menu {
    public GameMenu(MenuBar menuBar) {
        super("Game");

        MenuItem newGameItem = new MenuItem("New Game");
        newGameItem.setOnAction(event -> newGame(menuBar));

        javafx.scene.control.Menu changeDifficultyItem = new ChangeDifficultyMenu(menuBar);

        super.getItems().add(newGameItem);
        super.getItems().add(changeDifficultyItem);
    }

    private void newGame(MenuBar menuBar) {
        Game.getInstance().newGame();
        SceneChanger.changeScene(menuBar.getScene());
    }
}
