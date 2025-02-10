package rogue.game.common;

import rogue.game.common.enums.MapColor;
import rogue.game.common.enums.MapSymbol;

/**
 * Interface defining the contract for providing information about the game map.
 *
 * <p>This interface provides methods to access map data, including the symbol at a specific position,
 * the color of the symbol, and the overall dimensions of the map.
 */
public interface MapInfo {
    MapSymbol getSymbol(int y, int x);
    MapColor getSymbolColor(int y, int x);
    int getHeight();
    int getWidth();
}
