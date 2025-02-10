package rogue.game.domain.entities.items;

import lombok.Getter;
import rogue.game.common.enums.MapColor;
import rogue.game.domain.enums.ItemType;
import rogue.game.domain.entities.Position;
import rogue.game.domain.entities.level.Door;

/**
 * Represents a key element in the game world.
 *
 * <p> Keys are used to open certain {@link Door}. Each key has a {@link MapColor} that determines which doors it can open.
 */
@Getter
public class Key extends Item {
    private final MapColor color;

    public Key(MapColor color, Position position) {
        super(ItemType.KEY, position);
        this.color = color;
    }

    @Override
    public String getName() {
        final String colorName = color.toString();
        return colorName.substring(0, 1).toUpperCase() + colorName.substring(1).toLowerCase() + " key";
    }
}
