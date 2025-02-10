package rogue.game.context;

import rogue.game.common.observer.Observer;
import rogue.game.services.gameLogic.GameService;
import rogue.game.services.messaging.MessageService;
import rogue.game.services.player.PlayerService;
import rogue.game.services.stats.StatsService;
import rogue.game.services.map.MapService;
import rogue.game.domain.services.level.LevelService;
import rogue.game.domain.services.pubsub.PubSubService;

public record GameContext(
        PubSubService pubSubService,
        StatsService statsService,
        MapService mapService,
        GameService gameService,
        LevelService levelService,
        PlayerService playerService,
        MessageService messageService
) {
    public void subscribe(Observer observer) {
        pubSubService.subscribe(observer);
    }
}
