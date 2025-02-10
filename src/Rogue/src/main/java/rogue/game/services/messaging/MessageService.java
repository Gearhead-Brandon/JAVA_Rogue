package rogue.game.services.messaging;

import rogue.game.domain.enums.StatusMessage;

import java.util.List;

public interface MessageService{
    /**
     * @return the list of status messages
     */
    List<String> getStatusMessages();

    void updateStatusMessage(String message);
    void updateStatusMessage(StatusMessage message);
    void update();
    void addStatusMessage(String message);
    void resetStatusMessages();
}
