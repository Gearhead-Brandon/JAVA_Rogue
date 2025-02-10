package rogue.game.domain.services.generation.level.entitiesGeneration.impl;

import rogue.game.domain.entities.GameEntity;
import rogue.game.domain.entities.Portal;
import rogue.game.domain.entities.Position;
import rogue.game.domain.entities.level.Room;
import rogue.game.domain.enums.LevelAttribute;
import rogue.game.domain.services.balance.LevelEntitiesBalancer;
import rogue.game.domain.services.generation.level.entitiesGeneration.LevelEntitiesGenService;
import rogue.game.domain.services.generation.utill.GenerationUtil;
import rogue.game.domain.services.level.LevelService;

import java.util.concurrent.ThreadLocalRandom;

public class RogueLevelEntitiesGenService implements LevelEntitiesGenService {
    @Override
    public int generateEntities(LevelService levelService, GameEntity player, int coefficient, LevelEntitiesBalancer balanceService) {
        int playerRoomIndex = generatePlayer(levelService, player);
        generatePortal(levelService, playerRoomIndex);
        generateItems(levelService, coefficient, balanceService);
        generateEnemies(levelService, playerRoomIndex, coefficient, balanceService);
        return playerRoomIndex;
    }

    /**
     * Generates the player's starting {@link Position} and places them in a random {@link Room}.
     *
     * @param levelService The {@link LevelService}.
     * @param player The player {@link GameEntity}.
     * @return The index of the {@link Room} where the player is placed.
     */
    private int generatePlayer(LevelService levelService, GameEntity player){
        int currentRoomIndex = ThreadLocalRandom.current().nextInt(LevelAttribute.ROOMS_NUMBER.value);

        Room spawnRoom = levelService.getRoom(currentRoomIndex);

        player.setPosition(GenerationUtil.generateEntityRandomCoordinates(spawnRoom));

        spawnRoom.addEntity(player);

        return currentRoomIndex;
    }

    /**
     * Generates a {@link Portal} to the next level in a random room, ensuring it's not the same as the player's starting room.
     *
     * @param levelService The {@link LevelService}.
     * @param currentRoomIndex The index of the player's starting room.
     */
    private void generatePortal(LevelService levelService, final int currentRoomIndex){
        int index;
        do{
            index = ThreadLocalRandom.current().nextInt(LevelAttribute.ROOMS_NUMBER.value);
        }while(index == currentRoomIndex);

        Room r = levelService.getRoom(index);

        GameEntity portal = new Portal(GenerationUtil.generateEntityRandomCoordinates(r));

        r.addEntity(portal);
    }

    /**
     * Generates items in each {@link Room} based on the difficulty level.
     *
     * @param levelService The {@link LevelService}.
     * @param complexityFactor The difficulty level.
     * @param balanceService The {@link LevelEntitiesBalancer}.
     */
    private void generateItems(LevelService levelService, final int complexityFactor, LevelEntitiesBalancer balanceService) {
        for (int i = 0; i < LevelAttribute.ROOMS_NUMBER.value; i++) {
            Room r = levelService.getRoom(i);

            int numberOfItems = balanceService.getCountOfItems(complexityFactor);

            for (int j = 0; j < numberOfItems; j++)
                r.addEntity(balanceService.spawnItem(complexityFactor, GenerationUtil.generateEntityRandomCoordinates(r)));
        }
    }

    /**
     * Generates enemies in all rooms except the player's starting room.
     *
     * @param levelService The {@link LevelService}.
     * @param exceptionRoomIndex The index of the room where the player starts.
     * @param complexityFactor The difficulty level.
     * @param balanceService The {@link LevelEntitiesBalancer}.
     */
    private void generateEnemies(LevelService levelService, int exceptionRoomIndex, int complexityFactor, LevelEntitiesBalancer balanceService) {
        for (int i = 0; i < LevelAttribute.ROOMS_NUMBER.value; i++) {
            if(i == exceptionRoomIndex)
                continue;

            Room r = levelService.getRoom(i);

            final int numberOfEnemies = balanceService.getCountOfEnemies(complexityFactor);

            for (int j = 0; j < numberOfEnemies; j++)
                levelService.addEnemy(balanceService.spawnEnemy(complexityFactor, GenerationUtil.generateEntityRandomCoordinates(r)));
        }
    }
}
