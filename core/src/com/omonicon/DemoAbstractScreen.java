package com.omonicon;

/**
 * Created by user on 07/02/16.
 */

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;

public abstract class DemoAbstractScreen implements Screen {

    Game game;

    public DemoAbstractScreen (Game game) {
        this.game = game;
    }

    @Override
    public void resize (int width, int height) {
    }

    @Override
    public void show () {
    }

    @Override
    public void hide () {
    }

    @Override
    public void pause () {
    }

    @Override
    public void resume () {
    }

    @Override
    public void dispose () {
    }

}