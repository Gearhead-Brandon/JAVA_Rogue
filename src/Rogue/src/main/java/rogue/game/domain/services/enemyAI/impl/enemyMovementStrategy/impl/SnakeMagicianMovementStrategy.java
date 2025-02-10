package rogue.game.domain.services.enemyAI.impl.enemyMovementStrategy.impl;

import rogue.game.common.enums.MapSymbol;
import rogue.game.domain.entities.enemies.Enemy;
import rogue.game.domain.entities.enemies.SnakeMagician;
import rogue.game.domain.enums.Constants;
import rogue.game.services.map.MapService;
import rogue.game.domain.entities.Position;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

/**
 * This class implements a specific movement strategy for {@link SnakeMagician} enemies.
 */
public class SnakeMagicianMovementStrategy extends BaseEnemyMovementStrategy{

    public SnakeMagicianMovementStrategy(MapService mapService) {
        super(mapService);
    }

    @Override
    public Position move(Enemy enemy, Position topLeft, Position bottomRight, Position playerPosition) {
        SnakeMagician snakeMagician = (SnakeMagician) enemy;

        final boolean inRoom = (topLeft != Position.NONE && bottomRight != Position.NONE);

        boolean playerInHostilityRange = isPositionInHostilityRange(
            snakeMagician.getPosition(),
            playerPosition,
            snakeMagician.getHostility()
        );

        if(playerInHostilityRange)
            return this.moveToPosition(inRoom, snakeMagician.getPosition(), playerPosition);

        if(inRoom)
            return this.moveInRandomDirection(snakeMagician.getPosition());
        else
            return super.moveInRandomDirection(snakeMagician.getPosition());
    }

    /**
     * Moves the {@link SnakeMagician} towards the specified destination {@link Position}, taking into
     * account the room boundaries and potential obstacles.
     *
     * <p> The {@link SnakeMagician} will try to move diagonally towards the destination if possible.
     *
     * @param inRoom Indicates whether the {@link SnakeMagician} is currently in a room.
     * @param from The current {@link Position} of the {@link SnakeMagician}.
     * @param destination The destination {@link Position}.
     * @return The new {@link Position} of the {@link SnakeMagician}.
     */
    private Position moveToPosition(final boolean inRoom, Position from, Position destination) {
        List<Position> path = mapService.findPathByBFS(from, destination, BLOCKING_SYMBOLS);

        Position pos = from;

        if (path.size() > 1) {
            pos = path.get(1);

            MapSymbol sym = mapService.getMapSymbol(pos.x(), pos.y());

            if(inRoom && sym != MapSymbol.CORRIDOR && sym != MapSymbol.PLAYER) {
                int dx = pos.x() - from.x();
                int dy = pos.y() - from.y();

                if (Math.abs(dx) > Math.abs(dy)) {
                    final int sign = dx > 0 ? 1 : -1;
                    final int newX = from.x() + sign;

                    final int newY = from.y() + (dy > 0 ? 1 : -1) * sign;
                    final int newY2 = from.y() + (dy > 0 ? -1 : 1) * sign;

                    final int distanceToNewY = Math.abs(destination.y() - newY);
                    final int distanceToNewY2 = Math.abs(destination.y() - newY2);

                    int y ;

                    if(distanceToNewY == distanceToNewY2){
                        y = ThreadLocalRandom.current().nextBoolean() ? newY : newY2;
                    }else {
                        y = distanceToNewY < distanceToNewY2 ? newY : newY2;
                    }

                    sym = mapService.getMapSymbol(newX, y);

                    if (!BLOCKING_SYMBOLS.contains(sym)) {
                        pos = Position.of(newX, y);
                    }
                } else if (Math.abs(dx) < Math.abs(dy)) {
                    final int sign = dy > 0 ? 1 : -1;
                    final int newX = from.x() + (dx > 0 ? 1 : -1) * sign;
                    final int newX2 = from.x() + (dx > 0 ? -1 : 1) * sign;
                    final int newY = from.y() + sign;

                    final int distanceToNewX = Math.abs(destination.x() - newX);
                    final int distanceToNewX2 = Math.abs(destination.x() - newX2);

                    int x;

                    if(distanceToNewX == distanceToNewX2)
                        x = ThreadLocalRandom.current().nextBoolean() ? newX : newX2;
                    else
                        x = distanceToNewX < distanceToNewX2 ? newX : newX2;

                    sym = mapService.getMapSymbol(x, newY);

                    if (!BLOCKING_SYMBOLS.contains(sym))
                        pos = Position.of(x, newY);
                }
            }
        }

        return pos;
    }

    /**
     * Finds a random adjacent corridor to the given {@link Position }.
     *
     * <p> This method is used to help the {@link SnakeMagician} move between rooms.
     *
     * @param map The map service.
     * @param from The current {@link Position }.
     * @return An optional {@link Position } of the random adjacent corridor, or `Optional.empty()`
     *         if no suitable corridor is found.
     */
    private Optional<Position> findRandomAdjacentCorridor(MapService map, final Position from) {
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (map.getMapSymbol(from.x() + i, from.y() + j).equals(MapSymbol.CORRIDOR)) {
                    if(ThreadLocalRandom.current().nextBoolean())
                        return Optional.of(Position.of(from.x() + i, from.y() + j));
                }
            }
        }

        return Optional.empty();
    }

    /**
     * Moves the {@link SnakeMagician} in a random direction, prioritizing diagonal moves.
     *
     * <p> If no diagonal move is possible, a standard cardinal direction will be chosen.
     *
     * @param from The current {@link Position} of the {@link SnakeMagician}.
     * @return The new {@link Position} of the {@link SnakeMagician}.
     */
    @Override
    protected Position moveInRandomDirection(Position from) {
        Optional<Position> randomCorridor = findRandomAdjacentCorridor(mapService, from);
        if(randomCorridor.isPresent())
            return randomCorridor.get();

        final int curX = from.x();
        final int curY = from.y();

        int newX;
        int newY;

        int attempts = 0;
        final int maxAttempts = 4;

        MapSymbol sym;

        do{
            if(attempts++ > maxAttempts)
                return from;

            int direction = ThreadLocalRandom.current().nextInt(4);

            newX = curX + Constants.DIAGONAL_X.directionsValues.get(direction);
            newY = curY + Constants.DIAGONAL_Y.directionsValues.get(direction);

            sym = mapService.getMapSymbol(newX, newY);

        }while (BLOCKING_SYMBOLS.contains(sym));

        return Position.of(newX, newY);
    }
}
