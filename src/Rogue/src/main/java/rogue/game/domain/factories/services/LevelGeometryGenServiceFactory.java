package rogue.game.domain.factories.services;

import rogue.game.domain.services.generation.level.levelGeneration.LevelGeometryGenService;
import rogue.game.domain.services.generation.level.levelGeneration.impl.RogueLevelGeometryGenService;

/**
 * A factory class for creating a {@link LevelGeometryGenService} instance.
 *
 * <p> This class provides a static method to create a specific implementation of
 * the {@link LevelGeometryGenService} interface.
 */
public class LevelGeometryGenServiceFactory {
    public static LevelGeometryGenService createLevelGeometryGenService(){
        return new RogueLevelGeometryGenService();
    }
}
