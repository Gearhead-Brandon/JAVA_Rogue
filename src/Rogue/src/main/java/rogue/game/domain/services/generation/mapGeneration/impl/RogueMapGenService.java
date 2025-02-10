package rogue.game.domain.services.generation.mapGeneration.impl;

import lombok.NoArgsConstructor;
import rogue.game.domain.services.level.LevelService;
import rogue.game.domain.services.generation.mapGeneration.MapGenService;
import rogue.game.services.map.MapService;
import rogue.game.common.enums.MapColor;
import rogue.game.common.enums.MapSymbol;
import rogue.game.domain.enums.util.MapSymbolUtil;
import rogue.game.domain.entities.enemies.Enemy;
import rogue.game.domain.entities.GameEntity;
import rogue.game.domain.entities.level.*;
import rogue.game.domain.enums.CorridorType;
import rogue.game.domain.enums.Direction;
import rogue.game.domain.enums.LevelAttribute;
import rogue.game.domain.enums.util.SymbolMapper;
import rogue.game.domain.entities.Position;

import java.util.Optional;

/**
 * This class defines helper methods for converting {@link Level} data
 * ({@link Room}, {@link Corridor}, {@link Door}) into map representation
 * using the provided {@link MapService} instance.
 */
@NoArgsConstructor
public class RogueMapGenService implements MapGenService {
    @Override
    public void generate(MapService mapService, LevelService level, Position playerPosition) {
        roomsToMap(mapService, level);
        corridorsToMap(mapService, level);
        doorsToMap(mapService, level);
        entitiesToMap(mapService, level, playerPosition);
    }

    /**
     * Converts all rooms from the current {@link Level} (obtained from {@link LevelService}) into
     * map representation using the provided {@link MapService} instance.
     *
     * <p>This method iterates through all rooms in the {@link Level}, retrieves their top-left and bottom-right corners,
     * and calls separate methods to draw the room's outer walls and fill the inner area.
     *
     * @param map The service responsible for managing the map data
     * @param level The service responsible for managing the current level
     */
    private void roomsToMap(MapService map, LevelService level){
        for(int i = 0; i < LevelAttribute.ROOMS_NUMBER.value; i++){
            Position tRoomCorner = level.getRoom(i).getTopLeft();
            Position bRoomCorner = level.getRoom(i).getBottomRight();
            rectangleToMap(map, tRoomCorner, bRoomCorner);
            fillRectangle(map, tRoomCorner, bRoomCorner);
        }
    }

    /**
     * Draws a rectangle on the map using the provided top-left and bottom-right corner {@link Position}.
     *
     * <p> This method extracts x and y coordinates from the positions, iterates through the edges of the rectangle,
     * and sets the corresponding {@link MapSymbol} using the MapService's {@link MapService#setMapSymbol(int, int, MapSymbol)} method.
     *
     * @param map The service responsible for managing the map data.
     * @param top The top-left corner position of the rectangle ({@link Position}).
     * @param bot The bottom-right corner position of the rectangle ({@link Position}).
     */
    private void rectangleToMap(MapService map, Position top,  Position bot){
        final int topY = top.y();
        final int topX = top.x();

        final int botY = bot.y();
        final int botX = bot.x();

        final MapSymbol wall = MapSymbol.WALL;

        map.setMapSymbol(topX, topY, wall);

        int i = topX + 1;

        for(; i < botX; i++){
            map.setMapSymbol(i, topY, wall);
        }

        map.setMapSymbol(i, topY, wall);

        for(int j = topY + 1; j < botY; j++){
            map.setMapSymbol(topX, j, wall);
            map.setMapSymbol(botX, j, wall);
        }

        map.setMapSymbol(topX, botY, wall);
        i = topX + 1;

        for(; i < botX; i++){
            map.setMapSymbol(i, botY, wall);
        }

        map.setMapSymbol(i, botY, wall);
    }

    /**
     * Fills the inner area of a rectangle on the map, typically used to represent the floor of a room.
     *
     * @param map The service responsible for managing the map data.
     * @param top The top-left corner position of the rectangle ({@link Position}).
     * @param bot The bottom-right corner position of the rectangle ({@link Position}).
     */
    private void fillRectangle(MapService map, Position top, Position bot){
        final int topY = top.y() + 1;
        final int topX = top.x() + 1;

        final int botY = bot.y();
        final int botX = bot.x();

        for (int i = topY; i < botY; i++) {
            for(int j = topX; j < botX; j++){
                map.setMapSymbol(j, i, MapSymbol.FLOOR);
            }
        }
    }

    /**
     * Converts all corridors from the current {@link Level} (obtained from {@link LevelService}) into map representation
     * using the provided {@link MapService} instance.
     *
     * <p> For each corridor, it calls the appropriate drawing method based on the {@link CorridorType}
     * (LEFT_TO_RIGHT, LEFT_TURN, RIGHT_TURN, or TOP_TO_BOTTOM).
     *
     * @param map The service responsible for managing the map data.
     * @param level The service responsible for managing the current {@link Level} ({@link LevelService}).
     */
    private void corridorsToMap(MapService map, LevelService level){
        level.getCorridors().forEach(c -> drawCorridor(map, c));
    }

