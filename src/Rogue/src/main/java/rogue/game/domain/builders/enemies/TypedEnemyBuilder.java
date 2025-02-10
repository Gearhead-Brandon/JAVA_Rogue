package rogue.game.domain.builders.enemies;

import rogue.game.domain.entities.GameEntity;
import rogue.game.domain.entities.enemies.*;
import rogue.game.domain.enums.EnemyType;

/**
 * A type-specific {@link Enemy} builder.
 *
 * <p> This class extends {@link BaseEnemyBuilder} and allows for
 * the creation of different {@link Enemy} types besides {@link Mimic}
 * by specifying the {@link EnemyType}.
 *
 * <p> It uses the Builder pattern to allow for flexible
 * and consistent creation of {@link Enemy} objects.
 */
public class TypedEnemyBuilder extends BaseEnemyBuilder {
    private EnemyType type;

    public TypedEnemyBuilder withType(EnemyType type) {
        this.type = type;
        return this;
    }

    /**
     * Creates an enemy object based on the given type.
     *
     * <p> This method uses the {@link  Enemy} class constructors and passes them the inherited
     * properties from `BaseEnemyBuilder` (position, damage type, health, etc.).
     * The specific {@link Enemy} class is selected based on the {@link EnemyType} besides {@link Mimic}.
     *
     * @throws IllegalArgumentException if the specified type is not supported.
     * @return The created {@link GameEntity} ({@link Enemy}) object besides {@link Mimic}.
     */
    @Override
    public GameEntity build() {
        return switch (type) {
            case SNAKE_MAGICIAN -> new SnakeMagician(
                super.position,
                super.damageType,
                super.health,
                super.agility,
                super.strength,
                super.hostility
            );
            case GHOST -> new Ghost(
                super.position,
                super.damageType,
                super.health,
                super.agility,
                super.strength,
                super.hostility
            );
            case OGRE -> new Ogre(
                super.position,
                super.damageType,
                super.health,
                super.agility,
                super.strength,
                super.hostility
            );
            case VAMPIRE -> new Vampire(
                super.position,
                super.damageType,
                super.health,
                super.agility,
                super.strength,
                super.hostility
            );
            case ZOMBIE -> new Zombie(
                super.position,
                super.damageType,
                super.health,
                super.agility,
                super.strength,
                super.hostility
            );
            default -> throw new IllegalArgumentException("Unsupported enemy type: " + type);
        };
    }
}
