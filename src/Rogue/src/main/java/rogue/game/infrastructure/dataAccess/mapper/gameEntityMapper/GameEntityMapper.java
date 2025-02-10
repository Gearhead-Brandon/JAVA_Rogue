package rogue.game.infrastructure.dataAccess.mapper.gameEntityMapper;

import org.mapstruct.Mapper;
import org.mapstruct.Named;
import rogue.game.domain.entities.GameEntity;
import rogue.game.domain.entities.enemies.Enemy;
import rogue.game.infrastructure.dataAccess.mapper.modelEntityMapper.ModelEntityMapper;
import rogue.game.infrastructure.dataAccess.model.entities.enemies.EnemyModel;
import rogue.game.infrastructure.dataAccess.model.entities.GameEntityModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Mapper interface for converting between {@link GameEntity} and {@link GameEntityModel} objects.
 &
 * This interface provides methods to map various {@link GameEntity} (such as players, enemies, items)
 * to their corresponding {@link GameEntityModel} representations.
 * <p> It utilizes a {@link EntityMappersContainer} to dynamically retrieve the
 * appropriate {@link ModelEntityMapper} for a given entity type.
 */
@Mapper
public interface GameEntityMapper {
    /**
     * A container for managing {@link ModelEntityMapper}.
     */
    EntityMappersContainer container = new EntityMappersContainer();

    /**
     * Converts a {@link GameEntity} to a {@link GameEntityModel}.

     * This method dynamically retrieves the appropriate mapper for the given {@link GameEntity} type from the container
     * and delegates the conversion to that specific mapper.

     * @param entity The {@link GameEntity} to be converted.
     * @return The converted {@link GameEntityModel}, or null if the {@link GameEntity} type is not supported.
     */
    default GameEntityModel toModel(GameEntity entity){
        if ( entity == null ) {
            return null;
        }

        ModelEntityMapper mapper = container.getMapper(entity);

        return (mapper == null) ? null : mapper.toModel(entity);
    }

    /**
     * Converts a {@link GameEntityModel} to a {@link GameEntity}.

     * This method dynamically retrieves the appropriate mapper for the given {@link GameEntityModel} type from the container
     * and delegates the conversion to that specific mapper.

     * @param entity The {@link GameEntityModel} to be converted.
     * @return The converted {@link GameEntity}, or null if the {@link GameEntity} type is not supported.
     */
    default GameEntity toEntity(GameEntityModel entity){
        if ( entity == null ) {
            return null;
        }

        ModelEntityMapper mapper = container.getMapper(entity);

        return (mapper == null) ? null : mapper.toEntity(entity);
    }

    /**
     * Maps a {@link List} of {@link GameEntity} objects to a list of {@link GameEntityModel} objects.
     *
     * @param list The {@link List} of {@link GameEntity} objects to be converted.
     * @return The converted {@link List} of {@link GameEntityModel} objects.
     */
    @Named("toGameEntityModelList")
    List<GameEntityModel> toGameEntityModelList(List<GameEntity> list);


    /**
     * Maps a {@link List} of {@link Enemy} entities to a {@link List} of {@link EnemyModel} objects.

     * This method iterates over the {@link List} of {@link Enemy}, retrieves the appropriate mapper for each {@link Enemy},
     * and converts it to the corresponding {@link EnemyModel}.

     * @param list The {@link List} of {@link Enemy} entities to be converted.
     * @return The converted {@link List} of {@link EnemyModel} objects.
     */
    @Named("toEnemyModelsList")
    default List<EnemyModel> toEnemyModelsList(List<Enemy> list){
        if ( list == null ) {
            return null;
        }

        List<EnemyModel> modelList = new ArrayList<>( list.size() );

        for (Enemy enemy : list) {
            ModelEntityMapper mapper = container.getMapper( enemy );

            if( mapper != null ) {
                if (mapper.toModel(enemy) instanceof EnemyModel enemyModel)
                    modelList.add(enemyModel);
            }
        }

        return modelList;
    }
}
