package rogue.game.domain.entities.enemies;

import lombok.Getter;
import lombok.Setter;
import rogue.game.domain.enums.EnemyAttribute;
import rogue.game.domain.entities.Position;
import rogue.game.domain.enums.Constants;
import rogue.game.domain.enums.EntityCharacteristic;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Represents a Vampire enemy in the game world.
 *
 * <p> Vampires are agile enemies with high health that always dodge an attack on their first turn.
 */
@Getter
@Setter
public class Vampire extends Enemy {
    private boolean firstDodge;
    private int direction;

    public Vampire(Position position, int complexity) {
        super(
            position,
            EntityCharacteristic.MAX_HEALTH,
            (int)(EnemyAttribute.HIGH_HEALTH.value * (1f + EnemyAttribute.HEALTH_GROWTH_RATE.growthRate * (float)complexity)),
            (int)(EnemyAttribute.HIGH_AGILITY.value * (1f + EnemyAttribute.AGILITY_GROWTH_RATE.growthRate * (float)complexity)),
            (int)(EnemyAttribute.AVERAGE_STRENGTH.value * (1f + EnemyAttribute.STRENGTH_GROWTH_RATE.growthRate * (float)complexity)),
            EnemyAttribute.HIGH_HOSTILITY.value,
            EnemyAttribute.VAMPIRE_NAME.name
        );

        firstDodge = false;
        direction = ThreadLocalRandom.current().nextInt(Constants.DX.directionsValues.size());
    }

    public Vampire(
            Position position,
            EntityCharacteristic damageType,
            int health,
            int agility,
            int strength,
            int hostility
    ) {
        super(position, damageType, health, agility, strength, hostility, EnemyAttribute.VAMPIRE_NAME.name);

        this.firstDodge = false;
        this.direction = ThreadLocalRandom.current().nextInt(Constants.DX.directionsValues.size());
    }

    @Override
    public boolean canDodge(){
        if(firstDodge){
            firstDodge = false;
            return true;
        }else
            return false;
    }
}
