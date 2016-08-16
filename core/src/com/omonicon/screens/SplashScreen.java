package com.omonicon.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.Timer;

import com.omonicon.screens.tasks.ScreenSwitchTask;
import com.omonicon.screens.tasks.ScreenHideTask;

/**
 * Created by Jakov on 8.10.2015.
 */
public class SplashScreen extends AbstractScreen {

    private SpriteBatch mBatch       = null;
    private Texture     mImg         = null;
    private int         mImgPosX     = -1;
    private int         mImgPosY     = -1;
    private long        startTime   = 0;
    private ScreenType  currentScreenShown;
    private ScreenType  nextScreenToShow = null;
    private float       showTimeSec = 2.0f;
    private Color       mBgColor = Color.WHITE;

    public SplashScreen(String imageToShow, ScreenType currentScreenType) {
        mBatch = new SpriteBatch();
        mImg = new Texture(imageToShow);
        setBgColor(mBgColor);
        setImagePosition();
        startTime = TimeUtils.millis();
        currentScreenShown = currentScreenType;
    }

    @Override
    public void dispose() {
        if (mBatch != null) {
            mBatch.dispose();
            mBatch = null;
        }
        if (mImg != null) {
            mImg.dispose();
            mImg = null;
        }
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(mBgColor.r, mBgColor.g, mBgColor.b, mBgColor.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        if (mBatch != null) {
            mBatch.begin();
            if (mImg != null) {
                mBatch.draw(mImg, mImgPosX, mImgPosY);
            }
            mBatch.end();
        }
    }

    @Override
    public void show() {

        /* schedule to show main menu screen after showTimeSec seconds */
        if (nextScreenToShow != null && showTimeSec >= 0.0f) {
            Timer.schedule(new ScreenSwitchTask(nextScreenToShow), showTimeSec);
        }

        if (nextScreenToShow == null && showTimeSec >= 0.0f) {
            Timer.schedule(new ScreenHideTask(currentScreenShown), showTimeSec);
        }
    }

    @Override
    public void hide() {
        /* dispose current screen because it won't be needed anymore */
        ScreenManager screenManagerInstance = ScreenManager.getScreenManagerInstance();
        ScreenType css = currentScreenShown;
        screenManagerInstance.dispose(currentScreenShown);
        //currentScreenShown = ScreenType.NO_SCREEN;
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void resize(int width, int height) {

    }

    protected void setImagePosition() {
        int screenWidth = Gdx.graphics.getWidth();
        int screenHeight = Gdx.graphics.getHeight();
        int imgWidth = mImg.getWidth();
        int imgHeight = mImg.getHeight();
        mImgPosX = (screenWidth - imgWidth)/2;
        mImgPosY = (screenHeight - imgHeight)/2;
    }

    public Color getBgColor() {
        return mBgColor;
    }

    public void setBgColor(Color bgColor) {
        this.mBgColor = bgColor;
    }

    public ScreenType getNextScreenToShow() {
        return nextScreenToShow;
    }

    public void setNextScreenToShow(ScreenType nextScreenToShow) {
        this.nextScreenToShow = nextScreenToShow;
    }

    public float getShowTimeSec() {
        return showTimeSec;
    }

    public void setShowTimeSec(float showTimeSec) {
        this.showTimeSec = showTimeSec;
    }
}
