package rogue.game.domain.enums;

/**
 * Represents the different states a game can be in.
 */
public enum GameState {
    PLAYER_MOVEMENT(1),
    LEVEL_GENERATION(2),
    USING_INVENTORY(3);

    public final int value;

    GameState(int value) {
        this.value = value;
    }
}
