package rogue.game.services.gameLogic.impl;

import rogue.game.domain.factories.services.*;
import rogue.game.domain.repository.GameRepository;
import rogue.game.domain.services.battle.BattleService;
import rogue.game.common.enums.MapColor;
import rogue.game.common.enums.MapSymbol;
import rogue.game.common.enums.UserAction;
import rogue.game.services.gameLogic.GameService;
import rogue.game.domain.services.level.LevelService;
import rogue.game.services.map.MapService;
import rogue.game.services.messaging.MessageService;
import rogue.game.domain.services.pubsub.PubSubService;
import rogue.game.services.player.PlayerService;
import rogue.game.services.stats.StatsService;
import rogue.game.domain.enums.util.MapSymbolUtil;
import rogue.game.common.enums.util.UserActionUtil;
import rogue.game.domain.entities.enemies.Enemy;
import rogue.game.domain.enums.Angles;
import rogue.game.domain.enums.Constants;
import rogue.game.domain.enums.GameState;
import rogue.game.domain.enums.LevelAttribute;
import rogue.game.common.observer.EventType;
import rogue.game.domain.entities.level.*;

import rogue.game.domain.enums.ItemType;
import rogue.game.domain.entities.GameEntity;
import rogue.game.domain.enums.StatusMessage;
import rogue.game.domain.enums.util.SymbolMapper;
import rogue.game.domain.entities.Position;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * <p> The service responsible for the main logic of the game:
 * <ul>
 * <li>Handling user actions</li>
 * <li>Player interaction with game objects</li>
 * </ul>
 *
 * <p> Dependencies:
 * <ul>
 * <li>{@link GameRepository}: to interact with the game data storage</li>
 * <li>{@link PubSubService}: to notify view about changes in the game</li>
 * <li>{@link StatsService}: to collect game statistics</li>
 * <li>{@link MessageService}: to handle event messages for the player</li>
 * <li>{@link MapService}: to handle map rendering</li>
 * <li>{@link LevelService}: to handle level generation and management</li>
 * <li>{@link PlayerService}: to handle player movement and inventory</li>
 * </ul>
 */
public class RogueGameService implements GameService{
    private final GameRepository gameRepository;
    private final LevelService levelService;
    private final PlayerService playerService;
    private final MapService mapService;
    private final PubSubService pubSubService;
    private final MessageService messageService;
    private final StatsService statsService;

    private GameState state;

    /**
     * A map that associates map symbols with corresponding entity handlers.
     *
     * <p> The key is a map symbol and the value is a function that takes
     * the entity's position and returns a boolean indicating whether to move the player.
     */
    private Map<MapSymbol, Function<Position, Boolean>> entityHandlers;

    /**
     * A map that associates user actions with corresponding handlers.
     *
     * <p> The key is a user action, and the value is a consumer that takes the user action as input and performs the necessary actions.
     */
    private Map<UserAction, Consumer<UserAction>> userActionHandlers;

    public RogueGameService(
            GameRepository gameRepository,
            PubSubService pubSubService,
            StatsService statsService,
            MapService mapService,
            LevelService levelService,
            MessageService messageService,
            PlayerService playerService
    ) {
        this.gameRepository = gameRepository;
        this.levelService = levelService;
        this.messageService = messageService;
        this.mapService = mapService;
        this.playerService = playerService;
        this.statsService = statsService;
        this.pubSubService = pubSubService;

        createUserActionHandlers();
        createEntityHandlers();

        levelGeneration();

        state = GameState.PLAYER_MOVEMENT;
    }

    private void createUserActionHandlers(){
        userActionHandlers = Map.ofEntries(
            Map.entry(UserAction.UP, (Action) -> handleMovementAction(Angles.UP.angle, 0, -1)),
            Map.entry(UserAction.DOWN, (Action) -> handleMovementAction(Angles.DOWN.angle, 0, 1)),
            Map.entry(UserAction.LEFT, (Action) -> handleMovementAction(Angles.LEFT.angle, -1, 0)),
            Map.entry(UserAction.RIGHT, (Action) -> handleMovementAction(Angles.RIGHT.angle, 1, 0)),
            Map.entry(UserAction.USE_FOOD, (Action) -> handleInventoryAction(ItemType.FOOD, Action)),
            Map.entry(UserAction.USE_SCROLL, (Action) -> handleInventoryAction(ItemType.SCROLL, Action)),
            Map.entry(UserAction.USE_POTION, (Action) -> handleInventoryAction(ItemType.POTION, Action)),
            Map.entry(UserAction.USE_WEAPON, (Action) -> handleInventoryAction(ItemType.WEAPON, Action))
        );
    }

