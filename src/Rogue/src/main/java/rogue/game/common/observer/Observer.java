package rogue.game.common.observer;

/**
 * The Observer interface defines a contract for objects that
 * want to be notified of events.
 */
public interface Observer {
    void update(EventType type);
}
