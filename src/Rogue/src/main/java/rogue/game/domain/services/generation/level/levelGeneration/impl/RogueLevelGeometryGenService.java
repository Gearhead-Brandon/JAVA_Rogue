package rogue.game.domain.services.generation.level.levelGeneration.impl;

import rogue.game.domain.services.generation.level.levelGeneration.LevelGeometryGenService;
import rogue.game.domain.services.generation.utill.GenerationUtil;
import rogue.game.domain.services.level.LevelService;
import rogue.game.domain.entities.level.Level;
import rogue.game.domain.entities.level.Corridor;
import rogue.game.domain.entities.level.Door;
import rogue.game.domain.entities.level.Room;
import rogue.game.domain.enums.*;
import rogue.game.domain.enums.Direction;
import rogue.game.domain.entities.Position;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.BiPredicate;

/**
 * A service for generating geometry for roguelike levels.
 *
 * <p> Implements procedural level generation, creating random and non-linear maps.
 */
public class RogueLevelGeometryGenService implements LevelGeometryGenService {
    private final List<List<Room>> rooms;

    public RogueLevelGeometryGenService() {
        rooms = new ArrayList<>(LevelAttribute.ROOMS_PER_SIDE.value + 2);
    }

    private void reset() {
        rooms.clear();

        for (int i = 0; i < LevelAttribute.ROOMS_PER_SIDE.value + 2; i++) {
            rooms.add(new ArrayList<>(LevelAttribute.ROOMS_PER_SIDE.value + 2));
            for (int j = 0; j < LevelAttribute.ROOMS_PER_SIDE.value + 2; j++) {
                rooms.get(i).add(new Room());
            }
        }
    }

    @Override
    public void generateGeometry(LevelService levelService) {
        reset();
        generateSectors(levelService);
        generateConnections(levelService);
        generateRoomsGeometry();
        generateCorridorsGeometry(levelService);
    }

    /**
     * Assigns sectors and grid coordinates to each room in the generated level.
     *
     * <p> This method iterates over each room in the `rooms` list, assigning a unique sector number to each room.
     *
     * @param levelService The service responsible for managing {@link Level} data.
     */
    private void generateSectors(LevelService levelService) {
        int sector = 0;
        for (int i = 1; i < LevelAttribute.ROOMS_PER_SIDE.value + 1; i++) {
            for (int j = 1; j < LevelAttribute.ROOMS_PER_SIDE.value + 1; j++) {
                final Room r = rooms.get(i).get(j);
                r.setSector(sector++);
                r.setGrid_i(i);
                r.setGrid_j(j);
                levelService.addRoom(r);
            }
        }
    }

    /**
     * Generates connections between rooms in the level using a Depth-First Search (DFS) algorithm.
     *
     * @param levelService The service responsible for managing {@link Level} data.
     */
    private void generateConnections(LevelService levelService) {
        do {
            generateConnectionsThroughDFS();
        }while (!checkRoomsConnectivity(levelService));
    }

    /**
     * Generates connections between rooms in the level using a Depth-First Search (DFS) algorithm.
     *
     * <p> This method implements a probabilistic DFS approach to connect rooms in the level.
     * <p> It starts from a random room and explores its neighbors, adding them to the stack for further exploration.
     * <p> A random probability is used to determine whether to connect to a neighbor, allowing for a certain degree of randomness in the level generation.
     */
     private void generateConnectionsThroughDFS() {
        Set<Room> visited = new TreeSet<>(Comparator.comparingInt(Room::getSector));
        Deque<Room> stack = new ArrayDeque<>();

        int randomIndex = ThreadLocalRandom.current().nextInt(9);

        Room startRoom = rooms.get(randomIndex / 3 + 1).get(randomIndex % 3 + 1);

        stack.add(startRoom);
        visited.add(startRoom);

        while (!stack.isEmpty()) {
            Room currentRoom = stack.pop();

            List<Room> neighbors = getRoommates(currentRoom);
            Collections.shuffle(neighbors);

            for(var neighbor: neighbors) {
                if (!visited.contains(neighbor) || ThreadLocalRandom.current().nextDouble(0.0, 1.0) < 0.3) {
                    Optional<Direction> d = calcDirection(neighbor, currentRoom);

                    if (d.isPresent()) {
                        neighbor.setConnection(d.get(), currentRoom);
                        currentRoom.setConnection((d.get().value + 2) % 4, neighbor);

                        if (!visited.contains(neighbor)) {
                            stack.addFirst(neighbor);
                            visited.add(neighbor);
                        }
                    }
                }
            }
        }
    }

