package rogue.game.infrastructure.dataAccess.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import rogue.game.infrastructure.dataAccess.model.entities.PlayerModel;
import rogue.game.infrastructure.dataAccess.model.level.LevelModel;

import java.util.*;

/**
 * This class represents the serialized data of the game state.
 * <p> It contains various models representing different aspects of the game, such as player information, inventory, level, and statistics.
 */
@NoArgsConstructor
@Getter
@Setter
public class SerializeData {
    @JsonProperty("total_stats")
    private GameStatsModel totalStats = null;
    @JsonProperty("current_stats")
    private GameStatsModel currentStats = null;
    @JsonProperty("player")
    private PlayerModel playerModel = null;
    @JsonProperty("balancer")
    private BalancerModel balancerModel = null;
    @JsonProperty("inventory")
    private InventoryModel inventoryModel = null;
    @JsonProperty("walkthroughes")
    private final List<GameStatsModel> walkthroughes = new ArrayList<>();
    @JsonProperty("level")
    private LevelModel levelModel = null;

    public void addWalkthrough(GameStatsModel gameStats) {
        walkthroughes.add(gameStats);
        walkthroughes.sort(Comparator.comparingInt(GameStatsModel::getLevel).reversed());
    }
}
