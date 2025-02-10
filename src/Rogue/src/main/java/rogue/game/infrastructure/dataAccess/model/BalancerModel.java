package rogue.game.infrastructure.dataAccess.model;

import lombok.Getter;
import lombok.Setter;
import rogue.game.domain.enums.GameDifficulty;
import rogue.game.domain.entities.Balancer;

/**
 * Represents the model for the {@link Balancer} entity.
 */
@Getter
@Setter
public class BalancerModel {
    private GameDifficulty gameDifficulty = GameDifficulty.NORMAL;
    private int maxNumberOfEnemies = 0;
    private int minNumberOfEnemies = 0;
}
