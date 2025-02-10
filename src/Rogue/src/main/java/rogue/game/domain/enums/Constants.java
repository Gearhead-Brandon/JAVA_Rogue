package rogue.game.domain.enums;

import java.util.Collections;
import java.util.List;

/**
 * This enum defines various constants used in the game, including directions and their corresponding values.
 */
public enum Constants {
    /**
     * Represents a special "none" value, often used as a default or invalid value.
     */
    NONE(-1),
    /**
     * Represents the x-axis directions
     */
    DX(List.of(1, -1, 0, 0)),
    /**
     * Represents the y-axis directions
     */
    DY(List.of(0, 0, 1, -1)),
    /**
     * Represents the x-axis directions for diagonal movement
     */
    DIAGONAL_X(List.of(1, -1, 1, -1)),
    /**
     * Represents the y-axis directions for diagonal movement
     */
    DIAGONAL_Y(List.of(1, -1, -1, 1));

    public final int value;
    public final List<Integer> directionsValues;

    Constants(int value) {
        this.value = value;
        this.directionsValues = Collections.emptyList();
    }

    Constants(List<Integer> directionsValues) {
        this.directionsValues = directionsValues;
        this.value = 0;
    }
}
