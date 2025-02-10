package rogue.game.infrastructure.dataAccess.model.level;

import lombok.Getter;
import lombok.Setter;
import rogue.game.common.enums.MapColor;
import rogue.game.domain.entities.level.Door;
import rogue.game.infrastructure.dataAccess.model.PositionModel;

/**
 * Represents the model for the {@link Door} entity.
 */
@Getter
@Setter
public class DoorModel {
    private PositionModel position;
    private MapColor color;
    private boolean open;
}
