package com.omonicon.screens.tasks;

import com.badlogic.gdx.utils.Timer;
import com.omonicon.screens.ScreenType;
import com.omonicon.screens.ScreenManager;

/**
 * Created by Jakov on 8.10.2015..
 */
public class ScreenSwitchTask extends Timer.Task {

    private ScreenType screen = null;

    public ScreenSwitchTask(ScreenType screen) {
        this.screen = screen;
    }

    @Override
    public void run() {
        /* easily implemented screen switching thanks to singleton pattern */
        ScreenManager.getScreenManagerInstance().show(screen);
    }
}
