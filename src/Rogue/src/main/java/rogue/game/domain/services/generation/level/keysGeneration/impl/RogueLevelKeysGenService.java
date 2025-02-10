package rogue.game.domain.services.generation.level.keysGeneration.impl;

import rogue.game.common.enums.MapColor;
import rogue.game.domain.entities.GameEntity;
import rogue.game.domain.entities.Position;
import rogue.game.domain.entities.items.Key;
import rogue.game.domain.entities.level.Door;
import rogue.game.domain.entities.level.Level;
import rogue.game.domain.entities.level.Room;
import rogue.game.domain.enums.Direction;
import rogue.game.domain.enums.KeyRegenerationMode;
import rogue.game.domain.enums.LevelAttribute;
import rogue.game.domain.enums.util.DirectionUtil;
import rogue.game.domain.factories.ItemBuilderFactory;
import rogue.game.domain.services.generation.level.keysGeneration.LevelKeysGenService;
import rogue.game.domain.services.generation.utill.GenerationUtil;
import rogue.game.domain.services.level.LevelService;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Stream;

import static rogue.game.domain.enums.LevelAttribute.NUMBER_OF_KEYS_PER_LEVEL;

public class RogueLevelKeysGenService implements LevelKeysGenService {
    @Override
    public void generateKeys(LevelService levelService, int startRoomIndex) {
        KeyRegenerationMode generationResult;

        do {
            generationResult = generationColoredDoorsWithKeys(levelService, startRoomIndex);

            if(generationResult == KeyRegenerationMode.REGENERATION)
                removeDoorsWithKeys(levelService);

        }while (generationResult == KeyRegenerationMode.REGENERATION || !checkLevelConnectivity(levelService, startRoomIndex));
    }

    /**
     * Generates a {@link Key} of a specific {@link MapColor} in the given {@link Room}.
     *
     * @param room The {@link Room} where the {@link Key} will be placed.
     * @param color The {@link MapColor} of the {@link Key}.
     */
    private void generateKeyInRoom(final Room room, final MapColor color) {
        Position p = GenerationUtil.generateEntityRandomCoordinates(room);

        room.addEntity(
                ItemBuilderFactory.createKeyBuilder()
                        .withColor(color)
                        .withPosition(p)
                        .build()
        );
    }

    /**
     * Generates colored doors and corresponding keys, ensuring level connectivity.
     *
     * <p> This method creates a connected network of rooms with colored doors and their keys.
     *
     * @param levelService The {@link LevelService} for interacting with the {@link Level} data.
     * @param startRoomIndex The index of the starting room for the player.
     * @return The {@link KeyRegenerationMode} indicating if keys need to be regenerated
     *         due to potential connectivity issues.
     */
    private KeyRegenerationMode generationColoredDoorsWithKeys(LevelService levelService, final int startRoomIndex) {
        Room r = levelService.getRoom(startRoomIndex);

        List<MapColor> colors = Stream.of(
                MapColor.WHITE, MapColor.BLUE,
                MapColor.MAGENTA, MapColor.CYAN,
                MapColor.GREEN, MapColor.RED
        ).toList();

        List<Room> visited = new ArrayList<>();
        visited.add(r);

        Deque<Room> queue = new ArrayDeque<>();
        queue.add(r);

        List<MapColor> addedColors = new ArrayList<>();
        List<MapColor> currentColors = new ArrayList<>();

        for (int i = 0; i < NUMBER_OF_KEYS_PER_LEVEL.value; i++) {
            int randomIndex = ThreadLocalRandom.current().nextInt(colors.size());

            MapColor color = colors.get(randomIndex);

            if(currentColors.contains(color)) {
                i--;
                continue;
            }

            currentColors.add(color);
        }

        while (!queue.isEmpty()) {
            Room currentRoom = queue.poll();

            generateColoredDoor(currentRoom, currentColors, addedColors, startRoomIndex);

            for (Room neighbor : GenerationUtil.getRoommatesByConnections(currentRoom)) {
                if (!visited.contains(neighbor)) {
                    visited.add(neighbor);
                    queue.add(neighbor);
                }
            }
        }

        return generateKeys(levelService, addedColors, startRoomIndex);
    }

