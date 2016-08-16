package com.omonicon.game.platformer;

import com.badlogic.gdx.Game;
import com.omonicon.screens.ScreenManager;
import com.omonicon.screens.ScreenType;

/**
 * Created by user on 06/02/16.
 */
public class Platformer extends Game {
    private ScreenManager screenManagerInstance = null;

    @Override
    public void create () {
        initializeScreenManager();
        startSplashScreenSequence();
    }

    public void initializeScreenManager() {
        screenManagerInstance = ScreenManager.getScreenManagerInstance();
        screenManagerInstance.initialize(this);
    }

    public void startSplashScreenSequence() {
        screenManagerInstance.show(ScreenType.SPLASH_SCREEN_OCEAN_MEDIA);
    }

    @Override
    public void dispose() {
        super.dispose();
        screenManagerInstance.dispose();
    }

}
