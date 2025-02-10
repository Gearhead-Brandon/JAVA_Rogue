package rogue.game.domain.entities;

import lombok.Getter;
import lombok.Setter;
import rogue.game.domain.enums.GameDifficulty;

/**
 * This class stores data for balancing the game's difficulty
 */
@Getter
@Setter
public class Balancer {
    private GameDifficulty gameDifficulty = GameDifficulty.NORMAL;
    private int maxNumberOfEnemies = 0;
    private int minNumberOfEnemies = 0;
}
