package rogue.game.domain.factories;

import rogue.game.domain.builders.items.*;
import rogue.game.domain.entities.items.*;

/**
 * A factory class for creating builders for various item types.
 *
 * <p> This class provides static methods to create builders for different item types,
 * simplifying the creation of items with specific attributes and behaviors.
 * <p> It promotes code modularity and flexibility by separating the creation of items
 * from their usage.
 */
public class ItemBuilderFactory {
    /**
     * @return A new instance of the {@link BaseItemBuilder} class for
     * all item types except {@link Key} and {@link Weapon}.
     */
    public static BaseItemBuilder createBaseItemBuilder(){
        return new BaseItemBuilder();
    }

    /**
     * @return A new instance of the {@link KeyBuilder} class for creating {@link Key} items.
     */
    public static KeyBuilder createKeyBuilder(){
        return new KeyBuilder();
    }

    /**
     * @return A new instance of the {@link ComplexityBasedWeaponBuilder} class for creating {@link Weapon} items.
     */
    public static ComplexityBasedWeaponBuilder createComplexityBasedWeaponBuilder(){ return new ComplexityBasedWeaponBuilder(); }

    /**
     * @return A new instance of the {@link CustomWeaponBuilder} class for creating custom {@link Weapon} items.
     */
    public static CustomWeaponBuilder createCustomWeaponBuilder(){ return new CustomWeaponBuilder(); }

    /**
     * @return A new instance of the {@link ScrollBuilder} class for creating {@link Scroll} items.
     */
    public static ScrollBuilder createScrollBuilder(){ return new ScrollBuilder(); }

    /**
     * @return A new instance of the {@link PotionBuilder} class for creating {@link Potion} items.
     */
    public static PotionBuilder createPotionBuilder(){ return new PotionBuilder(); }
}
