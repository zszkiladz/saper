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
public class Records {

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

    public void addRecord(Record record, DifficultyLevel difficultyLevel) {
        if (difficultyLevel == DifficultyLevel.EASY) {
            easyRecords.add(record);
        } else if (difficultyLevel == DifficultyLevel.MEDIUM) {
            mediumRecords.add(record);
        } else if (difficultyLevel == DifficultyLevel.HARD) {
            hardRecords.add(record);
        }
        easyRecords.sort(Record::compareTo);
    }

    public void addRecords(List<Record> records, DifficultyLevel difficultyLevel) {
        if (difficultyLevel == DifficultyLevel.EASY) {
            easyRecords.addAll(records);
            easyRecords.sort(Record::compareTo);
        } else if (difficultyLevel == DifficultyLevel.MEDIUM) {
            mediumRecords.addAll(records);
            mediumRecords.sort(Record::compareTo);
        } else if (difficultyLevel == DifficultyLevel.HARD) {
            hardRecords.addAll(records);
            hardRecords.sort(Record::compareTo);
        }
    }

    public void addRecord(int time, DifficultyLevel difficultyLevel) {
        Record record = new Record("Anonim", time);
        if (difficultyLevel == DifficultyLevel.EASY) {
            easyRecords.add(record);
            easyRecords.sort(Record::compareTo);
        } else if (difficultyLevel == DifficultyLevel.MEDIUM) {
            mediumRecords.add(record);
            mediumRecords.sort(Record::compareTo);
        } else if (difficultyLevel == DifficultyLevel.HARD) {
            hardRecords.add(record);
            hardRecords.sort(Record::compareTo);
        }
    }

    public List<Record> getEasyRecords() {
        return easyRecords;
    }

    public List<Record> getMediumRecords() {
        return mediumRecords;
    }

    public List<Record> getHardRecords() {
        return hardRecords;
    }

    public void show() {
        easyRecords.forEach(System.out::println);
    }

    public void resetRecords() {
        easyRecords.clear();
        mediumRecords.clear();
        hardRecords.clear();
    }
}
