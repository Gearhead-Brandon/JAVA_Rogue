package rogue.game.domain.entities.gameMap;

import lombok.Getter;
import lombok.Setter;
import rogue.game.common.enums.MapColor;
import rogue.game.common.enums.MapSymbol;

/**
 * Represents an element on the game map.
 *
 * <p> A MapElement has a visible state and a {@link MapSymbol} associated with it.
 * <p> The {@link MapSymbol} determines the visual representation of the element on the map.
 */
@Getter
@Setter
public class MapElement {
    private boolean visible;
    private MapSymbol symbol;

    public MapElement(MapSymbol symbol){
        this.visible = true;
        this.symbol = symbol;
    }

    public MapColor getColor(){
        return symbol.color;
    }
}
