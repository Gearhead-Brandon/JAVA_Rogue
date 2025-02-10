package rogue.game.domain.enums;

/**
 * Represents the initial attributes of a player character.
 *
 * <p> These attributes define the player's starting health, strength, agility,
 * and initial facing direction.
 */
public enum PlayerAttribute {
    START_MAX_HEALTH(20),
    MIN_POSSIBLE_MAX_HEALTH(10),
    START_HEALTH(15),
    START_STRENGTH(16),
    START_AGILITY(6),
    START_ANGLE((int)Angles.RIGHT.angle);

    public final int value;

    PlayerAttribute(int value) {
        this.value = value;
    }
}
