package rogue.game.infrastructure.dataAccess.mapper.modelEntityMapper.impl.enemies;

import rogue.game.domain.entities.GameEntity;
import rogue.game.domain.entities.enemies.Ghost;
import rogue.game.domain.enums.EnemyType;
import rogue.game.domain.factories.EnemyBuilderFactory;
import rogue.game.infrastructure.dataAccess.mapper.MapperRegistry;
import rogue.game.infrastructure.dataAccess.mapper.modelEntityMapper.ModelEntityMapper;
import rogue.game.infrastructure.dataAccess.mapper.positionMapper.PositionMapper;
import rogue.game.infrastructure.dataAccess.model.entities.enemies.GhostModel;
import rogue.game.infrastructure.dataAccess.model.entities.GameEntityModel;

/**
 * Mapper for converting between {@link Ghost} and {@link GhostModel} objects.
 *
 * <p> This class implements the `{@link ModelEntityMapper} interface and
 * provides methods to map a {@link Ghost} entity to a {@link GhostModel} DTO and vice versa.
 * <p> It utilizes the {@link EnemyBuilderFactory} to create the {@link Ghost} entity instance
 * in the {@code toEntity} method.
 */
public class GhostMapper implements ModelEntityMapper {
    @Override
    public GameEntityModel toModel(GameEntity entity) {
        GhostModel ghostModel = null;

        if ( entity instanceof Ghost ghost ) {
            PositionMapper positionMapper = MapperRegistry.getMapper(PositionMapper.class);

            ghostModel = new GhostModel();
            ghostModel.setPosition(positionMapper.toModel(ghost.getPosition()));
            ghostModel.setDamageType(ghost.getDamageType());
            ghostModel.setHealth(ghost.getHealth());
            ghostModel.setAgility(ghost.getAgility());
            ghostModel.setStrength(ghost.getStrength());
            ghostModel.setHostility(ghost.getHostility());
        }

        return ghostModel;
    }

    @Override
    public GameEntity toEntity(GameEntityModel model) {
        GameEntity ghost = null;

        if ( model instanceof GhostModel ghostModel ) {
            PositionMapper positionMapper = MapperRegistry.getMapper(PositionMapper.class);

            ghost = EnemyBuilderFactory.createTypedEnemyBuilder()
                    .withType(EnemyType.GHOST)
                    .withDamageType(ghostModel.getDamageType())
                    .withHealth(ghostModel.getHealth())
                    .withAgility(ghostModel.getAgility())
                    .withStrength(ghostModel.getStrength())
                    .withHostility(ghostModel.getHostility())
                    .withPosition(positionMapper.toEntity(ghostModel.getPosition()))
                    .build();
        }

        return ghost;
    }
}
