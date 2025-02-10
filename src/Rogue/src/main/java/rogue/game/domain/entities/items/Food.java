package rogue.game.domain.entities.items;

import rogue.game.domain.enums.ItemType;
import rogue.game.domain.entities.Position;
/**
 * Represents a food item in the game world.
 *
 * <p> Food items can be picked up by the player to restore health.
 */
public class Food extends Item {
    private static final int DEFAULT_HEALTH = 8;

    public Food(Position position) {
        super(ItemType.FOOD, position);
    }

    /**
     * @return The amount of health restored when the food item is consumed.
     */
    public int getHealth() {
        return DEFAULT_HEALTH;
    }
}
