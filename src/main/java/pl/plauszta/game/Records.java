package pl.plauszta.game;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Records implements Serializable {

    private static Records instance;

    private final List<Record> recordsList;

    private Records() {
        this.recordsList = new ArrayList<>();
    }

    public static Records getInstance() {
        if (instance == null) {
            instance = new Records();
        }
        return instance;
    }

    public void addRecord(Record record) {
        recordsList.add(record);
    }

    public void addRecord(int time) {
        recordsList.add(new Record("Anonim", time));
    }

    public List<Record> getRecordsList() {
        return recordsList;
    }


}