    private void createEntityHandlers(){
        entityHandlers = Map.ofEntries(
            Map.entry(MapSymbol.TREASURE, playerService::handleTreasure),
            Map.entry(MapSymbol.FOOD, (p) -> playerService.pickUpItem(p, StatusMessage.FOOD_ADDED)),
            Map.entry(MapSymbol.SCROLL, (p) -> playerService.pickUpItem(p, StatusMessage.SCROLL_ADDED)),
            Map.entry(MapSymbol.POTION, (p) -> playerService.pickUpItem(p, StatusMessage.POTION_ADDED)),
            Map.entry(MapSymbol.WEAPON, (p) -> playerService.pickUpItem(p, StatusMessage.WEAPON_ADDED)),

            Map.entry(MapSymbol.BLUE_KEY, (p) -> playerService.pickUpItem(p, StatusMessage.KEY_ADDED)),
            Map.entry(MapSymbol.RED_KEY, (p) -> playerService.pickUpItem(p, StatusMessage.KEY_ADDED)),
            Map.entry(MapSymbol.GREEN_KEY, (p) -> playerService.pickUpItem(p, StatusMessage.KEY_ADDED)),
            Map.entry(MapSymbol.MAGENTA_KEY, (p) -> playerService.pickUpItem(p, StatusMessage.KEY_ADDED)),
            Map.entry(MapSymbol.CYAN_KEY, (p) -> playerService.pickUpItem(p, StatusMessage.KEY_ADDED)),
            Map.entry(MapSymbol.WHITE_KEY, (p) -> playerService.pickUpItem(p, StatusMessage.KEY_ADDED)),

            Map.entry(MapSymbol.SNAKE_MAGICIAN, this::attackEnemy),
            Map.entry(MapSymbol.VAMPIRE, this::attackEnemy),
            Map.entry(MapSymbol.OGRE, this::attackEnemy),
            Map.entry(MapSymbol.GHOST, this::attackEnemy),
            Map.entry(MapSymbol.ZOMBIE, this::attackEnemy),
            Map.entry(MapSymbol.MIMIC, this::attackEnemy)
        );
    }

    @Override
    public void userInput(UserAction action) {
        messageService.resetStatusMessages();

        switch (state) {
            case GameState.PLAYER_MOVEMENT -> handlePlayerAction(action);
            case GameState.USING_INVENTORY -> inventoryAction(action);
        }
    }

    private void handlePlayerAction(final UserAction action) {
        if(!playerService.isSleeping())
            userActionHandlers.getOrDefault(action, (Action) -> {}).accept(action);
        else
           enemiesMove();
    }

    private void handleMovementAction(float angle, int x, int y) {
        playerService.setViewAngle(angle);
        playerMove(x, y);
        enemiesMove();
        mapService.update();
        handlePlayerEffects();
        state = GameState.PLAYER_MOVEMENT;
    }

    private void handleInventoryAction(ItemType type, final UserAction action) {
        messageService.updateStatusMessage(StatusMessage.INVENTORY_MESSAGE);
        playerService.setCurrentInventoryItemType(type);
        inventoryAction(action);
    }

    private void playerMove(final int mx, final int my) {
        Position oldPos = playerService.getPosition();

        Position newPos = Position.of(oldPos.x() + mx, oldPos.y() + my);

        MapSymbol sym = mapService.getMapSymbol(newPos);

        if(!sym.equals(MapSymbol.WALL) && !sym.equals(MapSymbol.EMPTINESS)) {
            if (sym.equals(MapSymbol.CORRIDOR))
                handleCorridorMovement(newPos);
            else if (MapSymbolUtil.isDoor(sym))
                handleDoorMovement(newPos);
            else if (sym.equals(MapSymbol.PORTAL))
                enteredPortal();
            else if(!isEntity(sym, newPos))
                handleRoomMovement(newPos);
        }
    }

