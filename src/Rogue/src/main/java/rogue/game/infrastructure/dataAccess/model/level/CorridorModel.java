package rogue.game.infrastructure.dataAccess.model.level;

import lombok.Getter;
import lombok.Setter;
import rogue.game.domain.entities.level.Corridor;
import rogue.game.domain.enums.CorridorType;
import rogue.game.infrastructure.dataAccess.model.PositionModel;

import java.util.List;

/**
 * Represents the model for the {@link Corridor} entity.
 */
@Setter
@Getter
public class CorridorModel {
    private CorridorType type;
    private List<PositionModel> points;
}
