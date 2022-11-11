package puzzles.hoppers.model;

import puzzles.common.solver.Configuration;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Hoppers configuration class.
 *
 * @author Max Milazzo (mam9563@rit.edu)
 */
public class HoppersConfig implements Configuration {

    /** Number of rows in configuration */
    private final int numRows;

    /** Number of columns in configuration */
    private final int numCols;

    /** Main puzzle */
    private final String[][] puzzle;

    /**
     * Hoppers configuration constructor from file.
     *
     * @param fileName configuration file
     * @throws IOException if file read error
     */
    public HoppersConfig(String fileName) throws IOException {

        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;

            String[] dimens = br.readLine().split("\\s+");
            this.numRows = Integer.parseInt(dimens[0]);
            this.numCols = Integer.parseInt(dimens[1]);
            // gets row and column data

            this.puzzle = new String[this.numRows][this.numCols];

            int counter = 0;

            while ((line = br.readLine()) != null) {
                if (line.equals("")) {
                    break;
                }

                this.puzzle[counter] = line.split("\\s+");

                counter++;
            }
        }
    }

    /**
     * Hoppers configuration constructor with data.
     *
     * @param numRows puzzle row count
     * @param numCols puzzle column count
     * @param puzzle puzzle grid
     */
    public HoppersConfig(int numRows, int numCols, String[][] puzzle) {

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

            for (int j = 0; j < this.numCols; ++j) {

                if (this.puzzle[i][j].equals("G")) {  // no other frogs in puzzle
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * Gets possible legal jump spots.
     *
     * @param arraySize size of returned array
     * @param row current selected row
     * @param col current selected column
     */
    private int[][] getJumpSpots(int arraySize, int row, int col) {

        if (arraySize == 4) {

            return new int[][] {
                    {row - 2, col - 2},
                    {row - 2, col + 2},
                    {row + 2, col - 2},
                    {row + 2, col + 2},
            };

        } else {

            return new int[][] {
                    {row - 2, col - 2},
                    {row - 2, col + 2},
                    {row + 2, col - 2},
                    {row + 2, col + 2},
                    {row, col - 4},
                    {row, col + 4},
                    {row - 4, col},
                    {row + 4, col}
            };
        }
    }

    /**
     * Gets frog locations for possible legal jump spots.
     *
     * @param arraySize size of returned array
     * @param row current selected row
     * @param col current selected column
     */
    private int[][] getFrogSpots(int arraySize, int row, int col) {

        if (arraySize == 4) {

            return new int[][] {
                    {row - 1, col - 1},
                    {row - 1, col + 1},
                    {row + 1, col - 1},
                    {row + 1, col + 1},
            };

        } else {

            return new int[][] {
                    {row - 1, col - 1},
                    {row - 1, col + 1},
                    {row + 1, col - 1},
                    {row + 1, col + 1},
                    {row, col - 2},
                    {row, col + 2},
                    {row - 2, col},
                    {row + 2, col}
            };
        }
    }

    /**
     * Gets configuration neighbors for a specific frog.
     *
     * @param row frog row
     * @param col frog column
     * @param frog frog character
     * @return neighbors
     */
    private Collection<Configuration> neighborsAt(int row, int col, String frog) {

        Collection<Configuration> newNeighbors = new ArrayList<>();

        int count;

        if (row % 2 == 0) {  // gets different data based on coordinate value

            count = 8;

        } else {

            count = 4;

        }

        int[][] jumpSpots = getJumpSpots(count, row, col);
        int[][] frogSpots = getFrogSpots(count, row, col);

        for (int i = 0; i < count; ++i) {

            if (jumpSpots[i][0] >= 0 && jumpSpots[i][1] >= 0
                    && jumpSpots[i][0] < this.numRows && jumpSpots[i][1] < this.numCols) {

                if (this.puzzle[jumpSpots[i][0]][jumpSpots[i][1]].equals(".")
                    && this.puzzle[frogSpots[i][0]][frogSpots[i][1]].equals("G")) {

                    String[][] newPuzzle = new String[this.numRows][this.numCols];

                    for (int m = 0; m < this.numRows; ++m) {  // creates puzzle grid copy to edit

                        System.arraycopy(this.puzzle[m], 0, newPuzzle[m], 0, this.numCols);

                    }

                    newPuzzle[row][col] = ".";
                    newPuzzle[jumpSpots[i][0]][jumpSpots[i][1]] = frog;
                    newPuzzle[frogSpots[i][0]][frogSpots[i][1]] = ".";

                    newNeighbors.add(new HoppersConfig(this.numRows, this.numCols, newPuzzle));
                    // adds all neighbors to list

                }
            }
        }

        return newNeighbors;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<Configuration> getNeighbors() {

        Collection<Configuration> neighbors = new ArrayList<>();

        for (int i = 0; i < this.numRows; ++i) {

            for (int j = 0; j < this.numCols; ++j) {

                if (this.puzzle[i][j].equals("G")
                        || this.puzzle[i][j].equals("R")) {

                    neighbors.addAll(neighborsAt(i, j, this.puzzle[i][j]));
                    // gets all neighbors for each frog

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

        return !(this.puzzle[row][col].equals(".")
                || this.puzzle[row][col].equals("*"));

    }

    /**
     * Moves frog to new location
     *
     * @param beginRow starting row
     * @param beginCol starting column
     * @param moveRow row to move to
     * @param moveCol column to move to
     * @return if move was successful
     */
    public boolean move(int beginRow, int beginCol, int moveRow, int moveCol) {

        if (inModel(moveRow, moveCol)) {

            String frog = this.puzzle[beginRow][beginCol];

            int count;

            if (beginRow % 2 == 0) {  // gets different data based on coordinate value

                count = 8;

            } else {

                count = 4;

            }

            int[][] jumpSpots = getJumpSpots(count, beginRow, beginCol);
            int[][] frogSpots = getFrogSpots(count, beginRow, beginCol);

            for (int i = 0; i < count; ++i) {

                if (jumpSpots[i][0] >= 0 && jumpSpots[i][1] >= 0
                        && jumpSpots[i][0] < this.numRows && jumpSpots[i][1] < this.numCols) {

                    if (this.puzzle[jumpSpots[i][0]][jumpSpots[i][1]].equals(".")
                            && this.puzzle[frogSpots[i][0]][frogSpots[i][1]].equals("G")) {

                        if (moveRow == jumpSpots[i][0] && moveCol == jumpSpots[i][1]) {

                            this.puzzle[beginRow][beginCol] = ".";
                            this.puzzle[moveRow][moveCol] = frog;
                            this.puzzle[frogSpots[i][0]][frogSpots[i][1]] = ".";
                            // moves frog on main puzzle grid

                            return true;

                        }
                    }
                }
            }
        }

        return false;
        // returns false if frog does not move

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object other) {
        boolean result = false;

        if (other instanceof HoppersConfig) {
            HoppersConfig otherHoppersConfig = (HoppersConfig) other;

            for (int i = 0; i < this.numRows; ++i) {

                for (int j = 0; j < this.numCols; ++j) {

                    if (!this.puzzle[i][j].equals(otherHoppersConfig.puzzle[i][j])) {
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

        for (String[] innerPuzzle : this.puzzle) {

            for (String elem : innerPuzzle) {

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

        for (String[] innerPuzzle : this.puzzle) {

            maze += counter + "| ";

            for (String elem : innerPuzzle) {

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
    public String[][] getPuzzle() {
        return this.puzzle;
    }
}