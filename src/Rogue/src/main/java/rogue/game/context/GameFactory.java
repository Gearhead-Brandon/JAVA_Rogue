package rogue.game.context;

import rogue.game.services.gameLogic.GameService;
import rogue.game.services.gameLogic.impl.RogueGameService;
import rogue.game.services.messaging.MessageService;
import rogue.game.services.messaging.impl.GameMessageService;
import rogue.game.services.player.PlayerService;
import rogue.game.services.player.impl.RoguePlayerService;
import rogue.game.services.stats.StatsService;
import rogue.game.services.stats.impl.GameStatsService;
import rogue.game.controller.Controller;
import rogue.game.controller.impl.RogueController;
import rogue.game.domain.repository.GameRepository;
import rogue.game.services.map.MapService;
import rogue.game.services.map.impl.RogueMapService;
import rogue.game.domain.services.level.LevelService;
import rogue.game.domain.services.level.impl.RogueLevelService;
import rogue.game.domain.services.pubsub.PubSubService;
import rogue.game.domain.services.pubsub.impl.RoguePubSubService;
import rogue.game.infrastructure.dataAccess.impl.FileJsonRepository;
import rogue.game.view.View;
import rogue.game.view.impl.RogueView;

public class GameFactory {
    public static GameContext createGameContext() {
       String dataFilePath = "src/main/resources/";
       String dataFileName = "game_stats_and_progress.json";

        GameRepository gameRepository = new FileJsonRepository(dataFilePath, dataFileName);
        PubSubService pubSubService = new RoguePubSubService();
        StatsService statsService = new GameStatsService(gameRepository);
        MapService mapService = new RogueMapService(pubSubService);
        MessageService messageService = new GameMessageService(pubSubService);
        LevelService levelService = new RogueLevelService(gameRepository);

        PlayerService playerService = new RoguePlayerService(
                gameRepository,
                messageService,
                mapService,
                levelService,
                statsService,
                pubSubService
        );

        GameService gameService = new RogueGameService(
                gameRepository,
                pubSubService,
                statsService,
                mapService,
                levelService,
                messageService,
                playerService
        );

        return new GameContext(
                pubSubService,
                statsService,
                mapService,
                gameService,
                levelService,
                playerService,
                messageService
        );
    }

    public static Controller createController(GameContext gameContext) {
        return new RogueController(
                gameContext.gameService(),
                gameContext.mapService(),
                gameContext.statsService(),
                gameContext.playerService(),
                gameContext.messageService(),
                gameContext.levelService()
        );
    }

    public static View createView(Controller controller) {
        return new RogueView(controller);
    }
}
