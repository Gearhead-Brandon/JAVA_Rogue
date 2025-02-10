package rogue.game.infrastructure.dataAccess.mapper.modelEntityMapper.impl.items;

import rogue.game.domain.entities.GameEntity;
import rogue.game.domain.entities.items.Potion;
import rogue.game.domain.factories.ItemBuilderFactory;
import rogue.game.infrastructure.dataAccess.mapper.MapperRegistry;
import rogue.game.infrastructure.dataAccess.mapper.modelEntityMapper.ModelEntityMapper;
import rogue.game.infrastructure.dataAccess.mapper.positionMapper.PositionMapper;
import rogue.game.infrastructure.dataAccess.model.entities.GameEntityModel;
import rogue.game.infrastructure.dataAccess.model.entities.items.PotionModel;

/**
 * Mapper for converting between {@link Potion} and {@link PotionModel} objects.
 *
 * <p> This class implements the {@link ModelEntityMapper} interface and provides methods
 * to map a {@link Potion} entity to a {@link PotionModel} DTO and vice versa.
 * <p> It utilizes the {@link ItemBuilderFactory} to create the {@link Potion} entity instance
 * in the {@code toEntity} method.
 */
public class PotionMapper implements ModelEntityMapper {
    @Override
    public GameEntityModel toModel(GameEntity entity) {
        PotionModel model = null;

        if(entity instanceof Potion potion) {
            PositionMapper positionMapper = MapperRegistry.getMapper(PositionMapper.class);

            model = new PotionModel();
            model.setPosition( positionMapper.toModel(potion.getPosition()) );
            model.setImprovement( potion.getImprovement() );
            model.setAttribute( potion.getAttribute() );
            model.setTime( potion.getTime() + 1 );
        }

        return model;
    }

    @Override
    public GameEntity toEntity(GameEntityModel model) {
        GameEntity potion = null;

        if ( model instanceof PotionModel potionModel ) {
            PositionMapper positionMapper = MapperRegistry.getMapper(PositionMapper.class);

            potion = ItemBuilderFactory.createPotionBuilder()
                        .withAttribute(potionModel.getAttribute())
                        .withImprovement(potionModel.getImprovement())
                        .withTime(potionModel.getTime())
                        .withPosition(positionMapper.toEntity(potionModel.getPosition()))
                        .build();
        }

        return potion;
    }
}
