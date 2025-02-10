package rogue.game.domain.factories.services;

import rogue.game.domain.services.enemyAI.EnemyMovementService;
import rogue.game.domain.services.enemyAI.impl.RogueEnemyMovementService;
import rogue.game.services.map.MapService;

/**
 * A factory class for creating an {@link EnemyMovementService} instance.
 *
 * <p> This class provides a static method to create a specific implementation of t
 * the {@link EnemyMovementService} interface.
 */
public class EnemyMovementServiceFactory {
    public static EnemyMovementService createEnemyMovementService(MapService mapService) {
        return new RogueEnemyMovementService(mapService);
    }
}
