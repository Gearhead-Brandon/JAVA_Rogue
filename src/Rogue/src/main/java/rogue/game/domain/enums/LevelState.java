package rogue.game.domain.enums;

import rogue.game.domain.entities.level.Level;

/**
 * Tracks whether the {@link Level} was loaded from a save or created anew.
 */
public enum LevelState {
    /**
     * The level was not saved to the repository and a new one was created
     */
    NEW,
    /**
     * The level was downloaded from the repository.
     */
    LOADED
}
