package puzzles.jam.solver;

import puzzles.common.solver.Configuration;
import puzzles.common.solver.Solver;
import puzzles.jam.model.JamConfig;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Jam solver.
 *
 * @author Max Milazzo (mam9563@rit.edu)
 */
public class Jam {
    public static void main(String[] args) {
        if (args.length != 1) {

            System.out.println("Usage: java Jam filename");

        } else {

            Solver puzzleSolver = new Solver();
            // creates Solver instance

            try {
                
                Configuration start = new JamConfig(args[0]);
                // initializes start configuration

                System.out.println("File: " + args[0]);
                System.out.println(start);

                ArrayList<Configuration> path = (ArrayList<Configuration>) puzzleSolver.findPath(start);
                // uses BFS Solver class to solve puzzle

                if (path.size() > 0) {

                    for (int i = 0; i < path.size(); ++i) {  // displays path info

                        if (i != 0) {
                            System.out.println();
                        }

                        System.out.print("Step " + i + ":\n");
                        System.out.println(path.get(i));
                    }

                } else {
                    System.out.println("No solution");
                }

            } catch (IOException ioe) {
                System.out.println(ioe);
            }
        }
    }
}