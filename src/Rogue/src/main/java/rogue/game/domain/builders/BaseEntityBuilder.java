package rogue.game.domain.builders;

import rogue.game.domain.entities.GameEntity;
import rogue.game.domain.entities.Position;

/**
 * Represents the base builder for creating a {@link GameEntity}.
 *
 * <p> Provides a common way to set the {@link Position} of an {@link GameEntity} and
 * a {@code build} method to build the final entity.
 */
public abstract class BaseEntityBuilder {
    protected Position position = Position.NONE;

    public BaseEntityBuilder withPosition(Position position) {
        this.position = position;
        return this;
    }

    /**
     * Builds and returns the final entity object.
     *
     * <p> Subclasses must implement this method to create the specific entity type.
     *
     * @return The built entity object.
     */
    public abstract GameEntity build();
}
