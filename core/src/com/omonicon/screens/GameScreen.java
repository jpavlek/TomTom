package com.omonicon.screens;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;
import com.omonicon.game.world.World;
import com.omonicon.game.world.WorldController;
import com.omonicon.game.world.WorldRenderer;
import com.badlogic.gdx.input.GestureDetector.GestureListener;

/** b  wb sc
 * Created by user on 22/03/15.
 */
public class GameScreen implements Screen, InputProcessor, GestureListener {

    private World world;
    private WorldRenderer renderer;
    private WorldController controller;
    private int width;
    private int height;
    private Music themeSong;
    private boolean hasMusicSupport;
    private boolean paused;


    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.0353f, 0.22746f, 0.3687f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        if (!paused) {
            controller.update(delta);
        }

        renderer.render();
    }

    @Override
    public void resize(int width, int height) {
        renderer.setSize(width, height);
        this.width = width;
        this.height = height;
    }

    @Override
    public void pause() {
        paused = true;
    }

    @Override
    public void resume() {
        paused = false;
    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        Gdx.input.setInputProcessor(null);
        stopMusic();
        renderer.dispose();
    }

    @Override
    public void show() {
        startMusic();
        world = new World();
        renderer = new WorldRenderer(world);
        controller = new WorldController(world);
        paused = false;
        setInputProcessor();
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        int thirdWidth = width / 3;
        int halfHeight = height / 2;

        if (screenY < halfHeight) {
            controller.jumpPressed();
        }

        if (screenX <  thirdWidth) {
            controller.leftPressed();
        } else if (screenX > 2*thirdWidth) {
            controller.rightPressed();
        }

        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        /*
        int halfWidth = width / 2;
        if (screenX < halfWidth) {
            controller.leftReleased();
        } else if (screenX > halfWidth) {
            controller.rightReleased();
        }
        */
        controller.leftReleased();
        controller.rightReleased();
        controller.jumpReleased();
        return true;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }

    public void startMusic() {
        hasMusicSupport = true;
        String fileType = ".ogg";
        switch (Gdx.app.getType()) {
            case Desktop: {

            } break;
            case iOS: {
                hasMusicSupport = false;
                fileType = ".mp3";
            } break;
            case Android: {

            } break;
            default : {

            } break;
        }
        //https://soundcloud.com/eric-skiff/were-the-resistors
        if (hasMusicSupport) {
            //themeSong = Gdx.audio.newMusic(Gdx.files.internal("music/dragon_fyre-white_knights_remix"+fileType));
            //themeSong = Gdx.audio.newMusic(Gdx.files.internal("music/from_here"+fileType));
            themeSong = Gdx.audio.newMusic(Gdx.files.internal("music/we_re_the_resistors"+fileType));
            themeSong.setLooping(true);
            themeSong.setVolume(1.0f);
            themeSong.play();
        }
    }

    public void stopMusic() {
        if (hasMusicSupport) {
            themeSong.stop();
            themeSong.dispose();
        }
    }

    @Override
    public boolean pan(float x, float y, float deltaX, float deltaY) {
        renderer.panCamera(deltaX);
        return false;
    }

    @Override
    public boolean panStop(float x, float y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDown(float x, float y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean zoom(float initialDistance, float distance) {
        return false;
    }

    @Override
    public boolean fling(float velocityX, float velocityY, int button) {
        return false;
    }

    @Override
    public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
        return false;
    }

    @Override
    public boolean tap(float x, float y, int count, int button) {
        return false;
    }

    @Override
    public boolean longPress(float x, float y) {
        return false;
    }

    public void setInputProcessor() {
        InputMultiplexer inputMultiplexer = new InputMultiplexer();
        GestureDetector gestureDetector = new GestureDetector(this);
        inputMultiplexer.addProcessor(gestureDetector);
        inputMultiplexer.addProcessor(this);

        Gdx.input.setInputProcessor(inputMultiplexer);
    }
}
