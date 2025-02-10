package rogue.game.infrastructure.dataAccess.mapper.modelEntityMapper.impl.enemies;

import rogue.game.domain.entities.GameEntity;
import rogue.game.domain.entities.enemies.SnakeMagician;
import rogue.game.domain.enums.EnemyType;
import rogue.game.domain.factories.EnemyBuilderFactory;
import rogue.game.infrastructure.dataAccess.mapper.MapperRegistry;
import rogue.game.infrastructure.dataAccess.mapper.modelEntityMapper.ModelEntityMapper;
import rogue.game.infrastructure.dataAccess.mapper.positionMapper.PositionMapper;
import rogue.game.infrastructure.dataAccess.model.entities.enemies.SnakeMagicianModel;
import rogue.game.infrastructure.dataAccess.model.entities.GameEntityModel;

/**
 * Mapper for converting between {@link SnakeMagician} and {@link SnakeMagicianModel} objects.
 *
 * <p> This class implements the `{@link ModelEntityMapper} interface and
 * provides methods to map a {@link SnakeMagician} entity to a {@link SnakeMagicianModel} DTO and vice versa.
 * <p> It utilizes the {@link EnemyBuilderFactory} to create the {@link SnakeMagician} entity instance
 * in the {@code toEntity} method.
 */
public class SnakeMagicianMapper implements ModelEntityMapper {
    @Override
    public GameEntityModel toModel(GameEntity entity) {
        SnakeMagicianModel snakeMagicianModel = null;

        if ( entity instanceof SnakeMagician snakeMagician ) {
            PositionMapper positionMapper = MapperRegistry.getMapper(PositionMapper.class);

            snakeMagicianModel = new SnakeMagicianModel();
            snakeMagicianModel.setPosition(positionMapper.toModel(snakeMagician.getPosition()));

            snakeMagicianModel.setDamageType(snakeMagician.getDamageType());
            snakeMagicianModel.setHealth(snakeMagician.getHealth());
            snakeMagicianModel.setAgility(snakeMagician.getAgility());
            snakeMagicianModel.setStrength(snakeMagician.getStrength());
            snakeMagicianModel.setHostility(snakeMagician.getHostility());
        }

        return snakeMagicianModel;
    }

    @Override
    public GameEntity toEntity(GameEntityModel model) {
        GameEntity snakeMagician = null;

        if ( model instanceof SnakeMagicianModel snakeMagicianModel ) {
            PositionMapper positionMapper = MapperRegistry.getMapper(PositionMapper.class);

            snakeMagician = EnemyBuilderFactory.createTypedEnemyBuilder()
                    .withType(EnemyType.SNAKE_MAGICIAN)
                    .withDamageType(snakeMagicianModel.getDamageType())
                    .withHealth(snakeMagicianModel.getHealth())
                    .withAgility(snakeMagicianModel.getAgility())
                    .withStrength(snakeMagicianModel.getStrength())
                    .withHostility(snakeMagicianModel.getHostility())
                    .withPosition(positionMapper.toEntity(snakeMagicianModel.getPosition()))
                    .build();
        }

        return snakeMagician;
    }
}
