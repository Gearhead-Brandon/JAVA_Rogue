package rogue.game.infrastructure.dataAccess.model.entities.items;

import lombok.Getter;
import lombok.Setter;
import rogue.game.domain.entities.items.Potion;
import rogue.game.domain.enums.EntityCharacteristic;
import rogue.game.infrastructure.dataAccess.model.entities.GameEntityModel;

/**
 * Represents the model for the {@link Potion} entity.
 */
@Getter
@Setter
public class PotionModel extends GameEntityModel {
    private EntityCharacteristic attribute;
    private int improvement;
    private int time;
}
