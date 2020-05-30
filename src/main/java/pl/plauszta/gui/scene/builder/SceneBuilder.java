package pl.plauszta.gui.scene.builder;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.MenuBar;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;
import pl.plauszta.game.Game;

public class SceneBuilder {

    private final Game game = Game.getInstance();

    public void prepareScene(MenuBar menuBar, TextFlow statisticsText, Text time, int numberOfFlags) {
        new MenuBarBuilder().prepareMenuBar(menuBar);
        prepareStatistics(statisticsText, time, numberOfFlags);
    }

    private void prepareStatistics(TextFlow statisticsText, Text time, int numberOfFlags) {
        ObservableList<Node> children = statisticsText.getChildren();
        String minesStat = String.format("Mines: %d/%d%n", numberOfFlags, game.getMinesNumber());
        children.add(new Text(minesStat));
        Text timeLabel = new Text("Time: ");
        timeLabel.setTextAlignment(TextAlignment.RIGHT);
        children.add(timeLabel);
        time.setTextAlignment(TextAlignment.RIGHT);
        children.add(time);
    }
}
