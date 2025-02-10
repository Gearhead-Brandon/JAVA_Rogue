package rogue.game.infrastructure.dataAccess.model.level;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import rogue.game.domain.entities.level.Room;
import rogue.game.infrastructure.dataAccess.model.PositionModel;
import rogue.game.infrastructure.dataAccess.model.entities.GameEntityModel;

import java.util.List;

/**
 * Represents the model for the {@link Room} entity.
 */
@Setter
@Getter
public class RoomModel {
    private int sector;
    private int grid_i;
    private int grid_j;
    @JsonProperty("top_left")
    private PositionModel topLeft;
    @JsonProperty("bottom_right")
    private PositionModel bottomRight;
    private List<DoorModel> doors;
    private List<Integer> connections;
    private List<GameEntityModel> entities;
}
