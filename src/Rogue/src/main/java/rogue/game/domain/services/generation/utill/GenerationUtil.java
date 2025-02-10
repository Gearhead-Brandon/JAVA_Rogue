package rogue.game.domain.services.generation.utill;

import rogue.game.domain.entities.GameEntity;
import rogue.game.domain.entities.Position;
import rogue.game.domain.entities.level.Door;
import rogue.game.domain.entities.level.Room;
import rogue.game.domain.enums.Direction;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

/**
 * This class contains helper methods for generating random {@link Position}s
 * within the level's {@link Room} and founding room neighbors. These methods ensure that the generated {@link Position} do not intersect with existing objects or obstacles.
 */
public class GenerationUtil {
    /**
     * Generates a random {@link Position} within a room, avoiding occupied cells and door positions.
     *
     * <p> This method ensures that the generated {@link Position} is not on top of
     * an existing {@link GameEntity} or {@link Door}.
     *
     * @param r The {@link Room} in which to generate the position.
     * @return A random{@link Position} within the room's bounds.
     */
    public static Position generateEntityRandomCoordinates(Room r){
        int tLeftX = r.getTopLeft().x();
        int tLeftY = r.getTopLeft().y();
        int bRightX = r.getBottomRight().x();
        int bRightY = r.getBottomRight().y();

        int x;
        int y;

        do{
            x = ThreadLocalRandom.current().nextInt(bRightX - tLeftX - 1) + 1 + tLeftX;
            y = ThreadLocalRandom.current().nextInt(bRightY - tLeftY - 1) + 1 + tLeftY;
        }while(r.checkIfCellIsOccupied(Position.of(x, y)) || checkIfDoorIsOverlapped(r, x, y));

        return Position.of(x, y);
    }

    /**
     * Checks if a given {@link Position} within a {@link Room} overlaps with a {@link Door}.
     *
     * <p> This method checks the eight neighboring cells of the given position to see if any of
     * them coincide with a {@link Door}.
     *
     * @param r The room to check.
     * @param x The x-coordinate of the position.
     * @param y The y-coordinate of the position.
     * @return `true` if the position overlaps with a door, `false` otherwise.
     */
    private static boolean checkIfDoorIsOverlapped(Room r, final int x, final int y) {
        // Von Neumann neighborhood
        List<Direction> directions = List.of(Direction.values());

        for (int i = -1; i <= 1; i += 2) {
            for (Direction d : directions) {
                Position door = r.getDoorByDirection(d).map(Door::getPosition).orElse(Position.NONE);
                if (door != Position.NONE && door.x() + i == x && door.y() == y)
                    return true;
            }
        }

        for (int j = -1; j <= 1; j += 2) {
            for (Direction d : directions) {
                Position door = r.getDoorByDirection(d).map(Door::getPosition).orElse(Position.NONE);
                if (door != Position.NONE && door.x() == x && door.y() + j == y)
                    return true;
            }
        }

        return false;
    }

    /**
     * Retrieves a list of rooms directly connected to a given room based on its connection information.
     *
     * @param room The {@link Room} for which to retrieve connected rooms.
     */
    public static List<Room> getRoommatesByConnections(final Room room) {
        List <Room> result = new ArrayList<>();

        for(Direction d : Direction.values()) {
            Optional<Room> connection = room.getConnectionRoom(d);
            connection.ifPresent(result::add);
        }

        return result;
    }
}
