package rogue.game.view.impl;

import jcurses.system.CharColor;
import jcurses.system.Toolkit;
import rogue.game.common.enums.MapColor;
import rogue.game.common.MapInfo;
import rogue.game.common.enums.MapSymbol;
import rogue.game.common.enums.UserAction;
import rogue.game.common.observer.EventType;
import rogue.game.common.observer.Observer;
import rogue.game.common.enums.util.UserActionUtil;
import rogue.game.controller.Controller;
import rogue.game.view.View;
import rogue.game.view.enums.ViewAttribute;
import rogue.game.view.enums.ViewColor;
import rogue.game.view.enums.ViewSymbol;

import java.util.List;
import java.util.Map;

/**
 * Implements the game's view, handling the visual presentation and user interaction.
 *
 * <p> This class is responsible for displaying the game world, player information, and status messages.
 *
 * <p> It also handles user input and triggers appropriate actions in the game logic.
 */
public class RogueView implements View, Observer {
    private final Controller controller;

    /**
     * A map associating event types with their corresponding handler methods.
     *
     * <p> This map allows efficient event handling by directly linking an event type to the appropriate method.
     */
    private Map<EventType, Runnable> eventHandlers;

    /**
     * Maps {@link MapColor} to {@link CharColor}
     *
     * <p> This map is used to translate map colors into terminal color codes for display.
     */
    private Map<MapColor, CharColor> colorMap;

    public RogueView(Controller controller){
        this.controller = controller;;
        createEventHandlers();
        createColorMap();
    }

    private void createEventHandlers(){
        eventHandlers = Map.of(
                EventType.GOLD_UPDATE, this::updateGold,
                EventType.STATUS_MSG_UPDATE, this::updateGameMessages,
                EventType.MAP_UPDATE, this::updateMap,
                EventType.INVENTORY_UPDATE, this::updateInventory,
                EventType.LEVEL_UPDATE, this::updateLevel,
                EventType.HITS_UPDATE, this::updateHealth,
                EventType.STRENGTH_UPDATE, this::updateStrength
        );
    }

    private void createColorMap(){
        colorMap = Map.of(
                MapColor.GREEN, ViewColor.GREEN.color,
                MapColor.LIGHT_GREEN, ViewColor.LIGHT_GREEN.color,
                MapColor.RED, ViewColor.RED.color,
                MapColor.WHITE, ViewColor.WHITE.color,
                MapColor.YELLOW, ViewColor.BOLD_YELLOW.color,
                MapColor.ORANGE, ViewColor.ORANGE.color,
                MapColor.MAGENTA, ViewColor.MAGENTA.color,
                MapColor.BLUE, ViewColor.BLUE.color,
                MapColor.CYAN, ViewColor.CYAN.color
        );
    }

    @Override
    public void startEventLoop() {
        Toolkit.init();
        showMenu();
        Toolkit.shutdown();
    }

    /**
     * Displays the main menu, allowing the user to choose between playing the game and viewing statistics.
     */
    private void showMenu(){
        Toolkit.clearScreen(ViewColor.BLACK.color);

        final int PLAY_OPTION = 0;
        final int SHOW_STATISTICS_OPTION = 1;

        int currentOption = 0;

        printGameName();

        UserAction userAction;

        while(true){
            Toolkit.printString(ViewSymbol.PLAY.value, 49, 14, currentOption == PLAY_OPTION ? ViewColor.RED_ACTIVITY_STRING.color : ViewColor.RED.color);
            Toolkit.printString(ViewSymbol.STATISTICS.value, 46, 16, currentOption == SHOW_STATISTICS_OPTION ? ViewColor.RED_ACTIVITY_STRING.color : ViewColor.RED.color);

            userAction = UserActionUtil.fromValue(Toolkit.readCharacter().getCode());

            if(userAction == UserAction.UP) {
                currentOption = PLAY_OPTION;
            }else if(userAction == UserAction.DOWN) {
                currentOption = SHOW_STATISTICS_OPTION;
            }else if(userAction == UserAction.QUIT) {
                break;
            }else if(userAction == UserAction.ENTER){
                if(currentOption == PLAY_OPTION)
                    showGame();
                else
                    showStatistics();

                Toolkit.clearScreen(ViewColor.BLACK.color);
                printGameName();
            }
        }
    }

