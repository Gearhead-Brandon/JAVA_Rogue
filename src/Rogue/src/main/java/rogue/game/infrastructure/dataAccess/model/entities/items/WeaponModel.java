package rogue.game.infrastructure.dataAccess.model.entities.items;

import lombok.Getter;
import lombok.Setter;
import rogue.game.domain.entities.items.Weapon;
import rogue.game.infrastructure.dataAccess.model.entities.GameEntityModel;

/**
 * Represents the model for the {@link Weapon} entity.
 */
@Getter
@Setter
public class WeaponModel extends GameEntityModel {
    private int damage;
    private String name;
}
