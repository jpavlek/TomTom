package com.omonicon.screens;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Stage;

/**
 * Created by Jakov on 8.10.2015..
 * https://code.google.com/p/steigert-libgdx/source/browse/trunk/tyrian-game/src/com/blogspot/steigert/tyrian/screens/AbstractScreen.java?r=52
 https://github.com/angelnavarro/Gdx-Scene-Management/blob/master/core/src/com/pixnbgames/scene_management/screen/AbstractScreen.java
 https://github.com/angelnavarro/Gdx-Scene-Management
 */
public abstract class AbstractScreen extends Stage implements Screen {
    //public abstract ScreenEnum getScreenType();

    protected String getName() {
        return getClass().getSimpleName();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
    }

    @Override
    public void hide() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {

    }

}
