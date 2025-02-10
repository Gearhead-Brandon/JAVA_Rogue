package rogue.game.services.gameLogic;

import rogue.game.common.enums.UserAction;

/**
 * Interface defining the core functionality of the game service.
 *
 * <p> This interface encapsulates the essential game mechanics,
 * including handling user input, managing game state, and
 * providing information to the user interface.
 */
public interface GameService {
    /**
     * Handles user input
     * @param action the user action
     */
    void userInput(UserAction action);
}