    /**
     * Draws a corridor on the map based on its {@link CorridorType} (provided in the {@link Corridor} object).
     *
     * @param map The service responsible for managing the map data.
     * @param c The {@link Corridor} object containing its {@link CorridorType} and connection points ({@link Corridor}).
     */
    private void drawCorridor(MapService map, Corridor c){
        switch (c.getType()) {
            case CorridorType.LEFT_TO_RIGHT -> drawLeftToRightCorridor(map, c);
            case CorridorType.LEFT_TURN , CorridorType.RIGHT_TURN -> drawCorridorTurn(map, c);
            case CorridorType.TOP_TO_BOTTOM -> drawTopToBottomCorridor(map, c);
            default -> {}
        }
    }

    /**
     * Draws a straight corridor from left to right on the map.
     *
     * @param map The service responsible for managing the map data.
     * @param c The corridor object containing its connection points ({@link Corridor}).
     */
    private void drawLeftToRightCorridor(MapService map, Corridor c){
        drawHorizontalLine(map, c.getPoint(0), c.getPoint(1));
        drawVerticalLine(map, c.getPoint(1), c.getPoint(2));
        drawHorizontalLine(map, c.getPoint(2), c.getPoint(3));
    }

    /**
     * Draws a corridor with a left or right turn on the map.
     *
     * @param map The service responsible for managing the map data.
     * @param c The corridor object containing its connection points ({@link Corridor}).
     */
    private void drawCorridorTurn(MapService map, Corridor c){
        drawVerticalLine(map, c.getPoint(0), c.getPoint(1));
        drawHorizontalLine(map, c.getPoint(1), c.getPoint(2));
    }

    /**
     * Draws a straight corridor from top to bottom on the map.
     *
     * @param map The service responsible for managing the map data.
     * @param c The corridor object containing its connection points ({@link Corridor}).
     */
    private void drawTopToBottomCorridor(MapService map, Corridor c){
        drawVerticalLine(map, c.getPoint(0), c.getPoint(1));
        drawHorizontalLine(map, c.getPoint(1), c.getPoint(2));
        drawVerticalLine(map, c.getPoint(2), c.getPoint(3));
    }

    /**
     * Draws a horizontal line on the map between two positions.
     *
     * @param map The service responsible for managing the map data.
     * @param first The first position defining the starting point of the line ({@link Position}).
     * @param second The second position defining the ending point of the line ({@link Position}).
     */
    private void drawHorizontalLine(MapService map, Position first, Position second){
        for(int x = Math.min(first.x(), second.x()); x <= Math.max(first.x(), second.x()); x++)
            map.setMapSymbol(x, first.y(), MapSymbol.CORRIDOR);
    }

    /**
     * Draws a vertical line on the map between two positions.
     *
     * @param map The service responsible for managing the map data.
     * @param first The first position defining the starting point of the line ({@link Position}).
     * @param second The second position defining the ending point of the line ({@link Position}).
     */
    private void drawVerticalLine(MapService map, Position first, Position second){
        for(int y = Math.min(first.y(), second.y()); y <= Math.max(first.y(), second.y()); y++)
            map.setMapSymbol(first.x(), y, MapSymbol.CORRIDOR);
    }

    /**
     * Converts all doors from the current {@link Level} (obtained from {@link LevelService}) into map representation
     * using the provided {@link MapService} instance.
     *
     * @param map The service responsible for managing the map data.
     * @param level The service responsible for managing the current {@link Level} ({@link LevelService}).
     */
    private void doorsToMap(MapService map, LevelService level){
        for(int i = 0; i < LevelAttribute.ROOMS_NUMBER.value; i++){
            Room room = level.getRoom(i);

            for(Direction d : Direction.values()) {
                Optional<Door> door = room.getDoorByDirection(d);
                Position pos = door.map(Door::getPosition).orElse(Position.NONE);

                if (pos != Position.NONE) {
                    MapColor color = door.get().getColor();
                    map.setMapSymbol(pos, MapSymbolUtil.getDoorSymbolByColor(color));
                }
            }
        }
    }

    /**
     * Converts all {@link GameEntity} (excluding the player) from the current {@link Level}
     * and the player position into map representation using the provided {@link MapService} instance.
     *
     * <p> This method iterates through all rooms in the {@link Level} and retrieves all {@link GameEntity} within each room using.
     * It then iterates through each {@link GameEntity} and uses {@link SymbolMapper} to get the
     * appropriate {@link MapSymbol} for that {@link GameEntity} type.
     *
     * @param map The service responsible for managing the map data.
     * @param level The service responsible for managing the current {@link Level} ({@link LevelService}).
     * @param playerPosition The current position of the player ({@link Position}).
     */
    private void entitiesToMap(MapService map, LevelService level, Position playerPosition) {
        for(int i = 0; i < LevelAttribute.ROOMS_NUMBER.value; i++){
            Room room = level.getRoom(i);
            for(GameEntity e : room.getEntities())
                map.setMapSymbol(e.getPosition(), SymbolMapper.map(e));
        }

        for(Enemy e : level.getEnemiesList())
            map.setMapSymbol(e.getPosition(), SymbolMapper.map(e));

        map.setMapSymbol(playerPosition, MapSymbol.PLAYER);
    }
}
