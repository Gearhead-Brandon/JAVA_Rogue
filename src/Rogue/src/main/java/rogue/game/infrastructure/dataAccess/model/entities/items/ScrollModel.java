package rogue.game.infrastructure.dataAccess.model.entities.items;

import lombok.Getter;
import lombok.Setter;
import rogue.game.domain.entities.items.Scroll;
import rogue.game.domain.enums.EntityCharacteristic;
import rogue.game.infrastructure.dataAccess.model.entities.GameEntityModel;

/**
 * Represents the model for the {@link Scroll} entity.
 */
@Getter
@Setter
public class ScrollModel extends GameEntityModel {
    private EntityCharacteristic attribute;
}
