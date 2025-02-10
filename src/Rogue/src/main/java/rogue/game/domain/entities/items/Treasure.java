package rogue.game.domain.entities.items;

import rogue.game.domain.enums.ItemType;
import rogue.game.domain.entities.Position;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Represents a treasure item in the game world.
 *
 * <p> Treasure contain a random amount of gold when opened.
 */
public class Treasure extends Item {
    private static final int MIN_TREASURE = 5;
    private static final int MAX_TREASURE = 20;

    public Treasure(Position position) {
        super(ItemType.TREASURE, position);
    }

    public int getTreasure() {
       return ThreadLocalRandom.current().nextInt(MIN_TREASURE, MAX_TREASURE + 1);
    }
}
