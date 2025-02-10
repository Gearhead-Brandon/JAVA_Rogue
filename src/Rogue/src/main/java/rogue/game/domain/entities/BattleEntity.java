package rogue.game.domain.entities;

import rogue.game.domain.enums.EntityCharacteristic;

/**
 * This interface defines the core attributes and behaviors of a battle entity in the game.
 */
public interface BattleEntity{
    int getDamage();
    int getAgility();
    int getSpeed();

    /**
     * Applies damage to the entity
     * @param damage The amount of damage to be applied.
     * @param damageType The type of damage being applied.
     */
    void takeDamage(int damage, EntityCharacteristic damageType);
    EntityCharacteristic getDamageType();
    boolean isAlive();
    String getName();
    boolean canDodge();
    boolean isAttackGuaranteed();

    /**
     * Casts a sleep on the entity.
     * @return True if the sleep was cast, false otherwise.
     */
    boolean castSleep();

    /**
     * Sets whether the entity is sleeping or not.
     * @param castSleep True if the entity is sleeping, false otherwise.
     */
    void setCastSleep(boolean castSleep);
}
