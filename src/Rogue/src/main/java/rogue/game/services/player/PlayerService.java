package rogue.game.services.player;

import rogue.game.common.enums.MapColor;
import rogue.game.domain.entities.BattleEntity;
import rogue.game.domain.entities.Player;
import rogue.game.domain.enums.ItemType;
import rogue.game.domain.enums.StatusMessage;
import rogue.game.domain.entities.Position;

import java.util.List;

public interface PlayerService {
    int getHealth();
    int getStrengthWithWeapon();
    int getStrength();
    int getMaxHealth();
    boolean isSleeping();
    boolean isAlive();
    void setViewAngle(float angle);
    float getViewAngle();
    Position getPosition();
    void setPosition(Position position);
    int getCurrentRoomIndex();
    void setCurrentRoomIndex(int currentRoomIndex);

    /**
     * "Try" because health is limited
     * @param health the health to add
     * @return true if the health was added
     */
    boolean tryAddHealth(int health);

    String getName();

    /**
     * Processes the influence of the potion
     */
    void potionUpdate();

    /**
     * @return player as a BattleEntity
     */
    BattleEntity getBattleEntity();

    Player getEntity();

    /**
     * @return the list of items in the inventory
     */
    List<String> getInventoryList();

    void addGold(int gold);
    int getGold();
    void setCurrentInventoryItemType(ItemType type);
    void removeAllItemsByType(ItemType type);
    boolean containKeyByColor(final MapColor color);
    boolean pickUpItem(Position pos, StatusMessage sm);
    void useItemByIndex(int index);
    void openInventory();
    void closeInventory();
    boolean handleTreasure(Position pos);

    /**
     * Checks if the player can see the target in corridor
     * @param pos target
     * @return true if the player can see the target
     */
    boolean playerSeePosition(final Position pos);
    void reset();

    /**
     * Saves the player and inventory to the repository
     */
    void save();
}
