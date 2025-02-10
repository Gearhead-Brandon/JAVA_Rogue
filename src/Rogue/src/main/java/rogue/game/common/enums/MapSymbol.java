package rogue.game.common.enums;

/**
 * Enumeration representing different symbols used in the game map.
 *
 * <p> Each symbol has a corresponding character representation and a color.
 */
public enum MapSymbol {
    EMPTINESS(" ", MapColor.WHITE),

    WALL("#", MapColor.ORANGE),
    CORRIDOR("+", MapColor.WHITE),
    FLOOR(".", MapColor.CYAN),
    PORTAL("!", MapColor.YELLOW),

    DOOR("&", MapColor.YELLOW),
    WHITE_DOOR(DOOR.symbol, MapColor.WHITE),
    BLUE_DOOR(DOOR.symbol, MapColor.BLUE),
    MAGENTA_DOOR(DOOR.symbol, MapColor.MAGENTA),
    CYAN_DOOR(DOOR.symbol, MapColor.CYAN),
    GREEN_DOOR(DOOR.symbol, MapColor.GREEN),
    RED_DOOR(DOOR.symbol, MapColor.RED),

    KEY(">", MapColor.WHITE),
    WHITE_KEY(KEY.symbol, MapColor.WHITE),
    BLUE_KEY(KEY.symbol, MapColor.BLUE),
    MAGENTA_KEY(KEY.symbol, MapColor.MAGENTA),
    CYAN_KEY(KEY.symbol, MapColor.CYAN),
    GREEN_KEY(KEY.symbol, MapColor.GREEN),
    RED_KEY(KEY.symbol, MapColor.RED),

    PLAYER("@", MapColor.BLUE),

    FOOD("+", MapColor.RED),
    TREASURE("*", MapColor.YELLOW),
    WEAPON("/", MapColor.RED),
    SCROLL("~", MapColor.WHITE),
    POTION("?", MapColor.MAGENTA),

    ZOMBIE("Z", MapColor.GREEN),
    VAMPIRE("V", MapColor.RED),
    GHOST("G", MapColor.WHITE),
    OGRE("O", MapColor.YELLOW),
    SNAKE_MAGICIAN("S", MapColor.WHITE),
    MIMIC("M", MapColor.WHITE);

    public final String symbol;
    public final MapColor color;

    MapSymbol(String symbol, MapColor color){
        this.symbol = symbol;
        this.color = color;
    }
}
