package rogue.game.domain.services.pubsub;

import rogue.game.common.observer.EventType;
import rogue.game.common.observer.Observer;

/**
 * This interface defines a service responsible for managing observers and
 * notifying them of events.
 *
 * <p> It implements the Observer pattern, allowing objects to subscribe to
 * and receive notifications about specific events.
 */
public interface PubSubService {
    void notifyObserver(EventType type);
    void subscribe(Observer observer);
}
