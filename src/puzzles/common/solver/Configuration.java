package puzzles.common.solver;

import java.util.Collection;

/**
 * Interface for BFS configurations
 */
public interface Configuration {

    /**
     * Checks if configuration is a solution.
     *
     * @return whether or not configuration is a solution
     */
    boolean isSolution();

    /**
     * Gets configuration neighbors.
     *
     * @return configuration neighbors
     */
    Collection<Configuration> getNeighbors();

    /**
     * Checks configuration equality.
     *
     * @param other object to compare to
     * @return whether ot not configurations are equal
     */
    boolean equals(Object other);

    /**
     * Gets configuration hashcode.
     *
     * @return hashcode
     */
    int hashCode();

    /**
     * Returns configuration string representation.
     *
     * @return string representation
     */
    String toString();
}