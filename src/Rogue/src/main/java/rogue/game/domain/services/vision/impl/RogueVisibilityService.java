package rogue.game.domain.services.vision.impl;

import lombok.NoArgsConstructor;
import rogue.game.services.map.MapService;
import rogue.game.domain.enums.util.MapSymbolUtil;
import rogue.game.domain.services.vision.VisibilityService;
import rogue.game.common.enums.MapSymbol;
import rogue.game.domain.enums.Constants;
import rogue.game.domain.enums.MapAttribute;
import rogue.game.domain.entities.Position;

@NoArgsConstructor
public class RogueVisibilityService implements VisibilityService {
    @Override
    public void update(MapService map, Position pos, double angle) {
        final int startX = pos.x();
        final int startY = pos.y();

        // Von Neumann neighborhood
        for (int i = -1; i <= 1; i += 2){
            MapSymbol symbol = map.getMapSymbol(Position.of(startX, startY + i));
            if (symbol.equals(MapSymbol.WALL))
                map.setVisible(startX, startY + i, true);
        }
        for (int i = -1; i <= 1; i += 2){
            MapSymbol symbol = map.getMapSymbol(Position.of(startX + i, startY));
            if (symbol.equals(MapSymbol.WALL))
                map.setVisible(startX + i, startY, true);
        }

        angle = -angle;

        final double fieldOfView = Math.toRadians(120); // угол обзора
        final double halfFieldOfView = fieldOfView / 2;
        final double maxDistance = 30.0;

        angle = Math.toRadians(angle);

        final double radianStep = 0.0017;

        for (double i = angle - halfFieldOfView; i <= angle + halfFieldOfView; i += radianStep) {
            double rayX = startX;
            double rayY = startY;

            double sin_i = Math.sin(i);
            double cos_i = Math.cos(i);

            for (int step = 0; step < maxDistance; step++) {
                rayX += cos_i;
                rayY += sin_i;

                int x = (int)Math.round(rayX);
                int y = (int)Math.round(rayY);

                if (x < 0 || x >= MapAttribute.WIDTH.value || y < 0 || y >= MapAttribute.HEIGHT.value) {
                    break;
                }

                MapSymbol sym = map.getMapSymbol(Position.of(x, y));

                if(sym.equals(MapSymbol.WALL) || MapSymbolUtil.isDoor(sym)) {
                    bresenhamLine(map, startX, startY, x, y);
                    break;
                }
            }
        }
    }

/**
 * Implements the Bresenham's line algorithm to efficiently draw a line between two points on the map.
 *
 * <p> This method is used to mark cells as visible along the ray cast lines.
 *
 * @param map The map service providing map data.
 * @param x0 The x-coordinate of the starting point.
 * @param y0 The y-coordinate of the starting point.
 * @param x1 The x-coordinate of the ending point.
 * @param y1 The y-coordinate of the ending point. 1
 */
private void bresenhamLine(MapService map, int x0, int y0, int x1, int y1) {
        boolean steep = Math.abs(y1 - y0) > Math.abs(x1 - x0);

        if (steep) {
            int temp = x0;
            x0 = y0;
            y0 = temp;

            temp = x1;
            x1 = y1;
            y1 = temp;
        }

        if (x0 > x1) {
            int temp = x0;
            x0 = x1;
            x1 = temp;

            temp = y0;
            y0 = y1;
            y1 = temp;
        }

        int dx = x1 - x0;
        int dy = Math.abs(y1 - y0);
        int error = dx / 2;
        int y_step = (y0 < y1) ? 1 : -1;
        int y = y0;

        for (int x = x0; x <= x1; x++){
            int x_ = steep ? y : x;
            int y_ = steep ? x : y;

            map.setVisible(x_, y_, true);

            error -= dy;
            if (error < 0) {
                y += y_step;
                error += dx;
            }
        }
    }

    @Override
    public boolean seeInCorridor(MapService map, Position from, Position to) {
        final int px = from.x();
        final int py = from.y();

        final int ex = to.x();
        final int ey = to.y();

        for (int i = 0; i < 4; i++) {

            int dxi = Constants.DX.directionsValues.get(i);
            int dyi = Constants.DY.directionsValues.get(i);

            int nx = px + dxi;
            int ny = py + dyi;

            while (true) {
                if (nx == ex && ny == ey)
                    return true;

                MapSymbol sym = map.getMapSymbol(nx, ny);

                if (sym.equals(MapSymbol.EMPTINESS) || MapSymbolUtil.isDoor(sym) || sym.equals(MapSymbol.WALL))
                    break;

                nx += dxi;
                ny += dyi;
            }
        }

        return false;
    }

    @Override
    public void showCorridorCell(MapService map, Position pos){
        final int x = pos.x();
        final int y = pos.y();

        //Von Neumann neighborhood
        for (int i = -1; i <= 1; i += 2) {
            MapSymbol sym = map.getMapSymbol(x, y + i);
            if(isPassable(sym))
                map.setVisible(x, y + i, true);
        }

        for (int j = -1; j <= 1; j += 2) {
            MapSymbol sym = map.getMapSymbol(x + j, y);
            if(isPassable(sym))
                map.setVisible(x + j, y, true);
        }
    }

    private boolean isPassable(MapSymbol symbol) {
        return symbol.equals(MapSymbol.CORRIDOR) ||
                MapSymbolUtil.isDoor(symbol) ||
                MapSymbolUtil.isEnemy(symbol);
    }
 }
