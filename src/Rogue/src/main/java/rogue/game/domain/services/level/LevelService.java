package rogue.game.domain.services.level;

import rogue.game.services.stats.StatsService;
import rogue.game.domain.entities.enemies.Enemy;
import rogue.game.domain.entities.GameEntity;
import rogue.game.domain.entities.level.Corridor;
import rogue.game.domain.entities.level.Door;
import rogue.game.domain.entities.level.Room;
import rogue.game.domain.entities.Position;
import rogue.game.domain.entities.level.Level;

import java.util.List;
import java.util.Optional;

/**
 * Interface defining the core functionality of a level service.
 *
 * <p> This interface provides methods for managing level data, including rooms, corridors and enemies.
 */
public interface LevelService {
    void setLevelNumber(int level);
    void increaseLevel();
    int getLevelNumber();
    List<Enemy> getEnemiesList();
    Enemy getEnemyByPosition(Position position);
    Room getRoom(int index);
    void addRoom(Room room);
    Corridor getLastCorridor();
    void addCorridor(Corridor corridor);
    List<Corridor> getCorridors();
    void addEnemy(GameEntity enemy);

    /**
     * Gets a door at a specific {@link Position}, if any.
     *
     * @param pos The {@link Position} of the door.
     * @return An {@link Optional} containing the {@link Door}, or empty if no door is found.
     */
    Optional<Door> getDoorByPosition(final Position pos);

    /**
     * Gets the index of the room containing a given {@link Position}.
     *
     * @param pos The {@link Position} to check.
     * @return The index of the {@link Room}, or empty if the position is not within a room.
     */
    Optional<Integer> getRoomIndexByPosition(Position pos);

    /**
     * Generates a new {@link Level}.
     *
     * @param player The player {@link GameEntity}.
     * @param stats The {@link StatsService}.
     * @return The index of the player's starting room.
     */
    int generate(GameEntity player, StatsService stats);

    /**
     * Saves the current {@link Level} state.
     */
    void save();

    /**
     * Resets the {@link Level} to its initial state.
     */
    void reset();
}
