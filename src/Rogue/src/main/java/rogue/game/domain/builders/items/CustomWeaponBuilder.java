package rogue.game.domain.builders.items;

import rogue.game.domain.builders.BaseEntityBuilder;
import rogue.game.domain.entities.GameEntity;
import rogue.game.domain.entities.items.Weapon;

/**
 * Builder class for creating custom {@link Weapon} with specific damage and name.
 *
 * <p> This class allows you to define the damage and name of the {@link Weapon}
 * before building it.
 *
 * <p> This provides more control over the weapon's
 * properties compared to the {@link ComplexityBasedWeaponBuilder}.
 */
public class CustomWeaponBuilder extends BaseEntityBuilder {
    private int damage = 0;
    private String name = "";

    public CustomWeaponBuilder withDamage(int damage) {
        this.damage = damage;
        return this;
    }

    public CustomWeaponBuilder withName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public GameEntity build() {
        return new Weapon(damage, name, super.position);
    }
}
