package pl.plauszta.game;

import java.util.Random;
import java.util.function.LongConsumer;

public class Game {

    private static Game instance = new Game();

    private boolean[][] mines;
    private int minesNumber;
    private DifficultyLevel difficultyLevel;
    private int countHits;
    private final GameTimer timer;

    Random random = new Random();

    private Game() {
        difficultyLevel = DifficultyLevel.EASY;
        countHits = 0;
        timer = new GameTimer();
        newGame();
    }

    public void startTimer(LongConsumer consumer) {
        timer.start(consumer);
    }

    public long stopTimer() {
        return timer.reset();
    }

    public void setDifficultyLevel(DifficultyLevel difficultyLevel) {
        newGame(difficultyLevel);
    }

    public void setCustomGame(int sizeX, int sizeY, int minesNumber) {
        difficultyLevel = DifficultyLevel.CUSTOM;
        mines = new boolean[sizeX][sizeY];
        this.minesNumber = minesNumber;
        placeMines();
    }

    public void newGame(DifficultyLevel difficultyLevel) {
        this.difficultyLevel = difficultyLevel;
        countHits = 0;
        if (difficultyLevel == DifficultyLevel.EASY) {
            mines = new boolean[8][8];
            minesNumber = 10;
        } else if (difficultyLevel == DifficultyLevel.MEDIUM) {
            mines = new boolean[16][16];
            minesNumber = 40;
        } else if (difficultyLevel == DifficultyLevel.HARD) {
            mines = new boolean[30][16];
            minesNumber = 99;
        }
        placeMines();
    }

    private void placeMines() {
        for (int i = 0; i < minesNumber; i++) {
            int x;
            int y;
            do {
                x = random.nextInt(mines.length);
                y = random.nextInt(mines[0].length);
            } while (mines[x][y]);
            mines[x][y] = true;
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

    public boolean[][] getMines() {
        return mines;
    }

    public int[][] getGameBoard() {
        int[][] table = new int[mines.length][mines[0].length];
        for (int i = 0; i < table.length; i++) {
            for (int j = 0; j < table[0].length; j++) {
                if (mines[i][j]) {
                    table[i][j] = -1;
                    continue;
                }
                int countMines = getNumberOfMines(i, j);
                table[i][j] = countMines;
            }
        }

        return table;
    }

    private int getNumberOfMines(int i, int j) {
        int countMines = 0;
        for (int k = -1; k < 2; k++) {
            for (int l = -1; l < 2; l++) {
                boolean isInBounds = i + k >= 0 && j + l >= 0
                        && i + k < mines.length && j + l < mines[0].length;
                boolean isCenter = k == 0 && l == 0;
                if (isInBounds
                        && !isCenter
                        && mines[i + k][j + l]) {
                    countMines++;
                }
            }
        }
        return countMines;
    }

    public void addHit() {
        countHits++;
    }

    public boolean isOver() {
        int notMines = mines.length * mines[0].length - minesNumber;
        return countHits == notMines;
    }

    public DifficultyLevel getDifficultyLevel() {
        return difficultyLevel;
    }

    public int getMinesNumber() {
        return minesNumber;
    }

    public int getCountHits() {
        return countHits;
    }
}

