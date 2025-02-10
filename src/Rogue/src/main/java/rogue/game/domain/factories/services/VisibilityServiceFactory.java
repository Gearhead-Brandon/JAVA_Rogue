package rogue.game.domain.factories.services;

import rogue.game.domain.services.vision.VisibilityService;
import rogue.game.domain.services.vision.impl.RogueVisibilityService;

/**
 * A factory class for creating a {@link VisibilityService} instance.
 *
 * <p> This class provides a static method to create a specific implementation of
 * the {@link VisibilityService} interface.
 */
public class VisibilityServiceFactory {
    public static VisibilityService createVisibilityService(){
        return new RogueVisibilityService();
    }
}
