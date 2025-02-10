package rogue.game.view;

/**
 * This interface defines a contract responsible for the visual representation of the game.
 */
public interface View {
    /**
     * Starts the main game loop.
     *
     * <p> The game loop is responsible for updating the screen, handling user input, and managing game logic.
     */
    void startEventLoop();
}
