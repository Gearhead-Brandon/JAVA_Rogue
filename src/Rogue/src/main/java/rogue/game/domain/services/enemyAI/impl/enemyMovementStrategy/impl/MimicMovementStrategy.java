package rogue.game.domain.services.enemyAI.impl.enemyMovementStrategy.impl;

import rogue.game.common.enums.MapSymbol;
import rogue.game.domain.entities.enemies.Enemy;
import rogue.game.domain.entities.enemies.Mimic;
import rogue.game.domain.enums.EnemyAttribute;
import rogue.game.domain.enums.MimicAttribute;
import rogue.game.services.map.MapService;
import rogue.game.domain.entities.Position;
import rogue.game.domain.entities.level.Room;
import rogue.game.domain.enums.util.MapSymbolUtil;

import java.util.concurrent.ThreadLocalRandom;

/**
 * This class implements a specific movement strategy for {@link Mimic}.
 */
public class MimicMovementStrategy extends BaseEnemyMovementStrategy {

    public MimicMovementStrategy(MapService mapService) {
        super(mapService);
    }

    /**
     * Determines the next move for the {@link Mimic}.
     *
     * <p> If the player is within the Mimic's hostility range:
     * <ul>
     * <li> The Mimic will reveal its true form and pursue the player. </li>
     * </ul>
     *
     * <p> Otherwise:
     * <ul>
     * <li> If the Mimic is currently in its disguised form and the player is not nearby,
     *     it will eventually revert back to its disguised form. </li>
     * <li> If the Mimic is in its disguised form and the player is nearby or the Mimic is in
     *     its true form and the player is not nearby, it will move randomly. </li>
     * </ul>
     *
     * @param enemy The {@link Mimic}.
     * @param topLeft The top-left corner of the {@link Room}.
     * @param bottomRight The bottom-right corner of the {@link Room}.
     * @param playerPosition The current {@link Position} of the player.
     * @return The new {@link Position} of the {@link Mimic}.
     */
    @Override
    public Position move(Enemy enemy, Position topLeft, Position bottomRight, Position playerPosition) {
        Mimic mimic = (Mimic) enemy;
        boolean playerInHostilityRange = isPositionInHostilityRange(
            mimic.getPosition(),
            playerPosition,
            mimic.getHostility()
        );

        if(playerInHostilityRange) {
            mimic.setCountTimesPlayerNotVisible(0);

            mimic.setHostility(EnemyAttribute.LOW_HOSTILITY.value);

            if (mimic.getAppearance() != MapSymbol.MIMIC) {
                mimic.setAppearance(MapSymbol.MIMIC);
                return mimic.getPosition();
            }

            return super.moveToPosition(mimic.getPosition(), playerPosition);
        }

        final boolean checkArea = checkArea(mimic, topLeft, bottomRight);

        if (mimic.getAppearance().equals(MapSymbol.MIMIC) && !checkArea) {
            // If the mimic was in mimic form, increase the skip counter
            mimic.setCountTimesPlayerNotVisible(mimic.getCountTimesPlayerNotVisible() + 1);

            if (mimic.getCountTimesPlayerNotVisible() >= MimicAttribute.MAX_DETECTION_OF_PLAYER.value
                    && topLeft != Position.NONE && bottomRight != Position.NONE) {

                // If the player is not visible for a long time, then the mimic changes its appearance
                mimic.setAppearance(MimicAttribute.APPEARANCES_OF_MIMIC.list.get(ThreadLocalRandom.current()
                                                                                    .nextInt(0, MimicAttribute.APPEARANCES_OF_MIMIC.list.size())));
                mimic.setCountTimesPlayerNotVisible(0);
                mimic.setHostility(EnemyAttribute.LOW_HOSTILITY.value - 1);
            }
        }

        return checkArea ? super.moveInRandomDirection(mimic.getPosition()) : mimic.getPosition();
    }

    /**
     * Checks if the {@link Mimic} is in a position where it should revert to its disguised form.
     *
     * <p> This method checks if the {@link Mimic} is near a door or corridor.
     *
     * @param mimic The {@link Mimic}.
     * @param topLeft The top-left corner of the {@link Room}.
     * @param bottomRight The bottom-right corner of the {@link Room}.
     * @return `true` if the Mimic should remain in its current form, `false` otherwise.
     */
    private boolean checkArea(Mimic mimic, Position topLeft, Position bottomRight) {
        if(topLeft == Position.NONE && bottomRight == Position.NONE)
            return true;

        final int ex = mimic.getPosition().x();
        final int ey = mimic.getPosition().y();

        // Moore neighborhood
        for (int i = -1; i <= 1; i += 1) {
            for (int j = -1; j <= 1; j += 1) {
                MapSymbol sym = mapService.getMapSymbol(ex + j, ey + i);

                if(MapSymbolUtil.isDoor(sym) || sym.equals(MapSymbol.CORRIDOR)) {
                    mimic.setCountTimesPlayerNotVisible(0);
                    return true;
                }
            }
        }

        return false;
    }
}
