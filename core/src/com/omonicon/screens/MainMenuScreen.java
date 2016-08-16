package com.omonicon.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.utils.Align;

import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.Timer;
import com.omonicon.GUICreator;
import com.omonicon.screens.tasks.ScreenHideTask;
import com.omonicon.screens.tasks.ScreenSwitchTask;

/**
 * Created by Jakov on 8.10.2015..
*/
public class MainMenuScreen extends AbstractScreen {

    private static final String TITLE = "Tom Tom";
    private static final String SUBTITLE = "Tom Tom's Adventures";
    private float captionX1 = 0;
    private float captionX2 = 0;
    private float captionY1 = 0;
    private float captionY2 = 0;

    private Texture     txtrBtn     = null;
    private Color       mBgColor    = null;
    private ScreenType  lastScreenShown;

    private long        startTime   = 0;
    private ScreenType  currentScreenShown;
    private ScreenType  nextScreenToShow = null;
    private float       showTimeSec = 2.0f;
    private String      mFontFile = "fonts/NotoSans-unhinted/NotoSans-Bold.ttf"; //"fonts/font_littera_45.fnt";
    private BitmapFont  mFont48 = null;

    private SpriteBatch mBatch       = null;

    public MainMenuScreen(ScreenType lastScreenShown) {
        super();
        mBatch = new SpriteBatch();
        txtrBtn = new Texture( Gdx.files.internal("images/BtnPlusPlus.png") );
        mBgColor = Color.RED;
        // Setting listeners
        //btnPlay.addListener(GUICreator.createListener(ScreenType.LEVEL_SELECT));
        this.lastScreenShown = lastScreenShown;
        FileHandle fontFileHandle = Gdx.files.internal(mFontFile);
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(fontFileHandle);
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontParameter();
        parameter.size = 48;
        mFont48 = generator.generateFont(parameter);
        generator.dispose();
        defaultTextInitialization();

        buildStage();
    }

    public MainMenuScreen(ScreenType lastScreenShown, ScreenType currentScreenType) {
        super();
        mBatch = new SpriteBatch();
        txtrBtn = new Texture( Gdx.files.internal("images/BtnPlusPlus.png") );
        mBgColor = Color.BLUE;
        startTime = TimeUtils.millis();
        currentScreenShown = currentScreenType;
        this.lastScreenShown = lastScreenShown;
        defaultTextInitialization();
        buildStage();
    }

    public void setStartTime() {
        startTime = TimeUtils.millis();
    }

    public void setCurrentScreenShown(ScreenType currentScreenType) {
        currentScreenShown = currentScreenType;
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

    @Override
    public void show() {

        /* if needed, schedule to show main menu screen after showTimeSec seconds */
        if (nextScreenToShow != null && showTimeSec >= 0.0f) {
            Timer.schedule(new ScreenSwitchTask(nextScreenToShow), showTimeSec);
        }

        if (nextScreenToShow == null && showTimeSec >= 0.0f) {
            Timer.schedule(new ScreenHideTask(currentScreenShown), showTimeSec);
        }
    }

    public void defaultTextInitialization() {
        mFont48 = new BitmapFont();
        mFont48.setColor(Color.BLACK);
        mFont48.getData().setScale(4.0f);

        GlyphLayout wholeCaptionBounds = new GlyphLayout();
        wholeCaptionBounds.setText(mFont48, TITLE);
        captionX1 = wholeCaptionBounds.width/2;
        captionY1 = wholeCaptionBounds.height/2;

        GlyphLayout libCaptionBounds = new GlyphLayout();
        libCaptionBounds.setText(mFont48, SUBTITLE);
        float libBounds = libCaptionBounds.width;
        captionX2 = captionX1 + libBounds + 1f;
        captionY2 = libCaptionBounds.height/2;

    }

    public void buildStage() {

        // Adding actors
        //Image bg = new Image(txtrBg);
        //addActor(bg);

        ImageButton btnPlay = GUICreator.createButton(txtrBtn);
        btnPlay.setPosition(getWidth() / 2, 120.f, Align.center);
        addActor(btnPlay);

        // Setting listeners
        //btnPlay.addListener( GUICreator.createListener(ScreenEnum.LEVEL_SELECT) );
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(mBgColor.r, mBgColor.g, mBgColor.b, mBgColor.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        act(delta);
        draw();
        mBatch.begin();
        mFont48.setColor(0f, 0f, 0f, 1f);
        mFont48.draw(mBatch, TITLE, (getWidth() - captionX1)/2, getHeight() / 2 - captionY1);
        mFont48.setColor(0f, 0f, 0f, 1f);
        mFont48.draw(mBatch, SUBTITLE, (getWidth() - captionX2)/2, getHeight() / 2 - 3*captionY2);
        mBatch.end();
    }

    @Override
    public void hide() {
        ScreenManager instance =  ScreenManager.getScreenManagerInstance();
        instance.hide(lastScreenShown);
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

    @Override
    public void dispose() {
        super.dispose();
        txtrBtn.dispose();
    }

    public void setBgColor(Color bgColor) {
        this.mBgColor = bgColor;
    }

}
