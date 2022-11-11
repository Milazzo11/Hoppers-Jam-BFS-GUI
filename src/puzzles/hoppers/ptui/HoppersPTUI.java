package puzzles.hoppers.ptui;

import puzzles.common.Observer;
import puzzles.hoppers.model.HoppersConfig;
import puzzles.hoppers.model.HoppersModel;

import java.io.IOException;
import java.util.Scanner;

/**
 * Hoppers PTUI display.
 *
 * @author Max Milazzo (mam9563@rit.edu)
 */
public class HoppersPTUI implements Observer<HoppersModel, String> {

    /** Holds Hoppers model */
    private final HoppersModel model;

    /** Is game ongoing? */
    private boolean ongoing = true;

    /**
     * HoppersPTUI constructor.
     *
     * @param fileName file name to load
     * @throws IOException for error loading file
     */
    public HoppersPTUI(String fileName) throws IOException {

        this.model = new HoppersModel();
        this.model.addObserver(this);
        this.model.load(fileName, true);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void update(HoppersModel model, String msg) {

        this.ongoing = model.isOngoing();

        if (this.ongoing) {

            System.out.println(msg);

            HoppersConfig currentConfig = (HoppersConfig) model.getCurrentConfig();
            // gets current configuration

            System.out.println(currentConfig.PTUIString());

        }
    }

    /**
     * Displays command information message.
     */
    private void helpMessage() {

        System.out.println("h(int)              -- hint next move");
        System.out.println("l(oad) filename     -- load new puzzle file");
        System.out.println("s(elect) r c        -- select cell at r, c");
        System.out.println("q(uit)              -- quit the game");
        System.out.println("r(eset)             -- reset the current game");

    }

    /**
     * Handles input commands.
     *
     * @param input command line input
     * @throws IOException if error loading file
     */
    private void handle(String input) throws IOException {

        String[] command = input.split("\\s+");

        switch (command[0]) {
            case "h":
            case "hint":  // gives hint

                model.hint();

                break;
            case "l":
            case "load":  // loads file

                if (command.length > 1) {

                    model.load(command[1], true);

                } else {

                    System.out.println("ERROR: Invalid input");

                }

                break;
            case "s":
            case "select":  // selects grid spot

                try {

                    if (command.length > 2) {

                        model.select(Integer.parseInt(command[1]), Integer.parseInt(command[2]));

                    } else {

                        System.out.println("ERROR: Invalid input");
                    }

                } catch (NumberFormatException nfe) {

                    System.out.println("ERROR: Invalid input");

                }

                break;
            case "q":
            case "quit":  // quits game

                model.quit();

                break;
            case "r":
            case "reset":  // resets puzzle

                model.reset();

                break;
            default:

                helpMessage();

        }
    }

    /**
     * Loops to get user input.
     *
     * @throws IOException if error loading file
     */
    private void inputLoop() throws IOException {

        helpMessage();
        Scanner in = new Scanner(System.in);

        while (this.ongoing) {

            System.out.print("> ");
            handle(in.nextLine());

        }
    }

    /**
     * Program entry point.
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {
        if (args.length != 1) {

            System.out.println("Usage: java HoppersPTUI filename");

        } else {

            try {

                HoppersPTUI ptui = new HoppersPTUI(args[0]);
                ptui.inputLoop();
                // starts PTUI loop

            } catch (IOException ieo) {
                System.out.println("ERROR: File not found");
            }
        }
    }
}