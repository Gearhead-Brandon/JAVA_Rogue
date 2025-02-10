package rogue.game.domain.entities.items;

import lombok.Getter;
import lombok.Setter;
import rogue.game.domain.entities.GameEntity;
import rogue.game.domain.enums.ItemType;
import rogue.game.domain.entities.Position;

/**
 * Abstract class representing an item in the game world.
 *
 * <p> Provides a base implementation for items, including their {@link ItemType}.
 */
@Getter
@Setter
public abstract class Item extends GameEntity {
    private final ItemType itemType;

    public Item(ItemType itemType, Position position) {
        super(position);
        this.itemType = itemType;
    }

    /**
     * @return The name of the item based on its {@link ItemType}.
     */
    public String getName() {
        return itemType.toString();
    }
}
