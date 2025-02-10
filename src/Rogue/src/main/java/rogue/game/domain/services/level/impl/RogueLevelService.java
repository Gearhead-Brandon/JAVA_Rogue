package rogue.game.domain.services.level.impl;

import rogue.game.domain.factories.services.LevelEntitiesGenServiceFactory;
import rogue.game.domain.factories.services.LevelKeysGenServiceFactory;
import rogue.game.domain.factories.services.LevelEntitiesBalancerFactory;
import rogue.game.domain.factories.services.LevelGeometryGenServiceFactory;
import rogue.game.domain.services.balance.LevelEntitiesBalancer;
import rogue.game.domain.services.level.LevelService;
import rogue.game.services.stats.StatsService;
import rogue.game.domain.entities.enemies.Enemy;
import rogue.game.domain.entities.GameEntity;
import rogue.game.domain.entities.level.Corridor;
import rogue.game.domain.entities.level.Door;
import rogue.game.domain.entities.level.Level;
import rogue.game.domain.entities.level.Room;
import rogue.game.domain.enums.Direction;
import rogue.game.domain.enums.LevelAttribute;
import rogue.game.domain.enums.LevelState;
import rogue.game.domain.repository.GameRepository;
import rogue.game.domain.entities.Position;

import java.util.List;
import java.util.Optional;

/**
 * Service responsible for managing the level in the game.
 *
 * <p> This class provides functionality for working with levels in the game.
 */
public class RogueLevelService implements LevelService {
    private final Level level;
    private LevelState levelState;
    private final LevelEntitiesBalancer balancer;
    private final GameRepository gameRepository;

    public RogueLevelService(GameRepository gameRepository) {
        Optional<Level> level = gameRepository.getLevel();

        this.level = level.orElseGet(Level::new);

        this.levelState = level.isPresent() ? LevelState.LOADED : LevelState.NEW;

        if (level.isEmpty())
            this.level.setLevelNumber(1);

        this.gameRepository = gameRepository;
        this.balancer = LevelEntitiesBalancerFactory.createLevelEntitiesBalancer(gameRepository);
    }

    @Override
    public void setLevelNumber(int level) {
        this.level.setLevelNumber(level);
    }

    @Override
    public void increaseLevel() {
        level.setLevelNumber(level.getLevelNumber() + 1);
    }

    @Override
    public int getLevelNumber() {
        return level.getLevelNumber();
    }

    @Override
    public List<Enemy> getEnemiesList() {
        return level.getEnemies();
    }

    @Override
    public Enemy getEnemyByPosition(Position position) {
        return level.getEnemies().stream()
                .filter(e -> e.getPosition().x() == position.x() && e.getPosition().y() == position.y())
                .findFirst()
                .orElse(null);
    }

    @Override
    public Room getRoom(int index) {
        return level.getRoom(index);
    }

    @Override
    public void addRoom(Room room) {
        level.addRoom(room);
    }

    @Override
    public Corridor getLastCorridor() {
        return level.getLastCorridor();
    }

    @Override
    public void addCorridor(Corridor corridor) {
        level.addCorridor(corridor);
    }

    @Override
    public List<Corridor> getCorridors() {
        return level.getCorridors();
    }

    @Override
    public void addEnemy(GameEntity enemy) {
        level.addEnemy(enemy);
    }

    @Override
    public Optional<Door> getDoorByPosition(Position pos) {
        for(int i = 0; i < LevelAttribute.ROOMS_NUMBER.value; i++){
            Room r = level.getRoom(i);

            for (Direction d : Direction.values()) {
                Optional<Door> door = r.getDoorByDirection(d);
                if (door.isPresent() && door.get().getPosition().equals(pos))
                    return door;
            }
        }

        return Optional.empty();
    }

    @Override
    public Optional<Integer> getRoomIndexByPosition(Position pos) {
        for(int i = 0; i < LevelAttribute.ROOMS_NUMBER.value; i++){
            if(level.getRoom(i).isPointInside(pos))
                return Optional.of(i);
        }

        return Optional.empty();
    }

    @Override
    public void reset() {
        balancer.reset();
    }

    @Override
    public int generate(GameEntity player, StatsService statsService) {
        if(levelState == LevelState.LOADED) {
            levelState = LevelState.NEW;
            return getRoomIndexByPosition(player.getPosition()).orElse(0);
        }

        int levelNumber = level.getLevelNumber();

        level.reset();

        balancer.updateDifficulty(statsService, levelNumber);

        LevelGeometryGenServiceFactory.createLevelGeometryGenService().generateGeometry(this);

        int playerRoomIndex = LevelEntitiesGenServiceFactory.createLevelEntitiesGenService().generateEntities(this, player, levelNumber, balancer);

        LevelKeysGenServiceFactory.createLevelKeysGenService().generateKeys(this, playerRoomIndex);

        level.getRoom(playerRoomIndex).removeEntity(player);

        level.setLevelNumber(levelNumber);

        return playerRoomIndex;
    }

    @Override
    public void save() {
        gameRepository.update(level);
        gameRepository.update(balancer.getBalancer());
    }
}
