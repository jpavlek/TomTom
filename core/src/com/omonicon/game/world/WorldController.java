package com.omonicon.game.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.omonicon.physics.Block;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by user on 22/03/15.
 */
public class WorldController {

    enum Keys {
        LEFT, RIGHT, JUMP, FIRE
    }

    private static final long LONG_JUMP_PRESS   = 150L;
    private static final float GRAVITY          = -19.81f;
    private static final float MAX_JUMP_SPEED   = 20.0f;
    private static final float DAMP             = 0.9f;
    private static final float MAX_VEL          = 20.0f;

    private World   world;
    private Tom     tom;
    private long    jumpPressTime;
    private boolean isJumpPressed;
    private boolean grounded = false;

    // This is the rectangle pool used in collision detection
    // Good to avoid instantiation each frame
    private Pool<Rectangle> rectPool = new Pool<Rectangle>() {
        @Override
        protected Rectangle newObject() {
            return new Rectangle();
        }
    };

    static Map<Keys, Boolean> keys = new HashMap<Keys, Boolean>();
    static {
        keys.put(Keys.LEFT, false);
        keys.put(Keys.RIGHT, false);
        keys.put(Keys.JUMP, false);
        keys.put(Keys.FIRE, false);
    }

    // Blocks that Tom can collide with any given frame
    private Array<Block> collidable = new Array<Block>();

    public WorldController(World world) {
        this.world = world;
        this.tom = world.getTom();
        this.isJumpPressed = false;
        this.jumpPressTime = 0L;
    }

    public void leftPressed() {
        keys.put(Keys.LEFT, true);
    }

    public void leftReleased() {
        keys.put(Keys.LEFT, false);
    }

    public void rightPressed() {
        keys.put(Keys.RIGHT, true);
    }

    public void rightReleased() {
        keys.put(Keys.RIGHT, false);
    }

    public void jumpPressed() {
        keys.put(Keys.JUMP, true);
    }

    public void jumpReleased() {
        keys.put(Keys.JUMP, false);
        isJumpPressed = false;
    }

    private void checkCollisionWithBlocks(float delta) {
        Gdx.app.log("WorldController", "checkCollisionWithBlocks");
        // scale velocity to frame units
        tom.getVelocity().scl(delta);

        // Obtain the rectangle from the pool instead of instantiating it
        Rectangle tomRect = rectPool.obtain();
        // set the rectangle to bob's bounding box
        tomRect.set(tom.getBounds().x, tom.getBounds().y, tom.getBounds().width, tom.getBounds().height);

        // we first check the movement on the horizontal X axis
        int startX, endX;
        int startY = (int) tom.getBounds().y;
        int endY = (int) (tom.getBounds().y + tom.getBounds().height);
        // if Bob is heading left then we check if he collides with the block on his left
        // we check the block on his right otherwise
        if (tom.getVelocity().x < 0) {
            startX = endX = (int) Math.floor(tom.getBounds().x + tom.getVelocity().x);
        } else {
            startX = endX = (int) Math.floor(tom.getBounds().x + tom.getBounds().width + tom.getVelocity().x);
        }

        // get the block(s) tom can collide with
        populateCollidableBlocks(startX, startY, endX, endY);

        // simulate tom's movement on the X
        tomRect.x += tom.getVelocity().x;

        // clear collision boxes in world
        world.getCollisionRects().clear();

        // if tom collides, make his horizontal velocity 0
        for (Block block : collidable) {
            if (block == null) continue;
            if (tomRect.overlaps(block.getBounds())) {
                if (tom.getVelocity().x > 0) {
                    tomRect.x = block.getBounds().x - tomRect.getWidth() - 0.01f;
                } else if (tom.getVelocity().x < 0) {
                    tomRect.x = block.getBounds().x + block.getBounds().getWidth() + 0.01f;
                }
                tom.setVelocityX(0);
                world.getCollisionRects().add(block.getBounds());

                break;
            }
        }

        // reset the x position of the collision box
        tomRect.x = tom.getPosition().x;

        // the same thing but on the vertical Y axis
        startX = (int) tom.getBounds().x;
        endX = (int) (tom.getBounds().x + tom.getBounds().width);
        if (tom.getVelocity().y < 0.0f) {
            startY = endY = (int) Math.floor(tom.getBounds().y + tom.getVelocity().y);
            Gdx.app.log("Tom", "- Tom check collision log y: "+ startY);
        } else {
            startY = endY = (int) Math.floor(tom.getBounds().y + tom.getBounds().height + tom.getVelocity().y);
            Gdx.app.log("Tom", "+ Tom check collision log y: "+ startY);
        }

        populateCollidableBlocks(startX, startY, endX, endY);

        tomRect.y += tom.getVelocity().y;

        for (Block block : collidable) {
            if (block == null) continue;
            if (tomRect.overlaps(block.getBounds())) {
                Gdx.app.log("Tom", "Collision detected");
                if (tom.getVelocity().y < 0.0f) {
                    grounded = true;
                }
                tom.setVelocityY(0.0f);
                tom.setAccelerationY(0.0f);
                world.getCollisionRects().add(block.getBounds());
                break;
            }
        }
        // reset the collision box's position on Y
        tomRect.y = tom.getPosition().y;

        // update tom's position
        tom.getPosition().add(tom.getVelocity());
        tom.getBounds().x = tom.getPosition().x;
        tom.getBounds().y = tom.getPosition().y;

        // un-scale velocity (not in frame time)
        tom.getVelocity().scl(1f / delta);
    }