    private void handleCorridorMovement(Position pos) {
        Position oldPlayerPos = playerService.getPosition();

        Optional<Door> d = levelService.getDoorByPosition(oldPlayerPos);

        if (d.isPresent()) {
            MapColor color = d.get().getColor();
            mapService.setMapSymbol(oldPlayerPos, MapSymbolUtil.getDoorSymbolByColor(color));
            mapService.hideRoom(levelService.getRoom(playerService.getCurrentRoomIndex()));
        } else {
            mapService.setMapSymbol(oldPlayerPos, MapSymbol.CORRIDOR);
        }

        if (!mapService.isVisible(pos))
            mapService.setVisible(pos.x(), pos.y(), true);

        setPlayerToMap(pos);

        statsService.addPassedCell();

        mapService.showCorridorCell(pos);

        playerService.setCurrentRoomIndex(Constants.NONE.value);
    }

    private void handleDoorMovement(Position pos) {
        Optional<Door> d = levelService.getDoorByPosition(pos);

        if(d.isPresent() && !d.get().isOpen()) {
            MapColor color = d.get().getColor();

            if (playerService.containKeyByColor(color)) {
                d.get().open();
            } else {
                messageService.updateStatusMessage(StatusMessage.YOU_NEED_TO_FIND_STR.message + color.name().toLowerCase() + StatusMessage.KEY_STR.message);
                return;
            }
        }

        Position oldPlayerPos = playerService.getPosition();

        boolean playerInRoom = (playerService.getCurrentRoomIndex() != Constants.NONE.value)
                && levelService.getRoom(playerService.getCurrentRoomIndex()).isPointInside(oldPlayerPos);
        setPlayerToMap(pos);

        statsService.addPassedCell();

        if (playerInRoom) {
            mapService.setMapSymbol(oldPlayerPos, MapSymbol.FLOOR);
            mapService.hideRoom(levelService.getRoom(playerService.getCurrentRoomIndex()));
            mapService.showCorridorCell(pos);
            float angle = playerService.getViewAngle() + Angles.ROTATE.angle;
            mapService.updateVisibleArea(pos, angle == Angles.FULL_VIEW.angle ? Angles.RIGHT.angle : angle);
        } else {
            mapService.updateVisibleArea(pos, playerService.getViewAngle());
            mapService.setMapSymbol(oldPlayerPos, MapSymbol.CORRIDOR);
            playerService.setCurrentRoomIndex(levelService.getRoomIndexByPosition(playerService.getPosition()).orElse(Constants.NONE.value));
        }
    }

    private void enteredPortal() {
        levelService.increaseLevel();
        statsService.addLevel();
        playerService.removeAllItemsByType(ItemType.KEY);
        levelGeneration();
    }

    private boolean isEntity(MapSymbol sym, Position pos) {
        return !sym.equals(MapSymbol.FLOOR) && !MapSymbolUtil.isDoor(sym) && handleEntities(sym, pos);
    }

    private void handleRoomMovement(Position pos) {
        Position oldPlayerPos = playerService.getPosition();

        Optional<Door> door = levelService.getDoorByPosition(playerService.getPosition());

        if (door.isPresent()) {
            MapColor color = door.get().getColor();
            mapService.setMapSymbol(oldPlayerPos, MapSymbolUtil.getDoorSymbolByColor(color));
            mapService.showRoom(levelService.getRoom(playerService.getCurrentRoomIndex()));
        } else {
            mapService.setMapSymbol(oldPlayerPos, MapSymbol.FLOOR);
            placeEntityOfRoom(playerService.getCurrentRoomIndex(), playerService.getPosition());
        }

        setPlayerToMap(pos);
        statsService.addPassedCell();
    }

    private boolean handleEntities(final MapSymbol sym, Position pos) {
        return entityHandlers.getOrDefault(sym, (a) -> false).apply(pos);
    }

