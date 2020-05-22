package pl.plauszta;

import javafx.application.Application;
import pl.plauszta.game.DifficultyLevel;
import pl.plauszta.game.Records;
import pl.plauszta.gui.GuiGame;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;

public class Main {
    public static void main(String[] args) throws JAXBException {
        initRecords();
        Application.launch(GuiGame.class);
        saveRecords();
    }

    private static void initRecords() throws JAXBException {
        File file = new File("records.txt");
        JAXBContext jaxbContext = JAXBContext.newInstance(Records.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
        Records records = (Records) jaxbUnmarshaller.unmarshal(file);
        Records.getInstance().addRecords(records.getEasyRecords(), DifficultyLevel.EASY);
        Records.getInstance().addRecords(records.getMediumRecords(), DifficultyLevel.MEDIUM);
        Records.getInstance().addRecords(records.getHardRecords(), DifficultyLevel.HARD);
    }

    private static void saveRecords() throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(Records.class);
        Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
        jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

        jaxbMarshaller.marshal(Records.getInstance(), new File("records.txt"));
    }
}
