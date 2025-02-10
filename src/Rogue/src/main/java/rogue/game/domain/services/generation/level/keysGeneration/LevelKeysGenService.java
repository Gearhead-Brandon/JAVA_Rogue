package rogue.game.domain.services.generation.level.keysGeneration;

import rogue.game.domain.services.level.LevelService;
import rogue.game.domain.entities.items.Key;

/**
 * An interface defining a contract for a service responsible for generating {@link Key} in the game.
 */
public interface LevelKeysGenService {
    void generateKeys(LevelService levelService, int startRoomIndex);
}
