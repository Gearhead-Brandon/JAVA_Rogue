package rogue.game.infrastructure.dataAccess.mapper.modelEntityMapper.impl;

import rogue.game.domain.entities.GameEntity;
import rogue.game.domain.entities.Player;
import rogue.game.domain.entities.items.Potion;
import rogue.game.domain.entities.items.Weapon;
import rogue.game.infrastructure.dataAccess.mapper.MapperRegistry;
import rogue.game.infrastructure.dataAccess.mapper.modelEntityMapper.ModelEntityMapper;
import rogue.game.infrastructure.dataAccess.mapper.modelEntityMapper.impl.items.PotionMapper;
import rogue.game.infrastructure.dataAccess.mapper.modelEntityMapper.impl.items.WeaponMapper;
import rogue.game.infrastructure.dataAccess.mapper.positionMapper.PositionMapper;
import rogue.game.infrastructure.dataAccess.model.entities.PlayerModel;
import rogue.game.infrastructure.dataAccess.model.entities.GameEntityModel;
import rogue.game.infrastructure.dataAccess.model.entities.items.PotionModel;
import rogue.game.infrastructure.dataAccess.model.entities.items.WeaponModel;

/**
 * Mapper for converting between {@link Player} and {@link PlayerModel} objects.
 *
 * <p> This class implements the {@link ModelEntityMapper} interface and provides methods to map a
 * {@link Player} entity to a {@link PlayerModel} DTO and vice versa.
 * <p> It also handles the mapping of related entities like {@link Potion} and {@link Weapon}.
 */
public class PlayerMapper implements ModelEntityMapper {
    @Override
    public GameEntityModel toModel(GameEntity entity) {
        PlayerModel playerModel = null;

        if ( entity instanceof Player player ) {
            PositionMapper positionMapper = MapperRegistry.getMapper(PositionMapper.class);

            playerModel = new PlayerModel();
            playerModel.setPosition(positionMapper.toModel(player.getPosition()));

            playerModel.setMaxHealth(player.getMaxHealth());
            playerModel.setHealth(player.getHealth());
            playerModel.setAgility(player.getAgility());
            playerModel.setStrength(player.getStrength());
            playerModel.setViewAngle(player.getViewAngle());
            playerModel.setCurrentRoomIndex(player.getCurrentRoomIndex());

            GameEntityModel gem = new PotionMapper().toModel(player.getPotion());

            if(gem instanceof PotionModel potionModel)
                playerModel.setPotionModel(potionModel);

            gem = new WeaponMapper().toModel(player.getWeapon());

            if(gem instanceof WeaponModel weaponModel)
                playerModel.setWeaponModel(weaponModel);
        }

        return playerModel;
    }

    @Override
    public GameEntity toEntity(GameEntityModel model) {
        Player player = null;

        if ( model instanceof PlayerModel playerModel ) {
            PositionMapper positionMapper = MapperRegistry.getMapper(PositionMapper.class);

            player = new Player();
            player.setPosition(positionMapper.toEntity(playerModel.getPosition()));

            player.setMaxHealth(playerModel.getMaxHealth());
            player.setHealth(playerModel.getHealth());
            player.setAgility(playerModel.getAgility());
            player.setStrength(playerModel.getStrength());
            player.setViewAngle(playerModel.getViewAngle());
            player.setCurrentRoomIndex(playerModel.getCurrentRoomIndex());

            GameEntity e = new PotionMapper().toEntity(playerModel.getPotionModel());

            if(e instanceof Potion potion)
                player.applyPotion(potion);

            e = new WeaponMapper().toEntity(playerModel.getWeaponModel());

            if(e instanceof Weapon weapon)
                player.setWeapon(weapon);
        }

        return player;
    }
}