    /**
     * Generates doors for the given {@link Room}, considering the available colors and connectivity.
     *
     * @param room The current {@link Room}.
     * @param currentColors A list of available colors for doors in the current {@link Level}.
     * @param addedColors A list of colors already used for doors in the {@link Level}.
     * @param startRoomIndex The index of the starting {@link Room}.
     */
    private void generateColoredDoor(final Room room, final List<MapColor> currentColors, final List<MapColor> addedColors, final int startRoomIndex) {
        final double doorProbability = 0.6;

        for (Direction d : Direction.values()) {
            if(ThreadLocalRandom.current().nextDouble() < doorProbability)
                continue;

            if(room.getSector() == startRoomIndex)
                continue;

            Optional<Door> optionalDoor = room.getDoorByDirection(d);;

            if (optionalDoor.isPresent() && optionalDoor.get().isOpen()) {
                Door door = optionalDoor.get();
                MapColor doorColor;

                doorColor = currentColors.get(ThreadLocalRandom.current().nextInt(currentColors.size()));

                if(!addedColors.contains(doorColor))
                    addedColors.add(doorColor);

                door.setColor(doorColor);
                door.close();

                Optional<Room> connectingRoom = room.getConnectionRoom(d);
                connectingRoom.flatMap(cRoom -> cRoom.getDoorByDirection(DirectionUtil.getOpposite(d))).ifPresent((a) -> {
                    a.setColor(doorColor);
                    a.close();
                });
            }
        }
    }

    /**
     * Generates keys for the {@link Level} based on the used door colors.
     *
     * <p> This method performs a Breadth-First Search (BFS) to ensure that all rooms
     * with colored doors can be reached from the starting {@link Room}.
     *
     * <p> It checks for potential disconnections and regenerates the {@link Level} if necessary.
     *
     * @param levelService The {@link LevelService}.
     * @param addedColors A list of colors used for the doors.
     * @param startRoomIndex The index of the starting room.
     * @return A {@link KeyRegenerationMode} indicating whether key generation needs to be repeated.
     */
    private KeyRegenerationMode generateKeys(LevelService levelService, final List<MapColor> addedColors, final int startRoomIndex) {
        Room startRoom = levelService.getRoom(startRoomIndex);

        List<Room> visited = new ArrayList<>();
        Deque<Room> queue = new ArrayDeque<>();

        List<Direction> doorDirections = List.of(Direction.values());

        Set<MapColor> foundKeys = new HashSet<>();

        for (MapColor color : addedColors) {
            visited.clear();
            visited.add(startRoom);
            queue.add(startRoom);

            while (!queue.isEmpty()) {
                Room currentRoom = queue.poll();

                List<Room> roommates = new ArrayList<>();

                for (Direction d : doorDirections) {
                    Optional<Room> connectingRoom = currentRoom.getConnectionRoom(d);
                    Optional<Door> optionalDoor = currentRoom.getDoorByDirection(d);

                    if(connectingRoom.isEmpty() || optionalDoor.isEmpty())
                        continue;

                    Door door = optionalDoor.get();

                    if(door.isOpen() || foundKeys.contains(door.getColor()))
                        roommates.add(connectingRoom.get());
                }

                for (Room neighbor : roommates) {
                    if (!visited.contains(neighbor)) {
                        visited.add(neighbor);
                        queue.add(neighbor);
                    }
                }
            }

            if(selectingKeyGenerationRoom(visited, color, startRoomIndex) == KeyRegenerationMode.REGENERATION)
                return KeyRegenerationMode.REGENERATION;

            foundKeys.add(color);
        }

        return KeyRegenerationMode.NONE;
    }

    /**
     * Selects a suitable room to place a key of the given color.
     *
     * <p> This method implements a weighted selection algorithm to choose a room based on several factors:
     * <ul>
     * <li> Presence of other keys in the room </li>
     * <li> Number of doors in the room </li>
     * <li> Whether the room already has a door of the given color </li>
     * </ul>
     *
     * @param visited A list of visited rooms.
     * @param keyColor The color of the key to be generated.
     * @param startRoomIndex The index of the starting room.
     * @return The key regeneration mode indicating whether regeneration is needed.
     */
    private KeyRegenerationMode selectingKeyGenerationRoom(List<Room> visited, final MapColor keyColor, final int startRoomIndex){
        int weight = 0;
        Room keyRoom = visited.getFirst();

        for (Room room : visited){
            int roomWeight = calculateRoomWeight(room, keyColor, startRoomIndex);

            if(roomWeight >= weight) {
                keyRoom = room;
                weight = roomWeight;
            }
        }

        if(keyRoom.getSector() == startRoomIndex)
            return KeyRegenerationMode.REGENERATION;

        generateKeyInRoom(keyRoom, keyColor);

        return KeyRegenerationMode.NONE;
    }

