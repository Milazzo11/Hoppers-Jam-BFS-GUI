package puzzles.strings;

import puzzles.common.solver.Configuration;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Configuration class for strings puzzle.
 *
 * @author Max Milazzo (mam9563@rit.edu)
 */
public class StringsConfiguration implements Configuration {

    /** Configuration current string */
    private final String str;

    /** End goal string */
    private final String solution;

    /**
     * StringsConfiguration constructor.
     *
     * @param str configuration string
     * @param solution end goal string
     */
    public StringsConfiguration(String str, String solution) {
        this.str = str;
        this.solution = solution;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isSolution() {
        return this.str.equals(this.solution);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<Configuration> getNeighbors() {

        Collection<Configuration> neighbors = new ArrayList<>();

        for (int i = 0; i < this.str.length(); ++i) {
            int charAscii = this.str.charAt(i);
            StringBuilder upBuilder = new StringBuilder(this.str);
            StringBuilder downBuilder = new StringBuilder(this.str);

            if (charAscii == 65) {  // lowest possible ASCII value

                upBuilder.setCharAt(i, 'B');
                downBuilder.setCharAt(i, 'Z');

            } else if (charAscii == 90) {  // largest possible ASCII value

                upBuilder.setCharAt(i, 'A');
                downBuilder.setCharAt(i, 'Y');

            } else {

                upBuilder.setCharAt(i, (char) (charAscii + 1));
                downBuilder.setCharAt(i, (char) (charAscii - 1));
            }

            neighbors.add(new StringsConfiguration(upBuilder.toString(), this.solution));
            neighbors.add(new StringsConfiguration(downBuilder.toString(), this.solution));

        }

        return neighbors;

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object other) {
        boolean result = false;

        if (other instanceof StringsConfiguration) {
            StringsConfiguration otherStrConfig = (StringsConfiguration) other;
            result = this.toString().equals(otherStrConfig.toString());
        }

        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return this.str.hashCode();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return this.str;
    }
}