package rogue.game.domain.enums;

import rogue.game.domain.entities.level.Corridor;

/**
 * This enum represents different types of {@link Corridor} in the level.
 */
public enum CorridorType {
    /**
     * Represents a non-corridor or an invalid corridor type.
     */
    NONE(-1),
    /**
     * Represents a horizontal corridor going from left to right.
     */
    LEFT_TO_RIGHT(0),
    /**
     * Represents a corridor turning left.
     */
    LEFT_TURN(1),
    /**
     * Represents a corridor turning right.
     */
    RIGHT_TURN(2),
    /**
     * Represents a vertical corridor going from top to bottom.
     */
    TOP_TO_BOTTOM(3);

    public final int value;

    CorridorType(int value) {
        this.value = value;
    }
}
