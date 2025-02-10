package rogue.game.domain.entities.items;

import lombok.Getter;
import rogue.game.domain.enums.EntityCharacteristic;
import rogue.game.domain.enums.ItemType;
import rogue.game.domain.entities.Position;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Represents a scroll item in the game world.
 *
 * <p> Scrolls can be picked up by the player to permanently improve a specific {@link EntityCharacteristic}.
 */
@Getter
public class Scroll extends Item {
    private static final int MAX_IMPROVEMENT = 6;

    private final EntityCharacteristic attribute;

    public Scroll(Position position) {
        super(ItemType.SCROLL, position);
        attribute = getRandomAttribute();
    }

    public Scroll(EntityCharacteristic attribute, Position position) {
        super(ItemType.SCROLL, position);

        if (attribute == null)
            this.attribute = getRandomAttribute();
        else
            this.attribute = attribute;
    }

    private EntityCharacteristic getRandomAttribute() {
        return EntityCharacteristic.values()[ThreadLocalRandom.current().nextInt(EntityCharacteristic.values().length - 1)];
    }

    @Override
    public String getName() {
        return attribute.name;
    }

    public int getImprovement() {
        return ThreadLocalRandom.current().nextInt(1, attribute == EntityCharacteristic.AGILITY ? 3 : MAX_IMPROVEMENT) + 1;
    }
}
