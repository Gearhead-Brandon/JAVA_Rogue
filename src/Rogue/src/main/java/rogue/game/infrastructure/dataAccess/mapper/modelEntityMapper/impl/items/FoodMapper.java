package rogue.game.infrastructure.dataAccess.mapper.modelEntityMapper.impl.items;

import rogue.game.domain.entities.GameEntity;
import rogue.game.domain.entities.items.Food;
import rogue.game.domain.enums.ItemType;
import rogue.game.domain.factories.ItemBuilderFactory;
import rogue.game.infrastructure.dataAccess.mapper.MapperRegistry;
import rogue.game.infrastructure.dataAccess.mapper.modelEntityMapper.ModelEntityMapper;
import rogue.game.infrastructure.dataAccess.mapper.positionMapper.PositionMapper;
import rogue.game.infrastructure.dataAccess.model.entities.GameEntityModel;
import rogue.game.infrastructure.dataAccess.model.entities.items.FoodModel;

/**
 * Mapper for converting between {@link Food} and {@link FoodModel} objects.
 *
 * <p> This class implements the {@link ModelEntityMapper} interface and
 * provides methods to map a {@link Food} entity to a {@link FoodModel} DTO and vice versa.
 * <p> It utilizes the {@link ItemBuilderFactory} to create the necessary {@link GameEntity}
 * instance in the {@code toEntity} method.
 */
public class FoodMapper implements ModelEntityMapper {
    @Override
    public GameEntityModel toModel(GameEntity entity) {
        FoodModel foodModel = null;

        if(entity instanceof Food food) {
            PositionMapper positionMapper = MapperRegistry.getMapper(PositionMapper.class);

            foodModel = new FoodModel();
            foodModel.setPosition(positionMapper.toModel(food.getPosition()));
        }

        return foodModel;
    }

    @Override
    public GameEntity toEntity(GameEntityModel model) {
        GameEntity food = null;

        if ( model instanceof FoodModel foodModel ) {
            PositionMapper positionMapper = MapperRegistry.getMapper(PositionMapper.class);

            food = ItemBuilderFactory.createBaseItemBuilder()
                    .withType(ItemType.FOOD)
                    .withPosition(positionMapper.toEntity(foodModel.getPosition()))
                    .build();
        }

        return food;
    }
}
