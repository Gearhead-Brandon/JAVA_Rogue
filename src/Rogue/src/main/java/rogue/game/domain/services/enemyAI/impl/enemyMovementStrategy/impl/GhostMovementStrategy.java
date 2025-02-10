package rogue.game.domain.services.enemyAI.impl.enemyMovementStrategy.impl;

import rogue.game.common.enums.MapSymbol;
import rogue.game.domain.entities.enemies.Enemy;
import rogue.game.domain.entities.enemies.Ghost;
import rogue.game.services.map.MapService;
import rogue.game.domain.entities.Position;

import java.util.concurrent.ThreadLocalRandom;

/**
 * This class implements a specific movement strategy for {@link Ghost}.
 */
public class GhostMovementStrategy extends BaseEnemyMovementStrategy {

    public GhostMovementStrategy(MapService mapService) {
        super(mapService);
    }

    @Override
    public Position move(Enemy enemy, Position topLeft, Position bottomRight, Position playerPosition) {
        Ghost ghost = (Ghost) enemy;
        boolean playerInHostilityRange = isPositionInHostilityRange(
            ghost.getPosition(),
            playerPosition,
            ghost.getHostility()
        );

        if(playerInHostilityRange){
           ghost.setPlayerPursuit(true);
            return moveToPosition(ghost.getPosition(), playerPosition);
        }

        return (topLeft == Position.NONE && bottomRight == Position.NONE) ? super.moveInRandomDirection(ghost.getPosition())
                : randomMoveAroundRoom(ghost.getPosition(), topLeft, bottomRight);
    }

    /**
     * Moves the {@link Ghost} to a random position within the specified room boundaries.
     *
     * @param from The current {@link Position} of the {@link Ghost}.
     * @param topLeft The top-left corner of the room.
     * @param bottomRight The bottom-right corner of the room.
     * @return The new random {@link Position} of the {@link Ghost}.
     */
    private Position randomMoveAroundRoom(final Position from, final Position topLeft, final Position bottomRight) {
        int tLeftX = topLeft.x();
        int tLeftY = topLeft.y();
        int bRightX = bottomRight.x();
        int bRightY = bottomRight.y();

        int newX;
        int newY;

        final int cx = from.x();
        final int cy = from.y();

        MapSymbol sym;

        do {
            newX = ThreadLocalRandom.current().nextInt(bRightX - tLeftX - 1) + 1 + tLeftX;
            newY = ThreadLocalRandom.current().nextInt(bRightY - tLeftY - 1) + 1 + tLeftY;

            sym = mapService.getMapSymbol(newX, newY);
        }while (BLOCKING_SYMBOLS.contains(sym) || sym.equals(MapSymbol.PLAYER) || (newX == cx && newY == cy));

        return Position.of(newX, newY);
    }
}
