package rogue.game.domain.entities;

import static rogue.game.domain.enums.InventoryAttribute.*;

import lombok.Getter;
import lombok.Setter;
import rogue.game.common.enums.MapColor;
import rogue.game.domain.entities.items.Item;
import rogue.game.domain.enums.ItemType;
import rogue.game.domain.entities.items.*;

import java.util.*;
import java.util.function.Function;

/**
 * This class represents the player's inventory in the game.
 *
 * <p> It manages items of various types like {@link Food}, {@link Scroll},
 * {@link Potion}, {@link Weapon}, {@link Key} and gold.
 */
public class Inventory {

    /**
     * A Map that stores a {@link List} of {@link Item} for each {@link ItemType}.
     */
    private final Map<ItemType, List<Item>> items;
    @Setter
    @Getter
    private ItemType currentInventoryItemType;
    @Setter
    @Getter
    private int gold;

    public Inventory() {
        items = Map.of(
            ItemType.FOOD, new ArrayList<>(),
            ItemType.SCROLL, new ArrayList<>(),
            ItemType.POTION, new ArrayList<>(),
            ItemType.WEAPON, new ArrayList<>(),
            ItemType.KEY, new ArrayList<>()
        );

        currentInventoryItemType = ItemType.FOOD;

        gold = 0;
    }

    public void reset(){
        items.forEach((items, list) -> list.clear());
        currentInventoryItemType = ItemType.FOOD;
        gold = 0;
    }

    public boolean containKeyByColor(final MapColor color) {
        return items.get(ItemType.KEY)
                    .stream()
                    .anyMatch(item -> ((Key) item).getColor() == color);
    }

    /**
     * Attempts to add an {@link Item} to the inventory (if it's an {@link Item}).
     *
     * @param entity The entity to potentially add as an {@link Item}.
     * @return True if the item was added successfully, false otherwise (e.g., not an {@link Item} or inventory full).
     */
    public boolean tryAddItem(GameEntity entity){
        if(entity instanceof Item item) {
            List<Item> list = items.get(item.getItemType());

            if (list != null && list.size() < MAX_ITEMS.value) {
                list.add(item);
                return true;
            }
        }

        return false;
    }

    public void removeAllItemsByType(ItemType type){
        List <Item> list = items.get(type);
        list.clear();
    }

    public void removeItem(Item item){
        List <Item> list = items.get(item.getItemType());
        list.remove(item);
    }

    /**
     * Retrieves a {@link List} of {Item} of a specific {@link ItemType}.
     *
     * @param type The type of items to retrieve.
     * @return An unmodifiable {@link List} of {@link Item} (empty list if no items of that type exist).
     */
    public List<Item> getItemsByType(ItemType type){
        List<Item> list = items.get(type);

        return (list == null) ? Collections.emptyList()
                              : Collections.unmodifiableList(list);
    }

    public Optional<Item> getItem(int index){
        List <Item> list = items.get(currentInventoryItemType);

        if(index < 0 || index >= list.size())
            return Optional.empty();

        return Optional.of(list.get(index));
    }

    /**
     * Generates a formatted list of items in the inventory for display.
     *
     * @return A {@link List} of {@link String} representing the contents of the {@link Inventory}.
     */
    public List<String> getInventoryList(){
        List<String> list = new ArrayList<>();

        list.add(createStringForInventoryList(ItemType.FOOD, items.get(ItemType.FOOD).size() + RATIONS_OF_FOOD.string, NO_FOOD.string));

        list.add(createStringForInventoryList(ItemType.SCROLL, LIST_OF_SCROLLS.string +
                                                itemTypesToString(items.get(ItemType.SCROLL), Item::getName), NO_SCROLLS.string));

        list.add(createStringForInventoryList(ItemType.POTION, LIST_OF_POTIONS.string +
                                                itemTypesToString(items.get(ItemType.POTION), Item::getName), NO_POTIONS.string));

        list.add(createStringForInventoryList(ItemType.WEAPON, LIST_OF_WEAPONS.string +
                                                itemTypesToString(items.get(ItemType.WEAPON), (item) -> item.getName() + "(" + ((Weapon)item).getDamage() + ")"), NO_WEAPONS.string));

        list.add(createStringForInventoryList(ItemType.KEY, LIST_OF_KEYS.string +
                                                itemTypesToString(items.get(ItemType.KEY), Item::getName), NO_KEYS.string));

        return list;
    }

    /**
     * Creates a formatted string for a specific {@link ItemType} in the inventory list.
     *
     * @param type The {@link ItemType} for which to create the string.
     * @param string The base string to use (e.g., number of items).
     * @param listEmptyMessage The message to display if the {@link ItemType} has no items.
     * @return A formatted string representing the {@link ItemType} and its contents.
     */
    private String createStringForInventoryList(final ItemType type, final String string, final String listEmptyMessage){
        return items.get(type).isEmpty() ? listEmptyMessage : string ;
    }

    private String itemTypesToString(List<Item> items, Function<Item, String> formatter) {
        StringJoiner joiner = new StringJoiner(", ");

        items.forEach(item -> joiner.add(formatter.apply(item)));

        return joiner.toString();
    }
}
