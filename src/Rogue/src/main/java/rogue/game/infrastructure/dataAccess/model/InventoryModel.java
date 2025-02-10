package rogue.game.infrastructure.dataAccess.model;

import lombok.Getter;
import lombok.Setter;
import rogue.game.domain.entities.Inventory;
import rogue.game.domain.enums.ItemType;
import rogue.game.infrastructure.dataAccess.model.entities.GameEntityModel;

import java.util.List;
import java.util.Map;

/**
 * Represents the model for the {@link Inventory} entity.
 */
@Getter
@Setter
public class InventoryModel {
    private Map<ItemType, List<GameEntityModel>> items;
    private ItemType currentInventoryItemType;
    private int gold;
}
