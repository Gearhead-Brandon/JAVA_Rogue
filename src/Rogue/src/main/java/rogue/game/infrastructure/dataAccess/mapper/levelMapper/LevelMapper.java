package rogue.game.infrastructure.dataAccess.mapper.levelMapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import rogue.game.domain.entities.level.Level;
import rogue.game.infrastructure.dataAccess.model.level.LevelModel;
import rogue.game.domain.entities.level.*;
import rogue.game.domain.entities.enemies.Enemy;
import rogue.game.domain.enums.LevelAttribute;
import rogue.game.infrastructure.dataAccess.mapper.MapperRegistry;
import rogue.game.infrastructure.dataAccess.mapper.corridorMapper.CorridorMapper;
import rogue.game.infrastructure.dataAccess.mapper.gameEntityMapper.GameEntityMapper;
import rogue.game.infrastructure.dataAccess.mapper.roomMapper.RoomMapper;
import rogue.game.infrastructure.dataAccess.model.level.RoomModel;

import java.util.List;

/**
 * Mapper interface for converting between {@link Level} and {@link LevelModel} objects.
 *
 * <p> Uses {@link org.mapstruct.Mapper} library to generate mapping code.
 */
@Mapper(uses = {RoomMapper.class, CorridorMapper.class, GameEntityMapper.class})
public interface LevelMapper {

    @Mapping(target = "sequence", source = "sequence", qualifiedByName = "toRoomModelsList")
    @Mapping(target = "corridors", source = "corridors", qualifiedByName = "toCorridorModelsList")
    @Mapping(target = "enemies", source = "enemies", qualifiedByName = "toEnemyModelsList")
    LevelModel toModel(Level level);

    /**
     * Converts a {@link LevelModel} DTO to a {@link Level} entity.
     *
     * <p> It also handles the mapping of nested objects like {@link Room}, {@link Corridor}, and {@link Enemy} using the respective mappers.
     *
     * @param levelModel The {@link LevelModel} DTO to be converted.
     * @return The converted {@link Level} entity.
     */
    default Level toEntity(LevelModel levelModel){
        if (levelModel == null) {
            return null;
        }

        RoomMapper roomMapper = MapperRegistry.getMapper(RoomMapper.class);
        GameEntityMapper gameEntityMapper = MapperRegistry.getMapper(GameEntityMapper.class);
        CorridorMapper corridorMapper = MapperRegistry.getMapper(CorridorMapper.class);

        Level level = new Level();

        level.setLevelNumber(levelModel.getLevelNumber());

        levelModel.getCorridors().forEach(corridorModel -> level.addCorridor(corridorMapper.toEntity(corridorModel)));

        levelModel.getEnemies().forEach(enemyModel -> level.addEnemy(gameEntityMapper.toEntity(enemyModel)));

        List<RoomModel> rooms = levelModel.getSequence();
        rooms.forEach(roomModel -> level.addRoom(roomMapper.toEntity(roomModel)));

        List<Room> sequence = level.getSequence();

        for(int i = 0; i < LevelAttribute.ROOMS_NUMBER.value; i++){
            List<Integer> connectionsModel = rooms.get(i).getConnections();

            Room r = level.getRoom(i);

            for (int j = 0; j < 4; j++){
                Integer currentSector = connectionsModel.get(j);

                if(currentSector != null)
                    r.setConnection(j, sequence.get(currentSector));
            }
        }

        return level;
    }
}
