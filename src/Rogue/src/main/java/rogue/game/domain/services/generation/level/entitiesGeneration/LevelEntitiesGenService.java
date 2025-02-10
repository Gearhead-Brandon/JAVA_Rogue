package rogue.game.domain.services.generation.level.entitiesGeneration;

import rogue.game.domain.entities.GameEntity;
import rogue.game.domain.services.balance.LevelEntitiesBalancer;
import rogue.game.domain.services.level.LevelService;
import rogue.game.domain.entities.level.Level;

/**
 * This interface defines a service responsible for generating {@link GameEntity} within a game {@link Level}.
 */
public interface LevelEntitiesGenService {
    /**
     * Generates entities (player, portal, items, enemies) for the {@link Level}.
     *
     * @param levelService  The service responsible for managing {@link Level} data.
     * @param player The player {@link GameEntity}.
     * @param coefficient A coefficient influencing difficulty and {@link GameEntity} generation.
     * @param balanceService The service for balancing entity distribution.
     * @return The index of the room where the player is initially placed.
     */
   int generateEntities(LevelService levelService, GameEntity player, int coefficient, LevelEntitiesBalancer balanceService);
}
