package rogue.game.domain.entities;

import lombok.Getter;
import lombok.Setter;

/**
 * Abstract class representing a generic entity within the game world
 * .
 * <p> Provides a base implementation for entities with a position on the game map.
 */
@Setter
@Getter
public abstract class GameEntity {
    public static final GameEntity EMPTY_ENTITY = new GameEntity(Position.NONE) {};

    private Position position;

    public GameEntity(Position position){
        this.position = position;
    }
}
