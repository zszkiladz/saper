package pl.plauszta.game;

public class Record {
    private String name;
    private Long time;

    public Record(String name, Long time) {
        this.name = name;
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public Long getTime() {
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
