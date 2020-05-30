package pl.plauszta.game;

import java.util.Timer;
import java.util.TimerTask;
import java.util.function.LongConsumer;

public class GameTimer {

    private Timer timer;
    private long elapsedSeconds;
    private LongConsumer listener;

    public void start() {
        timer = new Timer();
        final TimerTask task = new TimerTask() {
            @Override
            public void run() {
                elapsedSeconds++;
                listener.accept(elapsedSeconds);
            }
        };

        elapsedSeconds = 0;
        listener.accept(elapsedSeconds);
        timer.scheduleAtFixedRate(task, 1000, 1000);
    }

    public long reset() {
        timer.cancel();
        timer = null;
        final long result = elapsedSeconds;
        elapsedSeconds = 0;
        return result;
    }

    long getElapsedSeconds() {
        return elapsedSeconds;
    }

    public void setTimeListener(LongConsumer listener) {
        this.listener = listener;
    }
}
