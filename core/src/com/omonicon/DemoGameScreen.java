package com.omonicon;

/**
 * Created by user on 07/02/16.
 */

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL20;
import com.omonicon.DemoMap;
import com.omonicon.DemoMapRenderer;


public class DemoGameScreen extends DemoAbstractScreen {

    DemoMap map;
    DemoMapRenderer mapRenderer;

    public DemoGameScreen (Game game) {
        super(game);
    }

    @Override
    public void show () {
        map = new DemoMap();
        mapRenderer = new DemoMapRenderer(map);
    }

    @Override
    public void render (float delta) {
        delta = Math.min(0.06f, Gdx.graphics.getDeltaTime());
        map.update(delta);
        Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        mapRenderer.render(delta);
        if (Gdx.input.isKeyPressed(Keys.ESCAPE)) {
            game.setScreen(new DemoMainMenu(game));
        }
    }

    @Override
    public void hide () {
        Gdx.app.debug("Cuboc", "dispose game screen");

    }
}

