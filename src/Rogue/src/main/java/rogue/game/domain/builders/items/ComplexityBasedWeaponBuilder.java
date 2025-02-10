package rogue.game.domain.builders.items;

import rogue.game.domain.builders.BaseEntityBuilder;
import rogue.game.domain.entities.GameEntity;
import rogue.game.domain.entities.items.Weapon;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Builder class for creating {@link Weapon} with varying complexity levels.
 *
 * <p> This class generates random weapon names from a predefined list and assigns a complexity level to the weapon.
 * <p> The complexity level can be set using the {@code withComplexity} method.
 */
public class ComplexityBasedWeaponBuilder extends BaseEntityBuilder {
    private static final List<String> WEAPONS = List.of("Sword", "Dagger", "Mace", "Axe", "Hammer", "Spear");

    private int complexity = 0;

    public ComplexityBasedWeaponBuilder withComplexity(int complexity) {
        this.complexity = complexity;
        return this;
    }

    @Override
    public GameEntity build() {
        String name = WEAPONS.get(ThreadLocalRandom.current().nextInt(WEAPONS.size()));
        return new Weapon(name, super.position, complexity);
    }
}
