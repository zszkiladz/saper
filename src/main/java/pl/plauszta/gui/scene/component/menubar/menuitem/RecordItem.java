package pl.plauszta.gui.scene.component.menubar.menuitem;

import javafx.scene.control.Alert;
import javafx.scene.control.MenuItem;
import pl.plauszta.game.Record;
import pl.plauszta.game.Records;

import java.time.LocalTime;
import java.util.List;

public class RecordItem extends MenuItem {
    public static final String TIME_ZERO = "00:00:00";

    public RecordItem(String level) {
        super(String.format("Show %s game records", level));
        super.setOnAction(event -> {
            List<Record> records;
            if ("easy".equals(level)) {
                records = Records.getInstance().getEasyRecords();
            } else if ("medium".equals(level)) {
                records = Records.getInstance().getMediumRecords();
            } else {
                records = Records.getInstance().getHardRecords();
            }
            showRecords(records);
        });
    }

    private void showRecords(List<Record> records) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Records");
        alert.setHeaderText(null);
        alert.getDialogPane().setPrefWidth(230);
        StringBuilder sb = new StringBuilder();
        if (records.isEmpty()) {
            sb.append("No records");
        }
        for (int i = 0; i < records.size() && i < 10; i++) {
            Record record = records.get(i);
            int time = record.getTime();
            sb.append(i + 1).append(". ").append(LocalTime.parse(TIME_ZERO).plusSeconds(time)).append(" ").append(record.getName()).append("\n");
        }
        alert.setContentText(sb.toString());
        alert.showAndWait();
    }
}
