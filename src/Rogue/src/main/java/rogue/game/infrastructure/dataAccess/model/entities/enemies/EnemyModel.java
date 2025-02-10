package rogue.game.infrastructure.dataAccess.model.entities.enemies;

import lombok.Getter;
import lombok.Setter;
import rogue.game.domain.entities.enemies.Enemy;
import rogue.game.domain.enums.EntityCharacteristic;
import rogue.game.infrastructure.dataAccess.model.entities.GameEntityModel;

/**
 * Represents the model for the {@link Enemy} entity.
 */
@Getter
@Setter
public class EnemyModel extends GameEntityModel {
    private EntityCharacteristic damageType;
    private int health;
    private int agility;
    private int strength;
    private int hostility;
}
