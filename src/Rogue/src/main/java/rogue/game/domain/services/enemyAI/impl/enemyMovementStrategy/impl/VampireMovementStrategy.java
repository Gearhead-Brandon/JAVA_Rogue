package rogue.game.domain.services.enemyAI.impl.enemyMovementStrategy.impl;

import rogue.game.common.enums.MapSymbol;
import rogue.game.domain.entities.enemies.Enemy;
import rogue.game.domain.entities.enemies.Vampire;
import rogue.game.domain.enums.Constants;
import rogue.game.services.map.MapService;
import rogue.game.domain.entities.Position;
import rogue.game.domain.entities.level.Room;

import java.util.concurrent.ThreadLocalRandom;

/**
 * This class implements a specific movement strategy for {@link Vampire}.
 */
public class VampireMovementStrategy extends BaseEnemyMovementStrategy {

    public VampireMovementStrategy(MapService mapService) {
        super(mapService);
    }

    /**
     * Determines the next move for the {@link Vampire} enemy.
     *
     * <p> If the player is within the Vampire's hostility range, it will pursue the player.
     *
     * <p> Otherwise, it will move in a random direction, but it will try to maintain its
     * current direction for a few moves, simulating a more deliberate and focused
     * movement pattern.
     *
     * @param enemy The {@link Vampire} enemy.
     * @param topLeft The top-left corner of the {@link Room}.
     * @param bottomRight The bottom-right corner of the {@link Room}.
     * @param playerPosition The current {@link Position} of the player.
     * @return The new {@link Position} of the {@link Vampire}.
     */
    @Override
    public Position move(Enemy enemy, Position topLeft, Position bottomRight, Position playerPosition) {
        Vampire vampire = (Vampire) enemy;

        boolean playerInHostilityRange = isPositionInHostilityRange(
            vampire.getPosition(),
            playerPosition,
            enemy.getHostility()
        );

        if(playerInHostilityRange)
            return moveToPosition(vampire.getPosition(), playerPosition);

        vampire.setFirstDodge(true);

        return this.moveInRandomDirection(vampire);
    }

    /**
     * Moves the {@link Vampire} in a random direction, but with a tendency to maintain its
     * current direction for a few moves.
     *
     * @param vampire The {@link Vampire} enemy.
     * @return The new {@link Position} of the {@link Vampire}.
     */
    private Position moveInRandomDirection(Vampire vampire) {
        Position from = vampire.getPosition();
        final int curX = from.x();
        final int curY = from.y();

        int newX;
        int newY;

        int attempts = 0;

        MapSymbol sym;

        do{
            if(attempts++ > 4)
                return from;

            if( ThreadLocalRandom.current().nextDouble() < 0.5)
                vampire.setDirection(ThreadLocalRandom.current().nextInt(Constants.DX.directionsValues.size()));

            newX = curX + Constants.DX.directionsValues.get(vampire.getDirection());
            newY = curY + Constants.DY.directionsValues.get(vampire.getDirection());

            sym = super.mapService.getMapSymbol(newX, newY);
        }while (BLOCKING_SYMBOLS.contains(sym));

        return Position.of(newX, newY);
    }
}
