package pl.plauszta.game;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Records implements Serializable {
    private Records instance;

    private final List<Record> records;

    private Records() {
        this.records = new ArrayList<>();
    }

    public Records getInstance() {
        if (instance == null) {
            instance = new Records();
        }
        return instance;
    }

    public void addRecord(Record record) {
        records.add(record);
    }

    public void addRecord(Long time) {
        records.add(new Record("Anonim", time));
    }

    public List<Record> getRecords() {
        return records;
    }


}
