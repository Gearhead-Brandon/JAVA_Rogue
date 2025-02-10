package rogue.game.domain.services.generation.mapGeneration;

import rogue.game.domain.services.level.LevelService;
import rogue.game.services.map.MapService;
import rogue.game.domain.entities.Position;

/**
 * This interface defines a service responsible for generating game map.
 */
public interface MapGenService {

    /**
     * Updates the game map based on the current {@link LevelService} and player position.

     * @param mapService The service responsible for managing the map data.
     * @param levelService The service responsible for managing the current level.
     * @param playerPosition The current position of the player.
     */
    void generate(MapService mapService, LevelService levelService, Position playerPosition);
}
