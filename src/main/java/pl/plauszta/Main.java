package pl.plauszta;

import javafx.application.Application;
import pl.plauszta.game.DifficultyLevel;
import pl.plauszta.game.Game;
import pl.plauszta.gui.GuiGame;

public class Main {
    public static void main(String[] args) {
        Game.getInstance().setDifficultyLevel(DifficultyLevel.EASY);

        Application.launch(GuiGame.class);
    }
}
