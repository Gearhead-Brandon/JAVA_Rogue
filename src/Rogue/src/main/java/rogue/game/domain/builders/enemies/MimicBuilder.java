package rogue.game.domain.builders.enemies;

import rogue.game.common.enums.MapSymbol;
import rogue.game.domain.entities.GameEntity;
import rogue.game.domain.entities.enemies.Mimic;

/**
 * Builder class for creating {@link Mimic} enemies, extending {@link BaseEnemyBuilder}.
 *
 * <p> This class inherits common {@link BaseEnemyBuilder} properties from {@link BaseEnemyBuilder} and
 * adds a specific property `appearance` ({@link MapSymbol}) to determine the Mimic's initial
 * visual representation on the map.
 */
public class MimicBuilder extends BaseEnemyBuilder {
    private MapSymbol appearance;

    public MimicBuilder withAppearance(MapSymbol appearance) {
        this.appearance = appearance;
        return this;
    }

    @Override
    public GameEntity build() {
        return new Mimic(
            super.position,
            super.damageType,
            super.health,
            super.agility,
            super.strength,
            super.hostility,
            appearance
        );
    }
}
