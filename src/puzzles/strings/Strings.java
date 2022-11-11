package puzzles.strings;

import puzzles.common.solver.Configuration;
import puzzles.common.solver.Solver;

import java.util.ArrayList;

/**
 * String puzzle main class.
 *
 * @author Max Milazzo (mam9563@rit.edu)
 */
public class Strings {

    /**
     * String puzzle main method (entry point).
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {

        if (args.length < 2) {  // checks for incorrect arguments

            System.out.println(("Usage: java Strings start finish"));

        } else {

            Solver puzzleSolver = new Solver();
            // creates Solver instance

            Configuration start = new StringsConfiguration(args[0], args[1]);
            // initializes start configuration

            System.out.println("Start: " + args[0] + ", End: " + args[1]);

            ArrayList<Configuration> path = (ArrayList<Configuration>) puzzleSolver.findPath(start);
            // uses BFS Solver class to solve puzzle

            if (path.size() > 0) {

                for (int i = 0; i < path.size(); ++i) {  // displays path info
                    System.out.print("Step " + i + ": ");
                    System.out.println(path.get(i));
                }

            } else {
                System.out.println("No solution");
            }
        }
    }
}