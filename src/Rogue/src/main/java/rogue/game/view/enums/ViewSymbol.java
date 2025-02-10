package rogue.game.view.enums;

/**
 * Enum representing various symbols used in the game view.
 */
public enum ViewSymbol {
    /**
     * The title label for the game.
     */
    ROGUE_LABEL("|| R O G U E 1980 ||"),

    /**
     * A vertical wall character for menu borders.
     */
    MENU_VERTICAL_WALL("=".repeat(20)),

    /**
     * A horizontal wall character for menu borders.
     */
    MENU_HORIZONTAL_WALL("||                ||"),

    /**
     * The label for the "Play" menu option.
     */
    PLAY(" PLAY "),

    /**
     * The label for the "Statistics" menu option.
     */
    STATISTICS(" STATISTICS "),

    /**
     * A message indicating the start of statistics.
     */
    STATISTICS_OF_ALL_WALKTHROUGHES(" --- STATISTICS OF ALL WALKTHROUGHES --- "),

    /**
     * A message indicating that no walkthroughes were found.
     */
    NO_WALKTHROUGHES_FOUND(" NO WALKTHROUGHES FOUND "),

    /**
     * A separator for statistics tables.
     */
    STATISTICS_SEPARATOR("-".repeat(130)),

    /**
     * A wide empty string for formatting.
     */
    EMPTY_WIDE_STR(" ".repeat(120)),

    /**
     * A half-wide empty string for formatting.
     */
    EMPTY_HALF_WIDE_STR(" ".repeat(45)),

    /**
     * An empty string for value placeholders.
     */
    EMPTY_VALUE_STR("     "),

    /**
     * A double-width empty string for value placeholders.
     */
    DOUBLE_EMPTY_VALUE_STR("        "),

    /**
     * The label for the "Level" statistic.
     */
    LABEL_LEVEL("Level: "),

    /**
     * The label for the "Gold" statistic.
     */
    LABEL_GOLD("Gold: "),

    /**
     * The label for the "Health" statistic.
     */
    LABEL_HEALTH("Hits: "),

    /**
     * The label for the "Strength" statistic.
     */
    LABEL_STRENGTH("Strength: "),

    /**
     * The label for the inventory options.
     */
    OPENING_INVENTORY("Inventory : Weapon - h : Food - j : Potion - k : Scroll - e"),

    /**
     * The label for the "More" option.
     */
    MORE(" More");

    public final String value;

    ViewSymbol(String symbol) {
        this.value = symbol;
    }
}
