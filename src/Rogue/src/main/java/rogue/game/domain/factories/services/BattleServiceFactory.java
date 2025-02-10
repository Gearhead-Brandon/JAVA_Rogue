package rogue.game.domain.factories.services;

import rogue.game.domain.services.battle.BattleService;
import rogue.game.domain.services.battle.impl.RogueBattleService;
import rogue.game.services.messaging.MessageService;

/**
 * A factory class for creating a {@link BattleService} instance.
 *
 * <p> This class provides a static method to create a specific implementation of
 * the {@link BattleService} interface.
 */
public class BattleServiceFactory {
    public static BattleService createBattleService(MessageService messageService) {
        return new RogueBattleService(messageService);
    }
}
