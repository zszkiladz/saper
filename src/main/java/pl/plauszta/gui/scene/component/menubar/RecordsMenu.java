package pl.plauszta.gui.scene.component.menubar;

import javafx.scene.control.*;
import pl.plauszta.game.Records;
import pl.plauszta.gui.scene.component.menubar.menuitem.RecordItem;

import java.util.Optional;

public class RecordsMenu extends Menu {

    public RecordsMenu() {
        super("Records");
        MenuItem resetRecordsItem = new MenuItem("Reset Records");
        setResetAction(resetRecordsItem);

        super.getItems().add(new RecordItem("easy"));
        super.getItems().add(new RecordItem("medium"));
        super.getItems().add(new RecordItem("hard"));
        super.getItems().add(new SeparatorMenuItem());
        super.getItems().add(resetRecordsItem);
    }

    private void setResetAction(MenuItem resetRecordsItem) {
        resetRecordsItem.setOnAction(event -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Reset records");
            alert.setHeaderText(null);
            alert.setContentText("Are you sure you want reset all records?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                Records.getInstance().resetRecords();
            }
        });
    }
}