    private void printGameName(){
        int x = 42;
        int y = 8;

        Toolkit.printString(ViewSymbol.MENU_VERTICAL_WALL.value, x, y++, ViewColor.RED.color);
        Toolkit.printString(ViewSymbol.MENU_HORIZONTAL_WALL.value, x, y++, ViewColor.RED.color);
        Toolkit.printString(ViewSymbol.ROGUE_LABEL.value, x, y++, ViewColor.RED.color);
        Toolkit.printString(ViewSymbol.MENU_HORIZONTAL_WALL.value, x, y++, ViewColor.RED.color);
        Toolkit.printString(ViewSymbol.MENU_VERTICAL_WALL.value, x, y, ViewColor.RED.color);
    }

    /**
     * Displays the statistics of all walkthroughes
     */
    private void showStatistics(){
        Toolkit.clearScreen(ViewColor.BLACK.color);

        UserAction userAction = UserAction.NONE;

        Toolkit.printString(ViewSymbol.STATISTICS_OF_ALL_WALKTHROUGHES.value, 40, 2, ViewColor.RED.color);

        int firstRow = 0;

        List<String> stats = controller.getListOfStats();

        final int size = controller.getListOfStats().size();

        if(size == 0)
            Toolkit.printString(ViewSymbol.NO_WALKTHROUGHES_FOUND.value, 49, 4, ViewColor.RED.color);

        while(userAction != UserAction.QUIT){
            if(size > 0) {
                clearArea(4, 30);
                printStatsWalkthroughes(stats, firstRow);
            }

            userAction = UserActionUtil.fromValue(Toolkit.readCharacter().getCode());

            if(size > 0) {
                if (userAction == UserAction.UP) {
                    if (firstRow > 0)
                        firstRow--;
                } else if (userAction == UserAction.DOWN) {
                    if (firstRow < size - ViewAttribute.MAX_NUMBER_OF_WALKTHROUGHES.value)
                        firstRow++;
                }
            }
        }
    }

    /**
     * Displays the statistics of all walkthroughes
     * @param stats list of stats to print
     * @param firstRow index of the first row
     */
    private void printStatsWalkthroughes(final List<String> stats, final int firstRow){
        int y = 4;

        int size = stats.size();

        final int sizeToPrint = Math.min(size, ViewAttribute.MAX_NUMBER_OF_WALKTHROUGHES.value);
        final int firstRowToPrint;

        if(size <= ViewAttribute.MAX_NUMBER_OF_WALKTHROUGHES.value)
            firstRowToPrint = 0;
        else
            firstRowToPrint = Math.max(0, firstRow % sizeToPrint);

        for(int i = firstRowToPrint; i < sizeToPrint + firstRowToPrint; i++){
            Toolkit.printString(stats.get(i), 5, y++, ViewColor.WHITE.color);

            if(sizeToPrint + firstRowToPrint - 1 != i)
                Toolkit.printString(ViewSymbol.STATISTICS_SEPARATOR.value, 2, y++, ViewColor.RED.color);
        }
    }

    /**
     * Starts the game
     */
    private void showGame(){
        Toolkit.clearScreen(ViewColor.BLACK.color);

        UserAction userAction = UserAction.NONE;

        printPlayerCharacteristicsLabels();
        updateMap();

        while(userAction != UserAction.QUIT){
            int input = Toolkit.readCharacter().getCode();
            userAction = UserActionUtil.fromValue(input);
            controller.userInput(userAction);
        }
    }

    @Override
    public void update(EventType type) {
        Toolkit.startPainting();;
        eventHandlers.getOrDefault(type, () -> {}).run();
        Toolkit.endPainting();
    }

    /**
     * Displays the game messages
     */
    private void updateGameMessages(){
        final List<String> messages = controller.getStatusMessages();

        if(messages.size() == 1) {
            Toolkit.printString(ViewSymbol.EMPTY_HALF_WIDE_STR.value, 4, 1,ViewColor.WHITE.color);
            Toolkit.printString(messages.getFirst(), 4, 1, ViewColor.BOLD_YELLOW.color);
        }else if(messages.size() > 1) {
            for (String message : messages) {
                Toolkit.printString(ViewSymbol.EMPTY_WIDE_STR.value, 4, 1, ViewColor.WHITE.color);
                Toolkit.printString(message, 4, 1, ViewColor.BOLD_YELLOW.color);
                Toolkit.printString(ViewSymbol.MORE.value, 4 + message.length() + 1, 1, ViewColor.WHITE_ACTIVITY_STRING.color);

                UserAction userAction;

                do {
                    userAction = UserActionUtil.fromValue(Toolkit.readCharacter().getCharacter());
                }while (!userAction.equals(UserAction.NEXT_MESSAGE));
            }

            Toolkit.printString(ViewSymbol.EMPTY_WIDE_STR.value, 4, 1, ViewColor.WHITE.color);
        }
    }

