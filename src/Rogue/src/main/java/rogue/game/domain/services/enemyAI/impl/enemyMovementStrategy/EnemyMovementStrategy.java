package rogue.game.domain.services.enemyAI.impl.enemyMovementStrategy;

import rogue.game.domain.entities.enemies.Enemy;
import rogue.game.domain.entities.Position;

/**
 * Describes the strategy for enemy movement in the game environment.
 *
 * <p> This interface defines a contract for various enemy movement algorithms,
 * such as following the player, patrolling, randomly walking, etc.
 */
public interface EnemyMovementStrategy {
    /**
     * Calculates the new {@link Position} of the {@link Enemy}.
     *
     * @param enemy The {@link Enemy} to calculate the new {@link Position} for.
     * @param topLeft The bottom left corner of the play field or movement area.
     * @param bottomRight The top right corner of the play field or area.
     * @param player The player's {@link Position}, which can affect enemy behavior.
     * @return The new {@link Position} of the {@link Enemy}.
     */
    Position move(Enemy enemy, Position topLeft, Position bottomRight, Position player);
}
