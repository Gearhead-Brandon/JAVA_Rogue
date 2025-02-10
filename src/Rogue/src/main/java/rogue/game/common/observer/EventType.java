package rogue.game.common.observer;

/**
 * Enumeration representing different types of game events.
 *
 * <p> These events are used to notify observers about changes in the game state.
 */
public enum EventType {
    MAP_UPDATE,
    STATUS_MSG_UPDATE,
    INVENTORY_UPDATE,
    LEVEL_UPDATE,
    HITS_UPDATE,
    STRENGTH_UPDATE,
    GOLD_UPDATE
}
