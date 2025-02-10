package rogue.game.infrastructure.dataAccess.mapper.modelEntityMapper.impl.enemies;

import rogue.game.domain.entities.GameEntity;
import rogue.game.domain.entities.enemies.Zombie;
import rogue.game.domain.enums.EnemyType;
import rogue.game.domain.factories.EnemyBuilderFactory;
import rogue.game.infrastructure.dataAccess.mapper.MapperRegistry;
import rogue.game.infrastructure.dataAccess.mapper.modelEntityMapper.ModelEntityMapper;
import rogue.game.infrastructure.dataAccess.mapper.positionMapper.PositionMapper;
import rogue.game.infrastructure.dataAccess.model.entities.enemies.ZombieModel;
import rogue.game.infrastructure.dataAccess.model.entities.GameEntityModel;

/**
 * Mapper for converting between {@link Zombie} and {@link ZombieModel} objects.
 *
 * <p> This class implements the `{@link ModelEntityMapper} interface and
 * provides methods to map a {@link Zombie} entity to a {@link ZombieModel} DTO and vice versa.
 * <p> It utilizes the {@link EnemyBuilderFactory} to create the {@link Zombie} entity instance
 * in the {@code toEntity} method.
 */
public class ZombieMapper implements ModelEntityMapper {
    @Override
    public GameEntityModel toModel(GameEntity entity) {
        ZombieModel zombieModel = null;

        if ( entity instanceof Zombie zombie ) {
            PositionMapper positionMapper = MapperRegistry.getMapper(PositionMapper.class);

            zombieModel = new ZombieModel();
            zombieModel.setPosition(positionMapper.toModel(zombie.getPosition()));

            zombieModel.setDamageType(zombie.getDamageType());
            zombieModel.setHealth(zombie.getHealth());
            zombieModel.setAgility(zombie.getAgility());
            zombieModel.setStrength(zombie.getStrength());
            zombieModel.setHostility(zombie.getHostility());
        }

        return zombieModel;
    }

    @Override
    public GameEntity toEntity(GameEntityModel model) {
        GameEntity zombie = null;

        if ( model instanceof ZombieModel zombieModel ) {
            PositionMapper positionMapper = MapperRegistry.getMapper(PositionMapper.class);

            zombie = EnemyBuilderFactory.createTypedEnemyBuilder()
                    .withType(EnemyType.ZOMBIE)
                    .withDamageType(zombieModel.getDamageType())
                    .withHealth(zombieModel.getHealth())
                    .withAgility(zombieModel.getAgility())
                    .withStrength(zombieModel.getStrength())
                    .withHostility(zombieModel.getHostility())
                    .withPosition(positionMapper.toEntity(zombieModel.getPosition()))
                    .build();
        }

        return zombie;
    }
}
