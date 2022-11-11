package puzzles.common.solver;

import java.util.*;

/**
 * BFS common solver class.
 *
 * @author Max Milazzo (mam9563@rit.edu)
 */
public class Solver {

    /**
     * Common BFS algorithm to find the shortest path between two configurations.
     *
     * @param start starting configuration
     * @return BFS shortest path to end
     */
    public Collection<Configuration> findPath(Configuration start, boolean display) {
        Queue<Configuration> q = new LinkedList<>();
        HashMap<Configuration, Configuration> predecessors = new HashMap<>();

        int totalConfigs = 1;
        int uniqueConfigs = 1;

        boolean found = false;

        q.add(start);
        predecessors.put(start, start);

        Configuration current = null;

        while (!q.isEmpty()) {  // runs until queue is empty
            current = q.remove();

            if (current.isSolution()) {
                found = true;
                break;
            }

            for (Configuration neighbor : current.getNeighbors()) {  // loops through all neighbors
                totalConfigs++;

                if (!predecessors.containsKey(neighbor)) {
                    uniqueConfigs++;

                    predecessors.put(neighbor, current);
                    q.add(neighbor);
                }
            }
        }

        ArrayList<Configuration> pathList = new ArrayList<>();

        if (found) {  // checks if a path to the end has been reached

            while (!current.equals(start)) {  // constructs path based on search algorithm results
                pathList.add(0, current);
                current = predecessors.get(current);
            }

            pathList.add(0, start);
        }

        if (display) {
            System.out.println("Total configs: " + totalConfigs);
            System.out.println("Unique configs: " + uniqueConfigs);
        }

        return pathList;
    }

    public Collection<Configuration> findPath(Configuration start) {
        return findPath(start, true);
    }
}