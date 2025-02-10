package rogue.game.domain.entities.enemies;

import lombok.Getter;
import rogue.game.domain.entities.GameEntity;
import rogue.game.domain.enums.EntityCharacteristic;
import rogue.game.domain.entities.Position;
import rogue.game.domain.entities.BattleEntity;

/**
 * Abstract class representing an enemy entity in the game world.
 *
 * <p> Enemies are entities that can attack the player and have specific combat characteristics.
 */
public abstract class Enemy extends GameEntity implements BattleEntity {
    private final EntityCharacteristic damageType;
    private final int baseTreasure;
    @Getter
    private int health;
    private final int agility;
    @Getter
    private final int strength;
    @Getter
    private int hostility;
    private final String name;

    public Enemy(Position position, EntityCharacteristic damageType, int health, int agility, int strength, int hostility, String name) {
        super(position);
        this.damageType = damageType;
        this.baseTreasure = health;
        this.health = health;
        this.agility = agility;
        this.strength = strength;
        this.hostility = hostility;
        this.name = name;
    }

    protected void setHostility(int hostility) {
        this.hostility = hostility;
    }

    @Override
    public int getAgility() {
        return agility;
    }

    @Override
    public int getSpeed() {
        return 1 + (int)(1.5f * agility);
    }

    @Override
    public void takeDamage(int damage, EntityCharacteristic damageType) {
        if (damageType == EntityCharacteristic.HEALTH)
            this.health -= damage;
    }

    @Override
    public int getDamage(){
        return strength;
    }

    @Override
    public EntityCharacteristic getDamageType() {
        return damageType;
    }

    @Override
    public boolean isAlive() {
        return health > 0;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean canDodge(){ return false; }

    @Override
    public boolean isAttackGuaranteed() { return false; }

    @Override
    public boolean castSleep() {
        return false;
    }

    @Override
    public void setCastSleep(boolean castSleep) {}

    public int getTreasure() {
        return (baseTreasure / 2) + hostility + strength + agility;
    }

    public boolean becomeInvisible(){
        return false;
    }
}
