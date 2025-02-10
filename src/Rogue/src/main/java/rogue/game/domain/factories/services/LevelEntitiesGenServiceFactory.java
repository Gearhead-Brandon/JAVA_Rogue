package rogue.game.domain.factories.services;

import rogue.game.domain.services.generation.level.entitiesGeneration.LevelEntitiesGenService;
import rogue.game.domain.services.generation.level.entitiesGeneration.impl.RogueLevelEntitiesGenService;

/**
 * Factory class for creating LevelEntitiesGenService instances.
 */
public class LevelEntitiesGenServiceFactory {
    public static LevelEntitiesGenService createLevelEntitiesGenService() {
        return new RogueLevelEntitiesGenService();
    }
}