    /**
     * Handles enemy movement logic.
     */
    private void enemiesMove() {
        if(state == GameState.LEVEL_GENERATION){
            return;
        }

        Iterator<Enemy> it = levelService.getEnemiesList().iterator();

        while(it.hasNext() && playerService.isAlive()) {
            enemyMove(it.next());

            if(!playerService.isAlive()) {
                restart(StatusMessage.YOU_WERE_DEFEATED);
                return;
            }
        }

        if(playerService.isAlive())
            messageService.update();

        setPlayerToMap(playerService.getPosition());
    }

    /**
     * Moves a single enemy based on its behavior and current situation.
     *
     * <p> It checks for collisions with the player and initiates combat if necessary. Updates the enemy's position on the map.
     *
     * @param enemy The enemy to be moved.
     */
    private void enemyMove(final Enemy enemy) {
        Optional<Integer> roomIndex = levelService.getRoomIndexByPosition(enemy.getPosition());

        Position topLeft = Position.NONE;
        Position bottomRight = Position.NONE;

        if(roomIndex.isPresent()) {
            topLeft = levelService.getRoom(roomIndex.get()).getTopLeft();
            bottomRight = levelService.getRoom(roomIndex.get()).getBottomRight();
        }

        Position oldPos = enemy.getPosition();

        Position newPos = EnemyMovementServiceFactory
                            .createEnemyMovementService(mapService)
                            .move(
                                enemy,
                                topLeft,
                                bottomRight,
                                playerService.getPosition()
                            );

        if (newPos.equals(playerService.getPosition())) {
            putEnemyToMap(enemy, roomIndex);
            processCombatRound(enemy, false);
        } else {
            if(MapSymbolUtil.isDoor(mapService.getMapSymbol(newPos))) {
                Optional<Door> d = levelService.getDoorByPosition(newPos);

                if (d.isPresent() && !d.get().isOpen())
                    return;
            }

            if(!newPos.equals(oldPos)) {
                removeEnemyFromMap(enemy);
                enemy.setPosition(newPos);
            }

            if (!enemy.becomeInvisible())
                putEnemyToMap(enemy, roomIndex);
        }
    }

    /**
     * Removes the enemy from the map and updates the corresponding map symbol based on the environment.
     *
     * @param e The enemy to be removed from the map.
     */
    private void removeEnemyFromMap(final Enemy e) {
        Position pos = e.getPosition();

        Optional<Integer> roomIndex = levelService.getRoomIndexByPosition(e.getPosition());

        if (roomIndex.isPresent()) {
            mapService.setMapSymbol(pos, MapSymbol.FLOOR);
            placeEntityOfRoom(roomIndex.get(), e.getPosition());

            Optional<Door> d = levelService.getDoorByPosition(e.getPosition());
            d.ifPresent(door -> mapService.setMapSymbol(pos, MapSymbolUtil.getDoorSymbolByColor(door.getColor())));
        } else {
            mapService.setMapSymbol(pos, MapSymbol.CORRIDOR);
        }
    }

    /**
     * Puts an enemy to the map
     * @param e the enemy
     * @param roomIndex the room index
     */
    private void putEnemyToMap(Enemy e, Optional<Integer> roomIndex) {
        Position pos = e.getPosition();

        if (roomIndex.isEmpty()) {
            if (playerService.playerSeePosition(e.getPosition()))
                mapService.setMapSymbol(pos, SymbolMapper.map(e));

        } else if (roomIndex.get() == playerService.getCurrentRoomIndex() || playerService.playerSeePosition(e.getPosition())) {
            mapService.setMapSymbol(pos, SymbolMapper.map(e));
        }
    }

    private boolean attackEnemy(Position pos) {
        processCombatRound(levelService.getEnemyByPosition(pos), true);
        return true;
    }

    /**
     * Handles player inventory actions
     * @param action the action
     */
    private void inventoryAction(final UserAction action) {
        if(action == UserAction.QUIT) {
            state = GameState.PLAYER_MOVEMENT;
            return;
        }

        if(state == GameState.PLAYER_MOVEMENT){
            playerService.openInventory();
            state = GameState.USING_INVENTORY;
        }else{
            if(UserActionUtil.isUsageOfInventory(action)) {
                playerService.closeInventory();
                state = GameState.PLAYER_MOVEMENT;
            }else if(UserActionUtil.isInventorySelection(action))
                playerService.useItemByIndex(UserActionUtil.getIndexInInventory(action));
        }

        enemiesMove();
        handlePlayerEffects();
    }

