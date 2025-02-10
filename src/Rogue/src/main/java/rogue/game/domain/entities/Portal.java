package rogue.game.domain.entities;

/**
 * Represents a portal {@link GameEntity} in the game world.
 *
 * <p> A portal is a special type of {@link GameEntity} that allows the player to transition between different levels of the game.
 */
public class Portal extends GameEntity {
    public Portal(Position position) { super(position); }
}
