package rogue.game.domain.entities.level;

import lombok.Getter;
import lombok.Setter;
import rogue.game.domain.enums.CorridorType;
import rogue.game.domain.entities.Position;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents a corridor in the {@link Level}.
 *
 * <p> A corridor is a path that connects different rooms in the map.
 *
 * <p> It has a specific type (e.g., straight, left turn, right turn) and
 * a {@link List} of {@link Position} defining its shape.
 */
public class Corridor {
    @Getter
    @Setter
    private CorridorType type;
    private final List<Position> points;

    public Corridor(){
        type = CorridorType.NONE;
        points = new ArrayList<>();
    }

    public void addPoint(final Position point){
        points.add(point);
    }

    public Position getPoint(int index) {
        return points.get(index);
    }

    public List<Position> getPoints() {
        return Collections.unmodifiableList(points);
    }
}
