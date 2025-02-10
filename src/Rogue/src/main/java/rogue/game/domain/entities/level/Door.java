package rogue.game.domain.entities.level;

import lombok.Getter;
import lombok.Setter;
import rogue.game.common.enums.MapColor;
import rogue.game.domain.entities.Position;

/**
 * Represents a door in the {@link Room}.
 *
 * <p> A door has a {@link Position} on the map, a {@link MapColor}, and an open/closed state.
 */
@Setter
@Getter
public class Door {
    private Position position;
    private MapColor color;
    private boolean open;

    public Door(Position position) {
        this.position = position;
        this.color = MapColor.YELLOW;
        this.open = true;
    }

    public void close() {
        this.open = false;
    }

    public void open() {
        this.open = true;
    }

    public void resetColor() {
        this.color = MapColor.YELLOW;
    }
}