    /**
     * Calculates the direction from one room to another based on their sector numbers.
     *
     * @param first The first room.
     * @param second The second room.
     * @return An `Optional` containing the direction from `room1` to `room2`, or empty if not applicable.
     */
    private Optional<Direction> calcDirection(final Room first, final Room second) {
        final int sector1 = first.getSector();
        final int sector2 = second.getSector();

        return Optional.ofNullable(switch (sector2 - sector1) {
            case 1 -> Direction.RIGHT;
            case -1 -> Direction.LEFT;
            case 3 -> Direction.BOTTOM;
            case -3 -> Direction.TOP;
            default -> null;
        });
    }

    /**
     * Checks if all rooms in the level are connected using a Breadth-First Search (BFS) algorithm.
     *
     * <p> This method performs a BFS traversal starting from any room in the level.
     * It keeps track of visited rooms and explores their neighbors using a queue.
     * If all rooms are eventually visited during the traversal, it means the level is connected.
     * @param levelService The service responsible for managing {@link Level} data.
     */
     private boolean checkRoomsConnectivity(LevelService levelService) {
        Set<Room> visited = new TreeSet<>(Comparator.comparingInt(Room::getSector));
        Deque<Room> queue = new ArrayDeque<>();

        for (int i = 0; i < LevelAttribute.ROOMS_NUMBER.value ; i++) {
            Room room = levelService.getRoom(i);

            queue.add(room);
            visited.add(room);

            while (!queue.isEmpty()) {
                Room currentRoom = queue.poll();
                for (Room neighbor : GenerationUtil.getRoommatesByConnections(currentRoom)) {
                    if (!visited.contains(neighbor)) {
                        queue.add(neighbor);
                        visited.add(neighbor);
                    }
                }
            }

            if (visited.size() != LevelAttribute.ROOMS_NUMBER.value) {
                for(int j = 0; j < i; j++) {
                    Room currentRoom = levelService.getRoom(j);
                    for(Direction d : Direction.values())
                        currentRoom.setConnection(d, null);
                }

                return false;
            }

            visited.clear();
        }

        return true;
    }

    /**
     * Finds the neighboring rooms of a given room within the grid, excluding the room itself.
     *
     * @param currentRoom The {@link Room} for which to retrieve neighboring rooms.
     */
     private List<Room> getRoommates(final Room currentRoom) {
        int row = 1, col = 1;

        for(int i = 1; i < rooms.size(); i++) {
            for(int j = 1; j < rooms.get(i).size(); j++) {
                if(rooms.get(i).get(j).equals(currentRoom)) {
                    row = i;
                    col = j;
                    break;
                }
            }
        }

        return getInternalRoomNeighbors(currentRoom, row, col);
    }

    /**
     * Finds the neighboring rooms of a given room within the grid, excluding the room itself.
     *
     * <p> This method takes a room and its grid coordinates as input. It calculates the valid
     * range of row and column indices for neighboring rooms, ensuring that they are within the bounds of the grid.
     *
     * <p> It then iterates through the neighboring positions (up, down, left, right) and
     * checks if the corresponding room exists and is not the current room.
     *
     * <p> If a valid neighbor is found, it is added to the result list.
     *
     * @param currentRoom The {@link Room} for which to retrieve neighboring rooms.
     * @param row The row index of the current room.
     * @param col The column index of the current room.
     */
    private List<Room> getInternalRoomNeighbors(Room currentRoom, int row, int col) {
        List <Room> result = new ArrayList<>();

        final List<Integer> dx = List.of(1, 0, -1, 0);
        final List<Integer> dy = List.of(0, 1, 0, -1);

        final int minRow = Math.max(row - 1, 1);
        final int maxRow = Math.min(row + 1, 3);
        final int minCol = Math.max(col - 1, 1);
        final int maxCol = Math.min(col + 1, 3);

        BiPredicate<Integer, Integer> inBounds = (i, j) -> i >= minRow && i <= maxRow && j >= minCol && j <= maxCol;

        for (int i = 0; i < 4; i++) {
            int newRow = row + dx.get(i);
            int newCol = col + dy.get(i);
            if (inBounds.test(newRow, newCol)) {
                Room neighbor = rooms.get(newRow).get(newCol);
                if (!neighbor.equals(currentRoom)) {
                    result.add(neighbor);
                }
            }
        }
        return result;
    }

