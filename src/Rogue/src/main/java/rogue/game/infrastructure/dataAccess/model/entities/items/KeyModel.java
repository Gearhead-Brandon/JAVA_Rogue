package rogue.game.infrastructure.dataAccess.model.entities.items;

import lombok.Getter;
import lombok.Setter;
import rogue.game.common.enums.MapColor;
import rogue.game.domain.entities.items.Key;
import rogue.game.infrastructure.dataAccess.model.entities.GameEntityModel;

/**
 * Represents the model for the {@link Key} entity.
 */
@Getter
@Setter
public class KeyModel extends GameEntityModel {
    private MapColor color;
}
