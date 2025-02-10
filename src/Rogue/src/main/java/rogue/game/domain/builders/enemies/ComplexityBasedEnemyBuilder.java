package rogue.game.domain.builders.enemies;

import rogue.game.domain.builders.BaseEntityBuilder;
import rogue.game.domain.entities.GameEntity;
import rogue.game.domain.entities.enemies.*;
import rogue.game.domain.enums.EnemyType;

/**
 * Builder class for creating {@link Enemy} based on their {@link EnemyType} and complexity level.
 *
 * <p> This builder allows you to specify the {@link EnemyType} and its complexity,
 * which determines its attributes and behavior.
 */
public class ComplexityBasedEnemyBuilder extends BaseEntityBuilder {
    private EnemyType type;
    private int complexity;

    public final ComplexityBasedEnemyBuilder withType(EnemyType type) {
        this.type = type;
        return this;
    }

    public final ComplexityBasedEnemyBuilder withComplexity(int complexity) {
        this.complexity = complexity;
        return this;
    }

    @Override
    public GameEntity build() {
        return switch (type) {
            case SNAKE_MAGICIAN -> new SnakeMagician(position, complexity - 1);
            case VAMPIRE -> new Vampire(position, complexity - 1);
            case ZOMBIE -> new Zombie(position, complexity - 1);
            case GHOST -> new Ghost(position, complexity - 1);
            case MIMIC -> new Mimic(position, complexity - 1);
            case OGRE -> new Ogre(position, complexity - 1);
        };
    }
}