    /**
     * Processes a combat round between the player and an enemy.
     * @param enemy the enemy
     * @param playerAttacks true if the player attacks, false if the enemy attacks
     */
    private void processCombatRound(final Enemy enemy, final boolean playerAttacks) {
        BattleService battleService = BattleServiceFactory.createBattleService(messageService);

        if (playerAttacks) {
            boolean result = battleService.tryToAttack(playerService.getBattleEntity(), enemy);

            if(result)
                statsService.addBlowInflicted();

            if(!enemy.isAlive())
                enemyKill(enemy);

        } else {
            int playerHealth = playerService.getHealth();
            int maxHealth = playerService.getMaxHealth();

            boolean result = battleService.tryToAttack(enemy, playerService.getBattleEntity());

            if(result)
                statsService.addMissedShot();

            if(playerService.getHealth() < playerHealth || playerService.getHealth() < maxHealth)
                pubSubService.notifyObserver(EventType.HITS_UPDATE);
        }
    }

    /**
     * Removes the enemy from the map and removes it from the list of enemies
     * @param enemy the enemy
     */
    private void enemyKill(final Enemy enemy) {
        messageService.addStatusMessage(playerService.getName() + StatusMessage.DEFEATED_STR.message + enemy.getName());

        int treasure = enemy.getTreasure();

        playerService.addGold(treasure);
        pubSubService.notifyObserver(EventType.GOLD_UPDATE);

        removeEnemyFromMap(enemy);
        levelService.getEnemiesList().remove(enemy);

        statsService.addTreasureAmount(treasure);
        statsService.addDefeatedEnemy();

        mapService.update();
    }

    private void restart(StatusMessage msg) {
        statsService.reset();

        levelService.reset();
        levelService.setLevelNumber(1);

        playerService.reset();

        levelGeneration();

        mapService.update();
        pubSubService.notifyObserver(EventType.LEVEL_UPDATE);
        pubSubService.notifyObserver(EventType.GOLD_UPDATE);
        pubSubService.notifyObserver(EventType.HITS_UPDATE);
        pubSubService.notifyObserver(EventType.STRENGTH_UPDATE);

        messageService.updateStatusMessage(msg);

        state = GameState.PLAYER_MOVEMENT;
    }

    /**
     * Places an entity of the room to the map if exists.
     * @param index room index
     * @param pos position of the entity
     */
    private void placeEntityOfRoom(int index, Position pos){
        if (index < 0)
            return;

        GameEntity e = levelService.getRoom(index).getEntityByPosition(pos);

        if(!e.equals(GameEntity.EMPTY_ENTITY))
            mapService.setMapSymbol(pos, SymbolMapper.map(e));
    }

    private void setPlayerToMap(Position pos){
        playerService.setPosition(pos);
        mapService.setMapSymbol(pos.x(), pos.y(), MapSymbol.PLAYER);
    }

    /**
     * Handles player effects, such as potion effects.
     *
     * <p> This method checks the current game state and processes potion effects if the game is not in the level generation phase.
     */
    public void handlePlayerEffects() {
        if(state != GameState.LEVEL_GENERATION)
            playerService.potionUpdate();
    }

    private void levelGeneration(){
        if(levelService.getLevelNumber() == LevelAttribute.FINAL_LEVEL.value) {
            restart(StatusMessage.YOU_WON);
        } else {
            state = GameState.LEVEL_GENERATION;

            playerService.setCurrentRoomIndex( levelService.generate(playerService.getEntity(), statsService) );

            statsService.prepareData();

            mapService.generate(levelService, playerService.getPosition());

            mapService.showRoom(levelService.getRoom(playerService.getCurrentRoomIndex()));

            saveState();

            pubSubService.notifyObserver(EventType.LEVEL_UPDATE);
        }
    }

    private void saveState(){
        new Thread(() -> {
            playerService.save();
            levelService.save();
            statsService.save();
            gameRepository.save();
        }).start();
    }
}
