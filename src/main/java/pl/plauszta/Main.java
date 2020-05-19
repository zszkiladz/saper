package pl.plauszta;

public class Main {
    public static void main(String[] args) {
        Game game = new Game(DifficultyLevel.MEDIUM);

        for (int[] bombs : game.getGameBoard()) {
            for (int b : bombs) {
                System.out.print(String.format("%3s", b == -1 ? "X" : b));
            }
            System.out.println();
        }
    }
}
