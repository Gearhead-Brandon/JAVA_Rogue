package rogue.game.controller;

import rogue.game.common.MapInfo;
import rogue.game.common.enums.UserAction;

import java.util.List;

/**
 * Interface defining the core functionality of the game controller.
 *
 * <p> This interface encapsulates the methods responsible for handling user input,
 * managing the game state, and providing information to the user interface.
 */
public interface Controller {
    /**
     * Handles user input
     * @param action the user action
     */
    void userInput(UserAction action);

    /**
     * Retrieves information about the current state of the map.
     *
     * @return The current map information.
     */
    MapInfo getMap();

    /**
     * @return the list of status messages
     */
    List<String> getStatusMessages();

    /**
     * @return the list of items in the inventory
     */
    List<String> getInventory();

    int getLevel();
    int getGold();
    int getStrength();
    int getStrengthWithWeapon();
    int getHealth();
    int getMaxHealth();

    /**
     * @return the list of stats
     */
    List<String> getListOfStats();
}
