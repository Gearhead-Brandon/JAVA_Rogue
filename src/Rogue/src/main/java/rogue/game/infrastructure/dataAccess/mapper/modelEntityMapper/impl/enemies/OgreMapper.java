package rogue.game.infrastructure.dataAccess.mapper.modelEntityMapper.impl.enemies;

import rogue.game.domain.entities.GameEntity;
import rogue.game.domain.entities.enemies.Ogre;
import rogue.game.domain.enums.EnemyType;
import rogue.game.domain.factories.EnemyBuilderFactory;
import rogue.game.infrastructure.dataAccess.mapper.MapperRegistry;
import rogue.game.infrastructure.dataAccess.mapper.modelEntityMapper.ModelEntityMapper;
import rogue.game.infrastructure.dataAccess.mapper.positionMapper.PositionMapper;
import rogue.game.infrastructure.dataAccess.model.entities.enemies.OgreModel;
import rogue.game.infrastructure.dataAccess.model.entities.GameEntityModel;

/**
 * Mapper for converting between {@link Ogre} and {@link OgreModel} objects.
 *
 * <p> This class implements the `{@link ModelEntityMapper} interface and
 * provides methods to map a {@link Ogre} entity to a {@link OgreModel} DTO and vice versa.
 * <p> It utilizes the {@link EnemyBuilderFactory} to create the {@link Ogre} entity instance
 * in the {@code toEntity} method.
 */
public class OgreMapper implements ModelEntityMapper {
    @Override
    public GameEntityModel toModel(GameEntity entity) {
        OgreModel ogreModel = null;

        if ( entity instanceof Ogre ogre ) {
            PositionMapper positionMapper = MapperRegistry.getMapper(PositionMapper.class);

            ogreModel = new OgreModel();
            ogreModel.setPosition(positionMapper.toModel(ogre.getPosition()));

            ogreModel.setDamageType(ogre.getDamageType());
            ogreModel.setHealth(ogre.getHealth());
            ogreModel.setAgility(ogre.getAgility());
            ogreModel.setStrength(ogre.getStrength());
            ogreModel.setHostility(ogre.getHostility());
        }

        return ogreModel;
    }

    @Override
    public GameEntity toEntity(GameEntityModel model) {
        GameEntity ogre = null;

        if ( model instanceof OgreModel ogreModel ) {
            PositionMapper positionMapper = MapperRegistry.getMapper(PositionMapper.class);

            ogre = EnemyBuilderFactory.createTypedEnemyBuilder()
                    .withType(EnemyType.OGRE)
                    .withDamageType(ogreModel.getDamageType())
                    .withHealth(ogreModel.getHealth())
                    .withAgility(ogreModel.getAgility())
                    .withStrength(ogreModel.getStrength())
                    .withHostility(ogreModel.getHostility())
                    .withPosition(positionMapper.toEntity(ogreModel.getPosition()))
                    .build();
        }

        return ogre;
    }
}
