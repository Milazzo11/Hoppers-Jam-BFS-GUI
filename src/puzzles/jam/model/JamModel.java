package puzzles.jam.model;

import puzzles.common.Observer;
import puzzles.common.solver.Configuration;
import puzzles.common.solver.Solver;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Jam model class.
 *
 * @author Max Milazzo (mam9563@rit.edu)
 */
public class JamModel {

    /** the collection of observers of this model */
    private final List<Observer<JamModel, String>> observers = new LinkedList<>();

    /** the current configuration */
    private JamConfig currentConfig;

    /** The selected coordinates */
    private int[] selected;

    /** If game is ongoing */
    private boolean ongoing = true;

    /** Current loaded file */
    private String currentFileName;

    /**
     * The view calls this to add itself as an observer.
     *
     * @param observer the view
     */
    public void addObserver(Observer<JamModel, String> observer) {
        this.observers.add(observer);
    }

    /**
     * The model's state has changed (the counter), so inform the view via
     * the update method
     */
    private void alertObservers(String msg) {
        for (var observer : observers) {
            observer.update(this, msg);
        }
    }

    /**
     * Gets next step in puzzle to solve.
     */
    public void hint() {

        Solver puzzleSolver = new Solver();
        // creates Solver instance

        ArrayList<Configuration> path = (ArrayList<Configuration>) puzzleSolver.findPath(currentConfig, false);
        // gets solve data

        if (path.size() > 1) {

            this.currentConfig = (JamConfig) path.get(1);
            alertObservers("Next step!");

        } else {

            if (this.currentConfig.isSolution()) {
                alertObservers("Already solved!");
            } else {
                alertObservers("No solution!");
            }
        }
    }

    /**
     * Loads file into configuration.
     *
     * @param fileName configuration file
     * @param init is this a reset or initialization?
     */
    public void load(String fileName, boolean init) {

        this.currentFileName = fileName;

        try {

            this.currentConfig = new JamConfig(fileName);
            // creates new configuration

        } catch (IOException ioe) {

            alertObservers("Failed to load: " + fileName);
            return;

        }

        this.selected = new int[] {-1, -1};
        // sets default coordinate value

        if (init) {
            alertObservers("Loaded: " + fileName);
        } else {
            alertObservers("Puzzle reset!");
        }
    }

    /**
     * Selects coordinate to move or move location.
     *
     * @param row puzzle row
     * @param col puzzle column
     */
    public void select(int row, int col) {

        if (this.selected[0] == -1) {  // selects initial coordinate

            if (currentConfig.inModel(row, col) && currentConfig.notEmpty(row, col)) {

                this.selected = new int[] {row, col};
                alertObservers("Selected (" + row + ", " + col + ")");

            } else {

                alertObservers("No car at (" + row + ", " + col + ")");
            }

        } else {  // selects move location

            boolean moved = this.currentConfig.move(this.selected[0], this.selected[1], row, col);

            if (moved) {

                alertObservers("Moved from (" + this.selected[0] + ", " + this.selected[1]
                        + ") to (" + row + ", " + col + ")");

            } else {

                alertObservers("Can't move from (" + this.selected[0] + ", " + this.selected[1]
                        + ") to (" + row + ", " + col + ")");

            }

            this.selected = new int[] {-1, -1};

        }
    }

    /**
     * Quits program.
     */
    public void quit() {

        this.ongoing = false;
        alertObservers("");

    }

    /**
     * Reloads current configuration.
     *
     * @throws IOException if file data error
     */
    public void reset() throws IOException {

        load(this.currentFileName, false);

    }

    /**
     * If game is ongoing.
     *
     * @return is game ongoing?
     */
    public boolean isOngoing() {
        return this.ongoing;
    }

    /**
     * Gets current configuration.
     *
     * @return current configuration.
     */
    public Configuration getCurrentConfig() {
        return this.currentConfig;
    }
}