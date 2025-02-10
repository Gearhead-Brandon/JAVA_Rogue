package rogue.game.infrastructure.dataAccess.model.entities.enemies;

import lombok.Getter;
import lombok.Setter;
import rogue.game.common.enums.MapSymbol;
import rogue.game.domain.entities.enemies.Mimic;

/**
 * Represents the model for the {@link Mimic} entity.
 */
@Getter
@Setter
public class MimicModel extends EnemyModel {
    private MapSymbol appearance;
}
