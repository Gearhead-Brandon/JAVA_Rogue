package rogue.game.infrastructure.dataAccess.mapper.modelEntityMapper.impl.items;

import rogue.game.domain.entities.GameEntity;
import rogue.game.domain.entities.items.Key;
import rogue.game.domain.factories.ItemBuilderFactory;
import rogue.game.infrastructure.dataAccess.mapper.MapperRegistry;
import rogue.game.infrastructure.dataAccess.mapper.modelEntityMapper.ModelEntityMapper;
import rogue.game.infrastructure.dataAccess.mapper.positionMapper.PositionMapper;
import rogue.game.infrastructure.dataAccess.model.entities.GameEntityModel;
import rogue.game.infrastructure.dataAccess.model.entities.items.KeyModel;

/**
 * Mapper for converting between {@link Key} and {@link KeyModel} objects.
 *
 * <p> This class implements the {@link ModelEntityMapper} interface and provides methods
 * to map a {@link Key} entity to a {@link KeyModel} DTO and vice versa.
 * <p> It utilizes the {@link ItemBuilderFactory} to create the {@link Key} entity instance
 * in the {@code toEntity} method.
 */
public class KeyMapper implements ModelEntityMapper {
    @Override
    public GameEntityModel toModel(GameEntity entity) {
        KeyModel keyModel = null;

        if(entity instanceof Key key) {
            PositionMapper positionMapper = MapperRegistry.getMapper(PositionMapper.class);

            keyModel = new KeyModel();
            keyModel.setPosition(positionMapper.toModel(key.getPosition()));
            keyModel.setColor(key.getColor());
        }

        return keyModel;
    }

    @Override
    public GameEntity toEntity(GameEntityModel model) {
        GameEntity key = null;

        if(model instanceof KeyModel keyModel) {
            PositionMapper positionMapper = MapperRegistry.getMapper(PositionMapper.class);

            key = ItemBuilderFactory.createKeyBuilder()
                    .withColor(keyModel.getColor())
                    .withPosition(positionMapper.toEntity(keyModel.getPosition()))
                    .build();
        }

        return key;
    }
}
