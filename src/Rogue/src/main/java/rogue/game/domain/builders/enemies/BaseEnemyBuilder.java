package rogue.game.domain.builders.enemies;

import rogue.game.domain.builders.BaseEntityBuilder;
import rogue.game.domain.entities.GameEntity;
import rogue.game.domain.enums.EntityCharacteristic;
import rogue.game.domain.entities.enemies.Enemy;

/**
 * Abstract base class for building different types of {@link GameEntity} entities.
 *
 * <p> This class defines common properties for enemies, such as damage type, health,
 * agility, strength, and hostility.
 *
 * <p> It offers methods for setting various enemy attributes, allowing for customization
 * of enemy behavior and difficulty.
 */
public abstract class BaseEnemyBuilder extends BaseEntityBuilder {
    protected EntityCharacteristic damageType;
    protected int health;
    protected int agility;
    protected int strength;
    protected int hostility;

    public BaseEnemyBuilder withDamageType(EntityCharacteristic damageType) {
        this.damageType = damageType;
        return this;
    }

    public BaseEnemyBuilder withHealth(int health) {
        this.health = health;
        return this;
    }

    public BaseEnemyBuilder withAgility(int agility) {
        this.agility = agility;
        return this;
    }

    public BaseEnemyBuilder withStrength(int strength) {
        this.strength = strength;
        return this;
    }

    public BaseEnemyBuilder withHostility(int hostility) {
        this.hostility = hostility;
        return this;
    }

    /**
     * Abstract method that subclasses must implement to create a specific {@link GameEntity} type.
     *
     * <p> Subclasses should use the builder's properties to construct the appropriate {@link GameEntity}
     * object with the specified attributes.
     *
     * @return The constructed {@link GameEntity} ({@link Enemy}).
     */
    @Override
    public abstract GameEntity build();
}
