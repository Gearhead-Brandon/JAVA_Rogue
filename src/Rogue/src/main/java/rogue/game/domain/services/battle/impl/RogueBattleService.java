
package rogue.game.domain.services.battle.impl;

import rogue.game.domain.services.battle.BattleService;
import rogue.game.services.messaging.MessageService;
import rogue.game.domain.entities.BattleEntity;

import java.util.concurrent.ThreadLocalRandom;

public class RogueBattleService implements BattleService {
    private static final int HIT_THRESHOLD = 10;
    private static final String HIT = " hit ";
    private static final String CAST_SLEPT = " cast sleep on ";
    private static final String MISS = " missed ";

    private final MessageService messageService;

    public RogueBattleService(MessageService messageService) {
        this.messageService = messageService;
    }

    /**
     * Attempts to attack a defender with a attacker.
     *
     * <p> This method implements the attack mechanics for a  attacker.
     *
     * <p> It first checks if the defender can dodge. If not, it performs a hit check.
     *
     * <p> If the attack hits, damage is dealt, and there's a chance to cast the 'sleep' spell.
     *
     * <p> If the attack misses, a corresponding message is sent.
     *
     * @param attacker The attacking entity.
     * @param defender The defending entity.
     * @return `true` if the attack was successful, `false` otherwise.
     */
    @Override
    public boolean tryToAttack(final BattleEntity attacker, final BattleEntity defender) {
        boolean result = false;

        if ((!defender.canDodge() && hitCheck(attacker, defender)) || attacker.isAttackGuaranteed()) {
            defender.takeDamage(attacker.getDamage(), attacker.getDamageType());

            messageService.addStatusMessage(attacker.getName() + HIT + defender.getName());

            final boolean castSleep = attacker.castSleep();

            defender.setCastSleep(castSleep);

            if (castSleep)
                messageService.addStatusMessage(attacker.getName() + CAST_SLEPT + defender.getName());

            result = true;
        }else {
            messageService.addStatusMessage(attacker.getName() + MISS + defender.getName());
        }

        return result;
    }

    /**
     * Checks if an attack hits its target.
     *
     * <p> This method determines whether an attack successfully hits its target.
     * <p> The check is based on the difference in agility and speed between the attacker and defender,
     * as well as a random roll.
     *
     * @param attacker The attacking entity.
     * @param defender The defending entity.
     * @return `true` if the attack hits, `false` otherwise.
     */
    private boolean hitCheck(final BattleEntity attacker, final BattleEntity defender) {
        int dexterityDifference = attacker.getAgility() - defender.getAgility();
        int speedDifference = attacker.getSpeed() - defender.getSpeed();

        int modifier = dexterityDifference + speedDifference;

        int roll = ThreadLocalRandom.current().nextInt(0, HIT_THRESHOLD * 2);

        return modifier + roll >= HIT_THRESHOLD;
    }
}
