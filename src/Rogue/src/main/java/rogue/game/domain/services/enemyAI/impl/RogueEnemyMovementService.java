package rogue.game.domain.services.enemyAI.impl;

import rogue.game.domain.entities.enemies.*;
import rogue.game.domain.services.enemyAI.EnemyMovementService;
import rogue.game.domain.services.enemyAI.impl.enemyMovementStrategy.EnemyMovementStrategy;
import rogue.game.domain.services.enemyAI.impl.enemyMovementStrategy.impl.*;
import rogue.game.services.map.MapService;
import rogue.game.domain.entities.Position;

import java.util.Map;

/**
 * This class is responsible for managing the movement of the various {@link Enemy} types in the game.
 *
 * <p> It provides a flexible system that allows you to define unique movement
 * strategies for each {@link Enemy} type.
 */
public class RogueEnemyMovementService implements EnemyMovementService {

    private final MapService mapService;

    /**
     * Maps enemy classes to their corresponding movement strategies.
     */
    private final Map<Class<? extends Enemy>, EnemyMovementStrategy> movementStrategies;

    public RogueEnemyMovementService(MapService mapService) {
        this.mapService = mapService;

        movementStrategies = Map.of(
                Zombie.class, new BaseEnemyMovementStrategy(mapService),
                Vampire.class, new VampireMovementStrategy(mapService),
                SnakeMagician.class, new SnakeMagicianMovementStrategy(mapService),
                Ogre.class, new OgreMovementStrategy(mapService),
                Ghost.class, new GhostMovementStrategy(mapService),
                Mimic.class, new MimicMovementStrategy(mapService)
        );
    }

    @Override
    public Position move(Enemy enemy, Position topLeft, Position bottomRight, Position player) {
        EnemyMovementStrategy strategy = movementStrategies.getOrDefault(
                enemy.getClass(),
                new BaseEnemyMovementStrategy(mapService)
        );

        return strategy.move(
                enemy,
                topLeft,
                bottomRight,
                player
        );
    }
}
