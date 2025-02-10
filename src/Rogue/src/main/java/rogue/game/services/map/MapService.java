package rogue.game.services.map;

import rogue.game.domain.services.level.LevelService;
import rogue.game.common.MapInfo;
import rogue.game.common.enums.MapSymbol;
import rogue.game.domain.entities.level.Room;
import rogue.game.domain.entities.Position;

import java.util.List;
import java.util.Set;

public interface MapService {
    void update();

    /**
     * Retrieves information about the current state of the map.
     *
     * @return The current map information.
     */
    MapInfo getMapInfo();

    MapSymbol getMapSymbol(int x, int y);
    MapSymbol getMapSymbol(Position pos);
    void setMapSymbol(int x, int y, MapSymbol symbol);
    void setMapSymbol(Position pos, MapSymbol symbol);
    boolean isVisible(Position pos);
    void setVisible(int x, int y, boolean visible);
    void hideRoom(Room r);
    void showRoom(Room r);

    /**
     * Shows a specific corridor cell on the map and its neighbors if they are a corridor or a door.
     *
     * @param pos The position of the corridor cell.
     */
    void showCorridorCell(Position pos);

    /**
     * Generates a new level of the map.
     *
     * @param level The level service, used to generate level information.
     * @param playerPosition The position of the player.
     */
    void generate(LevelService level, Position playerPosition);

    /**
     * Updates the visible area of the map based on the player's position and field of view.
     *
     * @param pos The player's current position.
     * @param angle The player's field of view angle.
     */
    void updateVisibleArea(Position pos, double angle);

    /**
     * Checks if the position is visible if the start position is in the corridor
     *
     * @param from Start position.
     * @param to Target position.
     * @return True if the target position is visible from the start position, false otherwise.
     */
    boolean seeInCorridor(Position from, Position to);

    /**
     * Finds a suitable position to place an element on the map for adjacent cells.
     *
     * @param pos The starting position to search for.
     * @return The found position to place the element, or null if no suitable position was found.
     */
    Position placeForItem(Position pos);

    /**
     * Finds the shortest path between two positions on the map using the breadth-first search (BFS) algorithm.
     *
     * <p> The algorithm sequentially visits all neighboring cells, marking them as visited and adding them to a queue.
     * <p> The process continues until the target cell is found or the queue is empty.
     * <p> Guarantees to find the shortest path if one exists.
     *
     * @param from The starting position of the search.
     * @param to The target position of the search.
     * @param blockingSymbols A set of symbols representing obstacles on the map.
     * @return A list of positions representing the found path, or an empty list if no path was found.
     */
    List<Position> findPathByBFS(Position from, Position to, Set<MapSymbol> blockingSymbols);
}
