package rogue.game.domain.enums;

import rogue.game.common.enums.MapSymbol;
import rogue.game.domain.entities.enemies.Mimic;

import java.util.Collections;
import java.util.List;

/**
 * This class defines constants related to the behavior and appearance of {@link Mimic} in the game.
 */
public enum MimicAttribute {

    /**
     *The required number of cases when the mimic is in true form and does not see the player, in order to change form to an item.
     */
    MAX_DETECTION_OF_PLAYER (4),

    /**
     * A list of all possible map symbols that a Mimic can disguise itself as.
     */
    APPEARANCES_OF_MIMIC (List.of(
            MapSymbol.TREASURE, MapSymbol.FOOD, MapSymbol.POTION,
            MapSymbol.SCROLL, MapSymbol.WEAPON, MapSymbol.WHITE_KEY,
            MapSymbol.BLUE_KEY, MapSymbol.MAGENTA_KEY, MapSymbol.CYAN_KEY,
            MapSymbol.GREEN_KEY, MapSymbol.RED_KEY
    ));

    public final int value;
    public final List<MapSymbol> list;

    MimicAttribute(int value){
        this.value = value;
        this.list = Collections.emptyList();
    }

    MimicAttribute(List<MapSymbol> list){
        this.list = list;
        this.value = 0;
    }
}
