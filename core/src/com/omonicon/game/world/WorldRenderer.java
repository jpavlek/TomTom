package com.omonicon.game.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Disposable;
import com.omonicon.physics.Block;

/**
 * Created by user on 22/03/15.
 */
public class WorldRenderer implements Disposable {

    private int windowWidth;
    private int windowHeight;

    // camera parameters
    private OrthographicCamera camera;
    private static final float CAMERA_WIDTH = 10f;
    private static final float CAMERA_HEIGHT = 7f;
    private float ppux;
    private float ppuy;

    //world
    private World world;

    //character
    private Tom tom;

    //background
    private Texture backGroundTexture;
    //block tex
    private Texture blockTexture;

    //canvas where we put all art
    private SpriteBatch spriteBatch;

    ShapeRenderer debugRenderer = new ShapeRenderer();


    //animations parameters
    private static final int        FRAME_COLS = 5;
    private static final int        FRAME_ROWS = 4;

    //private Sprite                backGroundSprite;
    private Texture tomSpriteAnimTexture;
    private TextureRegion[]         walkFrames;
    private TextureRegion[]         walkFrontFrames;
    private TextureRegion[]         walkLeftFrames;
    private TextureRegion[]         walkBackFrames;
    private TextureRegion[]         walkRightFrames;
    private TextureRegion currentFrame;
    private Animation walkAnimation;
    private Animation walkFrontAnimation;
    private Animation walkLeftAnimation;
    private Animation walkBackAnimation;
    private Animation walkRightAnimation;
    float stateTime;
    Vector3 lerpTarget = new Vector3();

    public WorldRenderer(World world) {
        this.world = null;
        this.tom = null;
        this.camera = null;
        this.spriteBatch = null;
        this.blockTexture = null;
        this.backGroundTexture = null;
        //this.backGroundSprite = null;
        windowWidth = 0;
        windowHeight = 0;
        tomSpriteAnimTexture = null;
        stateTime = 0.0f;
        initWorldRenderer(world);
    }

    public void initWorldRenderer(World world) {
        this.world = world;
        this.tom = world.getTom();
        cameraSetUp();
        this.spriteBatch = new SpriteBatch();
        loadTextures();
        setBackGround();
        createGameCharacterAnimations();
    }

    public void setSize(int w, int h) {
        this.windowWidth = w;
        this.windowHeight = h;
        this.ppux = w/CAMERA_WIDTH;
        this.ppuy = h/CAMERA_HEIGHT;
    }

    private void cameraSetUp() {
        this.camera = new OrthographicCamera(CAMERA_WIDTH, CAMERA_HEIGHT);
        //this.camera.setToOrtho(false);
        this.camera.position.set(CAMERA_WIDTH/2f, CAMERA_HEIGHT/2f, 0);
        //this.camera.translate(-CAMERA_WIDTH/2f, -CAMERA_HEIGHT/2f);
        this.camera.update();
    }

    public OrthographicCamera getCamera() {
        return camera;
    }

    public void panCamera(float deltaX) {
        this.camera.translate(deltaX/ppux,0);
        this.camera.update();
    }

    private void loadTextures() {

        //Load Block Texture
        blockTexture = new Texture(Gdx.files.internal("images/block.png"));

        //Load Game Character Sprite Animation Texture
        //Art Source by digibody @ elouai.com - http://digibody.deviantart.com/art/Animated-sprite-sheet-32x32-188456006
        //tomSpriteAnimTexture = new Texture(Gdx.files.internal("ass_32x32x4x5.png"));
        tomSpriteAnimTexture = new Texture(Gdx.files.internal("images/sprites/ass_128x128x4x5.png"));
        backGroundTexture = new Texture(Gdx.files.internal("images/torontowide.jpg"));
    }

    public void setBackGround() {
        //backGroundTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        //backGroundSprite = new Sprite(backGroundTexture);
        //backGroundSprite.setOrigin(0,0);
        //backGroundSprite.setPosition(-backGroundSprite.getWidth()/8,-backGroundSprite.getHeight()/8);
    }

