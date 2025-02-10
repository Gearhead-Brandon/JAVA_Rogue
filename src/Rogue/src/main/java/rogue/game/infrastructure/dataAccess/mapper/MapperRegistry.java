package rogue.game.infrastructure.dataAccess.mapper;

import rogue.game.domain.entities.GameEntity;
import rogue.game.infrastructure.dataAccess.mapper.modelEntityMapper.ModelEntityMapper;
import rogue.game.infrastructure.dataAccess.mapper.balancerMapper.BalancerMapper;
import rogue.game.infrastructure.dataAccess.mapper.balancerMapper.BalancerMapperImpl;
import rogue.game.infrastructure.dataAccess.mapper.corridorMapper.CorridorMapper;
import rogue.game.infrastructure.dataAccess.mapper.corridorMapper.CorridorMapperImpl;
import rogue.game.infrastructure.dataAccess.mapper.doorMapper.DoorMapper;
import rogue.game.infrastructure.dataAccess.mapper.doorMapper.DoorMapperImpl;
import rogue.game.infrastructure.dataAccess.mapper.gameEntityMapper.GameEntityMapper;
import rogue.game.infrastructure.dataAccess.mapper.gameEntityMapper.GameEntityMapperImpl;
import rogue.game.infrastructure.dataAccess.mapper.gameStatsMapper.GameStatsMapper;
import rogue.game.infrastructure.dataAccess.mapper.gameStatsMapper.GameStatsMapperImpl;
import rogue.game.infrastructure.dataAccess.mapper.inventoryMapper.InventoryMapper;
import rogue.game.infrastructure.dataAccess.mapper.inventoryMapper.InventoryMapperImpl;
import rogue.game.infrastructure.dataAccess.mapper.levelMapper.LevelMapper;
import rogue.game.infrastructure.dataAccess.mapper.levelMapper.LevelMapperImpl;
import rogue.game.infrastructure.dataAccess.mapper.positionMapper.PositionMapper;
import rogue.game.infrastructure.dataAccess.mapper.positionMapper.PositionMapperImpl;
import rogue.game.infrastructure.dataAccess.mapper.roomMapper.RoomMapper;
import rogue.game.infrastructure.dataAccess.mapper.roomMapper.RoomMapperImpl;
import rogue.game.infrastructure.dataAccess.model.entities.GameEntityModel;

import java.util.HashMap;
import java.util.Map;

/**
 * Helper class for managing {@link ModelEntityMapper} needed to map {@link GameEntity} to and from a repository {@link GameEntityModel}.
 *
 * <p> This class provides a centralized mechanism for retrieving mappers,
 * simplifying object mapping operations.
 */
public class MapperRegistry {

    /**
     * A map to store registered mappers.
     * The key is the mapper class, and the value is the mapper instance.
     */
    private static final Map<Class<?>, Object> mappers = new HashMap<>();

    /**
     * Initializes the mapper registry by registering the default mappers.
     */
    public static void init(){
        registerMapper(InventoryMapper.class, new InventoryMapperImpl());
        registerMapper(BalancerMapper.class, new BalancerMapperImpl());
        registerMapper(CorridorMapper.class, new CorridorMapperImpl());
        registerMapper(DoorMapper.class, new DoorMapperImpl());
        registerMapper(GameStatsMapper.class, new GameStatsMapperImpl());
        registerMapper(RoomMapper.class, new RoomMapperImpl());
        registerMapper(LevelMapper.class, new LevelMapperImpl());
        registerMapper(GameEntityMapper.class, new GameEntityMapperImpl());
        registerMapper(PositionMapper.class, new PositionMapperImpl());
    }

    /**
     * Registers a new mapper in the registry.
     *
     * @param mapperClass The class of the mapper to register.
     * @param mapperInstance The instance of the mapper to register.
     */
    private static <T> void registerMapper(Class<T> mapperClass, T mapperInstance) {
        mappers.put(mapperClass, mapperInstance);
    }


    /**
     * Retrieves a registered mapper instance.
     *
     * @param mapperClass The class of the mapper to retrieve.
     * @param <T> The type of the mapper.
     * @return The retrieved mapper instance, or null if not found.
     */
    @SuppressWarnings("unchecked")
    public static <T> T getMapper(Class<T> mapperClass) {
        return (T) mappers.get(mapperClass);
    }
}
