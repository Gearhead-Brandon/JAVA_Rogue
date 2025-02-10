package rogue.game.domain.enums.util;

import rogue.game.common.enums.MapColor;
import rogue.game.common.enums.MapSymbol;

/**
 * Utility class for handling map symbols.
 */
public class MapSymbolUtil {
    public static MapSymbol getDoorSymbolByColor(MapColor color){
        return switch (color) {
            case WHITE -> MapSymbol.WHITE_DOOR;
            case BLUE -> MapSymbol.BLUE_DOOR;
            case MAGENTA -> MapSymbol.MAGENTA_DOOR;
            case CYAN -> MapSymbol.CYAN_DOOR;
            case GREEN -> MapSymbol.GREEN_DOOR;
            case RED -> MapSymbol.RED_DOOR;
            default -> MapSymbol.DOOR;
        };
    }

    public static MapSymbol getKeySymbolByColor(MapColor color){
        return switch (color) {
            case BLUE -> MapSymbol.BLUE_KEY;
            case MAGENTA -> MapSymbol.MAGENTA_KEY;
            case CYAN -> MapSymbol.CYAN_KEY;
            case GREEN -> MapSymbol.GREEN_KEY;
            case RED -> MapSymbol.RED_KEY;
            default -> MapSymbol.WHITE_KEY;
        };
    }

    /**
     * Checks if the given symbol represents a door.
     *
     * @param symbol The symbol to check.
     * @return `true` if the symbol is a door, `false` otherwise.
     */
    public static boolean isDoor(MapSymbol symbol){
        return symbol.symbol.equals(MapSymbol.DOOR.symbol);
    }

    public static boolean isEnemy(MapSymbol symbol){
        return symbol.equals(MapSymbol.ZOMBIE) ||
                symbol.equals(MapSymbol.OGRE) ||
                symbol.equals(MapSymbol.VAMPIRE) ||
                symbol.equals(MapSymbol.GHOST) ||
                symbol.equals(MapSymbol.SNAKE_MAGICIAN) ||
                symbol.equals(MapSymbol.MIMIC);
    }
}
