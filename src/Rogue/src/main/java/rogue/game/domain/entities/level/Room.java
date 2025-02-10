package rogue.game.domain.entities.level;

import lombok.Getter;
import lombok.Setter;
import rogue.game.common.enums.MapColor;
import rogue.game.domain.enums.Constants;
import rogue.game.domain.enums.Direction;
import rogue.game.domain.enums.LevelAttribute;
import rogue.game.domain.entities.Position;
import rogue.game.domain.entities.GameEntity;
import rogue.game.domain.entities.items.Key;

import java.util.*;

/**
 * Represents a room in the {@link Level}.
 *
 * <p> A room has various attributes including its sector, top-left and bottom-right positions,
 * a {@link List} of {@link Door}, a {@link List} of connected {@link Room}, and a list of {@link GameEntity} present in the room.
 */
public class Room{
    @Setter
    @Getter
    private int sector;
    @Setter
    @Getter
    private int grid_i;
    @Setter
    @Getter
    private int grid_j;
    @Setter
    @Getter
    private Position topLeft;
    @Setter
    @Getter
    private Position bottomRight;
    private final List<Door> doors;
    private final List<Room> connections;
    private final List<GameEntity> entities;

    public Room(){
        sector = Constants.NONE.value;
        grid_i = Constants.NONE.value;
        grid_j = Constants.NONE.value;

        topLeft = Position.NONE;
        bottomRight = Position.NONE;

        doors = Arrays.asList(null, null, null, null);

        connections = Arrays.asList(null, null, null, null);

        entities = new ArrayList<>(LevelAttribute.MAX_ENTITIES_PER_ROOM.value);
    }

    /**
     * Sets the connection to another {@link Room} in a specific direction (index).
     *
     * @param index   The index of the connection (corresponding to a direction).
     * @param room    The {@link Room} to connect to.
     */
    public void setConnection(int index, Room room) {
        connections.set(index, room);
    }

    /**
     * Sets the connection to another {@link Room} in a specific direction (using {@link Direction}).
     *
     * @param direction {@link Direction} of the connection.
     * @param room      The {@link Room} to connect to.
     */
    public void setConnection(Direction direction, Room room){ connections.set(direction.value, room);}

    /**
     * Gets an optional containing the connected {@link Room} in a specified {@link Direction}.
     *
     * @param direction The {@link Direction}of the connection.
     * @return An Optional containing the connected {@link Room} if it exists, or empty if not.
     */
    public Optional<Room> getConnectionRoom(Direction direction){ return Optional.ofNullable(connections.get(direction.value)); }

    public int getCountOfConnections(){
        return (int)connections.stream().filter(Objects::nonNull).count();
    }

    /**
     * Sets the door at a specific {@link Direction} with a given {@link Position}.
     *
     * @param direction The {@link Direction} of the door.
     * @param pos        The {@link Position} of the door on the map.
     */
    public void setDoorByDirection(Direction direction, Position pos){ doors.set(direction.value, new Door(pos)); }

    /**
     * Gets an optional containing the door in a specified {@link Direction}.
     *
     * @param direction The {@link Direction} of the door.
     * @return An {@link Optional} containing the {@link Door} if it exists, or empty if not.
     */
    public Optional<Door> getDoorByDirection(Direction direction){ return Optional.ofNullable(doors.get(direction.value)); }

    /**
     * Checks if a specific cell position within the room is occupied by an {@link GameEntity} (excluding the empty entity).
     *
     * @param pos The {@link Position} of the cell to check.
     * @return True if the cell is occupied, false otherwise.
     */
    public boolean checkIfCellIsOccupied(Position pos){
        return getEntityByPosition(pos) != GameEntity.EMPTY_ENTITY;
    }

    public void addEntity(GameEntity entity){
        entities.add(entity);
    }

    public List<Door> getDoors(){ return Collections.unmodifiableList(doors); }

    public List<GameEntity> getEntities(){ return Collections.unmodifiableList(entities); }

    /**
     * Checks if the {@link Room} contains any keys ({@link GameEntity} of type {@link Key}).
     *
     * @return True if there's at least one key in the room, false otherwise.
     */
    public boolean containAnyKey(){
        return entities.stream().anyMatch(entity -> entity instanceof Key);
    }

    /**
     * Checks if the room has a {@link Door} with a specific {@link MapColor}.
     *
     * @param color The {@link MapColor} to check for.
     * @return True if there's a {@link Door} with the specified color, false otherwise.
     */
    public boolean containDoorThisColor(MapColor color){
        return doors.stream().anyMatch(door -> door != null && door.getColor() == color);
    }

    public GameEntity getEntityByPosition(Position pos){
        return entities.stream()
                .filter(entity -> entity.getPosition().x() == pos.x() && entity.getPosition().y() == pos.y())
                .findFirst()
                .orElse(GameEntity.EMPTY_ENTITY);
    }

    public boolean isPointInside(Position elementPos) {
        if(elementPos == Position.NONE)
            return false;

        int tLeftX = topLeft.x();
        int tLeftY = topLeft.y();
        int bRightX = bottomRight.x();
        int bRightY = bottomRight.y();

        int x = elementPos.x();
        int y = elementPos.y();

        return (x >= tLeftX && x <= bRightX) && (y >= tLeftY && y <= bRightY);
    }

    /**
     * Removes a specific {@link GameEntity} from the room's {@link List} of {@link GameEntity}.
     *
     * @param entity The {@link GameEntity} to remove.
     */
    public void removeEntity(GameEntity entity){ entities.remove(entity); }

    /**
     * Removes all keys ({@link GameEntity} of {@link Key}) from the room.
     */
    public void removeKeys(){
        entities.removeIf(entity -> entity instanceof Key);
    }
}