    public void processInput() {
        Gdx.app.log("WorldController", "processInput");
        boolean leftPressed = keys.get(Keys.LEFT);
        boolean rightPressed = keys.get(Keys.RIGHT);
        boolean jumpPressed = keys.get(Keys.JUMP);

        if (jumpPressed) {
            GameCharacter.State tomState = tom.getState();
            long jumpPressedInterval = System.currentTimeMillis() - jumpPressTime;
            if (tomState != GameCharacter.State.JUMP && !isJumpPressed ) {
                tom.setState(GameCharacter.State.JUMP);
                isJumpPressed = true;
                jumpPressTime = System.currentTimeMillis();
                tom.setVelocityY(MAX_JUMP_SPEED);
                tom.setAccelerationY(GRAVITY);
            } else if (jumpPressed && (jumpPressedInterval >= LONG_JUMP_PRESS)) {
                //tom.getVelocity().y = 0f;//2*MAX_JUMP_SPEED+LONG_JUMP_PRESS/1000f*GRAVITY;
                jumpPressed = false;
            } else if (jumpPressed) {
                //tom.getVelocity().y = MAX_JUMP_SPEED*(1+jumpPressedInterval/LONG_JUMP_PRESS)+GRAVITY*jumpPressedInterval/1000f;
                tom.setVelocityY(MAX_JUMP_SPEED*(1+jumpPressedInterval/LONG_JUMP_PRESS)+GRAVITY*jumpPressedInterval/1000f);
            }

        }

        if (leftPressed) {
            tom.setOrientation(GameCharacter.Orientation.FACING_LEFT);
            tom.setState(GameCharacter.State.WALK);
            tom.setVelocityX(-GameCharacter.SPEED);
        }

        if (rightPressed) {
            tom.setOrientation(GameCharacter.Orientation.FACING_RIGHT);
            tom.setState(GameCharacter.State.WALK);
            tom.setVelocityX(GameCharacter.SPEED);
        }

        if (leftPressed && rightPressed) {
            tom.setOrientation(GameCharacter.Orientation.FACING_FRONT);
            tom.setState(GameCharacter.State.IDLE);
            tom.setVelocityX(0f);
        }

        if (!leftPressed && !rightPressed && !jumpPressed) {
            tom.setOrientation(GameCharacter.Orientation.FACING_BACK);
            tom.setState(GameCharacter.State.IDLE);
            tom.setVelocityX(0f);
        }
    }

    public void update(float delta) {
        Gdx.app.log("WorldController", "update");
        float maxDelta = 1.0f/30.0f;
        if (delta>maxDelta) {
            delta = Math.min(delta, maxDelta);
        }

        processInput();

        // If Bob is grounded then reset the state to IDLE
        if (grounded && tom.getState().equals(GameCharacter.State.JUMP)) {
            tom.setState(GameCharacter.State.IDLE);
        }

        //apply gravity
        //TODO: acceleration handling
        tom.getAcceleration().y = GRAVITY;
        tom.getAcceleration().scl(delta);
        tom.getVelocity().add(tom.getAcceleration().x, tom.getAcceleration().y);

        // checking collisions with the surrounding blocks depending on Bob's velocity
        checkCollisionWithBlocks(delta);

        Vector2 velocity = tom.getVelocity();

        if (tom.getAcceleration().x == 0.0f) {
            tom.getVelocity().x *= DAMP;
        }

        if (tom.getVelocity().x > MAX_VEL) {
            tom.getVelocity().x = MAX_VEL;
        }

        if (tom.getVelocity().x < -MAX_VEL) {
            tom.getVelocity().x = -MAX_VEL;
        }

        if (tom.getVelocity().y > MAX_VEL) {
            tom.getVelocity().y = MAX_VEL;
        }

        if (tom.getVelocity().y < -MAX_VEL) {
            tom.getVelocity().y = -MAX_VEL;
        }

        tom.update(delta);
        tom.getBounds().x = tom.getPosition().x;
        tom.getBounds().y = tom.getPosition().y;

        //don't let character fall through the floor
        if (tom.getPosition().y < 0.0f) {
            tom.setPositionY(0.0f);
            if (tom.getState() == GameCharacter.State.JUMP) {
                tom.setState(GameCharacter.State.IDLE);
            }
        }

        //don't let character jump through the ceiling
        if (tom.getPosition().y > 6.3f) {
            tom.setPositionY(6.3f);
        }

        //don't let character go horizontally to the left outside the World
        if (tom.getPosition().x < 0f) {
            tom.setPositionX(0f);
            if (tom.getState() != GameCharacter.State.JUMP) {
                tom.setState(GameCharacter.State.IDLE);
            }
        }

        //don't let character go horizontally to the right outside the World
        if (tom.getPosition().x > world.getLevel().getLevelSize().x - tom.getBounds().width ) {
            tom.getPosition().x = world.getLevel().getLevelSize().x - tom.getBounds().width;
            tom.setPosition(tom.getPosition());
            if (!tom.getState().equals(GameCharacter.State.JUMP)) {
                tom.setState(GameCharacter.State.IDLE);
            }
        }

    }

    /** populate the collidable array with the blocks found in the enclosing coordinates **/
    private void populateCollidableBlocks(int startX, int startY, int endX, int endY) {
        collidable.clear();
        for (int x = startX; x <= endX; x++) {
            for (int y = startY; y <= endY; y++) {
                if (x >= 0 && x < world.getLevel().getWidth() && y >=0 && y < world.getLevel().getHeight()) {
                    collidable.add(world.getLevel().getBlock(x, y));
                }
            }
        }
    }
}
