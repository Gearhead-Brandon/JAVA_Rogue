package rogue.game.common.enums;

/**
 * Enumeration representing various user actions within the game.
 */
public enum UserAction {
    NONE('0'),
    UP('w'),
    RIGHT('d'),
    DOWN('s'),
    LEFT('a'),
    USE_FOOD('j'),
    USE_WEAPON('h'),
    USE_POTION('k'),
    USE_SCROLL('e'),
    QUIT('q'),
    ENTER('\n'),
    CHOOSE_1_ITEM('1'),
    CHOOSE_2_ITEM('2'),
    CHOOSE_3_ITEM('3'),
    CHOOSE_4_ITEM('4'),
    CHOOSE_5_ITEM('5'),
    CHOOSE_6_ITEM('6'),
    CHOOSE_7_ITEM('7'),
    CHOOSE_8_ITEM('8'),
    CHOOSE_9_ITEM('9'),
    NEXT_MESSAGE(' ');

    public final char value;

    UserAction(char value) {
        this.value = value;
    }
}
