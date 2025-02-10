package rogue.game.domain.entities.enemies;

import rogue.game.domain.enums.EnemyAttribute;
import rogue.game.domain.enums.EntityCharacteristic;
import rogue.game.domain.entities.Position;

/**
 * Represents a Zombie enemy in the game world.
 *
 * <p> Zombies are slow but resilient enemies with average strength.
 */
public class Zombie extends Enemy {
    public Zombie(Position position, int complexity) {
        super(
            position,
            EntityCharacteristic.HEALTH,
            (int)(EnemyAttribute.HIGH_HEALTH.value * (1f + EnemyAttribute.HEALTH_GROWTH_RATE.growthRate * (float)complexity)),
            (int)(EnemyAttribute.LOW_AGILITY.value * (1f + EnemyAttribute.AGILITY_GROWTH_RATE.growthRate * (float)complexity)),
            (int)(EnemyAttribute.AVERAGE_STRENGTH.value * (1f + EnemyAttribute.STRENGTH_GROWTH_RATE.growthRate * (float)complexity)),
            EnemyAttribute.AVERAGE_HOSTILITY.value,
            EnemyAttribute.ZOMBIE_NAME.name
        );
    }

    public Zombie(
        Position position,
        EntityCharacteristic damageType,
        int health,
        int agility,
        int strength,
        int hostility
    ) {
        super(position, damageType, health, agility, strength, hostility, EnemyAttribute.ZOMBIE_NAME.name);
    }
}
