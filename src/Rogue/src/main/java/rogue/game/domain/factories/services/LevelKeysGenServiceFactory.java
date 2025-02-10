package rogue.game.domain.factories.services;

import rogue.game.domain.services.generation.level.keysGeneration.LevelKeysGenService;
import rogue.game.domain.services.generation.level.keysGeneration.impl.RogueLevelKeysGenService;

/**
 * Factory class for creating {@link LevelKeysGenService} instances.
 */
public class LevelKeysGenServiceFactory {
    public static LevelKeysGenService createLevelKeysGenService() {
        return new RogueLevelKeysGenService();
    }
}
