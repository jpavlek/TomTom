package com.omonicon.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.utils.IntMap;

/**
 * Created by Jakov on 8.10.2015..
 * Screen manager class
 */
public final class ScreenManager {
    private static ScreenManager screenManagerInstance;
    private Game theGame;
    private IntMap<Screen> screens;

    private ScreenManager() {
        screens = new IntMap<Screen>();
    }

    public static ScreenManager getScreenManagerInstance() {
        if (screenManagerInstance == null) {
            screenManagerInstance = new ScreenManager();
        }
        return screenManagerInstance;
    }

    public void initialize(Game game) {
        this.theGame = game;
    }

    public void show(ScreenType screen) {

        if (theGame == null) {
            return;
        }

        int screenTypeId = screen.ordinal();

        if (!screens.containsKey(screenTypeId)) {
            screens.put(screenTypeId, screen.getScreenInstance());
        }
        Screen screenToShow = screens.get(screenTypeId);
        theGame.setScreen(screenToShow);
    }

    public void hide(ScreenType screen) {

        if (theGame == null) {
            return;
        }

        int screenTypeId = screen.ordinal();

        Screen screenToHide = screens.get(screenTypeId);
        if (screenToHide != null) {
            screenToHide.hide();
        }

        //theGame.setScreen(screenToHide);
    }

    public void dispose(ScreenType screen) {
        int screenTypeId = screen.ordinal();
        if ( !screens.containsKey(screenTypeId) ) {
            return;
        }
        Screen removedScreen = screens.remove(screenTypeId);

        if (removedScreen == null) {
            return;
        }
        removedScreen.dispose();
    }

    public void dispose() {
        for (Screen screen : screens.values()) {
            screen.dispose();
        }
        screens.clear();
        screenManagerInstance = null;
    }
}
