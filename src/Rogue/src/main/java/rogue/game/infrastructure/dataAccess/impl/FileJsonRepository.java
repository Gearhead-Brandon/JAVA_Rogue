package rogue.game.infrastructure.dataAccess.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.core.util.DefaultIndenter;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import rogue.game.domain.entities.*;
import rogue.game.domain.entities.level.Level;
import rogue.game.infrastructure.dataAccess.mapper.MapperRegistry;
import rogue.game.infrastructure.dataAccess.mapper.balancerMapper.BalancerMapper;
import rogue.game.infrastructure.dataAccess.mapper.gameEntityMapper.GameEntityMapper;
import rogue.game.infrastructure.dataAccess.mapper.gameStatsMapper.GameStatsMapper;
import rogue.game.infrastructure.dataAccess.mapper.inventoryMapper.InventoryMapper;
import rogue.game.infrastructure.dataAccess.mapper.levelMapper.LevelMapper;
import rogue.game.infrastructure.dataAccess.model.entities.PlayerModel;
import rogue.game.infrastructure.dataAccess.model.SerializeData;
import rogue.game.domain.repository.GameRepository;
import rogue.game.infrastructure.dataAccess.model.entities.GameEntityModel;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

/**
 * File-based JSON repository for storing and loading game data.
 *
 * <p> This class leverages the Jackson library for efficient JSON serialization and deserialization.
 * It provides methods to save the current game state to a file and load it from the file.
 */
public class FileJsonRepository implements GameRepository {
    private final Path filePath;
    private final ObjectMapper mapper;

    /**
     * The game data to be serialized and deserialized.
     */
    private final SerializeData serializeData;

    public FileJsonRepository(String filePath, String fileName) {
        this.filePath = Paths.get(filePath, fileName);

        if (Files.notExists(this.filePath)) {
            try {
                Files.createFile(this.filePath);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
        mapper.setDefaultPrettyPrinter(new DefaultPrettyPrinter().withArrayIndenter(new DefaultIndenter("\t", "\n")));

        this.serializeData = deserializeSaveData().orElseGet(SerializeData::new);

        MapperRegistry.init();
    }

    /**
     * Deserializes the game data from the file if it exists.
     * @return the deserialized game data
     */
    private Optional<SerializeData> deserializeSaveData() {
        try {
            if(Files.size(filePath) > 0) {
                return Optional.of(mapper.readValue(filePath.toFile(), new TypeReference<>() {}));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return Optional.empty();
    }

    @Override
    public void insert(GameStats gameStats) {
        serializeData.addWalkthrough(MapperRegistry.getMapper(GameStatsMapper.class).toModel(gameStats));
    }

    @Override
    public void save() {
        try {
            mapper.writeValue(filePath.toFile(), serializeData);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(Player player) {
        GameEntityModel model = MapperRegistry.getMapper(GameEntityMapper.class).toModel(player);

        if(model instanceof PlayerModel playerModel)
            serializeData.setPlayerModel(playerModel);
    }

    @Override
    public void update(Inventory inventory) {
        serializeData.setInventoryModel(MapperRegistry.getMapper(InventoryMapper.class).toModel(inventory));
    }

    @Override
    public void update(Level level) {
        serializeData.setLevelModel(MapperRegistry.getMapper(LevelMapper.class).toModel(level));
    }

    @Override
    public void update(Balancer balancer) {
        serializeData.setBalancerModel(MapperRegistry.getMapper(BalancerMapper.class).toModel(balancer));
    }

    @Override
    public void update(GameStats totalGameStats, GameStats currentGameStats) {
        serializeData.setTotalStats(MapperRegistry.getMapper(GameStatsMapper.class).toModel(totalGameStats));
        serializeData.setCurrentStats(MapperRegistry.getMapper(GameStatsMapper.class).toModel(currentGameStats));
    }

    @Override
    public Optional<Player> getPlayer() {
        GameEntity player = MapperRegistry.getMapper(GameEntityMapper.class).toEntity(serializeData.getPlayerModel());

        if(player instanceof Player player1){
            return Optional.of(player1);
        }

        return Optional.empty();
    }

    @Override
    public Optional<Inventory> getInventory() {
        return Optional.ofNullable(MapperRegistry.getMapper(InventoryMapper.class).toEntity(serializeData.getInventoryModel()));
    }

    @Override
    public Optional<Level> getLevel() {
        return Optional.ofNullable(MapperRegistry.getMapper(LevelMapper.class).toEntity(serializeData.getLevelModel()));
    }

    @Override
    public Optional<Balancer> getBalancer() {
        return Optional.ofNullable(MapperRegistry.getMapper(BalancerMapper.class).toEntity(serializeData.getBalancerModel()));
    }

    @Override
    public Optional<GameStats> getTotalGameStats() {
        return Optional.ofNullable(MapperRegistry.getMapper(GameStatsMapper.class).toEntity(serializeData.getTotalStats()));
    }

    @Override
    public Optional<GameStats> getCurrentGameStats() {
        return Optional.ofNullable(MapperRegistry.getMapper(GameStatsMapper.class).toEntity(serializeData.getCurrentStats()));
    }

    @Override
    public List<GameStats> getListOfStats() {
        GameStatsMapper gameMapper = MapperRegistry.getMapper(GameStatsMapper.class);
        return serializeData.getWalkthroughes().stream()
                .map(gameMapper::toEntity)
                .toList();
    }
}
