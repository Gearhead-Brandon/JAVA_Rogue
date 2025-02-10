package rogue.game.domain.entities.gameMap;

import rogue.game.common.enums.MapSymbol;
import rogue.game.domain.enums.MapAttribute;
import rogue.game.domain.entities.Position;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the game map, a 2D grid of map elements.
 *
 * <p> Provides methods for accessing and modifying map elements, checking visibility, and setting visibility.
 */

public class GameMap {
    private final List<List<MapElement>> map;

    public GameMap() {
        map = new ArrayList<>(MapAttribute.HEIGHT.value);

        for (int i = 0; i < MapAttribute.HEIGHT.value; i++) {
            map.add(new ArrayList<>(MapAttribute.WIDTH.value));

            for (int j = 0; j < MapAttribute.WIDTH.value; j++) {
                map.get(i).add(new MapElement(MapSymbol.EMPTINESS));
            }
        }
    }

    public MapSymbol getMapSymbol(int x, int y) { return map.get(y).get(x).getSymbol(); }
    public MapSymbol getMapSymbol(Position pos) { return map.get(pos.y()).get(pos.x()).getSymbol(); }

    public void setMapSymbol(int x, int y, MapSymbol symbol) { map.get(y).get(x).setSymbol(symbol); }
    public void setMapSymbol(Position pos, MapSymbol symbol) { map.get(pos.y()).get(pos.x()).setSymbol(symbol); }

    public boolean isVisible(Position pos) { return map.get(pos.y()).get(pos.x()).isVisible(); }
    public boolean isVisible(int x, int y) { return map.get(y).get(x).isVisible(); }

    public void setVisible(int x, int y, boolean visible) { map.get(y).get(x).setVisible(visible); }
}
