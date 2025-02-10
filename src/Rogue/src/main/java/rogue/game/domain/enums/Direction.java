package rogue.game.domain.enums;

/**
 * An enumeration representing the four cardinal directions in a two-dimensional space.
 */
public enum Direction {
    /**
     * The upward direction.
     */
    TOP(0),
    /**
     * The rightward direction.
     */
    RIGHT(1),
    /**
     * The downward direction.
     */
    BOTTOM(2),
    /**
     * The leftward direction.
     */
    LEFT(3);
    public final int value;

    Direction(int value) {
        this.value = value;
    }
}
