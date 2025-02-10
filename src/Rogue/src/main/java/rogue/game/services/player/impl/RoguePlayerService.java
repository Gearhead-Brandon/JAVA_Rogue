package rogue.game.services.player.impl;

import rogue.game.domain.services.level.LevelService;
import rogue.game.services.map.MapService;
import rogue.game.services.messaging.MessageService;
import rogue.game.domain.services.pubsub.PubSubService;
import rogue.game.services.player.PlayerService;
import rogue.game.services.stats.StatsService;
import rogue.game.common.enums.MapColor;
import rogue.game.common.enums.MapSymbol;
import rogue.game.common.observer.EventType;
import rogue.game.domain.entities.BattleEntity;
import rogue.game.domain.entities.Inventory;
import rogue.game.domain.entities.Player;
import rogue.game.domain.entities.GameEntity;
import rogue.game.domain.entities.items.*;
import rogue.game.domain.entities.level.Room;
import rogue.game.domain.enums.*;
import rogue.game.domain.repository.GameRepository;
import rogue.game.domain.enums.util.SymbolMapper;
import rogue.game.domain.entities.Position;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * Service responsible for managing the player's state and actions.
 *
 * <p> This service handles player movement and inventory management
 */
public class RoguePlayerService implements PlayerService {
    private final Player player;
    private final Inventory inventory;
    private final MessageService messageService;
    private final MapService mapService;
    private final LevelService levelService;
    private final StatsService statsService;
    private final PubSubService pubSubService;
    private final GameRepository gameRepository ;
    private final Map<ItemType, Consumer<Item>> itemUsageHandlers;

    public RoguePlayerService(GameRepository gameRepository, MessageService messageService,
                              MapService mapService, LevelService levelService,
                              StatsService statsService, PubSubService pubSubService) {


        this.gameRepository = gameRepository;
        this.player = gameRepository.getPlayer().orElseGet(Player::new);
        this.inventory = gameRepository.getInventory().orElseGet(Inventory::new);

        this.messageService = messageService;
        this.mapService = mapService;
        this.levelService = levelService;
        this.statsService = statsService;
        this.pubSubService = pubSubService;

        this.itemUsageHandlers = Map.of(
            ItemType.FOOD, this::handleFoodUsage,
            ItemType.SCROLL, this::handleScrollUsage,
            ItemType.POTION, this::handlePotionUsage,
            ItemType.WEAPON, this::handleWeaponUsage
        );
    }

    @Override
    public int getHealth() {
        return player.getHealth();
    }

    @Override
    public int getStrengthWithWeapon() {
        return player.getStrengthWithWeapon();
    }

    @Override
    public int getStrength() {
        return player.getStrength();
    }

    @Override
    public int getMaxHealth() {
        return player.getMaxHealth();
    }

    @Override
    public boolean isSleeping() {
        boolean isSleeping = player.isSleeping();
        player.setSleeping(false);
        return isSleeping;
    }

    @Override
    public boolean isAlive() {
        return player.isAlive();
    }

    @Override
    public void setViewAngle(float angle) {
        player.setViewAngle(angle);
    }

    @Override
    public float getViewAngle() {
        return player.getViewAngle();
    }

    @Override
    public Position getPosition() {
        return player.getPosition();
    }

    @Override
    public void setPosition(Position position) {
        player.setPosition(position);
    }

    @Override
    public int getCurrentRoomIndex() {
        return player.getCurrentRoomIndex();
    }

    @Override
    public void setCurrentRoomIndex(int currentRoomIndex) {
        player.setCurrentRoomIndex(currentRoomIndex);
    }

    @Override
    public boolean tryAddHealth(int health) {
        boolean res = true;

        final int currentHealth = player.getHealth();
        final int maxHealth = player.getMaxHealth();

        if(currentHealth == maxHealth)
            res = false;
        else player.setHealth(Math.min(health + currentHealth, maxHealth));

        return res;
    }

    @Override
    public String getName() {
        return player.getName();
    }

    @Override
    public void potionUpdate() {
        player.potionUpdate().ifPresent(skillType -> {
            switch(skillType) {
                case EntityCharacteristic.STRENGTH -> pubSubService.notifyObserver(EventType.STRENGTH_UPDATE);
                case EntityCharacteristic.MAX_HEALTH -> pubSubService.notifyObserver(EventType.HITS_UPDATE);
            }
            messageService.updateStatusMessage(StatusMessage.POTION_TIME_IS_UP);
        });
    }

    @Override
    public BattleEntity getBattleEntity() {
        return player;
    }

    @Override
    public Player getEntity() {
        return player;
    }

    @Override
    public List<String> getInventoryList() {
        return inventory.getInventoryList();
    }

    @Override
    public int getGold() {
        return inventory.getGold();
    }

    @Override
    public void setCurrentInventoryItemType(ItemType type) {
        inventory.setCurrentInventoryItemType(type);
    }

    @Override
    public void removeAllItemsByType(ItemType type) {
        inventory.removeAllItemsByType(type);
    }

    @Override
    public boolean containKeyByColor(MapColor color) {
        return inventory.containKeyByColor(color);
    }

    @Override
    public void addGold(int gold) {
        inventory.setGold(inventory.getGold() + gold);
    }

