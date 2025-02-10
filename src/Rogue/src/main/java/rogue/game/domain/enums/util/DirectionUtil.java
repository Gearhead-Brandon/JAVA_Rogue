package rogue.game.domain.enums.util;

import rogue.game.domain.enums.Direction;

import static rogue.game.domain.enums.Direction.*;

/**
 * This class provides utility methods for working with directions.
 */
public class DirectionUtil {

    /**
     * Returns the opposite direction of the given direction.
     *
     * @param direction The input direction.
     * @return The opposite direction.
     */
    public static Direction getOpposite(Direction direction) {
        return switch (direction) {
            case TOP -> BOTTOM;
            case RIGHT -> LEFT;
            case BOTTOM -> TOP;
            case LEFT -> RIGHT;
        };
    }
}
