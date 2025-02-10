package rogue.game.infrastructure.dataAccess.mapper.modelEntityMapper.impl.enemies;

import rogue.game.domain.entities.GameEntity;
import rogue.game.domain.entities.enemies.Vampire;
import rogue.game.domain.enums.EnemyType;
import rogue.game.domain.factories.EnemyBuilderFactory;
import rogue.game.infrastructure.dataAccess.mapper.MapperRegistry;
import rogue.game.infrastructure.dataAccess.mapper.modelEntityMapper.ModelEntityMapper;
import rogue.game.infrastructure.dataAccess.mapper.positionMapper.PositionMapper;
import rogue.game.infrastructure.dataAccess.model.entities.enemies.VampireModel;
import rogue.game.infrastructure.dataAccess.model.entities.GameEntityModel;

/**
 * Mapper for converting between {@link Vampire} and {@link VampireModel} objects.
 *
 * <p> This class implements the `{@link ModelEntityMapper} interface and
 * provides methods to map a {@link Vampire} entity to a {@link VampireModel} DTO and vice versa.
 * <p> It utilizes the {@link EnemyBuilderFactory} to create the {@link Vampire} entity instance
 * in the {@code toEntity} method.
 */
public class VampireMapper implements ModelEntityMapper {
    @Override
    public GameEntityModel toModel(GameEntity entity) {
        VampireModel vampireModel = null;

        if ( entity instanceof Vampire vampire ) {
            PositionMapper positionMapper = MapperRegistry.getMapper(PositionMapper.class);

            vampireModel = new VampireModel();
            vampireModel.setPosition(positionMapper.toModel(vampire.getPosition()));

            vampireModel.setDamageType(vampire.getDamageType());
            vampireModel.setHealth(vampire.getHealth());
            vampireModel.setAgility(vampire.getAgility());
            vampireModel.setStrength(vampire.getStrength());
            vampireModel.setHostility(vampire.getHostility());
        }

        return vampireModel;
    }

    @Override
    public GameEntity toEntity(GameEntityModel model) {
        GameEntity vampire = null;

        if ( model instanceof VampireModel vampireModel ) {
            PositionMapper positionMapper = MapperRegistry.getMapper(PositionMapper.class);

            vampire = EnemyBuilderFactory.createTypedEnemyBuilder()
                    .withType(EnemyType.VAMPIRE)
                    .withDamageType(vampireModel.getDamageType())
                    .withHealth(vampireModel.getHealth())
                    .withAgility(vampireModel.getAgility())
                    .withStrength(vampireModel.getStrength())
                    .withHostility(vampireModel.getHostility())
                    .withPosition(positionMapper.toEntity(vampireModel.getPosition()))
                    .build();
        }

        return vampire;
    }
}
