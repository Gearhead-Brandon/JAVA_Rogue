package rogue.game.domain.factories;

import rogue.game.domain.builders.enemies.*;
import rogue.game.domain.entities.enemies.*;

/**
 * A factory class for creating enemy builders.
 *
 * <p> Provides static methods to create instances of builders responsible
 * for configuring and creating various types of enemies in the game.
 */
public class EnemyBuilderFactory {
    /**
     * @return A new instance of the {@link ComplexityBasedEnemyBuilder} class for all types of {@link Enemy}.
     */
    public static ComplexityBasedEnemyBuilder createComplexityBasedEnemyBuilder(){
        return new ComplexityBasedEnemyBuilder();
    }

    /**
     * @return A new instance of the {@link TypedEnemyBuilder} class
     * for {@link Zombie}, {@link Ghost}, {@link Ogre}, {@link Vampire} and {@link SnakeMagician}.
     */
    public static TypedEnemyBuilder createTypedEnemyBuilder(){
        return new TypedEnemyBuilder();
    }

    /**
     * @return A new instance of the {@link MimicBuilder} class for {@link Mimic}.
     */
    public static MimicBuilder createMimicBuilder(){
        return new MimicBuilder();
    }
}
