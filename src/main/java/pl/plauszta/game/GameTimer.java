package pl.plauszta.game;

import java.util.Timer;
import java.util.TimerTask;
import java.util.function.LongConsumer;

public class GameTimer {

    private Timer timer;
    private long elapsedSeconds;

    public void start(LongConsumer consumer) {
        elapsedSeconds = 0;
        timer = new Timer();
        final TimerTask task = new TimerTask() {
            @Override
            public void run() {
                elapsedSeconds++;
                consumer.accept(elapsedSeconds);
            }
        };

        timer.scheduleAtFixedRate(task, 0, 1000);
    }

    public long reset() {
        timer.cancel();
        timer = null;
        return elapsedSeconds;
    }

    public long getElapsedSeconds() {
        return elapsedSeconds;
    }
}
