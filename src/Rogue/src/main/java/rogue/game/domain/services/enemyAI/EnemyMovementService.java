package rogue.game.domain.services.enemyAI;

import rogue.game.domain.entities.enemies.Enemy;
import rogue.game.domain.entities.Position;
import rogue.game.domain.entities.level.Room;
import rogue.game.domain.services.enemyAI.impl.enemyMovementStrategy.EnemyMovementStrategy;

/**
 * Describes the service responsible for calculating enemy movement.
 *
 * <p> This interface defines a contract for classes implementing various enemy movement algorithms.
 */
public interface EnemyMovementService {
    /**
     * Calculates and returns a new {@link Position} for the specified {@link Enemy}.
     *
     * <p> This method delegates the movement calculation to the appropriate {@link EnemyMovementStrategy}
     *
     * @param enemy The {@link Enemy} object to calculate movement for.
     * @param topLeft The top-left corner of the {@link Room}.
     * @param bottomRight The bottom-right corner of the {@link Room}.
     * @param player The player's position.
     * @return The new {@link Position} for the {@link Enemy}.
     */
    Position move(Enemy enemy, Position topLeft, Position bottomRight, Position player);
}
