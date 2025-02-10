package rogue.game.domain.enums;

/**
 * Represents the core characteristics of entities in the game.
 */
public enum EntityCharacteristic {
    AGILITY("Agility"),
    STRENGTH("Strength"),
    MAX_HEALTH("Max health"),
    HEALTH("Health");

    public final String name;

    EntityCharacteristic(String name) {
        this.name = name;
    }
}
