package rogue.game.domain.services.enemyAI.impl.enemyMovementStrategy.impl;

import rogue.game.common.enums.MapSymbol;
import rogue.game.domain.entities.enemies.Enemy;
import rogue.game.domain.entities.enemies.Ogre;
import rogue.game.domain.enums.Constants;
import rogue.game.domain.enums.MapAttribute;
import rogue.game.services.map.MapService;
import rogue.game.domain.entities.Position;
import rogue.game.domain.entities.level.Room;
import rogue.game.domain.enums.util.MapSymbolUtil;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * This class implements a specific movement strategy for {@link Ogre}.
 */
public class OgreMovementStrategy extends BaseEnemyMovementStrategy {

    public OgreMovementStrategy(MapService mapService) {
        super(mapService);
    }

    /**
     * Determines the next move for the {@link Ogre} enemy.
     *
     * <p> If the {@link Ogre} is resting, it will wake up and stay in its current {@link Position}.
     *
     * <p> If the player is within the Ogre's hostility range, it will pursue the player using
     * a pathfinding algorithm.
     *
     * <p> Otherwise, it will move randomly in a direction that is not blocked.
     *
     * @param enemy The {@link Ogre} enemy.
     * @param topLeft The top-left corner of the {@link Room}.
     * @param bottomRight The bottom-right corner of the {@link Room}.
     * @param playerPosition The current {@link Position} of the player.
     * @return The new {@link Position} of the {@link Ogre}.
     */
    @Override
    public Position move(Enemy enemy, Position topLeft, Position bottomRight, Position playerPosition) {
        Ogre ogre = (Ogre) enemy;

        if(ogre.isResting()) {
            ogre.setResting(false);
            return ogre.getPosition();
        }

        boolean playerInHostilityRange = isPositionInHostilityRange(
                ogre.getPosition(),
                playerPosition,
                ogre.getHostility()
        );

        if(playerInHostilityRange)
            return this.moveToPosition(ogre.getPosition(), playerPosition);

        return this.moveInRandomDirection(ogre.getPosition());
    }

    /**
     * Moves the {@link Ogre} towards the specified destination {@link Position}, using a pathfinding algorithm.
     *
     * <p> The {@link Ogre} will attempt to move towards the destination, taking into account obstacles
     * and potential doors.
     *
     * @param from The current {@link Position} of the {@link Ogre}.
     * @param destination The destination {@link Position}.
     * @return The new {@link Position} of the {@link Ogre}.
     */
    @Override
    protected Position moveToPosition(Position from, Position destination) {
        List<Position> path = mapService.findPathByBFS(from, destination, BLOCKING_SYMBOLS);

        Position pos = from;

        if (!path.isEmpty()) {
            final int size = path.size();
            pos = path.get(size > 1 ? 1 : 0);

            MapSymbol sym = mapService.getMapSymbol(pos.x(), pos.y());

            if(MapSymbolUtil.isDoor(sym))
                return pos;

            if (size > 2){
                Position p1 = path.get(1);

                sym = mapService.getMapSymbol(p1.x(), p1.y());

                if(MapSymbolUtil.isDoor(sym))
                    return p1;

                Position p2 = path.get(2);

                sym = mapService.getMapSymbol(p2.x(), p2.y());

                if(!BLOCKING_SYMBOLS.contains(sym) && sym != MapSymbol.PLAYER)
                    pos = p2;
            }
        }

        return pos;
    }

    /**
     * Moves the {@link Ogre} in a random direction, avoiding obstacles and potentially taking
     * multiple steps in a single move.
     *
     * @param from The current {@link Position} of the {@link Ogre}.
     * @return The new {@link Position} of the {@link Ogre}.
     */
    @Override
    protected Position moveInRandomDirection(Position from) {
        final int curX = from.x();
        final int curY = from.y();

        int newX;
        int newY;

        int attempts = 0;

        MapSymbol sym;

        do {
            if (attempts++ > 4)
                return from;

            int direction = ThreadLocalRandom.current().nextInt(4);

            newX = curX;
            newY = curY;

            boolean doubleMove = true;

            for(int i = 2; i > 0; i--) {
                newX += Constants.DX.directionsValues.get(direction);
                newY += Constants.DY.directionsValues.get(direction);

                if(newX < 0 || newY < 0 || newY >= MapAttribute.HEIGHT.value || newX >= MapAttribute.WIDTH.value) {
                    doubleMove = false;
                    break;
                }

                sym = mapService.getMapSymbol(newX, newY);

                if(MapSymbolUtil.isDoor(sym))
                    return Position.of(newX, newY);

                if (BLOCKING_SYMBOLS.contains(sym) || sym.equals(MapSymbol.PLAYER))
                    doubleMove = false;
            }

            if(doubleMove)
                break;

            newX = curX + Constants.DX.directionsValues.get(direction);
            newY = curY + Constants.DY.directionsValues.get(direction);

            sym = mapService.getMapSymbol(newX, newY);
        } while (BLOCKING_SYMBOLS.contains(sym) || sym.equals(MapSymbol.PLAYER));

        return Position.of(newX, newY);
    }
}
