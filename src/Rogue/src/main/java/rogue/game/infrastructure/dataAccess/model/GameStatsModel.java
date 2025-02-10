package rogue.game.infrastructure.dataAccess.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import rogue.game.domain.entities.GameStats;

/**
 * Represents the model for the {@link GameStats} entity.
 */
@Getter
@Setter
@NoArgsConstructor
public class GameStatsModel {
    @JsonProperty("treasure")
    private int treasureAmount;
    @JsonProperty("level")
    private int level;
    @JsonProperty("enemiesDefeated")
    private int countOfDefeatedEnemies;
    @JsonProperty("food")
    private int countOfEatenFood;
    @JsonProperty("potions")
    private int countOfPotionsDrank;
    @JsonProperty("scrolls")
    private int countOfReadScrolls;
    @JsonProperty("cells")
    private int countOfPassedCells;
    @JsonProperty("blowsInflicted")
    private int countOfBlowsInflicted;
    @JsonProperty("missedShots")
    private int countOfMissedShots;
}
