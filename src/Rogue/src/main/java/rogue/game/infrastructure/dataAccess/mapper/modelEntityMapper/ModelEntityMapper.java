package rogue.game.infrastructure.dataAccess.mapper.modelEntityMapper;

import rogue.game.domain.entities.GameEntity;
import rogue.game.infrastructure.dataAccess.model.entities.GameEntityModel;

/**
 * Interface for converting game entities between an internal representation ({@link GameEntity})
 * and a model representation for external systems ({@link GameEntityModel}).
 *
 * <p> Implementations of this interface must provide methods for converting
 * objects between these two representations. This allows the logic of the game to be separated
 * from its visual representation or other external systems.
 */
public interface ModelEntityMapper {
    GameEntityModel toModel(GameEntity entity);
    GameEntity toEntity(GameEntityModel model);
}