    @Override
    public boolean pickUpItem(Position pos, StatusMessage sm) {
        Room r = levelService.getRoom(player.getCurrentRoomIndex());
        GameEntity e = r.getEntityByPosition(pos);

        boolean res = true;

        if (e instanceof Item item) {
            if (inventory.tryAddItem(item)) {
                r.removeEntity(e);
                messageService.updateStatusMessage(sm.message);
                mapService.update();
            } else {
                messageService.updateStatusMessage(StatusMessage.CANT_CARRY_ITEMS_STR);
                return res;
            }

            mapService.setMapSymbol(pos, MapSymbol.FLOOR);

            res = false;
        }

        return res;
    }

    @Override
    public boolean handleTreasure(Position pos) {
        Room r = levelService.getRoom(player.getCurrentRoomIndex());
        GameEntity e = r.getEntityByPosition(pos);

        boolean result = true;

        if (e instanceof Treasure treasure) {
            r.removeEntity(e);
            int gold = treasure.getTreasure();

            messageService.updateStatusMessage(StatusMessage.FOUND_STR.message + gold + StatusMessage.FOUND_GOLD_STR.message);

            inventory.setGold(inventory.getGold() + gold);

            statsService.addTreasureAmount(gold);

            pubSubService.notifyObserver(EventType.GOLD_UPDATE);

            mapService.setMapSymbol(pos, MapSymbol.FLOOR);

            result = false;
        }

        return result;
    }

    @Override
    public void useItemByIndex(int index) {
        Optional<Item> item = inventory.getItem(index);

        if (item.isEmpty())
            messageService.updateStatusMessage(StatusMessage.THERE_IS_NO_SUCH_ITEM);
        else
            itemUsageHandlers.get(item.get().getItemType()).accept(item.get());
    }

    private void handleFoodUsage(Item item) {
        Food food = (Food) item;

        if (!tryAddHealth(food.getHealth())) {
            messageService.updateStatusMessage(StatusMessage.HEALTH_FULL);
        } else {
            pubSubService.notifyObserver(EventType.HITS_UPDATE);
            updateInventory(item, StatusMessage.FOOD_USED);

            statsService.addEatenFood();
        }
    }

    private void handleScrollUsage(Item item) {
        Scroll scroll = (Scroll) item;

        int improvement = scroll.getImprovement();

        statsService.addReadScroll();

        switch (scroll.getAttribute()) {
            case EntityCharacteristic.AGILITY -> player.addAgility(improvement);
            case EntityCharacteristic.STRENGTH -> {
                player.setStrength(player.getStrength() + improvement);
                pubSubService.notifyObserver(EventType.STRENGTH_UPDATE);
            }
            case EntityCharacteristic.MAX_HEALTH -> {
                player.setMaxHealth(player.getMaxHealth() + improvement);
                pubSubService.notifyObserver(EventType.HITS_UPDATE);
            }
        }

        updateInventory(item, StatusMessage.SCROLL_USED);
    }

    private void handlePotionUsage(Item item) {
        Potion potion = (Potion) item;

        player.applyPotion((potion));

        statsService.addPotionDrank();

        pubSubService.notifyObserver(potion.getAttribute() == EntityCharacteristic.STRENGTH ? EventType.STRENGTH_UPDATE : EventType.HITS_UPDATE);

        updateInventory(item, StatusMessage.POTION_USED);
    }

    private void handleWeaponUsage(Item item) {
        if (player.getCurrentRoomIndex() != Constants.NONE.value) {
            Weapon weapon = (Weapon) item;
            Optional<Weapon> oldWeapon = player.setWeapon(weapon);
            oldWeapon.ifPresent(this::throwPlayerItem);
            pubSubService.notifyObserver(EventType.STRENGTH_UPDATE);
            updateInventory(item, StatusMessage.WEAPON_USED);
        }else {
            messageService.updateStatusMessage(StatusMessage.CANT_CHANGE_WEAPON);
        }
    }

    /**
     * Throws an item onto a map in an adjacent cell.
     *
     * @param item The item to be thrown.
     */
    private void throwPlayerItem(final Item item) {
        Position itemPos = mapService.placeForItem(player.getPosition());

        if (itemPos != Position.NONE){
            mapService.setMapSymbol(itemPos.x(), itemPos.y(), SymbolMapper.map(item));

            item.setPosition(itemPos);
            levelService.getRoom(player.getCurrentRoomIndex()).addEntity(item);
            mapService.update();
        }
    }

    private void updateInventory(Item item, StatusMessage msg){
        inventory.removeItem(item);
        openInventory();
        messageService.updateStatusMessage(msg.message);
    }

    @Override
    public void openInventory(){
        pubSubService.notifyObserver(EventType.INVENTORY_UPDATE);
        messageService.updateStatusMessage(StatusMessage.INVENTORY_MESSAGE);
    }

    @Override
    public void closeInventory() {
        mapService.update();
        messageService.updateStatusMessage(StatusMessage.EMPTY_MESSAGE);
    }

    @Override
    public boolean playerSeePosition(final Position pos) {
        return mapService.seeInCorridor(player.getPosition(), pos);
    }

    @Override
    public void reset() {
        player.reset();
        inventory.reset();
    }

    @Override
    public void save() {
        gameRepository.update(player);
        gameRepository.update(inventory);
    }
}
