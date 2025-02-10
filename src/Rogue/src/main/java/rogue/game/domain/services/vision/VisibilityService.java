package rogue.game.domain.services.vision;

import rogue.game.services.map.MapService;
import rogue.game.domain.entities.Position;

/**
 * This interface defines a service responsible for handling visibility calculations within a map.
 */
public interface VisibilityService {
    /**
     * Updates the visibility state of a given {@link Position} on the map by ray casting.
     *
     * <p> This method calculates the visible area around the given position, taking into account the field of
     * view and obstacles.
     * <p> It uses a combination of Von Neumann neighborhood and ray casting techniques to determine the visible cells.
     *
     * @param map The {@link MapService} providing map data.
     * @param pos The {@link Position} to update.
     * @param angle The angle of view.
     */
    void update(MapService map, Position pos, double angle);

    /**
     * Checks if a {@link Position} is visible from another {@link Position} within a corridor.
     *
     * <p> This method performs a simple line-of-sight check to determine if the target position is visible from the starting position.
     * It iterates over the cells between the two positions and checks if any obstacles are present.
     *
     * @param map The {@link MapService} providing map data.
     * @param from The starting {@link Position}.
     * @param to The target {@link Position}.
     * @return `true` if the target {@link Position} is visible, `false` otherwise.
     */
    boolean seeInCorridor(MapService map, Position from, Position to);

    /**
     * Reveals neighboring cells of a given position in a corridor-like pattern.
     *
     * <p> This method iterates through the four adjacent cells of the given position (up, down, left, right)
     * and sets their visibility to true if they are passable.
     */
     void showCorridorCell(MapService map, Position pos);
}
