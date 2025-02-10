package rogue.game.view.enums;

/**
 * Enum representing various view attributes.
 */
public enum ViewAttribute {
    /**
     * Height of the status bar in the game window.
     */
    STATUS_BAR_HEIGHT(2),

    /**
     * Maximum number of walkthroughes to display.
     */
    MAX_NUMBER_OF_WALKTHROUGHES(15);

    public final int value;

    ViewAttribute(int value) {
        this.value = value;
    }
}