    /**
     * Calculates a weight for a given {@link Room} based on various factors.
     *
     * <p> The weight is used to determine the suitability of a {@link Room} for placing a {@link Key}.
     *
     * <p> A higher weight indicates a more suitable {@link Room}.
     *
     * @param room The {@link Room} to be evaluated.
     * @param keyColor The {@link MapColor} of the {@link Key}.
     * @param startRoomIndex The index of the starting room.
     * @return The calculated weight for the {@link Room}.
     */
    private int calculateRoomWeight(final Room room, final MapColor keyColor, final int startRoomIndex){
        int weight = 5;

        final int sector = room.getSector();

        if(sector == startRoomIndex)
            weight -= 15;

        if(room.containAnyKey())
            weight -= 15;

        if(room.containDoorThisColor(keyColor))
            weight -= 20;

        if(sector != startRoomIndex) {
            final int countOfConnections = room.getCountOfConnections();

            if (countOfConnections == 1)
                weight += 15;
            else if (countOfConnections == 2)
                weight += 5;
        }

        return weight;
    }

    /**
     * Checks if the generated {@link Level} is connected.
     *
     * <p> This method performs a Breadth-First Search (BFS) to ensure that all rooms can be reached from the starting room,
     * given the generated doors and keys.
     *
     * @param levelService The {@link LevelService}.
     * @param startRoomIndex The index of the starting room.
     * @return `true` if the level is connected, `false` otherwise.
     */
    private boolean checkLevelConnectivity(LevelService levelService, final int startRoomIndex){
        Room startRoom = levelService.getRoom(startRoomIndex);

        List<Room> visited = new ArrayList<>();
        Deque<Room> queue = new ArrayDeque<>();

        List<Direction> doorDirections = List.of(Direction.values());

        Set<MapColor> foundKeys = new HashSet<>();

        visited.add(startRoom);
        queue.add(startRoom);

        while (!queue.isEmpty()) {
            Room currentRoom = queue.poll();

            boolean foundKey = false;

            for(GameEntity e : currentRoom.getEntities()) {
                if (e instanceof Key key && !foundKeys.contains(key.getColor())) {
                    foundKeys.add(key.getColor());
                    foundKey = true;
                }
            }

            if(foundKey) {
                visited.clear();
                queue.clear();
                visited.add(startRoom);
                queue.add(startRoom);
                continue;
            }

            List<Room> roommates = new ArrayList<>();

            for (Direction d : doorDirections) {
                Optional<Room> connectingRoom = currentRoom.getConnectionRoom(d);
                Optional<Door> optionalDoor = currentRoom.getDoorByDirection(d);

                if(connectingRoom.isEmpty() || optionalDoor.isEmpty())
                    continue;

                Door door = optionalDoor.get();

                if(door.isOpen() || foundKeys.contains(door.getColor()))
                    roommates.add(connectingRoom.get());
            }

            for (Room neighbor : roommates) {
                if (!visited.contains(neighbor)) {
                    visited.add(neighbor);
                    queue.add(neighbor);
                }
            }
        }

        if(visited.size() != LevelAttribute.ROOMS_NUMBER.value){
            removeDoorsWithKeys(levelService);
            return false;
        }

        return true;
    }

    /**
     * Removes all doors and keys from the {@link Level}.
     *
     * <p> This method is called if the generated level is not connected and needs to be regenerated.
     *
     * @param levelService The {@link LevelService} for interacting with {@link Level} data.
     */
    private void removeDoorsWithKeys(LevelService levelService){
        List<Direction> doorDirections = List.of(Direction.values());

        for(int i = 0; i < LevelAttribute.ROOMS_NUMBER.value; i++) {
            Room room = levelService.getRoom(i);
            room.removeKeys();

            for (Direction d : doorDirections) {
                Optional<Door> optionalDoor = room.getDoorByDirection(d);

                if(optionalDoor.isPresent()) {
                    Door door = optionalDoor.get();
                    door.open();
                    door.resetColor();
                }
            }
        }
    }
}
