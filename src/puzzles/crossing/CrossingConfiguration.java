package puzzles.crossing;

import puzzles.common.solver.Configuration;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Configuration class for crossing puzzle.
 *
 * @author Max Milazzo (mam9563@rit.edu)
 */
public class CrossingConfiguration implements Configuration {

    /** Pups on left of river */
    private final int pupsOnLeft;

    /** Wolves on left of river */
    private final int wolvesOnLeft;

    /** Pups on right of river */
    private final int pupsOnRight;

    /** Wolves on right of river */
    private final int wolvesOnRight;

    /** Boat location */
    private final boolean boatSide;
    // false = left, true = right

    /**
     * CrossingConfiguration primary constructor.
     *
     * @param pups number of pups at start
     * @param wolves number of wolves at start
     */
    public CrossingConfiguration(int pups, int wolves) {
        this.pupsOnLeft = pups;
        this.wolvesOnLeft = wolves;
        this.pupsOnRight = 0;
        this.wolvesOnRight = 0;
        this.boatSide = false;
    }

    /**
     * CrossingConfiguration secondary constructor.
     * Used to generate new configurations.
     *
     * @param pupsOnLeft number of pups on left of river
     * @param wolvesOnLeft number of wolves on left of river
     * @param pupsOnRight number of pups on right of river
     * @param wolvesOnRight number of wolves on right of river
     * @param boatSide boat location
     */
    public CrossingConfiguration(int pupsOnLeft, int wolvesOnLeft,
                                 int pupsOnRight, int wolvesOnRight, boolean boatSide) {

        this.pupsOnLeft = pupsOnLeft;
        this.wolvesOnLeft = wolvesOnLeft;
        this.pupsOnRight = pupsOnRight;
        this.wolvesOnRight = wolvesOnRight;
        this.boatSide = boatSide;

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isSolution() {
        return this.pupsOnLeft == 0 && this.wolvesOnLeft == 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<Configuration> getNeighbors() {

        Collection<Configuration> neighbors = new ArrayList<>();

        if (this.boatSide) {  // adds possible configurations if boat is on the right

            if (this.pupsOnRight >= 2) {

                neighbors.add(new CrossingConfiguration(this.pupsOnLeft + 2,
                        this.wolvesOnLeft, this.pupsOnRight - 2, this.wolvesOnRight, false));

                neighbors.add(new CrossingConfiguration(this.pupsOnLeft + 1,
                        this.wolvesOnLeft, this.pupsOnRight - 1, this.wolvesOnRight, false));

            } else if (this.pupsOnRight == 1) {

                neighbors.add(new CrossingConfiguration(this.pupsOnLeft + 1,
                        this.wolvesOnLeft, 0, this.wolvesOnRight, false));

            }

            if (this.wolvesOnRight >= 1) {

                neighbors.add(new CrossingConfiguration(this.pupsOnLeft, this.wolvesOnLeft + 1,
                        this.pupsOnRight, this.wolvesOnRight - 1, false));

            }

        } else {  // adds possible configurations if boat is on the left

            if (this.pupsOnLeft >= 2) {

                neighbors.add(new CrossingConfiguration(this.pupsOnLeft - 2,
                        this.wolvesOnLeft, this.pupsOnRight + 2, this.wolvesOnRight, true));

                neighbors.add(new CrossingConfiguration(this.pupsOnLeft - 1,
                        this.wolvesOnLeft, this.pupsOnRight + 1, this.wolvesOnRight, true));

            } else if (this.pupsOnLeft == 1) {

                neighbors.add(new CrossingConfiguration(0,
                        this.wolvesOnLeft, this.pupsOnRight + 1, this.wolvesOnRight, true));

            }

            if (this.wolvesOnLeft >= 1) {

                neighbors.add(new CrossingConfiguration(this.pupsOnLeft, this.wolvesOnLeft - 1,
                        this.pupsOnRight, this.wolvesOnRight + 1, true));

            }
        }

        return neighbors;

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object other) {
        boolean result = false;

        if (other instanceof CrossingConfiguration) {
            CrossingConfiguration otherCrossingConfig = (CrossingConfiguration) other;
            result = this.pupsOnLeft == otherCrossingConfig.pupsOnLeft &&
                    this.wolvesOnLeft == otherCrossingConfig.wolvesOnLeft &&
                    this.pupsOnRight == otherCrossingConfig.pupsOnRight &&
                    this.wolvesOnRight == otherCrossingConfig.wolvesOnRight &&
                    this.boatSide == otherCrossingConfig.boatSide;
        }

        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return this.toString().hashCode();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {

        if (this.boatSide) {

            return "       left=[" + this.pupsOnLeft + ", " + this.wolvesOnLeft +
                    "], right=[" + this.pupsOnRight + ", " + this.wolvesOnRight + "]  (BOAT)";

        } else {

            return "(BOAT) left=[" + this.pupsOnLeft + ", " + this.wolvesOnLeft +
                    "], right=[" + this.pupsOnRight + ", " + this.wolvesOnRight + "]";

        }
    }
}