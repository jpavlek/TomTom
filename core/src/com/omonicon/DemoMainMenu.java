package com.omonicon;

/**
 * Created by user on 07/02/16.
 */

import com.badlogic.gdx.Game;
        import com.badlogic.gdx.Gdx;
        import com.badlogic.gdx.Input.Keys;
        import com.badlogic.gdx.graphics.GL20;
        import com.badlogic.gdx.graphics.Texture;
        import com.badlogic.gdx.graphics.g2d.SpriteBatch;
        import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class DemoMainMenu extends DemoAbstractScreen {

    TextureRegion title;
    SpriteBatch batch;
    float time = 0;

    public DemoMainMenu (Game game) {
        super(game);
    }

    @Override
    public void show () {
        title = new TextureRegion(new Texture(Gdx.files.internal("data/title.png")), 0, 0, 480, 320);
        batch = new SpriteBatch();
        batch.getProjectionMatrix().setToOrtho2D(0, 0, 480, 320);
    }

    @Override
    public void render (float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        batch.draw(title, 0, 0);
        batch.end();

        time += delta;
        if (time > 1) {
            if (Gdx.input.isKeyPressed(Keys.ANY_KEY) || Gdx.input.justTouched()) {
                game.setScreen(new DemoIntroScreen(game));
            }
        }
    }

    @Override
    public void hide () {
        Gdx.app.debug("DemoPlatformer", "dispose main menu");
        batch.dispose();
        title.getTexture().dispose();
    }
}