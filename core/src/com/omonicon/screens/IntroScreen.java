package com.omonicon.screens;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.omonicon.screens.tasks.ScreenSwitchTask;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.Timer;

class IntroScreen implements Screen {
	
	private static final String LIB = "lib";
	private static final String GDX = "GDX\nJakov Pavlek\nOcean Media\netc etc etc etc\netc";

	private BitmapFont mFont = null;
	private float captionX1 = 0;
	private float captionX2 = 0;
	private float captionY = 0;


	private SpriteBatch mBatch       = null;
	private Texture     mImg         = null;
	private int         mImgPosX     = -1;
	private int         mImgPosY     = -1;
	private long        startTime   = 0;
	private ScreenType  currentScreenShown;
	private ScreenType  nextScreenToShow = null;
	private float       showTimeSec = 2.0f;
	private Color		mBgColor = Color.WHITE;;

	public IntroScreen(String imageToShow, ScreenType currentScreenType) {
		mBatch = new SpriteBatch();
		mImg = new Texture(imageToShow);
		defaultTextInitialization();
		setBgColor(mBgColor);
		setImagePosition();
		startTime = TimeUtils.millis();
		currentScreenShown = currentScreenType;
	}

	public IntroScreen() {
		mBatch = new SpriteBatch();
		defaultTextInitialization();
	}

	public void defaultTextInitialization() {

        FileHandle fontFileHandle = Gdx.files.internal("fonts/Android Nation Bold.ttf");
        //FileHandle fontFileHandle = ćđGdx.files.internal("fonts/Amiga Forever.ttf");
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(fontFileHandle);
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 48;
        mFont = generator.generateFont(parameter);
        generator.dispose();

		mFont.setColor(Color.BLACK);
		// defaultBig.setScale(2.0f);
		// enable linear texture filtering for smooth fonts
		//mFont.getRegion().getTexture().setFilter( TextureFilter.Linear, TextureFilter.Linear);

		GlyphLayout wholeCaptionBounds = new GlyphLayout();
		String libGDX = LIB + GDX;
		wholeCaptionBounds.setText(mFont, libGDX);
		captionX1 = -wholeCaptionBounds.width/2;
		captionY = wholeCaptionBounds.height/2;

		GlyphLayout libCaptionBounds = new GlyphLayout();
		libCaptionBounds.setText(mFont, LIB);
		float libBounds = libCaptionBounds.width;
		captionX2 = captionX1 + libBounds + 1f;

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

	protected void setImagePosition() {
		int screenWidth = Gdx.graphics.getWidth();
		int screenHeight = Gdx.graphics.getHeight();
		int imgWidth = mImg.getWidth();
		int imgHeight = mImg.getHeight();
		mImgPosX = (screenWidth - imgWidth)/2;
		mImgPosY = (screenHeight - imgHeight)/2;
	}

	public float getShowTimeSec() {
		return showTimeSec;
	}

	public void setShowTimeSec(float showTimeSec) {
		this.showTimeSec = showTimeSec;
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(1f, 1f, 1f, 1f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		mBatch.begin();
		mFont.setColor(0f, 0f, 0f, 1f);
		mFont.draw(mBatch, LIB, captionX1, captionY);
		mFont.setColor(1f, 0f, 0f, 1f);
		mFont.draw(mBatch, GDX, captionX2, captionY);
		mBatch.end();
	}

	@Override
	public void resize(int width, int height) {
		mBatch.getProjectionMatrix().setToOrtho2D(-width / 2, -height / 2, width, height);
	}

	@Override
	public void show() {
		/* schedule to show main menu screen after 2 seconds */
		Timer.schedule(new ScreenSwitchTask(ScreenType.MAIN_MENU_SCREEN), 2f);
	}

	@Override
	public void hide() {
		/* dispose intro screen because it won't be needed anymore */
		ScreenManager.getScreenManagerInstance().dispose(ScreenType.INTRO_SCREEN);
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void dispose() {
		mFont.dispose();
		mBatch.dispose();
	}

}
