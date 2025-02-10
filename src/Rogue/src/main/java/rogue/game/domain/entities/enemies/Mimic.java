package rogue.game.domain.entities.enemies;

import lombok.Getter;
import lombok.Setter;
import rogue.game.common.enums.MapSymbol;
import rogue.game.domain.enums.EnemyAttribute;
import rogue.game.domain.enums.EntityCharacteristic;
import rogue.game.domain.enums.MimicAttribute;
import rogue.game.domain.entities.Position;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Represents a Mimic enemy in the game world.
 *
 * <p> Mimics are disguised as various in-game items and transform into
 * aggressive enemies when the player gets close.
 */
@Getter
@Setter
public class Mimic extends Enemy {
    private int countTimesPlayerNotVisible = 0;
    private MapSymbol appearance;

    public Mimic(Position position, int complexity) {
        super(
            position,
            EntityCharacteristic.HEALTH,
            (int)(EnemyAttribute.HIGH_HEALTH.value * (1f + EnemyAttribute.HEALTH_GROWTH_RATE.growthRate * (float)complexity)),
            (int)(EnemyAttribute.HIGH_AGILITY.value * (1f + EnemyAttribute.AGILITY_GROWTH_RATE.growthRate * (float)complexity)),
            (int)(EnemyAttribute.LOW_STRENGTH.value * (1f + EnemyAttribute.STRENGTH_GROWTH_RATE.growthRate * (float)complexity)),
            EnemyAttribute.LOW_HOSTILITY.value - 1,
            EnemyAttribute.MIMIC_NAME.name
        );

        this.appearance = MimicAttribute.APPEARANCES_OF_MIMIC.list.get(ThreadLocalRandom.current().nextInt(0, MimicAttribute.APPEARANCES_OF_MIMIC.list.size()));
        this.countTimesPlayerNotVisible = 0;
    }

    public Mimic(
            Position position,
            EntityCharacteristic damageType,
            int health,
            int agility,
            int strength,
            int hostility,
            MapSymbol appearance
    ) {
        super(position, damageType, health, agility, strength, hostility, EnemyAttribute.MIMIC_NAME.name);

        this.countTimesPlayerNotVisible = 0;
        this.appearance = appearance;
    }

    public void setHostility(int hostility) {
        super.setHostility(hostility);
    }
}
