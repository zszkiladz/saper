package pl.plauszta.game;

import javax.xml.bind.annotation.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement()
@XmlAccessorType(XmlAccessType.FIELD)
public class Records implements Serializable {

    private static Records instance;

    @XmlElements(@XmlElement)
    private final List<Record> easyRecords;
    @XmlElements(@XmlElement)
    private final List<Record> mediumRecords;
    @XmlElements(@XmlElement)
    private final List<Record> hardRecords;

    private Records() {
        this.easyRecords = new ArrayList<>();
        this.mediumRecords = new ArrayList<>();
        this.hardRecords = new ArrayList<>();
    }

    public static Records getInstance() {
        if (instance == null) {
            instance = new Records();
        }
        return instance;
    }

    public void addRecord(Record record) {
        easyRecords.add(record);
        easyRecords.sort(Record::compareTo);
    }

    public void addRecords(List<Record> records) {
        easyRecords.addAll(records);
        //easyRecords.sort(Record::compareTo);
    }

    public void addRecord(int time) {
        easyRecords.add(new Record("Anonim", time));
        easyRecords.sort(Record::compareTo);
    }

    public List<Record> getEasyRecords() {
        return easyRecords;
    }

    public void show() {
        easyRecords.forEach(System.out::println);
    }
}
