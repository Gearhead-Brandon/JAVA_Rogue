package rogue.game.infrastructure.dataAccess.mapper.modelEntityMapper.impl;

import rogue.game.domain.entities.Portal;
import rogue.game.domain.entities.GameEntity;
import rogue.game.infrastructure.dataAccess.mapper.MapperRegistry;
import rogue.game.infrastructure.dataAccess.mapper.modelEntityMapper.ModelEntityMapper;
import rogue.game.infrastructure.dataAccess.mapper.positionMapper.PositionMapper;
import rogue.game.infrastructure.dataAccess.model.entities.PortalModel;
import rogue.game.infrastructure.dataAccess.model.entities.GameEntityModel;

/**
 * Mapper for converting between {@link Portal} and {@link PortalModel} objects.
 *
 * <p> This class implements the `ModelEntityMapper` interface and provides methods
 * to map a {@link Portal} entity to a {@link PortalModel} DTO and vice versa.
 */
public class PortalMapper implements ModelEntityMapper {
    @Override
    public GameEntityModel toModel(GameEntity entity) {
        PortalModel portalModel = null;

        if ( entity instanceof Portal portal ) {
            PositionMapper positionMapper = MapperRegistry.getMapper(PositionMapper.class);
            portalModel = new PortalModel(positionMapper.toModel(portal.getPosition()));
        }

        return portalModel;
    }

    @Override
    public GameEntity toEntity(GameEntityModel model) {
        Portal portal = null;

        if ( model instanceof PortalModel portalModel ) {
            PositionMapper positionMapper = MapperRegistry.getMapper(PositionMapper.class);
            portal = new Portal(positionMapper.toEntity(portalModel.getPosition()));
        }

        return portal;
    }
}
