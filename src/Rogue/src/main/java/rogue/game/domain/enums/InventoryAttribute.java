package rogue.game.domain.enums;

/**
 * Represents various inventory attributes and their corresponding values.
 * <p> These attributes are used to track inventory capacity, item quantities, and item lists.
 */
public enum InventoryAttribute {
    MAX_ITEMS(9),
    RATIONS_OF_FOOD(" rations of food"),
    NO_FOOD("No food"),
    NO_SCROLLS("No scrolls"),
    NO_POTIONS("No potions"),
    NO_WEAPONS("No weapons"),
    NO_KEYS("No keys"),
    LIST_OF_SCROLLS("Scrolls: "),
    LIST_OF_POTIONS("Potions: "),
    LIST_OF_WEAPONS("Weapons: "),
    LIST_OF_KEYS("Keys: ");

    public final int value;
    public final String string;

    InventoryAttribute(final int value) {
        this.value = value;
        this.string = "";
    }

    InventoryAttribute(final String string) {
        this.value = 0;
        this.string = string;
    }
}
