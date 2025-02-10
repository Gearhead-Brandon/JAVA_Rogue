package rogue.game.domain.enums.util;

import rogue.game.common.enums.MapSymbol;
import rogue.game.domain.entities.Player;
import rogue.game.domain.entities.Portal;
import rogue.game.domain.entities.enemies.*;
import rogue.game.domain.entities.items.*;
import rogue.game.domain.entities.GameEntity;

import java.util.Map;
import java.util.function.Function;

/**
 * This class maps {@link GameEntity} to their corresponding {@link MapSymbol}.
 */
public class SymbolMapper {

    /**
     * A static, final map that directly associates common {@link GameEntity} classes with their {@link MapSymbol}.
     */
    private static final Map<Class<?>, MapSymbol> symbolMap = Map.ofEntries(
            Map.entry(Player.class, MapSymbol.PLAYER),
            Map.entry(Portal.class, MapSymbol.PORTAL),

            Map.entry(Food.class, MapSymbol.FOOD),
            Map.entry(Potion.class, MapSymbol.POTION),
            Map.entry(Scroll.class, MapSymbol.SCROLL),
            Map.entry(Treasure.class, MapSymbol.TREASURE),
            Map.entry(Weapon.class, MapSymbol.WEAPON),

            Map.entry(Zombie.class, MapSymbol.ZOMBIE),
            Map.entry(Ogre.class, MapSymbol.OGRE),
            Map.entry(Vampire.class, MapSymbol.VAMPIRE),
            Map.entry(Ghost.class, MapSymbol.GHOST),
            Map.entry(SnakeMagician.class, MapSymbol.SNAKE_MAGICIAN)
    );

    /**
     * A static, final map that handles special cases for entity symbol mapping.
     * <p> It uses a function to determine the symbol based on the specific entity instance.
     */
    private static final Map<Class<?>, Function<Object, MapSymbol>> specialSymbolMap = Map.of(
            Mimic.class, (c) -> (c instanceof Mimic) ? ((Mimic)c).getAppearance() : MapSymbol.EMPTINESS,
            Key.class, (c) -> (c instanceof Key) ? MapSymbolUtil.getKeySymbolByColor(((Key)c).getColor()) : MapSymbol.EMPTINESS
    );

    /**
     * Maps an {@link GameEntity} object to its corresponding {@link MapSymbol}.
     *
     * @param entity The {@link GameEntity} object to be mapped.
     * @return The corresponding {@link MapSymbol}.
     */
    public static MapSymbol map(Object entity) {
        MapSymbol symbol = specialSymbolMap.getOrDefault(entity.getClass(), (c) -> MapSymbol.EMPTINESS).apply(entity);
        return symbol != MapSymbol.EMPTINESS ? symbol : symbolMap.getOrDefault(entity.getClass(), MapSymbol.EMPTINESS);
    }
}
