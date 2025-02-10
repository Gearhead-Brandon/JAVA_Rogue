package rogue.game.domain.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * This class represents the statistics of a game session, tracking various metrics like treasure collected,
 * levels completed, items used, and enemy encounters.
 */
@NoArgsConstructor
@Setter
@Getter
public class GameStats {
    public static int NUMBER_OF_LEVELS_FOR_DATA_TRANSFER = 2;

    private int treasureAmount;
    private int level;
    private int countOfEatenFood;
    private int countOfPotionsDrank;
    private int countOfReadScrolls;
    private int countOfPassedCells;
    private int countOfDefeatedEnemies;
    private int countOfBlowsInflicted;
    private int countOfMissedShots;

    /**
     * Merges the statistics of another GameStats object into this one.
     *
     * @param other The other GameStats object to merge.
     */
    public void merge(GameStats other) {
        if(other == null)
            return;

        this.treasureAmount += other.treasureAmount;
        this.level += other.level;
        this.countOfEatenFood += other.countOfEatenFood;
        this.countOfPotionsDrank += other.countOfPotionsDrank;
        this.countOfReadScrolls += other.countOfReadScrolls;
        this.countOfPassedCells += other.countOfPassedCells;
        this.countOfDefeatedEnemies += other.countOfDefeatedEnemies;
        this.countOfBlowsInflicted += other.countOfBlowsInflicted;
        this.countOfMissedShots += other.countOfMissedShots;

        other.treasureAmount = 0;
        other.level = 0;
        other.countOfEatenFood = 0;
        other.countOfPotionsDrank = 0;
        other.countOfReadScrolls = 0;
        other.countOfPassedCells = 0;
        other.countOfDefeatedEnemies = 0;
        other.countOfBlowsInflicted = 0;
        other.countOfMissedShots = 0;
    }

    public void reset(){
        treasureAmount = 0;
        level = 0;
        countOfEatenFood = 0;
        countOfPotionsDrank = 0;
        countOfReadScrolls = 0;
        countOfPassedCells = 0;
        countOfDefeatedEnemies = 0;
        countOfBlowsInflicted = 0;
        countOfMissedShots = 0;
    }

    @Override
    public String toString() {
        return "treasure " + treasureAmount + ", level " + level +
                ", food " + countOfEatenFood + ", potions " + countOfPotionsDrank +
                ", scrolls " + countOfReadScrolls + ", passed cells " + countOfPassedCells +
                ", enemies defeated " + countOfDefeatedEnemies + ", blows inflicted " + countOfBlowsInflicted +
                ", missed shots " + countOfMissedShots;
    }
}
