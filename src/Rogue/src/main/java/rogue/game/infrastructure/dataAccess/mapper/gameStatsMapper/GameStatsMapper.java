package rogue.game.infrastructure.dataAccess.mapper.gameStatsMapper;

import org.mapstruct.Mapper;
import rogue.game.domain.entities.GameStats;
import rogue.game.infrastructure.dataAccess.model.GameStatsModel;

/**
 * Mapper for transforming {@link GameStats} and {@link GameStatsModel} objects.
 *
 * <p> Uses the {@link org.mapstruct.Mapper} library to generate the mapping code.
 */
@Mapper
public interface GameStatsMapper {
    GameStatsModel toModel(GameStats gameStats);
    GameStats toEntity(GameStatsModel gameStatsModel);
}
