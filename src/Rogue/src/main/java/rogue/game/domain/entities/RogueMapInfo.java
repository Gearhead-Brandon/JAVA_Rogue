package rogue.game.domain.entities;

import rogue.game.common.enums.MapColor;
import rogue.game.common.MapInfo;
import rogue.game.common.enums.MapSymbol;
import rogue.game.domain.entities.gameMap.GameMap;
import rogue.game.domain.enums.MapAttribute;

/**
 * It provides information about the game map based.
 */
public class RogueMapInfo implements MapInfo {
    private final GameMap map;

    public RogueMapInfo(GameMap map) {
        this.map = map;
    }

    @Override
    public MapSymbol getSymbol(int y, int x){
        return map.isVisible(x, y) ? map.getMapSymbol(x, y)
                                   : MapSymbol.EMPTINESS;
    }

    @Override
    public MapColor getSymbolColor(int y, int x){
        return map.isVisible(x, y) ? map.getMapSymbol(x, y).color
                                   : MapColor.WHITE;
    }

    @Override
    public int getHeight() {
        return MapAttribute.HEIGHT.value;
    }

    @Override
    public int getWidth() {
        return MapAttribute.WIDTH.value;
    }
}
