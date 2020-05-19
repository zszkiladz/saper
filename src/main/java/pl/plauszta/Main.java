package pl.plauszta;

import javafx.application.Application;
import pl.plauszta.game.DifficultyLevel;
import pl.plauszta.game.Game;
import pl.plauszta.gui.GuiGame;

public class Main {
    public static void main(String[] args) {
        Game.getInstance().setDifficultyLevel(DifficultyLevel.EASY);

        for (int[] bombs : Game.getInstance().getGameBoard()) {
            for (int b : bombs) {
                System.out.print(String.format("%3s", b == -1 ? "X" : b));
            }
            System.out.println();
        }

        Application.launch(GuiGame.class);


    }
}
