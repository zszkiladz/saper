package pl.plauszta.gui.scene;

import javafx.scene.control.MenuBar;
import pl.plauszta.game.Game;
import pl.plauszta.gui.scene.component.menubar.GameMenu;
import pl.plauszta.gui.scene.component.menubar.HelpMenu;
import pl.plauszta.gui.scene.component.menubar.RecordsMenu;

public class MenuBarMaker {
    private final Game game = Game.getInstance();

    public void prepareMenuBar(MenuBar menuBar) {
        menuBar.getMenus().add(new GameMenu(menuBar));
        menuBar.getMenus().add(new RecordsMenu());
        menuBar.getMenus().add(new HelpMenu());
    }
}
