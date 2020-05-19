package pl.plauszta.game;

import java.util.Random;

public class Game {

    private static Game instance = new Game();

    private boolean[][] bombs;
    private int bombsNumber;
    private DifficultyLevel difficultyLevel;
    private int countHits = 0;

    Random random = new Random();

    private Game() {
        difficultyLevel = DifficultyLevel.EASY;
        newGame();
    }

    public void setDifficultyLevel(DifficultyLevel difficultyLevel) {
        this.difficultyLevel = difficultyLevel;
        newGame(difficultyLevel);
    }

    public void setCustomGame(int sizeX, int sizeY, int bombsNumber) {
        difficultyLevel = DifficultyLevel.CUSTOM;
        bombs = new boolean[sizeX][sizeY];
        this.bombsNumber = bombsNumber;
        placeBombs();
    }

    public void newGame(DifficultyLevel difficultyLevel) {
        this.difficultyLevel = difficultyLevel;
        if (difficultyLevel == DifficultyLevel.EASY) {
            bombs = new boolean[8][8];
            bombsNumber = 10;
        } else if (difficultyLevel == DifficultyLevel.MEDIUM) {
            bombs = new boolean[16][16];
            bombsNumber = 40;
        } else if (difficultyLevel == DifficultyLevel.HARD) {
            bombs = new boolean[16][30];
            bombsNumber = 99;
        }
        placeBombs();
    }

    private void placeBombs() {
        for (int i = 0; i < bombsNumber; i++) {
            int x;
            int y;
            do {
                x = random.nextInt(bombs.length);
                y = random.nextInt(bombs[0].length);
            } while (bombs[x][y]);
            bombs[x][y] = true;
        }
    }

    public void newGame() {
        newGame(difficultyLevel);
    }

    public static Game getInstance() {
        if (instance == null) {
            instance = new Game();
        }
        return instance;
    }

    public boolean[][] getBombs() {
        return bombs;
    }

    public int[][] getGameBoard() {
        int[][] table = new int[bombs.length][bombs[0].length];
        for (int i = 0; i < table.length; i++) {
            int[] rows = table[i];
            for (int j = 0; j < rows.length; j++) {
                if (bombs[i][j]) {
                    table[i][j] = -1;
                    continue;
                }
                int countBombs = getNumberOfBombs(i, j);
                table[i][j] = countBombs;
            }
        }

        return table;
    }

    private int getNumberOfBombs(int i, int j) {
        int countBombs = 0;
        for (int k = -1; k < 2; k++) {
            for (int l = -1; l < 2; l++) {
                boolean isInBounds = i + k >= 0 && j + l >= 0
                        && i + k < bombs.length && j + l < bombs[0].length;
                boolean isCenter = k == 0 && l == 0;
                if (isInBounds
                        && !isCenter
                        && bombs[i + k][j + l]) {
                    countBombs++;
                }
            }
        }
        return countBombs;
    }

    public void addHit() {
        countHits++;
    }

    public boolean isOver() {
        int notBombs = bombs.length * bombs[0].length - bombsNumber;
        return countHits == notBombs;
    }
}

