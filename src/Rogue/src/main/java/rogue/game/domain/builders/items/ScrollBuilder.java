package rogue.game.domain.builders.items;

import rogue.game.domain.builders.BaseEntityBuilder;
import rogue.game.domain.entities.GameEntity;
import rogue.game.domain.entities.items.Scroll;
import rogue.game.domain.enums.EntityCharacteristic;

/**
 * Builder class for creating {@link Scroll} that affect a specific {@link EntityCharacteristic}.
 *
 * <p> This class allows you to set the attribute that the scroll will modify before building it.
 */
public class ScrollBuilder extends BaseEntityBuilder {
    private EntityCharacteristic attribute;

    public ScrollBuilder withAttribute(EntityCharacteristic attribute) {
        this.attribute = attribute;
        return this;
    }

    @Override
    public GameEntity build() {
        return new Scroll(attribute, super.position);
    }
}
