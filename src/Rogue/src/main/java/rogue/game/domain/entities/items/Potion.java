package rogue.game.domain.entities.items;

import lombok.Getter;
import rogue.game.domain.enums.EntityCharacteristic;
import rogue.game.domain.enums.ItemType;
import rogue.game.domain.entities.Position;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Represents a potion {@link Item} in the game world.
 *
 * <p> Potions can be picked up by the player to temporarily improve specific {@link EntityCharacteristic}.
 */
public class Potion extends Item {
    private static final List<EntityCharacteristic> usedAttributes = List.of(
        EntityCharacteristic.AGILITY,
        EntityCharacteristic.STRENGTH,
        EntityCharacteristic.MAX_HEALTH
    );

    private static final int MAX_IMP = 6;
    private static final int MIN_TIME = 10;
    private static final int MAX_TIME = 18;

    @Getter
    private final int improvement;
    @Getter
    private final EntityCharacteristic attribute;
    private int time;

    public Potion(Position position) {
        super(ItemType.POTION, position);
        attribute = usedAttributes.get(ThreadLocalRandom.current().nextInt(usedAttributes.size()));
        improvement = ThreadLocalRandom.current().nextInt(1, attribute == EntityCharacteristic.AGILITY ? 3 : MAX_IMP) + 1;
        time = ThreadLocalRandom.current().nextInt(MIN_TIME, MAX_TIME + 1) + 2;
    }

    public Potion(EntityCharacteristic attribute, int improvement, int time, Position position) {
        super(ItemType.POTION, position);

        if(attribute == null)
            this.attribute = usedAttributes.get(ThreadLocalRandom.current().nextInt(usedAttributes.size()));
        else
            this.attribute = attribute;

        if (improvement < 1 || improvement > MAX_IMP)
            this.improvement = ThreadLocalRandom.current().nextInt(1, attribute == EntityCharacteristic.AGILITY ? 3 : MAX_IMP) + 1;
        else
            this.improvement = improvement;

        if (time < MIN_TIME || time > MAX_TIME)
            this.time = ThreadLocalRandom.current().nextInt(MIN_TIME, MAX_TIME + 1) + 2;
        else
            this.time = time;
    }

    @Override
    public String getName() {
        return attribute.name;
    }

    public int getTime() {
        return --time;
    }
}