    /**
     * Generates the geometry (corner positions) and potential {@link Door} locations
     * for each {@link Room} in the {@link Level}.
     */
     private void generateRoomsGeometry() {
        for (int i = 1; i < LevelAttribute.ROOMS_PER_SIDE.value + 1; i++) {
            for (int j = 1; j < LevelAttribute.ROOMS_PER_SIDE.value + 1; j++) {
                generateCorners(rooms.get(i).get(j), (i - 1) * LevelAttribute.SECTOR_HEIGHT.value, (j - 1) * LevelAttribute.SECTOR_WIDTH.value);
                generateDoors(rooms.get(i).get(j));
            }
        }
    }

    /**
     * Generates random corner positions for a given room within its designated sector.
     *
     * <p> The generated random values are added to the offset values to determine the final corner {@link Position}
     * for the top-left and bottom-right corners of the room. These {@link Position} are then set within the room object.
     *
     * @param room The room for which to generate corner {@link Position}.
     * @param offsetY The vertical offset for the room based on its sector {@link Position}.
     * @param offsetX The horizontal offset for the room based on its sector {@link Position}.
     */
    private void generateCorners(Room room, final int offsetY, final int offsetX) {
        room.setTopLeft(Position.of(ThreadLocalRandom.current().nextInt(LevelAttribute.CORNER_HOR_RANGE.value) + offsetX + 1,
                ThreadLocalRandom.current().nextInt(LevelAttribute.CORNER_VERT_RANGE.value) + offsetY + 1));

        room.setBottomRight(Position.of(LevelAttribute.SECTOR_WIDTH.value - ThreadLocalRandom.current().nextInt(LevelAttribute.CORNER_HOR_RANGE.value) + offsetX - 1,
                LevelAttribute.SECTOR_HEIGHT.value - ThreadLocalRandom.current().nextInt(LevelAttribute.CORNER_VERT_RANGE.value) + offsetY - 1));
    }

    /**
     * Generates potential door locations for a room based on its connections and geometry.
     *
     * @param room The {@link Room} for which to generate doors.
     */
    private void generateDoors(Room room) {
        Position topLeft = room.getTopLeft();
        Position bottomRight = room.getBottomRight();

        int tLeftY = topLeft.y();
        int tLeftX = topLeft.x();
        int bRightY = bottomRight.y();
        int bRightX = bottomRight.x();

        if (room.getConnectionRoom(Direction.TOP).isPresent()) {
            room.setDoorByDirection(Direction.TOP, Position.of(ThreadLocalRandom.current().nextInt(bRightX - tLeftX - 1) + tLeftX + 1, tLeftY));
        }

        if (room.getConnectionRoom(Direction.RIGHT).isPresent()) {
            room.setDoorByDirection(Direction.RIGHT, Position.of(bRightX, ThreadLocalRandom.current().nextInt(bRightY - tLeftY - 1) + tLeftY + 1));
        }

        if (room.getConnectionRoom(Direction.BOTTOM).isPresent()) {
            room.setDoorByDirection(Direction.BOTTOM, Position.of(
                    ThreadLocalRandom.current().nextInt(bRightX - tLeftX - 1) + tLeftX + 1, bRightY));
        }

        if (room.getConnectionRoom(Direction.LEFT).isPresent()) {
            room.setDoorByDirection(Direction.LEFT, Position.of(tLeftX, ThreadLocalRandom.current().nextInt(bRightY - tLeftY - 1) + tLeftY + 1));
        }
    }

