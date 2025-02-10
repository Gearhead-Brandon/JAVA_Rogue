package rogue.game.domain.builders.items;

import rogue.game.domain.builders.BaseEntityBuilder;
import rogue.game.domain.entities.GameEntity;
import rogue.game.domain.entities.items.Potion;
import rogue.game.domain.enums.EntityCharacteristic;

/**
 * Builder class for creating {@link Potion} with specific {@link EntityCharacteristic},
 * improvements, and duration.
 *
 * <p> This class allows you to customize the potion's effects and how long they last.
 */
public class PotionBuilder extends BaseEntityBuilder {
    private int improvement;
    private EntityCharacteristic attribute;
    private int time;

    public PotionBuilder withImprovement(int improvement) {
        this.improvement = improvement;
        return this;
    }

    public PotionBuilder withAttribute(EntityCharacteristic attribute) {
        this.attribute = attribute;
        return this;
    }

    public PotionBuilder withTime(int time) {
        this.time = time;
        return this;
    }

    @Override
    public GameEntity build() {
        return new Potion(attribute, improvement, time, position);
    }
}
