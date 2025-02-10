package rogue.game.domain.entities;

import lombok.Getter;
import lombok.Setter;
import rogue.game.domain.enums.EntityCharacteristic;
import rogue.game.domain.entities.items.Potion;
import rogue.game.domain.entities.items.Weapon;
import rogue.game.domain.enums.PlayerAttribute;

import java.util.Optional;

/**
 * This class represents the player character in the game.
 *
 * <p> It inherits from both `GameEntity` and `BattleEntity` interfaces,
 * providing information about the player's position and battle attributes.
 */
@Getter
public class Player extends GameEntity implements BattleEntity {
    private static final String NAME = "You";

    @Setter
    private boolean isSleeping;
    @Setter
    private int maxHealth;
    @Setter
    private int health;
    @Setter
    private int agility;
    @Setter
    private int strength;
    @Setter
    private float viewAngle;
    @Setter
    int currentRoomIndex;

    /**
     * The potion currently in effect on the player (can be null).
     */
    private Potion potion;

    /**
     * The weapon currently equipped by the player (can be null).
     */
    private Weapon weapon;

    public Player() {
        super(Position.NONE);
        isSleeping = false;
        maxHealth = PlayerAttribute.START_MAX_HEALTH.value;
        health = PlayerAttribute.START_HEALTH.value;
        agility = PlayerAttribute.START_AGILITY.value;
        strength = PlayerAttribute.START_STRENGTH.value;
        viewAngle = PlayerAttribute.START_ANGLE.value;
        potion = null;
        weapon = null;
        currentRoomIndex = 0;
    }

    public void reset(){
        isSleeping = false;
        maxHealth = PlayerAttribute.START_MAX_HEALTH.value;
        health = PlayerAttribute.START_HEALTH.value;
        agility = PlayerAttribute.START_AGILITY.value;
        strength = PlayerAttribute.START_STRENGTH.value;
        viewAngle = PlayerAttribute.START_ANGLE.value;
        potion = null;
        weapon = null;
        currentRoomIndex = 0;
    }

    public void addAgility(int agility) {
        this.agility += agility;
    }

    @Override
    public int getAgility() {
        return agility + ((potion != null) ?
                        potion.getAttribute() == EntityCharacteristic.AGILITY ? potion.getImprovement() : 0
                        : 0);
    }

    public int getMaxHealth() {
        return maxHealth + ((potion != null) ?
                        potion.getAttribute() == EntityCharacteristic.MAX_HEALTH ? potion.getImprovement() : 0
                        : 0);
    }

    public int getStrength(){
        return strength + ((potion != null) ?
                        potion.getAttribute() == EntityCharacteristic.STRENGTH ? potion.getImprovement() : 0
                        : 0);
    }

    @Override
    public int getSpeed() {
        return 1 + (int)(1.5f * agility);
    }

    /**
     * Reduces the entity's health or max health based on the damage type.
     *
     * @param damage the amount of damage to apply
     * @param damageType the type of damage (health or max health)
     */
    @Override
    public void takeDamage(int damage, EntityCharacteristic damageType) {
        if (damageType == EntityCharacteristic.MAX_HEALTH) {
            if(maxHealth - damage < PlayerAttribute.MIN_POSSIBLE_MAX_HEALTH.value) {
                maxHealth = PlayerAttribute.MIN_POSSIBLE_MAX_HEALTH.value;
            }else{
                maxHealth -= damage;
            }

            if(health > maxHealth)
                health = maxHealth;

        }else if (damageType == EntityCharacteristic.HEALTH) {
            health -= damage;
        }
    }

    @Override
    public int getDamage() {
        return getStrengthWithWeapon();
    }

    @Override
    public EntityCharacteristic getDamageType() {
        return EntityCharacteristic.HEALTH;
    }

    @Override
    public boolean isAlive() {
        return health > 0;
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public boolean canDodge(){
        return false;
    }

    @Override
    public boolean isAttackGuaranteed() { return false; }

    @Override
    public boolean castSleep() {
        return false;
    }

    @Override
    public void setCastSleep(boolean castSleep) {
        isSleeping = castSleep;
    }

    public Optional<Weapon> setWeapon(Weapon weapon) {
        if(this.weapon != null){
            Weapon oldWeapon = this.weapon;
            this.weapon = weapon;
            return Optional.of(oldWeapon);
        }

        this.weapon = weapon;
        return Optional.empty();
    }

    public int getStrengthWithWeapon(){
        return weapon == null ? getStrength() : getStrength() + weapon.getDamage();
    }

    /**
     * Applies a {@link Potion} to the player, updating its properties accordingly.
     *
     * <p> If a potion is already active, its effects are first removed.
     *
     * @param potion the potion to apply
     */
    public void applyPotion(Potion potion) {
        this.potion = potion;
    }

    /**
     * Checks if the active potion has expired and updates the entity's properties accordingly.
     *
     * <p> If the potion has expired, its effects are removed and the updated characteristic is returned.
     *
     * @return the updated characteristic, or an empty Optional if the potion has not expired
     */
    public Optional<EntityCharacteristic> potionUpdate() {
        if(potion != null && potion.getTime() <= 0) {
                EntityCharacteristic type = potion.getAttribute();
                potion = null;
                return Optional.of(type);
        }

        return Optional.empty();
    }
}
