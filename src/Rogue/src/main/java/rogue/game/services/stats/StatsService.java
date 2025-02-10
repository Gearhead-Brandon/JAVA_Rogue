package rogue.game.services.stats;

import java.util.List;

public interface StatsService {
    /**
     * @return the list of stats
     */
    List<String> getListOfStats();

    int getLevel();
    int getCountOfEatenFood();
    int getCountOfMissedShots();
    int getCountOfBlowsInflicted();
    void addTreasureAmount(int treasure);
    void addLevel();
    void addPassedCell();
    void addEatenFood();
    void addReadScroll();
    void addPotionDrank();
    void addBlowInflicted();
    void addMissedShot();
    void addDefeatedEnemy();
    void prepareData();
    void reset();

    /**
     * Saves the stats to the repository
     */
    void save();
}