    /**
     * Generates the geometry for corridors connecting rooms in the {@link Level}.
     *
     * <p> This method iterates through each room in the {@link Level} and checks its connections to neighboring rooms.
     *
     * <p> For each connected pair of rooms, it determines the appropriate {@link Direction} (straight, left turn, right turn, or top-to-bottom)
     * and calls the corresponding generation method to create the {@link Corridor}.
     *
     * @param levelService The service responsible for managing level data.
     */
    private void generateCorridorsGeometry(LevelService levelService) {
        for (int i = 1; i < LevelAttribute.ROOMS_PER_SIDE.value + 1; i++) {
            for (int j = 1; j < LevelAttribute.ROOMS_PER_SIDE.value + 1; j++) {
                Room room = rooms.get(i).get(j);

                Optional<Room> optionalRoom = room.getConnectionRoom(Direction.RIGHT);
                Room rightRoom = optionalRoom.orElse(null);

                if (optionalRoom.isPresent() && rightRoom.getConnectionRoom(Direction.LEFT).orElse(null) == room) {
                    levelService.addCorridor(new Corridor());
                    generateLeftToRightCorridor(room, rightRoom, levelService.getLastCorridor());
                }

                optionalRoom = room.getConnectionRoom(Direction.BOTTOM);

                if (optionalRoom.isPresent()) {
                    Room bottomRoom = optionalRoom.get();

                    int grid_i_diff = room.getGrid_i() - bottomRoom.getGrid_i();
                    int grid_j_diff = room.getGrid_j() - bottomRoom.getGrid_j();

                    levelService.addCorridor(new Corridor());

                    if (grid_i_diff == -1 && grid_j_diff > 0) {
                        generateLeftTurnCorridor(room, bottomRoom, levelService.getLastCorridor());
                    } else if (grid_i_diff == -1 && grid_j_diff < 0) {
                        generateRightTurnCorridor(room, bottomRoom, levelService.getLastCorridor());
                    } else {
                        generateTopToBottomCorridor(room, bottomRoom, levelService.getLastCorridor());
                    }
                }
            }
        }
    }

    /**
     * Creates a horizontal {@link Corridor} between two rooms.
     *
     * <p> Calculates the optimal path between the {@link Room} doors, taking into account obstacles.
     *
     * <p> Adds points to create a smooth curve for the {@link Corridor}.
     *
     * @param lRoom The left {@link Room}.
     * @param rRoom The right {@link Room}.
     * @param corridor The {@link Corridor} object to fill with points.
     */
    private void generateLeftToRightCorridor(final Room lRoom, final Room rRoom, final Corridor corridor) {
        corridor.setType(CorridorType.LEFT_TO_RIGHT);
        Optional<Door> optionalLeftDoor = lRoom.getDoorByDirection(Direction.RIGHT);
        Optional<Door> optionalRightDoor = rRoom.getDoorByDirection(Direction.LEFT);

        if(optionalLeftDoor.isEmpty() || optionalRightDoor.isEmpty())
            return;

        Door leftDoor = optionalLeftDoor.get();
        Door rightDoor = optionalRightDoor.get();

        corridor.addPoint(leftDoor.getPosition());

        int xMin = leftDoor.getPosition().x();
        int xMax = rightDoor.getPosition().x();

        for (int i = 1; i < LevelAttribute.ROOMS_PER_SIDE.value + 1; i++) {
            if (rooms.get(i).get(lRoom.getGrid_j()).getSector() != Constants.NONE.value && i != lRoom.getGrid_i())
                xMin = Math.max(rooms.get(i).get(lRoom.getGrid_j()).getBottomRight().x(), xMin);
        }

        for (int i = 1; i < LevelAttribute.ROOMS_PER_SIDE.value + 1; i++) {
            if (rooms.get(i).get(rRoom.getGrid_j()).getSector() != Constants.NONE.value && i != rRoom.getGrid_i())
                xMax = Math.min(rooms.get(i).get(rRoom.getGrid_j()).getTopLeft().x(), xMin);
        }

        int randomCenterX = (xMax - xMin - 1) < 0 ?  xMin : ThreadLocalRandom.current().nextInt(xMax - xMin - 1) + 1 + xMin;

        Position secondPoint = Position.of(randomCenterX + 1, leftDoor.getPosition().y());
        Position thirdPoint = Position.of(randomCenterX + 1, rightDoor.getPosition().y());

        corridor.addPoint(secondPoint);
        corridor.addPoint(thirdPoint);
        corridor.addPoint(rightDoor.getPosition());
    }

