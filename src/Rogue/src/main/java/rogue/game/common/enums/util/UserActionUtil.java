package rogue.game.common.enums.util;

import rogue.game.common.enums.UserAction;

/**
 * Utility class for handling user actions.
 *
 * <p> Provides methods for converting character inputs to UserAction enums,
 * checking if an action is related to inventory usage, and
 * extracting inventory selection index from the action.
 */
public class UserActionUtil {
    public static UserAction fromValue(int value) {
        if(value >= 'A' && value <= 'Z')
            value = value - 'A' + 'a';

        for (UserAction action : UserAction.values()) {
            if (action.value == value) {
                return action;
            }
        }

        return UserAction.NONE;
    }

    public static boolean isUsageOfInventory(UserAction action) {
        return action.equals(UserAction.USE_FOOD) || action.equals(UserAction.USE_POTION)
                || action.equals(UserAction.USE_SCROLL) || action.equals(UserAction.USE_WEAPON);
    }

    public static boolean isInventorySelection(UserAction action) {
        return action.value >= '1' && action.value <= '9';
    }

    public static int getIndexInInventory(UserAction action) {
        return action.value - '1';
    }
}
