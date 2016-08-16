package com.omonicon;

/**
 * Created by user on 07/02/16.
 */

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class DemoBob {

    static final int IDLE = 0;
    static final int RUN = 1;
    static final int JUMP = 2;
    static final int SPAWN = 3;
    public static final int DYING = 4;
    static final int DEAD = 5;
    static final int LEFT = -1;
    static final int RIGHT = 1;
    static final float ACCELERATION = 20f;
    static final float JUMP_VELOCITY = 10;
    static final float GRAVITY = 20.0f;
    static final float MAX_VEL = 6f;
    static final float DAMP = 0.90f;

    Vector2 pos = new Vector2();
    Vector2 accel = new Vector2();
    Vector2 vel = new Vector2();
    public Rectangle bounds = new Rectangle();

    private int state = SPAWN;
    private float stateTime = 0;
    int dir = LEFT;
    DemoMap map;
    boolean grounded = false;

    public DemoBob (DemoMap map, float x, float y) {
        this.map = map;
        pos.x = x;
        pos.y = y;
        bounds.width = 0.6f;
        bounds.height = 0.8f;
        bounds.x = pos.x + 0.2f;
        bounds.y = pos.y;
        state = SPAWN;
        stateTime = 0;
    }

    public void update (float deltaTime) {

        processKeys();

        accel.y = -GRAVITY;
        accel.scl(deltaTime);
        vel.add(accel.x, accel.y);

        if (accel.x == 0) {
            vel.x *= DAMP;
        }

        if (vel.x > MAX_VEL) {
            vel.x = MAX_VEL;
        }

        if (vel.x < -MAX_VEL) {
            vel.x = -MAX_VEL;
        }

        vel.scl(deltaTime);

        tryMove();

        vel.scl(1.0f / deltaTime);

        if (state == SPAWN) {
            if (stateTime > 0.4f) {
                state = IDLE;
            }
        }

        if (state == DYING) {
            if (stateTime > 0.4f) {
                state = DEAD;
            }
        }

        stateTime += deltaTime;
    }

    private void processKeys () {

        if (map.getCube().state == DemoCube.CONTROLLED || state == SPAWN || state == DYING) return;

        float x0 = (Gdx.input.getX(0) / (float)Gdx.graphics.getWidth()) * 480;
        float x1 = (Gdx.input.getX(1) / (float)Gdx.graphics.getWidth()) * 480;
        float y0 = 320 - (Gdx.input.getY(0) / (float)Gdx.graphics.getHeight()) * 320;

        boolean leftButton = (Gdx.input.isTouched(0) && x0 < 70) || (Gdx.input.isTouched(1) && x1 < 70);
        boolean rightButton = (Gdx.input.isTouched(0) && x0 > 70 && x0 < 134) || (Gdx.input.isTouched(1) && x1 > 70 && x1 < 134);
        boolean jumpButton = (Gdx.input.isTouched(0) && x0 > 416 && x0 < 480 && y0 < 64)
                || (Gdx.input.isTouched(1) && x1 > 416 && x1 < 480 && y0 < 64);

        if ((Gdx.input.isKeyPressed(Keys.W) || jumpButton) && state != JUMP) {
            state = JUMP;
            vel.y = JUMP_VELOCITY;
            grounded = false;
        }

        if (Gdx.input.isKeyPressed(Keys.A) || leftButton) {
            if (state != JUMP) state = RUN;
            dir = LEFT;
            accel.x = ACCELERATION * dir;
        } else if (Gdx.input.isKeyPressed(Keys.D) || rightButton) {
            if (state != JUMP) state = RUN;
            dir = RIGHT;
            accel.x = ACCELERATION * dir;
        } else {
            if (state != JUMP) state = IDLE;
            accel.x = 0;
        }
    }

    Rectangle[] collisionRectangles = {
            new Rectangle(), new Rectangle(), new Rectangle(), new Rectangle(), new Rectangle()
    };

    private void tryMove () {

        bounds.x += vel.x;

        fetchCollidableRects();

        for (int i = 0; i < collisionRectangles.length; i++) {
            Rectangle rect = collisionRectangles[i];
            if (bounds.overlaps(rect)) {
                if (vel.x < 0) {
                    bounds.x = rect.x + rect.width + 0.01f;
                } else {
                    bounds.x = rect.x - bounds.width - 0.01f;
                }
                vel.x = 0;
            }
        }

        bounds.y += vel.y;

        fetchCollidableRects();

        for (int i = 0; i < collisionRectangles.length; i++) {
            Rectangle rect = collisionRectangles[i];
            if (bounds.overlaps(rect)) {
                if (vel.y < 0) {
                    bounds.y = rect.y + rect.height + 0.01f;
                    grounded = true;
                    if (state != DYING && state != SPAWN) state = Math.abs(accel.x) > 0.1f ? RUN : IDLE;
                } else {
                    bounds.y = rect.y - bounds.height - 0.01f;
                }
                vel.y = 0;
            }
        }

        pos.x = bounds.x - 0.2f;
        pos.y = bounds.y;
    }

    private void fetchCollidableRects () {

        int p1x = (int)bounds.x;
        int p1y = (int)Math.floor(bounds.y);
        int p2x = (int)(bounds.x + bounds.width);
        int p2y = (int)Math.floor(bounds.y);
        int p3x = (int)(bounds.x + bounds.width);
        int p3y = (int)(bounds.y + bounds.height);
        int p4x = (int)bounds.x;
        int p4y = (int)(bounds.y + bounds.height);

        int[][] tiles = map.getTiles();

        int tile1 = tiles[p1x][map.getTiles()[0].length - 1 - p1y];
        int tile2 = tiles[p2x][map.getTiles()[0].length - 1 - p2y];
        int tile3 = tiles[p3x][map.getTiles()[0].length - 1 - p3y];
        int tile4 = tiles[p4x][map.getTiles()[0].length - 1 - p4y];

        if (state != DYING && (map.isDeadly(tile1) || map.isDeadly(tile2) || map.isDeadly(tile3) || map.isDeadly(tile4))) {
            state = DYING;
            stateTime = 0;
        }

        if (tile1 == DemoMap.TILE) {
            collisionRectangles[0].set(p1x, p1y, 1, 1);
        } else {
            collisionRectangles[0].set(-1, -1, 0, 0);
        }

        if (tile2 == DemoMap.TILE) {
            collisionRectangles[1].set(p2x, p2y, 1, 1);
        } else {
            collisionRectangles[1].set(-1, -1, 0, 0);
        }

        if (tile3 == DemoMap.TILE) {
            collisionRectangles[2].set(p3x, p3y, 1, 1);
        } else {
            collisionRectangles[2].set(-1, -1, 0, 0);
        }

        if (tile4 == DemoMap.TILE) {
            collisionRectangles[3].set(p4x, p4y, 1, 1);
        } else {
            collisionRectangles[3].set(-1, -1, 0, 0);
        }

        if (map.getCube().state == DemoCube.FIXED) {
            collisionRectangles[4].x = map.getCube().getBounds().x;
            collisionRectangles[4].y = map.getCube().getBounds().y;
            collisionRectangles[4].width = map.getCube().getBounds().width;
            collisionRectangles[4].height = map.getCube().getBounds().height;
        } else {
            collisionRectangles[4].set(-1, -1, 0, 0);
        }

    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public float getStateTime() {
        return stateTime;
    }

    public void setStateTime(float stateTime) {
        this.stateTime = stateTime;
    }
}