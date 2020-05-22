package pl.plauszta.gui.scene.component.menubar;

import javafx.scene.control.*;
import pl.plauszta.game.DifficultyLevel;
import pl.plauszta.game.Game;
import pl.plauszta.gui.CustomGameParams;
import pl.plauszta.gui.scene.SceneChanger;

import java.util.Optional;

public class ChangeDifficultyMenu extends Menu {

    private final Game game = Game.getInstance();
    private final MenuBar menuBar;
    private final RadioMenuItem easyItem;
    private final RadioMenuItem mediumItem;
    private final RadioMenuItem hardItem;
    private final RadioMenuItem customItem;

    public ChangeDifficultyMenu(MenuBar menuBar) {
        super("Change difficulty");
        this.menuBar = menuBar;
        easyItem = new RadioMenuItem("Easy");
        mediumItem = new RadioMenuItem("Medium");
        hardItem = new RadioMenuItem("Hard");
        customItem = new RadioMenuItem("Custom");

        selectCurrentLevelRadioItem();

        ToggleGroup toggleGroup = new ToggleGroup();
        toggleGroup.getToggles().addAll(easyItem, mediumItem, hardItem, customItem);

        getItems().addAll(easyItem, mediumItem, hardItem, new SeparatorMenuItem(), customItem);

        setupActionsOnRadioItems();
    }

    private void selectCurrentLevelRadioItem() {
        if (game.getDifficultyLevel() == DifficultyLevel.EASY) {
            easyItem.setSelected(true);
        } else if (game.getDifficultyLevel() == DifficultyLevel.MEDIUM) {
            mediumItem.setSelected(true);
        } else if (game.getDifficultyLevel() == DifficultyLevel.HARD) {
            hardItem.setSelected(true);
        } else {
            customItem.setSelected(true);
        }
    }

    private void setupActionsOnRadioItems() {

        easyItem.setOnAction(event -> changeToDefinedDifficulty(DifficultyLevel.EASY));

        mediumItem.setOnAction(event -> changeToDefinedDifficulty(DifficultyLevel.MEDIUM));

        hardItem.setOnAction(event -> changeToDefinedDifficulty(DifficultyLevel.HARD));

        customItem.setOnAction(event -> fillCustomGameParametersOrAbandonCustomLevel());
    }

    private void changeToDefinedDifficulty(DifficultyLevel level) {
        game.setDifficultyLevel(level);
        SceneChanger.changeScene(menuBar.getScene());
    }

    private void fillCustomGameParametersOrAbandonCustomLevel() {
        Dialog<CustomGameParams> dialog = new CustomGameParametersDialog();
        Optional<CustomGameParams> parameters = dialog.showAndWait();

        if (parameters.isPresent()) {
            CustomGameParams customGameParams = parameters.get();
            while (customGameParams.getX() == 0) {
                parameters = dialog.showAndWait();
                if (parameters.isEmpty()) {
                    selectCurrentLevelRadioItem();
                    return;
                }
                customGameParams = parameters.get();
            }
            game.setCustomGame(customGameParams.getX(), customGameParams.getY(), customGameParams.getNumber());
            SceneChanger.changeScene(menuBar.getScene());
        }
        selectCurrentLevelRadioItem();
    }
}