    /**
     * Creates a hallway with a left turn between two rooms.
     *
     * <p> This method connects the upper room to the lower left, forming a 90-degree angle.
     *
     * @param tRoom Upper {@link Room}.
     * @param blRoom Lower left {@link Room}.
     * @param corridor Hallway object.
     */
    private void generateLeftTurnCorridor(final Room tRoom, final Room blRoom, final Corridor corridor) {
        corridor.setType(CorridorType.LEFT_TURN);

        Optional<Door> optionalBottomDoor = tRoom.getDoorByDirection(Direction.BOTTOM);
        Optional<Door> optionalRightDoor = blRoom.getDoorByDirection(Direction.RIGHT);

        if(optionalBottomDoor.isEmpty() || optionalRightDoor.isEmpty())
            return;

        Door bottomDoor = optionalBottomDoor.get();
        Door rightDoor = optionalRightDoor.get();

        corridor.addPoint(bottomDoor.getPosition());

        Position secondPoint = Position.of(bottomDoor.getPosition().x(), rightDoor.getPosition().y());

        corridor.addPoint(secondPoint);
        corridor.addPoint(rightDoor.getPosition());
    }

    /**
     * Generates a right-turn {@link Corridor} between two rooms.
     *
     * <p> This method connects the bottom {@link Door} of the top {@link Room} to the left {@link Door}
     * of the bottom-right {@link Room}, creating a right-angled turn.
     *
     * @param tRoom The top {@link Room}.
     * @param brRoom The bottom-right {@link Room}.
     * @param corridor The {@link Corridor} object to be populated.
     */
    private void generateRightTurnCorridor(final Room tRoom, final Room brRoom, final Corridor corridor) {
        corridor.setType(CorridorType.RIGHT_TURN);

        Optional<Door> optionalBottomDoor = tRoom.getDoorByDirection(Direction.BOTTOM);
        Optional<Door> optionalLeftDoor = brRoom.getDoorByDirection(Direction.LEFT);

        if(optionalBottomDoor.isEmpty() || optionalLeftDoor.isEmpty())
            return;

        Door bottomDoor = optionalBottomDoor.get();
        Door leftDoor = optionalLeftDoor.get();

        corridor.addPoint(bottomDoor.getPosition());

        Position secondPoint = Position.of( bottomDoor.getPosition().x(), leftDoor.getPosition().y());

        corridor.addPoint(secondPoint);
        corridor.addPoint(leftDoor.getPosition());
    }

    /**
     * Generates a vertical {@link Corridor} connecting two rooms.
     *
     * <p> This method creates a {@link Corridor} between the top and bottom rooms,
     * considering potential obstacles (occupied sectors) along the path.
     *
     * @param tRoom The top {@link Room}.
     * @param bRoom The bottom {@link Room}.
     * @param corridor The {@link Corridor} object to be populated.
     */
    private void generateTopToBottomCorridor(final Room tRoom, final Room bRoom, final Corridor corridor) {
        corridor.setType(CorridorType.TOP_TO_BOTTOM);

        Optional<Door> optionalBottomDoor = tRoom.getDoorByDirection(Direction.BOTTOM);
        Optional<Door> optionalTopDoor = bRoom.getDoorByDirection(Direction.TOP);

        if(optionalTopDoor.isEmpty() || optionalBottomDoor.isEmpty())
            return;

        Door bottomDoor = optionalBottomDoor.get();
        Door topDoor = optionalTopDoor.get();

        corridor.addPoint(bottomDoor.getPosition());

        int yMin = bottomDoor.getPosition().y();
        int yMax = topDoor.getPosition().y();

        for (int j = 1; j < LevelAttribute.ROOMS_PER_SIDE.value + 1; j++) {
            if (rooms.get(tRoom.getGrid_i()).get(j).getSector() != Constants.NONE.value)
                yMin = Math.max(rooms.get(tRoom.getGrid_i()).get(j).getBottomRight().y(), yMin);
        }

        for (int j = 1; j < LevelAttribute.ROOMS_PER_SIDE.value + 1; j++) {
            if (rooms.get(bRoom.getGrid_i()).get(j).getSector() != Constants.NONE.value)
                yMax = Math.min(rooms.get(bRoom.getGrid_i()).get(j).getTopLeft().y(), yMax);
        }

        int randomCenterY;

        if(yMax - yMin - 1 <= 0)
            randomCenterY = yMin;
        else
            randomCenterY = ThreadLocalRandom.current().nextInt(yMax - yMin - 1) + 1 + yMin;

        Position secondPoint = Position.of(bottomDoor.getPosition().x(), randomCenterY);
        Position thirdPoint = Position.of(topDoor.getPosition().x(), randomCenterY);

        corridor.addPoint(secondPoint);
        corridor.addPoint(thirdPoint);
        corridor.addPoint(topDoor.getPosition());
    }
}
