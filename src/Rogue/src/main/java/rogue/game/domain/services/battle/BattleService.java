package rogue.game.domain.services.battle;

import rogue.game.domain.entities.BattleEntity;

/**
 * Service that provides functionality for managing combat operations.
 *
 * <p> This interface defines the operations needed to organize and manage combat
 * between different entities (BattleEntity).
 */
public interface BattleService {
    /**
     * Attempts to attack the defender with the attacker.
     *
     * @param attacker The entity initiating the attack.
     * @param defender The entity being attacked.
     * @return `true` if the attack was successful, `false` otherwise.
     */
    boolean tryToAttack(BattleEntity attacker, BattleEntity defender);
}
