package rogue.game.infrastructure.dataAccess.mapper.modelEntityMapper.impl.enemies;

import rogue.game.domain.entities.GameEntity;
import rogue.game.domain.entities.enemies.Mimic;
import rogue.game.domain.factories.EnemyBuilderFactory;
import rogue.game.infrastructure.dataAccess.mapper.MapperRegistry;
import rogue.game.infrastructure.dataAccess.mapper.modelEntityMapper.ModelEntityMapper;
import rogue.game.infrastructure.dataAccess.mapper.positionMapper.PositionMapper;
import rogue.game.infrastructure.dataAccess.model.entities.enemies.MimicModel;
import rogue.game.infrastructure.dataAccess.model.entities.GameEntityModel;

/**
 * Mapper for converting between {@link Mimic} and {@link MimicModel} objects.
 *
 * <p> This class implements the `{@link ModelEntityMapper} interface and
 * provides methods to map a {@link Mimic} entity to a {@link MimicModel} DTO and vice versa.
 * <p> It utilizes the {@link EnemyBuilderFactory} to create the {@link Mimic} entity instance
 * in the {@code toEntity} method.
 */
public class MimicMapper implements ModelEntityMapper {
    @Override
    public GameEntityModel toModel(GameEntity entity) {
        MimicModel mimicModel = null;

        if (entity instanceof Mimic mimic) {
            PositionMapper positionMapper = MapperRegistry.getMapper(PositionMapper.class);

            mimicModel = new MimicModel();
            mimicModel.setPosition(positionMapper.toModel(mimic.getPosition()));

            mimicModel.setDamageType(mimic.getDamageType());
            mimicModel.setHealth(mimic.getHealth());
            mimicModel.setAgility(mimic.getAgility());
            mimicModel.setStrength(mimic.getStrength());
            mimicModel.setHostility(mimic.getHostility());

            mimicModel.setAppearance(mimic.getAppearance());
        }

        return mimicModel;
    }

    @Override
    public GameEntity toEntity(GameEntityModel model) {
        GameEntity mimic = null;

        if (model instanceof MimicModel mimicModel) {
            PositionMapper positionMapper = MapperRegistry.getMapper(PositionMapper.class);

            mimic = EnemyBuilderFactory.createMimicBuilder()
                    .withAppearance(mimicModel.getAppearance())
                    .withDamageType(mimicModel.getDamageType())
                    .withHealth(mimicModel.getHealth())
                    .withAgility(mimicModel.getAgility())
                    .withStrength(mimicModel.getStrength())
                    .withHostility(mimicModel.getHostility())
                    .withPosition(positionMapper.toEntity(mimicModel.getPosition()))
                    .build();
        }

        return mimic;
    }
}
