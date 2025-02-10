package rogue.game.domain.entities.enemies;

import rogue.game.domain.enums.EnemyAttribute;
import rogue.game.domain.entities.Position;
import rogue.game.domain.enums.EntityCharacteristic;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Represents a Snake Magician enemy in the game world.
 *
 * <p> Snake Magicians are agile enemies that have a chance to cast a sleep spell on the player.
 */
public class SnakeMagician extends Enemy {
    private static final double SLEEP_PROBABILITY = 0.75;

    public SnakeMagician(Position position, int complexity) {
        super(
            position,
            EntityCharacteristic.HEALTH,
            (int)(EnemyAttribute.HIGH_HEALTH.value * (1f + EnemyAttribute.HEALTH_GROWTH_RATE.growthRate * (float)complexity)),
            (int)(EnemyAttribute.VERY_HIGH_AGILITY.value * (1f + EnemyAttribute.AGILITY_GROWTH_RATE.growthRate * (float)complexity)),
            (int)(EnemyAttribute.LOW_STRENGTH.value * (1f + EnemyAttribute.STRENGTH_GROWTH_RATE.growthRate * (float)complexity)),
            EnemyAttribute.HIGH_HOSTILITY.value,
            EnemyAttribute.SNAKE_MAGICIAN_NAME.name
        );
    }

    public SnakeMagician(
            Position position,
            EntityCharacteristic damageType,
            int health,
            int agility,
            int strength,
            int hostility
    ) {
        super(position, damageType, health, agility, strength, hostility, EnemyAttribute.SNAKE_MAGICIAN_NAME.name);
    }


    @Override
    public boolean castSleep() {
        return ThreadLocalRandom.current().nextDouble() > SLEEP_PROBABILITY;
    }
}
