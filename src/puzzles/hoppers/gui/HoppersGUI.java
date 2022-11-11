package puzzles.hoppers.gui;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import puzzles.common.Observer;
import puzzles.hoppers.model.HoppersConfig;
import puzzles.hoppers.model.HoppersModel;

import javafx.application.Application;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

/**
 * Hoppers GUI class.
 *
 * @author Max Milazzo (mam9563@rit.edu)
 */
public class HoppersGUI extends Application implements Observer<HoppersModel, String> {

    /** The resources directory is located directly underneath the gui package */
    private final static String RESOURCES_DIR = "resources/";

    /** Defines size of icons used */
    private final static int ICON_SIZE = 75;

    /** Gets red frog image data */
    private final Image redFrog = new Image(getClass().getResourceAsStream(RESOURCES_DIR+"red_frog.png"));

    /** Gets green frog image data */
    private final Image greenFrog = new Image(getClass().getResourceAsStream(RESOURCES_DIR+"green_frog.png"));

    /** Gets lily pad image data */
    private final Image lilyPad = new Image(getClass().getResourceAsStream(RESOURCES_DIR+"lily_pad.png"));

    /** Gets water image data */
    private final Image water = new Image(getClass().getResourceAsStream(RESOURCES_DIR+"water.png"));

    /** Holds Hoppers model */
    private HoppersModel model;

    /** Number of rows in configuration */
    private int gameRows;

    /** Number of columns in configuration */
    private int gameCols;

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

        this.model = new HoppersModel();
        this.model.addObserver(this);
        this.model.load(filename, true);
        // initializes the model

        HoppersConfig temp = (HoppersConfig) this.model.getCurrentConfig();

        this.gameRows = temp.getNumRows();
        this.gameCols = temp.getNumCols();
        // gets puzzle size data

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
                grid[i][j].setMinSize(ICON_SIZE, ICON_SIZE);
                grid[i][j].setMaxSize(ICON_SIZE, ICON_SIZE);
                // sets button sizing

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
                HoppersConfig temp = (HoppersConfig) this.model.getCurrentConfig();

                this.gameRows = temp.getNumRows();
                this.gameCols = temp.getNumCols();

                gridHolder.getChildren().clear();
                this.grid = new Button[this.gameRows][this.gameCols];
                gridInit(gridHolder);
                // rebuilds grid

                stage.setHeight(ICON_SIZE * this.gameRows + 120);
                stage.setWidth(ICON_SIZE * this.gameCols + 15);

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

        HoppersConfig temp = (HoppersConfig) this.model.getCurrentConfig();
        applyUpdate(temp.getPuzzle(), "HoppersGUI");
        // invokes initial display update

    }

    /**
     * Applies model updates to the display.
     *
     * @param puzzle holds puzzle data
     * @param msg holds message data from model
     */
    public void applyUpdate(String[][] puzzle, String msg) {

        if (puzzle.length == this.gameRows && puzzle[0].length == this.gameCols) {
            this.info.setText(msg);

            for (int i = 0; i < this.gameRows; ++i) {

                for (int j = 0; j < this.gameCols; ++j) {

                    switch (puzzle[i][j]) {  // updates grid with model puzzle data

                        case "*" -> this.grid[i][j].setGraphic(new ImageView(water));
                        case "." -> this.grid[i][j].setGraphic(new ImageView(lilyPad));
                        case "G" -> this.grid[i][j].setGraphic(new ImageView(greenFrog));
                        case "R" -> this.grid[i][j].setGraphic(new ImageView(redFrog));

                    }
                }
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void update(HoppersModel hoppersModel, String msg) {

        HoppersConfig temp = (HoppersConfig) hoppersModel.getCurrentConfig();

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
            System.out.println("Usage: java HoppersGUI filename");
        } else {
            Application.launch(args);
        }
    }
}