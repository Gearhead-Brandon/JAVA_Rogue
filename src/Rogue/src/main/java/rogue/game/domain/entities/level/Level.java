package rogue.game.domain.entities.level;

import lombok.Getter;
import lombok.Setter;

import rogue.game.domain.entities.GameEntity;
import rogue.game.domain.entities.enemies.Enemy;

import java.util.*;

import static rogue.game.domain.enums.LevelAttribute.*;

public class Level {
    @Getter
    @Setter
    private int levelNumber;
    private final List<Room> sequence;
    private final List<Corridor> corridors;
    @Getter
    private final List<Enemy> enemies;

    /**
     * Represents a single level in the game.
     *
     * <p> A level consists of a sequence of {@link Room} connected by {@link Corridor}.
     *
     * <p> It also maintains a list of {@link Enemy} present in the level.
     */
    public Level() {
        levelNumber = 0;
        sequence = new ArrayList<>(ROOMS_NUMBER.value);
        corridors = new ArrayList<>(MAX_CORRIDORS_NUMBER.value);
        enemies = new ArrayList<>();
    }

    public void reset(){
        levelNumber = 0;
        sequence.clear();
        corridors.clear();
        enemies.clear();
    }

    public List<Room> getSequence() { return Collections.unmodifiableList(sequence); }

    public Room getRoom(int index){
        return sequence.get(index);
    }

    public void addRoom(Room room){
        sequence.add(room);
    }

    public Corridor getLastCorridor(){
        return corridors.getLast();
    }

    public void addCorridor(Corridor corridor){
        corridors.add(corridor);
    }

    public void addEnemy(GameEntity e){
        if(e instanceof Enemy enemy) enemies.add(enemy);
    }

    public List<Corridor> getCorridors() { return Collections.unmodifiableList(corridors); }
}
