package rogue.game.infrastructure.dataAccess.model.entities;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.*;
import rogue.game.infrastructure.dataAccess.model.PositionModel;
import rogue.game.infrastructure.dataAccess.model.entities.enemies.*;
import rogue.game.infrastructure.dataAccess.model.entities.items.*;
import rogue.game.domain.entities.GameEntity;

/**
 * Represents the model for the {@link GameEntity} entity.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = FoodModel.class, name = "food_model"),
        @JsonSubTypes.Type(value = KeyModel.class, name = "key_model"),
        @JsonSubTypes.Type(value = PotionModel.class, name = "potion_model"),
        @JsonSubTypes.Type(value = ScrollModel.class, name = "scroll_model"),
        @JsonSubTypes.Type(value = WeaponModel.class, name = "weapon_model"),
        @JsonSubTypes.Type(value = TreasureModel.class, name = "treasure_model"),
        @JsonSubTypes.Type(value = GhostModel.class, name = "ghost_model"),
        @JsonSubTypes.Type(value = MimicModel.class, name = "mimic_model"),
        @JsonSubTypes.Type(value = OgreModel.class, name = "ogre_model"),
        @JsonSubTypes.Type(value = SnakeMagicianModel.class, name = "snake_magician_model"),
        @JsonSubTypes.Type(value = VampireModel.class, name = "vampire_model"),
        @JsonSubTypes.Type(value = ZombieModel.class, name = "zombie_model"),
        @JsonSubTypes.Type(value = PortalModel.class, name = "portal_model"),
})
public class GameEntityModel {
    private PositionModel position;
}
