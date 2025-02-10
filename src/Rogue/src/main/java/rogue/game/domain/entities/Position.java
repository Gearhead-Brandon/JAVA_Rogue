package rogue.game.domain.entities;

import rogue.game.domain.enums.Constants;

/**
 * This record class defines a position with x and y coordinates.
 */
public record Position(int x, int y) {
    /**
     * A special position representing an invalid or unspecified position.
     */
    public static final Position NONE = of(Constants.NONE.value, Constants.NONE.value);

    /**
     * Creates a new Position object.
     *
     * <p> This static factory method creates a new Position object with the given x and y coordinates.
     * <p> If both x and y are equal to `Constants.NONE.value`, it returns the special `NONE` position.
     *
     * @param x The x coordinate of the position.
     * @param y The y coordinate of the position.
     * @return A new Position object, or the `NONE` position if both coordinates are invalid.
     */
    public static Position of(int x, int y) {
        return (x == Constants.NONE.value && y == Constants.NONE.value) ? NONE : new Position(x, y);
    }
}
