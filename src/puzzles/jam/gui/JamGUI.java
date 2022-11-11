package puzzles.jam.gui;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import puzzles.common.Observer;
import puzzles.jam.model.JamConfig;
import puzzles.jam.model.JamModel;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Jam GUI class.
 *
 * @author Max Milazzo (mam9563@rit.edu)
 */
public class JamGUI extends Application  implements Observer<JamModel, String>  {

    /** The resources directory is located directly underneath the gui package */
    private final static String RESOURCES_DIR = "resources/";
    // not used in this implementation, but kept if resources add in future

    /** X car color */
    private final static String X_CAR_COLOR = "#DF0101";

    /** Empty space color */
    private static final String BASE_COLOR = "lightgrey";

    /** Possible car colors */
    private static final String[] COLORS = new String[] {
            "yellow",
            "blue",
            "orange",
            "purple",
            "pink",
            "green"
    };

    /** Defines button font size */
    private final static int BUTTON_FONT_SIZE = 20;

    /** Defines size of icons used */
    private final static int ICON_SIZE = 75;

    /** Holds Jam model */
    private JamModel model;

    /** Number of rows in configuration */
    private int gameRows;

    /** Number of columns in configuration */
    private int gameCols;

    /** Maps cars to their correct colors */
    private HashMap<Character, String> colorMap;

    /** Game info label */
    private final Label info = new Label();

    /** Main game grid */
    private Button[][] grid;

    /**
     * Initializes necessary model data.
     * Sets up observer.
     */
    public void init() {
        String filename = getParameters().getRaw().get(0);

        this.model = new JamModel();
        this.model.addObserver(this);
        this.model.load(filename, true);
        // initializes the model

        JamConfig temp = (JamConfig) this.model.getCurrentConfig();

        this.gameRows = temp.getNumRows();
        this.gameCols = temp.getNumCols();
        this.colorMap = new HashMap<>();
        // sets up puzzle size and color data

    }

    /**
     * Initializes the main grid.
     *
     * @param gridHolder node to hold main grid
     */
    public void gridInit(GridPane gridHolder) {
        for (int i = 0; i < this.gameRows; ++i) {

            for (int j = 0; j < this.gameCols; ++j) {

                grid[i][j] = new Button();
                grid[i][j].setStyle(
                        "-fx-font-size: " + BUTTON_FONT_SIZE + ";" +
                                "-fx-background-color:" + BASE_COLOR + ";" +
                                "-fx-font-weight: bold;");
                grid[i][j].setMinSize(ICON_SIZE, ICON_SIZE);
                grid[i][j].setMaxSize(ICON_SIZE, ICON_SIZE);
                // sets button style

                final int fi = i;
                final int fj = j;

                grid[i][j].setOnAction(event -> model.select(fi, fj));
                // sets button event

                gridHolder.add(grid[i][j], j, i);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void start(Stage stage) throws Exception {

        VBox root = new VBox(10);
        root.setAlignment(Pos.CENTER);

        root.getChildren().add(this.info);

        this.grid = new Button[this.gameRows][this.gameCols];
        GridPane gridHolder = new GridPane();

        gridInit(gridHolder);
        // initializes main grid

        root.getChildren().add(gridHolder);

        HBox buttons = new HBox();
        buttons.setAlignment(Pos.CENTER);

        Button hint = new Button("Hint");
        Button reset = new Button("Reset");
        Button load = new Button("Load");
        // creates control buttons

        hint.setOnAction(event -> model.hint());
        // sets hint button event

        reset.setOnAction(event -> {
            try {
                model.reset();
            } catch (IOException ioe) {
                System.out.println("Unexpected error resetting file");
            }
        });
        // sets reset button event

        final FileChooser fileChooser = new FileChooser();

        load.setOnAction(e -> {

            File file = fileChooser.showOpenDialog(stage);
            // get the file selected

            if (file != null) {
                this.model.load(file.getAbsolutePath(), true);
                JamConfig temp = (JamConfig) this.model.getCurrentConfig();

                this.gameRows = temp.getNumRows();
                this.gameCols = temp.getNumCols();

                gridHolder.getChildren().clear();
                this.grid = new Button[this.gameRows][this.gameCols];
                gridInit(gridHolder);
                // rebuilds grid

                stage.setHeight(ICON_SIZE * this.gameRows + 120);
                stage.setWidth(ICON_SIZE * this.gameCols + 15);

                colorSetup(temp.getPuzzle());
                applyUpdate(temp.getPuzzle(), "Loaded: " + file.getName());
            }
        });
        // sets load button event

        buttons.getChildren().addAll(hint, reset, load);
        root.getChildren().add(buttons);

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

        stage.setHeight(ICON_SIZE * this.gameRows + 120);
        stage.setWidth(ICON_SIZE * this.gameCols + 15);

        JamConfig temp = (JamConfig) this.model.getCurrentConfig();
        colorSetup(temp.getPuzzle());
        applyUpdate(temp.getPuzzle(), "JamGUI");
        // sets up colors and invokes initial display update

    }

    /**
     * Sets up car color data.
     *
     * @param puzzle grid puzzle representation
     */
    public void colorSetup(Character[][] puzzle) {

        ArrayList<Character> visited = new ArrayList<>();
        visited.add('.');
        visited.add('X');

        this.colorMap.put('.', BASE_COLOR);
        this.colorMap.put('X', X_CAR_COLOR);
        // sets empty and 'X' car colors

        int colorCount = 0;
        int maxCount = COLORS.length;

        for (int i = 0; i < this.gameRows; ++i) {

            for (int j = 0; j < this.gameCols; ++j) {

                if (!visited.contains(puzzle[i][j])) {

                    visited.add(puzzle[i][j]);

                    this.colorMap.put(puzzle[i][j], COLORS[colorCount % maxCount]);
                    // sets car colors

                    colorCount++;

                }
            }
        }
    }

    /**
     * Applies model updates to the display.
     *
     * @param puzzle holds puzzle data
     * @param msg holds message data from model
     */
    public void applyUpdate(Character[][] puzzle, String msg) {

        if (puzzle.length == this.gameRows && puzzle[0].length == this.gameCols) {
            this.info.setText(msg);

            for (int i = 0; i < this.gameRows; ++i) {

                for (int j = 0; j < this.gameCols; ++j) {

                    this.grid[i][j].setText(Character.toString(puzzle[i][j]));
                    this.grid[i][j].setStyle("-fx-background-color:" + this.colorMap.get(puzzle[i][j]) + ";");
                    // sets display information based on model data

                    if (puzzle[i][j].equals('.')) {
                        this.grid[i][j].setStyle("-fx-border-color:black;");
                        this.grid[i][j].setText("");
                    }
                    // sets display for empty spaces

                }
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void update(JamModel jamModel, String msg) {

        JamConfig temp = (JamConfig) jamModel.getCurrentConfig();

        applyUpdate(temp.getPuzzle(), msg);
        // updates display

    }

    /**
     * Program entry point.
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java JamGUI filename");
        } else {
            Application.launch(args);
        }
    }
}