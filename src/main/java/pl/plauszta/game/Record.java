package pl.plauszta.game;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Record implements Comparable<Record> {
    @XmlAttribute
    private String name;

    @XmlAttribute
    private int time;

    public Record(String name, int time) {
        this.name = name;
        this.time = time;
    }

    public Record() {
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

    @Override
    public int compareTo(Record record) {
        return (this.time - record.time);
    }
}
