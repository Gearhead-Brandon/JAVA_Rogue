package rogue.game.infrastructure.dataAccess.mapper.modelEntityMapper.impl.items;

import rogue.game.domain.entities.GameEntity;
import rogue.game.domain.entities.items.Scroll;
import rogue.game.domain.factories.ItemBuilderFactory;
import rogue.game.infrastructure.dataAccess.mapper.MapperRegistry;
import rogue.game.infrastructure.dataAccess.mapper.modelEntityMapper.ModelEntityMapper;
import rogue.game.infrastructure.dataAccess.mapper.positionMapper.PositionMapper;
import rogue.game.infrastructure.dataAccess.model.entities.GameEntityModel;
import rogue.game.infrastructure.dataAccess.model.entities.items.ScrollModel;

/**
 * Mapper for converting between {@link Scroll} and {@link ScrollModel} objects.
 *
 * <p> This class implements the {@link ModelEntityMapper} interface and
 * provides methods to map a {@link Scroll} entity to a {@link ScrollModel} DTO and vice versa.
 * <p> It utilizes the {@link ItemBuilderFactory} to create the {@link Scroll} entity instance
 * in the {@code toEntity} method.
 */
public class ScrollMapper implements ModelEntityMapper {
    @Override
    public GameEntityModel toModel(GameEntity entity) {
        ScrollModel model = null;

        if(entity instanceof Scroll scroll) {
            PositionMapper positionMapper = MapperRegistry.getMapper(PositionMapper.class);

            model = new ScrollModel();
            model.setPosition(positionMapper.toModel(scroll.getPosition()));
            model.setAttribute(scroll.getAttribute());
        }

        return model;
    }

    @Override
    public GameEntity toEntity(GameEntityModel model) {
        GameEntity scroll = null;

        if(model instanceof ScrollModel scrollModel) {
            PositionMapper positionMapper = MapperRegistry.getMapper(PositionMapper.class);

            scroll = ItemBuilderFactory.createScrollBuilder()
                        .withAttribute(scrollModel.getAttribute())
                        .withPosition(positionMapper.toEntity(scrollModel.getPosition()))
                        .build();
        }

        return scroll;
    }
}
