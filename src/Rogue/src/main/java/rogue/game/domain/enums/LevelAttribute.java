package rogue.game.domain.enums;

import rogue.game.domain.entities.level.Level;

/**
 * Represents various attributes and constants related to {@link Level} generation and configuration.
 */
public enum LevelAttribute {
    FINAL_LEVEL(22),
    ROOMS_PER_SIDE(3),
    ROOMS_NUMBER(9),
    MAX_CORRIDORS_NUMBER(12),
    SECTOR_HEIGHT(30 / 3),
    SECTOR_WIDTH(90 / 3),
    CORNER_VERT_RANGE((SECTOR_HEIGHT.value - 6) / 2),
    CORNER_HOR_RANGE((SECTOR_WIDTH.value - 6) / 2),
    MAX_ITEMS_PER_ROOM(2),
    MAX_ENEMIES_PER_ROOM(3),
    MAX_ENTITIES_PER_ROOM(MAX_ITEMS_PER_ROOM.value + MAX_ENEMIES_PER_ROOM.value + 2),
    NUMBER_OF_KEYS_PER_LEVEL(3);

    public final int value;

    LevelAttribute(int value) {
        this.value = value;
    }
}
