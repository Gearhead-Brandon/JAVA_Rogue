package rogue.game.infrastructure.dataAccess.model.entities;

import lombok.NoArgsConstructor;
import rogue.game.domain.entities.Portal;
import rogue.game.infrastructure.dataAccess.model.PositionModel;

/**
 * Represents the model for the {@link Portal} entity.
 */
@NoArgsConstructor
public class PortalModel extends GameEntityModel {
    public PortalModel(PositionModel position) {
        super(position);
    }
}