    /**
     * Displays the inventory of the player
     */
    private void updateInventory(){
        List<String> inventory = controller.getInventory();

        for(int i = 0; i < inventory.size(); i++){
            Toolkit.printString(ViewSymbol.EMPTY_WIDE_STR.value, 0, i + ViewAttribute.STATUS_BAR_HEIGHT.value, ViewColor.BOLD_YELLOW.color);
            Toolkit.printString(inventory.get(i), 30, i + ViewAttribute.STATUS_BAR_HEIGHT.value, ViewColor.BOLD_YELLOW.color);
        }
    }

    private void updateMap(){
        clearArea(ViewAttribute.STATUS_BAR_HEIGHT.value, controller.getMap().getHeight());
        MapInfo mapInfo = controller.getMap();

        for(int i = 0; i < mapInfo.getHeight(); i++){
            for(int j = 0; j < mapInfo.getWidth(); j++){
                MapSymbol v = mapInfo.getSymbol(i, j);

                if(v != MapSymbol.EMPTINESS)
                    Toolkit.printString(v.symbol, j, i + ViewAttribute.STATUS_BAR_HEIGHT.value, colorMap.get(mapInfo.getSymbolColor(i, j)));
            }
        }
    }

    private void updateLevel(){
        Toolkit.printString(ViewSymbol.EMPTY_VALUE_STR.value, 17, 35, ViewColor.WHITE.color);
        Toolkit.printString(String.valueOf(controller.getLevel()), 17, 35, ViewColor.BOLD_YELLOW.color);
    }

    private void updateHealth(){
        Toolkit.printString(ViewSymbol.DOUBLE_EMPTY_VALUE_STR.value, 31, 35, ViewColor.WHITE.color);
        Toolkit.printString(controller.getHealth() + "/" + controller.getMaxHealth(), 31, 35, ViewColor.BOLD_YELLOW.color);
    }

    private void updateStrength(){
        Toolkit.printString(ViewSymbol.EMPTY_VALUE_STR.value, 53, 35, ViewColor.WHITE.color);
        Toolkit.printString(controller.getStrength() + "/" + controller.getStrengthWithWeapon(), 53, 35, ViewColor.BOLD_YELLOW.color);
    }

    private void updateGold(){
        Toolkit.printString(ViewSymbol.EMPTY_VALUE_STR.value, 71, 35, ViewColor.WHITE.color);
        Toolkit.printString(String.valueOf(controller.getGold()), 71, 35, ViewColor.BOLD_YELLOW.color);
    }

    private void clearArea(final int firstRow,final int height){
        for(int i = firstRow; i < height + firstRow; i++)
            Toolkit.printString(ViewSymbol.EMPTY_WIDE_STR.value, 0, i , ViewColor.WHITE.color);
    }

    /**
     * Displays the labels of the player characteristics
     */
    private void printPlayerCharacteristicsLabels(){
        Toolkit.startPainting();

        Toolkit.printString(ViewSymbol.LABEL_LEVEL.value, 9, 35, ViewColor.BOLD_YELLOW.color);
        updateLevel();

        Toolkit.printString(ViewSymbol.LABEL_HEALTH.value, 24, 35, ViewColor.BOLD_YELLOW.color);
        updateHealth();

        Toolkit.printString(ViewSymbol.LABEL_STRENGTH.value, 42, 35, ViewColor.BOLD_YELLOW.color);
        updateStrength();

        Toolkit.printString(ViewSymbol.LABEL_GOLD.value, 64, 35, ViewColor.BOLD_YELLOW.color);
        updateGold();

        Toolkit.printString(ViewSymbol.OPENING_INVENTORY.value, 9, 38, ViewColor.BOLD_YELLOW.color);

        Toolkit.endPainting();
    }
}
