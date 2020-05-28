package pl.plauszta.gui.scene.builder;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.MenuBar;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;
import javafx.util.Duration;
import pl.plauszta.game.Game;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

public class SceneBuilder {

    private final Game game = Game.getInstance();

    public void prepareScene(MenuBar menuBar, TextFlow statisticsText, Text time, int numberOfFlags) {
        new MenuBarBuilder().prepareMenuBar(menuBar);

        initTimer(time);
        prepareStatistics(statisticsText, time, numberOfFlags);
    }

    private void initTimer(Text time) {
        game.startTimer(elapsedSeconds -> time.setText(String.valueOf(elapsedSeconds)));

//        LocalTime startTime = LocalTime.now();
//        Timeline clock = new Timeline(new KeyFrame(Duration.ZERO, e -> {
//            LocalTime currentTime = LocalTime.now().minusSeconds(startTime.toSecondOfDay());
//            String timeString = currentTime.truncatedTo(ChronoUnit.SECONDS).toString();
//            time.setText(timeString);
//        }),
//                new KeyFrame(Duration.seconds(1))
//        );
//        clock.setCycleCount(Animation.INDEFINITE);
//        clock.play();
    }

    private void prepareStatistics(TextFlow statisticsText, Text time, int numberOfFlags) {
        ObservableList<Node> children = statisticsText.getChildren();
        String minesStat = String.format("Mines: %d/%d%n", numberOfFlags, game.getMinesNumber());
        children.add(new Text(minesStat));
        Text t = new Text("Time: ");
        t.setTextAlignment(TextAlignment.RIGHT);
        children.add(t);
        time.setTextAlignment(TextAlignment.RIGHT);
        children.add(time);
    }
}
