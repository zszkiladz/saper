package pl.plauszta.gui;

import javafx.scene.control.Button;

public class BoardButton extends Button {
    private Status status;

    public BoardButton() {
        this.status = Status.UNMARKED;
    }

    public Status getStatus() {
        return status;
    }

    public void changeStatus() {
        status = status.equals(Status.MARKED) ? Status.UNMARKED : Status.MARKED;
    }
}
