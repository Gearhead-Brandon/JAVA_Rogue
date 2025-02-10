package rogue.game.infrastructure.dataAccess.model.level;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import rogue.game.domain.entities.level.Level;
import rogue.game.infrastructure.dataAccess.model.entities.enemies.EnemyModel;

import java.util.List;

/**
 * Represents the model for the {@link Level} entity.
 */
@Setter
@Getter
@NoArgsConstructor
public class LevelModel {
    private int levelNumber;
    private List<RoomModel> sequence;
    private List<CorridorModel> corridors;
    private List<EnemyModel> enemies;
}
