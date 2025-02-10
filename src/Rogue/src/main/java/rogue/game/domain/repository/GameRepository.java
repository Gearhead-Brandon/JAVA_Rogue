package rogue.game.domain.repository;

import rogue.game.domain.entities.Balancer;
import rogue.game.domain.entities.GameStats;
import rogue.game.domain.entities.Inventory;
import rogue.game.domain.entities.Player;
import rogue.game.domain.entities.level.Level;

import java.util.List;
import java.util.Optional;

/**
 * This interface defines the game repository, which is responsible for
 * provides methods for storing and retrieving various game data
 */
public interface GameRepository {
    void insert(GameStats gameStats);
    void update(Player player);
    void update(Inventory inventory);
    void update(Level level);
    void update(Balancer balancer);
    void update(GameStats totalGameStats, GameStats currentGameStats);
    void save();

    Optional<Player> getPlayer();
    Optional<Inventory> getInventory();
    Optional<Level> getLevel();
    Optional<Balancer> getBalancer();
    Optional<GameStats> getTotalGameStats();
    Optional<GameStats> getCurrentGameStats();

    /**
     * @return A list of all game statistics from the repository.
     */
    List<GameStats> getListOfStats();
}
