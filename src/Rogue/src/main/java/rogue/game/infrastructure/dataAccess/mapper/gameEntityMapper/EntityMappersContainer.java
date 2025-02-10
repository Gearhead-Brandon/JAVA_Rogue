package rogue.game.infrastructure.dataAccess.mapper.gameEntityMapper;

import rogue.game.infrastructure.dataAccess.mapper.modelEntityMapper.impl.enemies.*;
import rogue.game.infrastructure.dataAccess.mapper.modelEntityMapper.impl.items.*;
import rogue.game.infrastructure.dataAccess.model.entities.PlayerModel;
import rogue.game.infrastructure.dataAccess.model.entities.PortalModel;
import rogue.game.infrastructure.dataAccess.model.entities.enemies.*;
import rogue.game.infrastructure.dataAccess.model.entities.items.*;
import rogue.game.domain.entities.Player;
import rogue.game.domain.entities.Portal;
import rogue.game.domain.entities.GameEntity;
import rogue.game.domain.entities.enemies.*;
import rogue.game.domain.entities.items.*;
import rogue.game.infrastructure.dataAccess.mapper.modelEntityMapper.ModelEntityMapper;
import rogue.game.infrastructure.dataAccess.mapper.modelEntityMapper.impl.PlayerMapper;
import rogue.game.infrastructure.dataAccess.mapper.modelEntityMapper.impl.PortalMapper;
import rogue.game.infrastructure.dataAccess.model.entities.GameEntityModel;

import java.util.HashMap;
import java.util.Map;

/**
 * A container class for managing mappers between {@link GameEntity} and their corresponding {@link GameEntityModel} representations.
 *
 * <p> This class provides methods to retrieve the appropriate mapper for a given {@link GameEntity} or {@link GameEntityModel} object,
 * simplifying the mapping process.
 */
public class EntityMappersContainer {

    /**
     * A map to store entity-to-model mappers.
     * <p> The key is the class of the {@link GameEntity}, and the value is the corresponding {@link ModelEntityMapper}.
     */
    private final Map<Class<? extends GameEntity>, ModelEntityMapper> entityMappers = new HashMap<>();

    /**
     * A map to store model-to-entity mappers.
     * <p> The key is the class of the {@link GameEntityModel}, and the value is the corresponding {@link ModelEntityMapper}.
     */
    private final Map<Class<? extends GameEntityModel>, ModelEntityMapper> modelMappers = new HashMap<>();

    public EntityMappersContainer() {
        PortalMapper portalMapper = new PortalMapper();
        entityMappers.put(Portal.class, portalMapper);
        modelMappers.put(PortalModel.class, portalMapper);

        PlayerMapper playerMapper = new PlayerMapper();
        entityMappers.put(Player.class, playerMapper);
        modelMappers.put(PlayerModel.class, playerMapper);

        KeyMapper keyMapper = new KeyMapper();
        entityMappers.put(Key.class, keyMapper);
        modelMappers.put(KeyModel.class, keyMapper);

        ScrollMapper scrollMapper = new ScrollMapper();
        entityMappers.put(Scroll.class, scrollMapper);
        modelMappers.put(ScrollModel.class, scrollMapper);

        WeaponMapper weaponMapper = new WeaponMapper();
        entityMappers.put(Weapon.class, weaponMapper);
        modelMappers.put(WeaponModel.class, weaponMapper);

        PotionMapper potionMapper = new PotionMapper();
        entityMappers.put(Potion.class, potionMapper);
        modelMappers.put(PotionModel.class, potionMapper);

        FoodMapper foodMapper = new FoodMapper();
        entityMappers.put(Food.class, foodMapper);
        modelMappers.put(FoodModel.class, foodMapper);

        TreasureMapper treasureMapper = new TreasureMapper();
        entityMappers.put(Treasure.class, treasureMapper);
        modelMappers.put(TreasureModel.class, treasureMapper);

        ZombieMapper zombieMapper = new ZombieMapper();
        entityMappers.put(Zombie.class, zombieMapper);
        modelMappers.put(ZombieModel.class, zombieMapper);

        SnakeMagicianMapper snakeMagicianMapper = new SnakeMagicianMapper();
        entityMappers.put(SnakeMagician.class, snakeMagicianMapper);
        modelMappers.put(SnakeMagicianModel.class, snakeMagicianMapper);

        VampireMapper vampireMapper = new VampireMapper();
        entityMappers.put(Vampire.class, vampireMapper);
        modelMappers.put(VampireModel.class, vampireMapper);

        OgreMapper ogreMapper = new OgreMapper();
        entityMappers.put(Ogre.class, ogreMapper);
        modelMappers.put(OgreModel.class, ogreMapper);

        GhostMapper ghostMapper = new GhostMapper();
        entityMappers.put(Ghost.class, ghostMapper);
        modelMappers.put(GhostModel.class, ghostMapper);

        MimicMapper mimicMapper = new MimicMapper();
        entityMappers.put(Mimic.class, mimicMapper);
        modelMappers.put(MimicModel.class, mimicMapper);
    }

    /**
     * Retrieves the appropriate mapper for a given {@link GameEntityModel}.
     *
     * @param entity The {@link GameEntity} for which to retrieve the {@link ModelEntityMapper}.
     * @return The corresponding mapper instance.
     */
    public ModelEntityMapper getMapper(GameEntity entity) {
        return entityMappers.get(entity.getClass());
    }

    /**
     * Retrieves the appropriate mapper for a given {@link GameEntityModel}.
     *
     * @param entity The {@link GameEntityModel} for which to retrieve the {@link ModelEntityMapper}.
     * @return The corresponding mapper instance.
     */
    public ModelEntityMapper getMapper(GameEntityModel entity) {
        return modelMappers.get(entity.getClass());
    }
}
