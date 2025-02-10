package rogue.game.domain.services.enemyAI.impl.enemyMovementStrategy.impl;

import lombok.AllArgsConstructor;
import rogue.game.common.enums.MapSymbol;
import rogue.game.domain.entities.enemies.Enemy;
import rogue.game.domain.enums.Constants;
import rogue.game.domain.services.enemyAI.impl.enemyMovementStrategy.EnemyMovementStrategy;
import rogue.game.services.map.MapService;
import rogue.game.domain.entities.Position;

import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Basic enemy movement strategy.
 *
 * <p> This class provides common functionality for various enemy movement strategies
 */
@AllArgsConstructor
public class BaseEnemyMovementStrategy implements EnemyMovementStrategy {
    /**
     * A set of map symbols that represent obstacles or other entities that block the enemy's movement.
     */
    protected static final Set<MapSymbol> BLOCKING_SYMBOLS = Set.of(
        MapSymbol.EMPTINESS, MapSymbol.WALL, MapSymbol.PORTAL,
        MapSymbol.ZOMBIE, MapSymbol.VAMPIRE, MapSymbol.GHOST,
        MapSymbol.OGRE, MapSymbol.SNAKE_MAGICIAN, MapSymbol.MIMIC
    );

    protected final MapService mapService;

    public Position move(Enemy enemy, Position topLeft, Position bottomRight, Position playerPosition){
        boolean playerInHostilityRange = isPositionInHostilityRange(
            enemy.getPosition(),
            playerPosition,
            enemy.getHostility()
        );

        if(playerInHostilityRange)
            return moveToPosition(enemy.getPosition(), playerPosition);

        return moveInRandomDirection(enemy.getPosition());
    }

    /**
     * Checks if the destination {@link Position} is within the enemy's hostility range.
     *
     * @param current The current {@link Position} of the {@link Enemy}.
     * @param destination The destination {@link Position}.
     * @param hostility The enemy's hostility level.
     * @return `true` if the destination is within range, `false` otherwise.
     */
    protected boolean isPositionInHostilityRange(Position current, Position destination, int hostility) {
        int dx = Math.abs(current.x() - destination.x());
        int dy = Math.abs(current.y() - destination.y());

        return dx <= hostility && dy <= hostility;
    }

    /**
     * Moves the enemy towards the specified destination {@link Position}, avoiding obstacles.
     *
     * @param from The current {@link Position} of the enemy.
     * @param destination The destination {@link Position}.
     * @return The new {@link Position} of the {@link Enemy}.
     */
    protected Position moveToPosition(Position from, Position destination) {
        int dx = destination.x() - from.x();
        int dy = destination.y() - from.y();

        int moveX = Math.abs(dx) > Math.abs(dy) ? (dx > 0 ? 1 : -1) : 0;
        int moveY = Math.abs(dx) <= Math.abs(dy) ? (dy > 0 ? 1 : -1) : 0;

        int x = from.x() + moveX;
        int y = from.y() + moveY;

        MapSymbol sym = mapService.getMapSymbol(x, y);

        if(BLOCKING_SYMBOLS.contains(sym)) {
            if (moveX != 0) {
                moveX = 0;
                moveY = dy > 0 ? 1 : -1;
            } else {
                moveX = dx > 0 ? 1 : -1;
                moveY = 0;
            }

            x = from.x() + moveX;
            y = from.y() + moveY;

            sym = mapService.getMapSymbol(x, y);

            if (BLOCKING_SYMBOLS.contains(sym))
                return from;
        }

        return Position.of(x, y);
    }

    /**
     * Moves the {@link Enemy} in a random direction, avoiding obstacles.
     *
     * @param from The current {@link Position} of the {@link Enemy}.
     * @return The new {@link Position} of the {@link Enemy}.
     */
    protected Position moveInRandomDirection(Position from) {
        final int curX = from.x();
        final int curY = from.y();

        int newX;
        int newY;

        int attempts = 0;

        MapSymbol sym;

        do{
            if(attempts++ > 4)
                return from;

            int direction = ThreadLocalRandom.current().nextInt(4);

            newX = curX + Constants.DX.directionsValues.get(direction);
            newY = curY + Constants.DY.directionsValues.get(direction);

            sym = mapService.getMapSymbol(newX, newY);
        }while (BLOCKING_SYMBOLS.contains(sym));

        return Position.of(newX, newY);
    }
}
