package rogue.game.domain.entities.items;

import lombok.Getter;
import rogue.game.domain.enums.ItemType;
import rogue.game.domain.entities.Position;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Represents a weapon item in the game world.
 *
 * <p> Weapons can be equipped by the player to deal damage in combat.
 */
public class Weapon extends Item {
    public static final int MIN_DAMAGE = 6;
    public static final int MAX_DAMAGE = 20;

    @Getter
    private final int damage;
    private final String name;

    public Weapon(String name, Position position, int complexity) {
        super(ItemType.WEAPON, position);
        this.damage = ThreadLocalRandom.current().nextInt(MIN_DAMAGE, MAX_DAMAGE) + complexity;
        this.name = name;
    }

    public Weapon(int damage, String name, Position position) {
        super(ItemType.WEAPON, position);
        this.damage = damage;
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }
}
