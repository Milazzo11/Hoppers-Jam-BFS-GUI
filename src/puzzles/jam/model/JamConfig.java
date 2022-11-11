package puzzles.jam.model;

import puzzles.common.solver.Configuration;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Jam configuration class.
 *
 * @author Max Milazzo (mam9563@rit.edu)
 */
public class JamConfig implements Configuration {

    /** Number of rows in configuration */
   private final int numRows;

    /** Number of columns in configuration */
   private final int numCols;

    /** Main puzzle */
   private final Character[][] puzzle;

    /**
     * Jam configuration constructor from file.
     *
     * @param fileName configuration file
     * @throws IOException if file read error
     */
   public JamConfig(String fileName) throws IOException {

        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;

            String[] dimens = br.readLine().split("\\s+");
            this.numRows = Integer.parseInt(dimens[0]);
            this.numCols = Integer.parseInt(dimens[1]);
            // gets row and column data

            br.readLine();
            // the car count is unneeded in this implementation, so it is skipped over

            this.puzzle = new Character[this.numRows][this.numCols];

            while ((line = br.readLine()) != null) {
                if (line.equals("")) {
                    break;
                }

                String[] data = line.split("\\s+");

                char name = data[0].charAt(0);

                int startRow = Integer.parseInt(data[1]);
                int startCol = Integer.parseInt(data[2]);
                int endRow = Integer.parseInt(data[3]);
                int endCol = Integer.parseInt(data[4]);
                // gets car position data

                if (startRow == endRow) {

                    for (int i = startCol; i <= endCol; ++i) {
                        this.puzzle[startRow][i] = name;
                    }

                } else {

                    for (int j = startRow; j <= endRow; ++j) {
                        this.puzzle[j][startCol] = name;
                    }

                }

                for (int m = 0; m < this.numRows; ++m) {

                    for (int n = 0; n < this.numCols; ++n) {

                        if (this.puzzle[m][n] == null) {

                            this.puzzle[m][n] = '.';
                            // sets puzzle spots to empty if no cars in spot

                        }
                    }
                }
            }
        }
    }

    /**
     * Jam configuration constructor with data.
     *
     * @param numRows puzzle row count
     * @param numCols puzzle column count
     * @param puzzle puzzle grid
     */
    public JamConfig(int numRows, int numCols, Character[][] puzzle) {

        this.numRows = numRows;
        this.numCols = numCols;
        this.puzzle = puzzle;

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isSolution() {

        for (int i = 0; i < this.numRows; ++i) {

            if (this.puzzle[i][this.numCols - 1].equals('X')) {
                return true;
            }
        }

        return false;
    }

    /**
     * Moves a single car in neighbor generation process.
     *
     * @param row car row
     * @param col car column
     * @param carChar car character
     * @return neighbor configurations for a specific car
     */
    private Collection<Configuration> moveCar(int row, int col, Character carChar) {

        Collection<Configuration> carMovements = new ArrayList<>();
        Character[][] newPuzzle1 = new Character[this.numRows][this.numCols];
        Character[][] newPuzzle2 = new Character[this.numRows][this.numCols];

        for (int m = 0; m < this.numRows; ++m) {

            System.arraycopy(this.puzzle[m], 0, newPuzzle1[m], 0, this.numCols);
            System.arraycopy(this.puzzle[m], 0, newPuzzle2[m], 0, this.numCols);
            // creates new puzzle grid copies to hold neighbor configurations

        }

        if (row + 1 < this.numRows) {

            if (this.puzzle[row + 1][col].equals(carChar)) {  // if car is vertical

                int endRow = row + 1;

                while (true) {

                    if (endRow + 1 < this.numRows) {

                        if (this.puzzle[endRow + 1][col].equals(carChar)) {

                            endRow++;

                        } else {

                            break;

                        }

                    } else {

                        break;

                    }
                }
                // gets last row coordinate of car

                if (row - 1 >= 0) {  // negative direction movement

                    if (this.puzzle[row - 1][col].equals('.')) {

                        newPuzzle1[row - 1][col] = carChar;
                        newPuzzle1[endRow][col] = '.';

                        carMovements.add(new JamConfig(this.numRows, this.numCols, newPuzzle1));

                    }
                }

                if (endRow + 1 < this.numRows) {  // positive direction movement

                    if (this.puzzle[endRow + 1][col].equals('.')) {

                        newPuzzle2[endRow + 1][col] = carChar;
                        newPuzzle2[row][col] = '.';

                        carMovements.add(new JamConfig(this.numRows, this.numCols, newPuzzle2));

                    }
                }
            }
        }

        if (col + 1 < this.numCols) {

            if (this.puzzle[row][col + 1].equals(carChar)) {    // if car is horizontal

                int endCol = col + 1;

                while (true) {

                    if (endCol + 1 < this.numCols) {

                        if (this.puzzle[row][endCol + 1].equals(carChar)) {

                            endCol++;

                        } else {

                            break;

                        }

                    } else {

                        break;

                    }
                }
                // gets last column coordinate of car

                if (col - 1 >= 0) {  // negative direction movement

                    if (this.puzzle[row][col - 1].equals('.')) {

                        newPuzzle1[row][col - 1] = carChar;
                        newPuzzle1[row][endCol] = '.';

                        carMovements.add(new JamConfig(this.numRows, this.numCols, newPuzzle1));

                    }
                }

                if (endCol + 1 < this.numCols) {  // positive direction movement

                    if (this.puzzle[row][endCol + 1].equals('.')) {

                        newPuzzle2[row][endCol + 1] = carChar;
                        newPuzzle2[row][col] = '.';

                        carMovements.add(new JamConfig(this.numRows, this.numCols, newPuzzle2));

                    }
                }
            }
        }

        return carMovements;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<Configuration> getNeighbors() {

        Collection<Configuration> neighbors = new ArrayList<>();
        ArrayList<Character> usedCars = new ArrayList<>();

        for (int i = 0; i < this.numRows; ++i) {

            for (int j = 0; j < this.numCols; ++j) {

                if (!this.puzzle[i][j].equals('.')
                        && !usedCars.contains(this.puzzle[i][j])) {

                    usedCars.add(this.puzzle[i][j]);

                    neighbors.addAll(moveCar(i , j, this.puzzle[i][j]));
                    // gets call neighbors for each car

                }
            }
        }

        return neighbors;
    }

    /**
     * Is coordinate in model?
     *
     * @param row coordinate row
     * @param col coordinate column
     * @return if coordinate is in model
     */
    public boolean inModel(int row, int col) {

        return row < this.numRows && col < this.numCols
                && row >= 0 && col >= 0;

    }

    /**
     * Is coordinate value not empty?
     *
     * @param row coordinate row
     * @param col coordinate column
     * @return if coordinate is not empty
     */
    public boolean notEmpty(int row, int col) {

        return !this.puzzle[row][col].equals('.');

    }

    /**
     * Gets the lowest coordinate starting point of car.
     *
     * @param beginRow selected row
     * @param beginCol selected column
     * @param carChar car character
     * @return lowest coordinate point containing car
     */
    private int[] getStart(int beginRow, int beginCol, Character carChar) {

        if (inModel(beginRow - 1, beginCol)) {

            if (this.puzzle[beginRow - 1][beginCol].equals(carChar)) {

                while (true) {

                    if (beginRow - 1 >= 0) {

                        if (this.puzzle[beginRow - 1][beginCol].equals(carChar)) {

                            beginRow--;

                        } else {

                            break;

                        }

                    } else {

                        break;

                    }
                }
                // gets starting row in vertical car case

                return new int[] {beginRow, beginCol};

            }
        }

        if (inModel(beginRow + 1, beginCol)) {

            if (this.puzzle[beginRow + 1][beginCol].equals(carChar)) {
                return new int[] {beginRow, beginCol};
            }

        }
        // vertical case where selected coordinates are starting coordinates

        if (inModel(beginRow, beginCol - 1)) {

            if (this.puzzle[beginRow][beginCol - 1].equals(carChar)) {

                while (true) {

                    if (beginCol - 1 >= 0) {

                        if (this.puzzle[beginRow][beginCol - 1].equals(carChar)) {

                            beginCol--;

                        } else {

                            break;

                        }

                    } else {

                        break;

                    }
                }
                // gets starting column in horizontal car case

                return new int[] {beginRow, beginCol};

            }
        }

        if (inModel(beginRow, beginCol + 1)) {

            if (this.puzzle[beginRow][beginCol + 1].equals(carChar)) {
                return new int[] {beginRow, beginCol};
            }

        }
        // horizontal case where selected coordinates are starting coordinates

        return new int[] {0, 0};
    }

    /**
     * Moves car to new location in main puzzle.
     *
     * @param beginRow starting row
     * @param beginCol starting column
     * @param moveRow row to move to
     * @param moveCol column to move to
     * @return if move was successful
     */
    public boolean move(int beginRow, int beginCol, int moveRow, int moveCol) {

        Character carChar = this.puzzle[beginRow][beginCol];

        if (inModel(moveRow, moveCol)) {

            int[] coords = getStart(beginRow, beginCol, carChar);

            int startRow = coords[0];
            int startCol = coords[1];

            if (startRow + 1 < this.numRows) {

                if (this.puzzle[startRow + 1][startCol].equals(carChar)) {  // vertical movement

                    int endRow = startRow + 1;

                    while (true) {

                        if (endRow + 1 < this.numRows) {

                            if (this.puzzle[endRow + 1][startCol].equals(carChar)) {

                                endRow++;

                            } else {

                                break;

                            }

                        } else {

                            break;

                        }
                    }
                    // gets last coordinate row

                    if (moveRow == endRow + 1 && moveCol == startCol
                            && this.puzzle[moveRow][moveCol].equals('.')) {

                        this.puzzle[moveRow][moveCol] = carChar;
                        this.puzzle[startRow][startCol] = '.';

                        return true;

                    }
                    // positive movement option

                    if (moveRow == startRow - 1 && moveCol == startCol
                            && this.puzzle[moveRow][moveCol].equals('.')) {

                        this.puzzle[moveRow][moveCol] = carChar;
                        this.puzzle[endRow][startCol] = '.';

                        return true;

                    }
                    // negative movement option

                }
            }

            if (startCol + 1 < this.numCols) {

                if (this.puzzle[startRow][startCol + 1].equals(carChar)) {  // horizontal movement

                    int endCol = startCol + 1;

                    while (true) {

                        if (endCol + 1 < this.numCols) {

                            if (this.puzzle[startRow][endCol + 1].equals(carChar)) {

                                endCol++;

                            } else {

                                break;

                            }

                        } else {

                            break;

                        }
                    }
                    // gets last coordinate column

                    if (moveCol == endCol + 1 && moveRow == startRow
                            && this.puzzle[moveRow][moveCol].equals('.')) {

                        this.puzzle[moveRow][moveCol] = carChar;
                        this.puzzle[startRow][startCol] = '.';

                        return true;

                    }
                    // positive movement option

                    if (moveCol == startCol - 1 && moveRow == startRow
                            && this.puzzle[moveRow][moveCol].equals('.')) {

                        this.puzzle[moveRow][moveCol] = carChar;
                        this.puzzle[startRow][endCol] = '.';

                        return true;

                    }
                    // negative movement option

                }
            }
        }

        return false;
        // returns false if car does not move

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object other) {
        boolean result = false;

        if (other instanceof JamConfig) {
            JamConfig otherJamConfig = (JamConfig) other;

            for (int i = 0; i < this.numRows; ++i) {

                for (int j = 0; j < this.numCols; ++j) {

                    if (!this.puzzle[i][j].equals(otherJamConfig.puzzle[i][j])) {
                        return false;
                    }
                }
            }

            result = true;

        }

        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {

        int hash = 0;

        for (int i = 0; i < this.numRows; ++i) {

            for (int j = 0; j < this.numCols; ++j) {

                hash += this.puzzle[i][j].hashCode();

            }
        }

        return hash;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        String maze = "";

        for (Character[] innerPuzzle : this.puzzle) {

            for (char elem : innerPuzzle) {

                maze += elem + " ";

            }

            maze = maze.strip();

            maze += "\n";
        }

        maze = maze.strip();

        return maze;
    }

    /**
     * Gets maze data as a clean PTUI-suitable string.
     *
     * @return PTUI-suitable model representation
     */
    public String PTUIString() {
        String maze = "";

        for (int i = 0; i < this.numCols; ++i) {
            maze += i + " ";
        }

        maze = maze.strip();
        maze += "\n  ";

        for (int j = 0; j < 2 * this.numCols; ++j) {
            maze += "-";
        }

        maze += "\n";

        int counter = 0;

        for (Character[] innerPuzzle : this.puzzle) {

            maze += counter + "| ";

            for (Character elem : innerPuzzle) {

                maze += elem + " ";

            }

            maze = maze.strip();

            maze += "\n";
            counter++;
        }

        return "   " + maze;

    }

    /**
     * Gets row count.
     *
     * @return row number
     */
    public int getNumRows() {
        return this.numRows;
    }

    /**
     * Gets column count.
     *
     * @return column number
     */
    public int getNumCols() {
        return this.numCols;
    }

    /**
     * Gets puzzle grid.
     *
     * @return puzzle
     */
    public Character[][] getPuzzle() {
        return this.puzzle;
    }
}