package rogue.game.domain.factories.services;

import rogue.game.domain.services.generation.mapGeneration.MapGenService;
import rogue.game.domain.services.generation.mapGeneration.impl.RogueMapGenService;

/**
 * A factory class for creating a {@link MapGenService} instance.
 *
 * <p> This class provides a static method to create a specific implementation of
 * the {@link MapGenService} interface.
 */
public class MapGenServiceFactory {
    public static MapGenService createMapGenService(){
        return new RogueMapGenService();
    }
}
