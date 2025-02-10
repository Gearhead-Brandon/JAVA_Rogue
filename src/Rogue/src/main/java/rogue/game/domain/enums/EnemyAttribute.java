package rogue.game.domain.enums;

import rogue.game.domain.entities.enemies.Enemy;

/**
 * This enum represents various {@link Enemy} attributes and their corresponding values.
 *
 * <p> These attributes are used to define the characteristics of different {@link Enemy} types in the game.
 */
public enum EnemyAttribute {
    LOW_AGILITY(2),
    HIGH_AGILITY(5),
    VERY_HIGH_AGILITY(7),

    LOW_STRENGTH(1),
    AVERAGE_STRENGTH(3),
    HIGH_STRENGTH(5),

    LOW_HOSTILITY(2),
    AVERAGE_HOSTILITY(4),
    HIGH_HOSTILITY(6),

    LOW_HEALTH(7),
    HIGH_HEALTH(12),

    STRENGTH_GROWTH_RATE(0.1f),
    AGILITY_GROWTH_RATE(0.1f),
    HEALTH_GROWTH_RATE(0.1f),

    ZOMBIE_NAME("Zombie"),
    VAMPIRE_NAME("Vampire"),
    SNAKE_MAGICIAN_NAME("Snake Magician"),
    GHOST_NAME("Ghost"),
    OGRE_NAME("Ogre"),
    MIMIC_NAME("Mimic");

    public final int value;
    public final float growthRate;
    public final String name;

    EnemyAttribute(int value) {
        this.value = value;
        this.growthRate = 0f;
        this.name = null;
    }

    EnemyAttribute(float growthRate) {
        this.value = 0;
        this.growthRate = growthRate;
        this.name = null;
    }

    EnemyAttribute(String name) {
        this.value = 0;
        this.growthRate = 0f;
        this.name = name;
    }
}
