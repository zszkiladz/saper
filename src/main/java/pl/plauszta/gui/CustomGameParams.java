package pl.plauszta.gui;

public class CustomGameParams {
    private final int x;
    private final int y;
    private final int number;

    public CustomGameParams(int x, int y, int number) {
        this.x = x;
        this.y = y;
        this.number = number;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getNumber() {
        return number;
    }
}
