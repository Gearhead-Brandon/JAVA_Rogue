package rogue.game.view.enums;

import jcurses.system.CharColor;

/**
 * Enum representing different colors used for displaying text in the game view.
 */
public enum ViewColor {

    /**
     * Black text color.
     */
    BLACK(new CharColor(CharColor.BLACK, CharColor.BLACK)),

    /**
     * Red text color.
     */
    RED(new CharColor(CharColor.BLACK, CharColor.RED)),

    /**
     * Green text color.
     */
    GREEN(new CharColor(CharColor.BLACK, CharColor.GREEN)),

    /**
     * Light green text color.
     */
    LIGHT_GREEN(new CharColor(CharColor.BLACK, CharColor.GREEN, CharColor.NORMAL, CharColor.NORMAL)),

    /**
     * White text color.
     */
    WHITE(new CharColor(CharColor.BLACK, CharColor.WHITE)),

    /**
     * Orange text color (combination of yellow and bold).
     */
    ORANGE(new CharColor(CharColor.BLACK, CharColor.YELLOW, CharColor.NORMAL, CharColor.NORMAL)),

    /**
     * Yellow text color with bold style.
     */
    BOLD_YELLOW(new CharColor(CharColor.BLACK, CharColor.YELLOW, CharColor.NORMAL, CharColor.BOLD)),

    /**
     * Magenta text color with bold style.
     */
    MAGENTA(new CharColor(CharColor.BLACK, CharColor.MAGENTA, CharColor.NORMAL, CharColor.BOLD)),

    /**
     * Blue text color.
     */
    BLUE(new CharColor(CharColor.BLACK, CharColor.BLUE)),

    /**
     * Cyan text color.
     */
    CYAN(new CharColor(CharColor.BLACK, CharColor.CYAN)),

    /**
     * White text color with black background (used for activity strings).
     */
    WHITE_ACTIVITY_STRING(new CharColor(CharColor.WHITE, CharColor.BLACK)),

    /**
     * Red text color with black background (used for activity strings).
     */
    RED_ACTIVITY_STRING(new CharColor(CharColor.RED, CharColor.BLACK));

    public final CharColor color;

    ViewColor(CharColor color){
        this.color = color;
    }
}
