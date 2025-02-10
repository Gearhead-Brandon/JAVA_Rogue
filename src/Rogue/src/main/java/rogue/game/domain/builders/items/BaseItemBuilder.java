package rogue.game.domain.builders.items;

import rogue.game.domain.builders.BaseEntityBuilder;
import rogue.game.domain.entities.GameEntity;
import rogue.game.domain.entities.items.*;
import rogue.game.domain.enums.ItemType;

/**
 * Base class for building different types of game items.
 *
 * <p> This class provides a foundation for creating specific item types
 * (e.g., {@link Food}, {@link Scroll}, {@link Potion}, {@link Treasure})
 */
public class BaseItemBuilder extends BaseEntityBuilder {
    private ItemType type = ItemType.TREASURE;

    public BaseItemBuilder withType(ItemType type) {
        this.type = type;
        return this;
    }

    @Override
    public GameEntity build(){
        return switch (type) {
            case ItemType.FOOD -> new Food(super.position);
            case ItemType.SCROLL -> new Scroll(super.position);
            case ItemType.POTION -> new Potion(super.position);
            default -> new Treasure(super.position);
        };
    }
}
