package pl.plauszta.gui.scene.component.button;

import javafx.scene.control.Button;

public class BoardButton extends Button {
    private Status status;

    public BoardButton(int i, int j) {
        this.status = Status.UNMARKED;
        this.setPrefWidth(30);
        this.setPrefHeight(30);
        this.setId(i + " " + j); //FIXME replace whitespaces with '-'
        this.getStyleClass().add("board-button");
    }

    public Status getStatus() {
        return status;
    }

    public void changeStatus() {
        status = status.equals(Status.MARKED) ? Status.UNMARKED : Status.MARKED;
    }

    public void setClicked() {
        status = Status.CLICKED;
    }
}
