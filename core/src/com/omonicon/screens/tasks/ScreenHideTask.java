package com.omonicon.screens.tasks;

import com.badlogic.gdx.utils.Timer;
import com.omonicon.screens.ScreenType;
import com.omonicon.screens.ScreenManager;

/**
 * Created by Jakov on 8.10.2015..
 */
public class ScreenHideTask extends Timer.Task {

    private ScreenType screen = null;

    public ScreenHideTask(ScreenType screen) {
        this.screen = screen;
    }

    @Override
    public void run() {
        /* easily implemented screen hiding thanks to singleton pattern */
        ScreenManager instance = ScreenManager.getScreenManagerInstance();
        instance.hide(screen);
    }
}
