package rogue.game.domain.services.generation.level.levelGeneration;

import rogue.game.domain.services.level.LevelService;
import rogue.game.domain.entities.level.Level;

/**
 * This interface defines a service responsible for generating the geometry of a level.
 */
public interface LevelGeometryGenService {
    /**
     * Generates a new {@link Level}.
     *
     * @param levelService The service responsible for managing {@link Level} data and operations.
     */
    void generateGeometry(LevelService levelService);
}