    private void createGameCharacterAnimations() {

        int characterFrameWidth = tomSpriteAnimTexture.getWidth()/FRAME_COLS;
        int characterFrameHeight = tomSpriteAnimTexture.getHeight()/FRAME_ROWS;

        TextureRegion[][] tmp = TextureRegion.split(tomSpriteAnimTexture, characterFrameWidth, characterFrameHeight);

        walkFrames = new TextureRegion[FRAME_COLS * FRAME_ROWS];
        walkFrontFrames = new TextureRegion[FRAME_COLS];
        walkLeftFrames = new TextureRegion[FRAME_COLS*2];
        walkBackFrames = new TextureRegion[FRAME_COLS];
        walkRightFrames = new TextureRegion[FRAME_COLS*2];

        //Fill Animation Frames with Texture Regions
        int index = 0;
        for (int i = 0; i < FRAME_ROWS; i++) {
            for (int j = 0; j < FRAME_COLS; j++) {
                walkFrames[index++] = tmp[i][j];
                switch (i) {
                    case 0: {
                        walkFrontFrames[j] = tmp[i][j];
                    } break;
                    case 1: {
                        walkLeftFrames[j] = tmp[i][j];
                        walkLeftFrames[FRAME_COLS*2-1-j] = tmp[i][j];
                    } break;
                    case 2: {
                        walkBackFrames[j] = tmp[i][j];
                    } break;
                    case 3: {
                        walkRightFrames[j] = tmp[i][j];
                        walkRightFrames[FRAME_COLS*2-1-j] = tmp[i][j];
                    } break;
                }

            }

        }

        //Create Animations
        walkAnimation = new Animation(0.04f, walkFrames);
        walkFrontAnimation = new Animation(0.04f, walkFrontFrames);
        walkLeftAnimation = new Animation(0.04f, walkLeftFrames);
        walkBackAnimation = new Animation(0.04f, walkBackFrames);
        walkRightAnimation = new Animation(0.04f, walkRightFrames);

        //Set Initial State Timer
        stateTime = 0f;
    }

    private void drawBackGround() {
        spriteBatch.draw(backGroundTexture, 0.0f, 0.0f, windowWidth, windowHeight);
    }

    private void drawBlocks() {
        for ( Block block: world.getLevel().getBlocks() ) {
            Rectangle rect = block.getMechanics().getBounds();
            float x1 = rect.x;
            float y1 = rect.y;
            spriteBatch.draw(blockTexture, x1*ppux, y1*ppuy, ppux, ppuy);
        }
    }

    private void drawCollisionBlocks() {
        debugRenderer.setProjectionMatrix(camera.combined);
        debugRenderer.begin(ShapeRenderer.ShapeType.Filled);
        debugRenderer.setColor(new Color(0, 0, 1, 1));
        for (Rectangle rect : world.getCollisionRects()) {
            debugRenderer.rect(rect.x, rect.y, rect.width, rect.height);
        }
        debugRenderer.end();
    }


    private void drawTom() {
        Vector2 tomPosition = tom.getMoveMechanics().getRelPosition();
        GameCharacter.Orientation tomOrientation = tom.getOrientation();
        int orientationOrdinal = tomOrientation.ordinal();
        Animation currentAnimation = null;
        switch (orientationOrdinal) {
            case 0: {
                currentAnimation = walkFrontAnimation;
            } break;
            case 1: {
                currentAnimation = walkLeftAnimation;
            } break;
            case 2: {
                currentAnimation = walkBackAnimation;
            } break;
            case 3: {
                currentAnimation = walkRightAnimation;
            } break;
            default: {
                currentAnimation = walkFrontAnimation;
            } break;
        }

        stateTime += Gdx.graphics.getDeltaTime();
        currentFrame = currentAnimation.getKeyFrame(stateTime, true);
        spriteBatch.draw(currentFrame, tomPosition.x * ppux , tomPosition.y * ppuy );

        //spriteBatch.draw(tomSpriteAnimTexture, 0f, 0f);
    }


    public void render() {
        camera.update();
        Vector2 tomPosition = tom.getMoveMechanics().getRelPosition();
        //camera.position.lerp(lerpTarget.set(tomPosition.x, tomPosition.y, 0), 2f * Gdx.graphics.getDeltaTime());
        //spriteBatch.setProjectionMatrix(camera.combined);
        //spriteBatch.setProjectionMatrix(camera.combined);
        spriteBatch.begin();
        drawBackGround();
        //backGroundSprite.draw(spriteBatch);
        drawBlocks();
        drawTom();
        spriteBatch.end();
        drawCollisionBlocks();
    }

    @Override
    public void dispose() {
        tomSpriteAnimTexture.dispose();
        blockTexture.dispose();
        spriteBatch.dispose();
        backGroundTexture.dispose();
    }
}
