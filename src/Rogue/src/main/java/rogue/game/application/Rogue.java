package rogue.game.application;

import rogue.game.context.GameContext;
import rogue.game.context.GameFactory;
import rogue.game.common.observer.Observer;
import rogue.game.controller.Controller;
import rogue.game.view.View;

public class Rogue {
    public static void main( String[] args ) {
        GameContext gameContext = GameFactory.createGameContext();

        Controller controller = GameFactory.createController(gameContext);

        View v = GameFactory.createView(controller);

        gameContext.subscribe((Observer) v);

        v.startEventLoop();
    }
}
