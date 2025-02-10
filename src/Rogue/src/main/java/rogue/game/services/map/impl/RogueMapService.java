package rogue.game.services.map.impl;

import rogue.game.domain.enums.Constants;
import rogue.game.domain.factories.services.MapGenServiceFactory;
import rogue.game.domain.factories.services.VisibilityServiceFactory;
import rogue.game.domain.services.level.LevelService;
import rogue.game.services.map.MapService;
import rogue.game.domain.services.pubsub.PubSubService;
import rogue.game.domain.services.vision.VisibilityService;
import rogue.game.common.MapInfo;
import rogue.game.common.enums.MapSymbol;
import rogue.game.common.observer.EventType;
import rogue.game.domain.entities.gameMap.GameMap;
import rogue.game.domain.enums.MapAttribute;
import rogue.game.domain.entities.RogueMapInfo;
import rogue.game.domain.entities.level.Room;
import rogue.game.domain.entities.Position;

import java.util.*;

/**
 * Service responsible for managing the game map.
 *
 * <P> This service provides access to map information.
 */
public class RogueMapService implements MapService {
    private final GameMap map;
    private final MapInfo mapInfo;
    private final VisibilityService visibilityService;
    private final PubSubService pubSubService;

    public RogueMapService(PubSubService pubSubService) {
        map = new GameMap();
        mapInfo = new RogueMapInfo(map);
        this.visibilityService = VisibilityServiceFactory.createVisibilityService();
        this.pubSubService = pubSubService;
    }

    @Override
    public void update() {
        pubSubService.notifyObserver(EventType.MAP_UPDATE);
    }

    @Override
    public MapInfo getMapInfo() { return mapInfo; }

    @Override
    public MapSymbol getMapSymbol(int x, int y) { return map.getMapSymbol(x, y); }

    @Override
    public MapSymbol getMapSymbol(Position pos) { return map.getMapSymbol(pos); }

    @Override
    public void setMapSymbol(int x, int y, MapSymbol symbol) { map.setMapSymbol(x, y, symbol);}

    @Override
    public void setMapSymbol(Position pos, MapSymbol symbol) { map.setMapSymbol(pos, symbol); }

    @Override
    public boolean isVisible(Position pos) { return map.isVisible(pos); }

    @Override
    public void setVisible(int x, int y, boolean visible) { map.setVisible(x, y, visible); }

    @Override
    public void hideRoom(Room r){
        int tLeftX = r.getTopLeft().x() + 1;
        int tLeftY = r.getTopLeft().y() + 1;
        int bRightX = r.getBottomRight().x() - 1;
        int bRightY = r.getBottomRight().y() - 1;

        for (int i = tLeftX; i <= bRightX; i++)
            for (int j = tLeftY; j <=  bRightY; j++)
                setVisible(i, j, false);
    }

    @Override
    public void showRoom(Room r){
        int tLeftX = r.getTopLeft().x();
        int tLeftY = r.getTopLeft().y();
        int bRightX = r.getBottomRight().x();
        int bRightY = r.getBottomRight().y();

        for (int i = tLeftX; i <= bRightX; i++)
            for (int j = tLeftY; j <= bRightY; j++)
                setVisible(i, j, true);
    }

    @Override
    public void showCorridorCell(Position pos){
        visibilityService.showCorridorCell(this, pos);
    }

    @Override
    public void updateVisibleArea(Position pos, double angle) {
        visibilityService.update(this, pos, angle);
    }

    @Override
    public boolean seeInCorridor(Position from, Position to) {
        return visibilityService.seeInCorridor(this, from, to);
    }

    @Override
    public Position placeForItem(Position pos) {
        int px = pos.x();
        int py = pos.y();

        // Moore neighborhood
        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                if (getMapSymbol(px + j, py + i).equals(MapSymbol.FLOOR))
                    return Position.of(px + j, py + i);
            }
        }

        return Position.NONE;
    }

    @Override
    public void generate(LevelService levelService, Position playerPosition) {
        reset();
        MapGenServiceFactory.createMapGenService().generate(
                this,
                levelService,
                playerPosition
        );
    }

    private void reset() {
        for(int i = 0; i < MapAttribute.HEIGHT.value; i++){
            for(int j = 0; j < MapAttribute.WIDTH.value; j++){
                map.setMapSymbol(j, i, MapSymbol.EMPTINESS);
                map.setVisible(j, i, false);
            }
        }
    }

    @Override
    public List<Position> findPathByBFS(Position from, Position to, Set<MapSymbol> blockingSymbols) {
        Queue<Position> queue = new ArrayDeque<>();
        Set<Position> visited = new HashSet<>();
        Map<Position, Position> parentMap = new HashMap<>();

        queue.add(from);
        visited.add(from);

        while (!queue.isEmpty()) {
            Position current = queue.poll();

            if (current.equals(to))
                return reconstructPath(parentMap, to);

            for(Position next : getNeighbours(current, blockingSymbols)) {
                MapSymbol sym = map.getMapSymbol(next.x(), next.y());

                if (!visited.contains(next) && sym != MapSymbol.WALL && sym != MapSymbol.EMPTINESS) {
                    parentMap.put(next, current);
                    queue.add(next);
                    visited.add(next);
                }
            }
        }

        return Collections.emptyList();
    }

    /**
     * Gets the neighboring positions of a given position on the map, excluding blocked positions.
     *
     * @param position The current position.
     * @param blockingSymbols A set of symbols representing blocking elements on the map.
     * @return A list of neighboring positions that are not blocked.
     */
    private List<Position> getNeighbours(Position position, Set<MapSymbol> blockingSymbols) {
        List<Position> positions = new ArrayList<>();

        for(int i = 0; i < 4; i++){
            int x = position.x() + Constants.DX.directionsValues.get(i);
            int y = position.y() + Constants.DY.directionsValues.get(i);

            if(x < 0 || y < 0 || y >= MapAttribute.HEIGHT.value || x >= MapAttribute.WIDTH.value)
                continue;

            MapSymbol sym = map.getMapSymbol(x, y);

            if(blockingSymbols.contains(sym))
                continue;

            positions.add(Position.of(x, y));
        }

        return positions;
    }

    /**
     * Reconstructs the path from the target position to the starting position using the parent map.
     *
     * @param parentMap A map containing parent-child relationships between positions.
     * @param goal The target position.
     * @return A list of positions representing the path from the start to the goal.
     */
    private List<Position> reconstructPath(Map<Position, Position> parentMap, Position goal) {
        List<Position> path = new ArrayList<>();
        Position current = goal;

        while (current != null) {
            path.addFirst(current);
            current = parentMap.get(current);
        }

        return path;
    }
}
