package rogue.game.infrastructure.dataAccess.model;

import rogue.game.domain.entities.Position;

/**
 * Represents a {@link Position} in the game
 * @param x value of the position
 * @param y value of the position
 */
public record PositionModel(int x, int y) {}
