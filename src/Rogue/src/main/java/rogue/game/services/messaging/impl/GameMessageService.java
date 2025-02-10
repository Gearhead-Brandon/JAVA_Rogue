package rogue.game.services.messaging.impl;

import rogue.game.services.messaging.MessageService;
import rogue.game.domain.services.pubsub.PubSubService;
import rogue.game.common.observer.EventType;
import rogue.game.domain.enums.StatusMessage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Service responsible for managing game messages.
 *
 * <p> This class stores a list of status messages and notifies subscribers
 * about changes in the list.
 */
public class GameMessageService implements MessageService {
    private final List<String> statusMessages;
    private final PubSubService pubSubService;

    public GameMessageService(PubSubService pubSubService) {
        statusMessages = new ArrayList<>();
        this.pubSubService = pubSubService;
    }

    @Override
    public List<String> getStatusMessages() {
        return Collections.unmodifiableList(statusMessages);
    }

    @Override
    public void updateStatusMessage(String message) {
        statusMessages.clear();
        statusMessages.add(message);
        pubSubService.notifyObserver(EventType.STATUS_MSG_UPDATE);
    }

    @Override
    public void updateStatusMessage(StatusMessage message) {
        updateStatusMessage(message.message);
    }

    @Override
    public void update() {
       pubSubService.notifyObserver(EventType.STATUS_MSG_UPDATE);
    }

    @Override
    public void addStatusMessage(String message) {
        statusMessages.add(message);
    }

    @Override
    public void resetStatusMessages(){
        statusMessages.clear();
        statusMessages.add(StatusMessage.EMPTY_MESSAGE.message);
        update();
        statusMessages.clear();
    }
}
