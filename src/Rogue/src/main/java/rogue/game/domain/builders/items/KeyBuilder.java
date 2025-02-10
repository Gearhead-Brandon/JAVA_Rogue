package rogue.game.domain.builders.items;

import rogue.game.common.enums.MapColor;
import rogue.game.domain.builders.BaseEntityBuilder;
import rogue.game.domain.entities.GameEntity;
import rogue.game.domain.entities.items.Key;

/**
 * Builder class for creating {@link Key} with a specific color.
 *
 * <p> This class allows you to set the color of the {@link Key} before building it,
 * providing flexibility in key creation.
 */
public class KeyBuilder extends BaseEntityBuilder {
    private MapColor color = MapColor.WHITE;

    public KeyBuilder withColor(MapColor color) {
        this.color = color;
        return this;
    }

    @Override
    public GameEntity build() {
        return new Key(color, super.position);
    }
}
