package pl.plauszta.game;

public class Record {
    private final String name;
    private final int time;

    public Record(String name, int time) {
        this.name = name;
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public int getTime() {
        return time;
    }

    @Override
    public String toString() {
        return "Record{" +
                "name='" + name + '\'' +
                ", time=" + time +
                '}';
    }
}
