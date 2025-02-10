package rogue.game.infrastructure.dataAccess.mapper.modelEntityMapper.impl.items;

import rogue.game.domain.entities.GameEntity;
import rogue.game.domain.entities.items.Weapon;
import rogue.game.domain.factories.ItemBuilderFactory;
import rogue.game.infrastructure.dataAccess.mapper.MapperRegistry;
import rogue.game.infrastructure.dataAccess.mapper.modelEntityMapper.ModelEntityMapper;
import rogue.game.infrastructure.dataAccess.mapper.positionMapper.PositionMapper;
import rogue.game.infrastructure.dataAccess.model.entities.GameEntityModel;
import rogue.game.infrastructure.dataAccess.model.entities.items.WeaponModel;

/**
 * Mapper for converting between {@link Weapon} and {@link WeaponModel} objects.
 *
 * <p> This class implements the {@link ModelEntityMapper} interface and
 * provides methods to map a {@link Weapon} entity to a {@link WeaponModel} DTO and vice versa.
 * <p> It utilizes the `{@link ItemBuilderFactory} to create the {@link Weapon} entity instance
 * in the {@code toEntity} method.
 */
public class WeaponMapper implements ModelEntityMapper {
    @Override
    public GameEntityModel toModel(GameEntity entity) {
        PositionMapper positionMapper = MapperRegistry.getMapper(PositionMapper.class);

        WeaponModel weaponModel = null;

        if(entity instanceof Weapon weapon) {
            weaponModel = new WeaponModel();
            weaponModel.setPosition(positionMapper.toModel(weapon.getPosition()));
            weaponModel.setDamage(weapon.getDamage());
            weaponModel.setName(weapon.getName());
        }

        return weaponModel;
    }

    @Override
    public GameEntity toEntity(GameEntityModel model) {
        GameEntity weapon = null;

        if(model instanceof WeaponModel weaponModel) {
            PositionMapper positionMapper = MapperRegistry.getMapper(PositionMapper.class);

            weapon = ItemBuilderFactory.createCustomWeaponBuilder()
                        .withDamage(weaponModel.getDamage())
                        .withName(weaponModel.getName())
                        .withPosition(positionMapper.toEntity(weaponModel.getPosition()))
                        .build();
        }

        return weapon;
    }
}
