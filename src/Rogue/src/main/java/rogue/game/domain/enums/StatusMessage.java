package rogue.game.domain.enums;

/**
 * This enum represents various status messages that can be displayed to the player during gameplay.
 * These messages provide feedback on player actions, item interactions, and game events.
 */
public enum StatusMessage {
    EMPTY_MESSAGE (""),
    HEALTH_FULL ("Health is full"),
    FOOD_ADDED ("Food added"),
    FOOD_USED ("Food was used"),
    SCROLL_ADDED ("Scroll added"),
    SCROLL_USED ("Scroll was read"),
    POTION_ADDED ("Potion added"),
    POTION_USED ("Potion was drunk"),
    POTION_TIME_IS_UP ("Potion time is up"),
    WEAPON_ADDED ("Weapon added"),
    WEAPON_USED ("Weapon was chosen"),
    CANT_CHANGE_WEAPON("You can't change weapon here"),
    KEY_ADDED("Key added"),
    CANT_CARRY_ITEMS_STR ("You can't carry so much items"),
    FOUND_STR ("Found "),
    FOUND_GOLD_STR (" pieces of gold"),
    INVENTORY_MESSAGE ("Inventory"),
    THERE_IS_NO_SUCH_ITEM ("There is no such item"),
    DEFEATED_STR (" have defeated "),
    YOU_WERE_DEFEATED ("You were defeated!"),
    YOU_WON ("You won!"),
    YOU_NEED_TO_FIND_STR ("You need to find "),
    KEY_STR(" key");

    public final String message;

    StatusMessage(String message) {
        this.message = message;
    }
}
