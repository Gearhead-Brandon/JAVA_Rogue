package rogue.game.domain.services.balance;

import rogue.game.services.stats.StatsService;
import rogue.game.domain.entities.Balancer;
import rogue.game.domain.entities.GameEntity;
import rogue.game.domain.entities.Position;
import rogue.game.domain.entities.items.Item;
import rogue.game.domain.entities.enemies.Enemy;

/**
 * Interface defining a contract for the entity balancer in a level.
 *
 * <p> This interface provides methods for managing the number and types of entities (items, enemies)
 * in a level, ensuring gameplay balance.
 */
public interface LevelEntitiesBalancer {
    Balancer getBalancer();

    /**
     * Updates the difficulty level of the {@link Balancer} based on the provided {@link StatsService} and coefficient.
     *
     * @param statsService The service providing player and level statistics.
     * @param coefficient A factor influencing the difficulty adjustment.
     */
    void updateDifficulty(StatsService statsService, int coefficient);

    /**
     * Calculates the number of items to spawn based on the complexity factor.
     *
     * @param complexityFactor A factor determining the complexity of the level.
     * @return The number of {@link Item} to spawn.
     */
    int getCountOfItems(final int complexityFactor);

    /**
     * Spawns an {@link Item} at the specified {@link Position} with the given complexity.
     *
     * @param complexityFactor A factor determining the complexity of the item.
     * @param position The {@link Position} to spawn the item.
     * @return The spawned {@link Item} {@link GameEntity}.
     */
    GameEntity spawnItem(final int complexityFactor, Position position);

    /**
     * Calculates the number of {@link Enemy} to spawn based on the coefficient.
     *
     * @param coefficient A factor determining the number of {@link Enemy}.
     * @return The number of {@link Enemy} to spawn.
     */
    int getCountOfEnemies(final int coefficient);

    /**
     * Spawns an {@link Enemy} at the given {@link Position} with the given difficulty.
     *
     * @param complexityFactor The difficulty factor of the enemy.
     * @param position The {@link Position} at which the {@link Enemy} will be spawned.
     * @return The spawned {@link Enemy} {@link GameEntity} object.
     */
    GameEntity spawnEnemy(final int complexityFactor, Position position);

    /**
     * Resets the {@link Balancer} to its initial state.
     *
     * <p> This method can be used when starting a new level or reloading the game.
     */
    void reset();
}
