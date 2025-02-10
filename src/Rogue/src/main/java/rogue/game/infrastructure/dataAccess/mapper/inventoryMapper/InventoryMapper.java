package rogue.game.infrastructure.dataAccess.mapper.inventoryMapper;

import org.mapstruct.Mapper;
import rogue.game.domain.entities.Inventory;
import rogue.game.domain.entities.items.Item;
import rogue.game.domain.enums.ItemType;
import rogue.game.infrastructure.dataAccess.mapper.MapperRegistry;
import rogue.game.infrastructure.dataAccess.mapper.gameEntityMapper.GameEntityMapper;
import rogue.game.infrastructure.dataAccess.model.InventoryModel;
import rogue.game.infrastructure.dataAccess.model.entities.GameEntityModel;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Mapper interface for converting between {@link Inventory} and {@link InventoryModel} objects.
 */
@Mapper
public interface InventoryMapper {
    default InventoryModel toModel(Inventory inventory){
        if ( inventory == null ) {
            return null;
        }

        GameEntityMapper gameEntityMapper = MapperRegistry.getMapper(GameEntityMapper.class);

        InventoryModel inventoryModel = new InventoryModel();

        Map<ItemType, List<GameEntityModel>> items = new HashMap<>();

        ItemType[] values = new ItemType[]{
                ItemType.FOOD, ItemType.SCROLL, ItemType.POTION, ItemType.WEAPON
        };

        for ( var itemType : values ) {
            List<Item> list = inventory.getItemsByType( itemType );
            if(list.equals(Collections.emptyList()) )
                continue;

            List<GameEntityModel> newList = list.stream()
                    .map(gameEntityMapper::toModel)
                    .toList();

            items.put( itemType, newList );
        }

        inventoryModel.setItems( items );
        inventoryModel.setCurrentInventoryItemType( inventory.getCurrentInventoryItemType() );
        inventoryModel.setGold( inventory.getGold() );

        return inventoryModel;
    }

    default Inventory toEntity(InventoryModel inventoryModel){
        if ( inventoryModel == null ) {
            return null;
        }

        GameEntityMapper gameEntityMapper = MapperRegistry.getMapper(GameEntityMapper.class);

        Inventory inventory = new Inventory();

        inventory.setGold(inventoryModel.getGold());
        inventory.setCurrentInventoryItemType(inventoryModel.getCurrentInventoryItemType());

        Map<ItemType, List<GameEntityModel>> it = inventoryModel.getItems();

        if (it != null) {
            for (Map.Entry<ItemType, List<GameEntityModel>> entry : it.entrySet()) {
                for (GameEntityModel gameEntityModel : entry.getValue()) {
                    inventory.tryAddItem(gameEntityMapper.toEntity(gameEntityModel));
                }
            }
        }

        return inventory;
    }
}
