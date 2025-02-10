package rogue.game.domain.entities.enemies;

import lombok.Getter;
import lombok.Setter;
import rogue.game.domain.enums.EnemyAttribute;
import rogue.game.domain.enums.EntityCharacteristic;
import rogue.game.domain.entities.Position;

/**
 * Represents an Ogre enemy in the game world.
 *
 * <p> Ogres are powerful enemies with high strength but low agility.
 *
 * <p> They can rest for a turn, doubling their attack damage on the next
 * turn but becoming unable to attack.
 */
@Getter
@Setter
public class Ogre extends Enemy {
    private boolean isResting;

    public Ogre(Position position, int complexity) {
        super(
            position,
            EntityCharacteristic.HEALTH,
            (int)(EnemyAttribute.HIGH_HEALTH.value * (1f + EnemyAttribute.HEALTH_GROWTH_RATE.growthRate * (float)complexity)),
            (int)(EnemyAttribute.LOW_AGILITY.value * (1f + EnemyAttribute.AGILITY_GROWTH_RATE.growthRate * (float)complexity)),
            (int)(EnemyAttribute.HIGH_STRENGTH.value * (1f + EnemyAttribute.STRENGTH_GROWTH_RATE.growthRate * (float)complexity)),
            EnemyAttribute.AVERAGE_HOSTILITY.value,
            EnemyAttribute.OGRE_NAME.name
        );

        isResting = false;
    }

    public Ogre(
            Position position,
            EntityCharacteristic damageType,
            int health,
            int agility,
            int strength,
            int hostility
    ) {
        super(position, damageType, health, agility, strength, hostility, EnemyAttribute.OGRE_NAME.name);

        this.isResting = false;
    }

    @Override
    public int getDamage(){
        isResting = true;
        return super.getDamage();
    }

    @Override
    public boolean isAttackGuaranteed() {
        return !isResting;
    }
}
