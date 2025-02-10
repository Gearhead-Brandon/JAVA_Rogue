package rogue.game.controller.impl;

import rogue.game.services.gameLogic.GameService;
import rogue.game.services.stats.StatsService;
import rogue.game.common.MapInfo;
import rogue.game.common.enums.UserAction;
import rogue.game.controller.Controller;
import rogue.game.services.map.MapService;
import rogue.game.services.messaging.MessageService;
import rogue.game.services.player.PlayerService;
import rogue.game.domain.services.level.LevelService;

import java.util.List;

/**
 * The Rogue game controller is responsible for handling user input, managing the game state, and providing information to the user interface.
 *
 * <p> This class acts as a facade, unifying various game services and
 * providing a single entry point for interacting with the game world.
 */
public class RogueController implements Controller {
    private final GameService gameService;
    private final MapService mapService;
    private final StatsService statsService;
    private final PlayerService playerService;
    private final LevelService levelService;
    private final MessageService messageService;

    public RogueController(
            GameService rogueService,
            MapService mapService,
            StatsService statsService,
            PlayerService playerService,
            MessageService messageService,
            LevelService levelService
    ) {
        this.gameService = rogueService;
        this.mapService = mapService;
        this.statsService = statsService;
        this.playerService = playerService;
        this.messageService = messageService;
        this.levelService = levelService;
    }

    @Override
    public void userInput(UserAction action) {
        gameService.userInput(action);
    }

    @Override
    public MapInfo getMap() { return mapService.getMapInfo(); }

    @Override
    public List<String> getStatusMessages() { return messageService.getStatusMessages(); }

    @Override
    public List<String> getInventory() {
        return playerService.getInventoryList();
    }

    @Override
    public int getLevel() { return levelService.getLevelNumber(); }

    @Override
    public int getGold() {
        return playerService.getGold();
    }

    @Override
    public int getStrength() {
        return playerService.getStrength();
    }

    @Override
    public int getStrengthWithWeapon() {
        return playerService.getStrengthWithWeapon();
    }

    @Override
    public int getHealth() { return playerService.getHealth(); }

    @Override
    public int getMaxHealth() { return playerService.getMaxHealth(); }

    @Override
    public List<String> getListOfStats(){ return statsService.getListOfStats(); }
}
