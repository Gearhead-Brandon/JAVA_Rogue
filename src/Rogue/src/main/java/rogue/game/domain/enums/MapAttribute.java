package rogue.game.domain.enums;

/**
 * Represents attributes related to the map's dimensions.
 */
public enum MapAttribute {
    HEIGHT(30),
    WIDTH(90);

    public final int value;

    MapAttribute(int value) {
        this.value = value;
    }
}
