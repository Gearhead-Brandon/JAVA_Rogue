package rogue.game.domain.factories.services;

import rogue.game.domain.repository.GameRepository;
import rogue.game.domain.services.balance.LevelEntitiesBalancer;
import rogue.game.domain.services.balance.impl.RogueLevelEntitiesBalancer;

/**
 * A factory class for creating a {@link LevelEntitiesBalancer} instance.
 *
 * <p> This class provides a static method to create a specific implementation of
 * the {@link LevelEntitiesBalancer} interface.
 */
public class LevelEntitiesBalancerFactory {
    public static LevelEntitiesBalancer createLevelEntitiesBalancer(GameRepository gameRepository){
        return new RogueLevelEntitiesBalancer(gameRepository);
    }
}
