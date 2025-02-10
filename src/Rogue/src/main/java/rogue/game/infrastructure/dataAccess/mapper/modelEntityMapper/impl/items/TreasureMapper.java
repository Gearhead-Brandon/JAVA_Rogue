package rogue.game.infrastructure.dataAccess.mapper.modelEntityMapper.impl.items;

import rogue.game.domain.entities.GameEntity;
import rogue.game.domain.entities.items.Treasure;
import rogue.game.domain.enums.ItemType;
import rogue.game.domain.factories.ItemBuilderFactory;
import rogue.game.infrastructure.dataAccess.mapper.MapperRegistry;
import rogue.game.infrastructure.dataAccess.mapper.modelEntityMapper.ModelEntityMapper;
import rogue.game.infrastructure.dataAccess.mapper.positionMapper.PositionMapper;
import rogue.game.infrastructure.dataAccess.model.entities.GameEntityModel;
import rogue.game.infrastructure.dataAccess.model.entities.items.TreasureModel;

/**
 * Mapper for converting between {@link Treasure} and {@link TreasureModel} objects.
 *
 * <p> This class implements the {@link ModelEntityMapper} interface and
 * provides methods to map a {@link Treasure} entity to a {@link TreasureModel} DTO and vice versa.
 * <p> It utilizes the {@link ItemBuilderFactory} to create the {@link Treasure} entity instance
 * in the {@code toEntity} method.
 */
public class TreasureMapper implements ModelEntityMapper {
    @Override
    public GameEntityModel toModel(GameEntity entity) {
        PositionMapper positionMapper = MapperRegistry.getMapper(PositionMapper.class);

        TreasureModel treasureModel = null;

        if(entity instanceof Treasure treasure) {
            treasureModel = new TreasureModel();
            treasureModel.setPosition(positionMapper.toModel(treasure.getPosition()));
        }

        return treasureModel;
    }

    @Override
    public GameEntity toEntity(GameEntityModel model) {
        GameEntity treasure = null;

        if(model instanceof TreasureModel treasureMode) {
            PositionMapper positionMapper = MapperRegistry.getMapper(PositionMapper.class);

            treasure = ItemBuilderFactory.createBaseItemBuilder()
                        .withType(ItemType.TREASURE)
                        .withPosition(positionMapper.toEntity(treasureMode.getPosition()))
                        .build();
        }

        return treasure;
    }
}
