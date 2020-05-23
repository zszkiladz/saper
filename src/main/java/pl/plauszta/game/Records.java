package pl.plauszta.game;

import javax.xml.bind.annotation.*;
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
            addRecordToList(record, easyRecords);
        } else if (difficultyLevel == DifficultyLevel.MEDIUM) {
            addRecordToList(record, mediumRecords);
        } else if (difficultyLevel == DifficultyLevel.HARD) {
            addRecordToList(record, hardRecords);
        }
    }

    private void addRecordToList(Record record, List<Record> records) {
        final int recordsSize = records.size();
        if (recordsSize > 9) {
            if (record.getTime() > records.get(recordsSize - 1).getTime()) {
                return;
            }
            records.remove(recordsSize - 1);
        }
        records.add(record);
        records.sort(Record::compareTo);
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
