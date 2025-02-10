package rogue.game.domain.entities.enemies;

import lombok.Setter;
import rogue.game.domain.enums.EnemyAttribute;
import rogue.game.domain.enums.EntityCharacteristic;
import rogue.game.domain.entities.Position;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Represents a ghost enemy in the game world.
 *
 * <p>Ghosts are agile enemies that can turn invisible and
 * move randomly around the room, making them difficult to attack.
 */
@Setter
public class Ghost extends Enemy {
    private static final float PROBABILITY_OF_BECOMING_INVISIBLE = 0.3f;

    /**
     * The player pursuit flag is needed to not become invisible
     */
    private boolean playerPursuit;

    public Ghost(Position position, int complexity) {
        super(
            position,
            EntityCharacteristic.HEALTH,
            (int)(EnemyAttribute.LOW_HEALTH.value * (1f + EnemyAttribute.HEALTH_GROWTH_RATE.growthRate * (float)complexity)),
            (int)(EnemyAttribute.HIGH_AGILITY.value * (1f + EnemyAttribute.AGILITY_GROWTH_RATE.growthRate * (float)complexity)),
            (int)(EnemyAttribute.LOW_STRENGTH.value * (1f + EnemyAttribute.STRENGTH_GROWTH_RATE.growthRate * (float)complexity)),
            EnemyAttribute.LOW_HOSTILITY.value,
            EnemyAttribute.GHOST_NAME.name
        );

        playerPursuit = false;
    }

    public Ghost(
            Position position,
            EntityCharacteristic damageType,
            int health,
            int agility,
            int strength,
            int hostility
    ) {
        super(position, damageType, health, agility, strength, hostility, EnemyAttribute.GHOST_NAME.name);
        this.playerPursuit = false;
    }

    @Override
    public boolean becomeInvisible(){
        if(playerPursuit)
            return false;

        final double rand = ThreadLocalRandom.current().nextDouble();
        return rand < PROBABILITY_OF_BECOMING_INVISIBLE;
    }
}
