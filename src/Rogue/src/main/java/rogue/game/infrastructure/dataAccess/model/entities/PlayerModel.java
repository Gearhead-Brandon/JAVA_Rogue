package rogue.game.infrastructure.dataAccess.model.entities;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import rogue.game.domain.entities.Player;
import rogue.game.infrastructure.dataAccess.model.entities.items.PotionModel;
import rogue.game.infrastructure.dataAccess.model.entities.items.WeaponModel;

/**
 * Represents the model for the {@link Player} entity.
 */
@Getter
@Setter
public class PlayerModel extends GameEntityModel {
    private int maxHealth;
    private int health;
    private int agility;
    private int strength;
    private float viewAngle;
    int currentRoomIndex;
    private PotionModel potionModel;
    private WeaponModel weaponModel;
}
