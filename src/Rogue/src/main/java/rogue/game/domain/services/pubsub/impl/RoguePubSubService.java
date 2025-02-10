package rogue.game.domain.services.pubsub.impl;

import rogue.game.domain.services.pubsub.PubSubService;
import rogue.game.common.observer.EventType;
import rogue.game.common.observer.Observer;

import java.util.ArrayList;
import java.util.List;

/**
 * This class implements the `PubSubService` interface, providing a
 * concrete implementation for managing observers and notifications.
 *
 * <p> It maintains a list of subscribed observers and
 * notifies them when a specific event occurs.
 */
public class RoguePubSubService implements PubSubService {
    private final List<Observer> observers = new ArrayList<>();

    @Override
    public void subscribe(Observer observer) { observers.add(observer); }

    @Override
    public void notifyObserver(EventType type) { observers.forEach(observer -> observer.update(type)); }
}
