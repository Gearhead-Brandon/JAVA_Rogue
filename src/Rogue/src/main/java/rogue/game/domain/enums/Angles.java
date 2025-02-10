package rogue.game.domain.enums;

/**
 * An enum representing various angles used in the game.
 */
public enum Angles {
    /**
     * Represents an angle of 90 degrees, typically pointing upwards.
     */
    UP(90.0f),
    /**
     * Represents an angle of 0 degrees, typically pointing to the right.
     */
    RIGHT(0.0f),
    /**
     * Represents an angle of 270 degrees, typically pointing downwards.
     */
    DOWN(270.0f),
    /**
     * Represents an angle of 180 degrees, typically pointing to the left.
     */
    LEFT(180.0f),
    /**
     * Represents a 180-degree rotation.
     */
    ROTATE(180.0f),
    /**
     * Represents a full 360-degree view.
     */
    FULL_VIEW(360.0f);

    public final float angle;

    Angles(float angle) {
        this.angle = angle;
    }
}
