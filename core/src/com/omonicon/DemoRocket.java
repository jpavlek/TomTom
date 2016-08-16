package com.omonicon;

/**
 * Created by user on 07/02/16.
 */

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class DemoRocket {

    static final int FLYING = 0;
    static final int EXPLODING = 1;
    static final int DEAD = 2;
    static final float VELOCITY = 6;

    DemoMap map;
    float stateTime = 0;
    int state = FLYING;
    Vector2 startPos = new Vector2();
    Vector2 pos = new Vector2();
    Vector2 vel = new Vector2();
    Rectangle bounds = new Rectangle();
    Rectangle[] collisionRectangles = {new Rectangle(), new Rectangle(), new Rectangle(), new Rectangle()};


    public DemoRocket (DemoMap map, float x, float y) {
        this.map = map;
        this.startPos.set(x, y);
        this.pos.set(x, y);
        this.bounds.x = x + 0.2f;
        this.bounds.y = y + 0.2f;
        this.bounds.width = 0.6f;
        this.bounds.height = 0.6f;
        this.vel.set(-VELOCITY, 0);
    }

    public void update (float deltaTime) {
        if (state == FLYING) {

            vel.set(map.bob.pos);
            vel.sub(pos).nor().scl(VELOCITY);
            pos.add(vel.x * deltaTime, vel.y * deltaTime);
            bounds.x = pos.x + 0.2f;
            bounds.y = pos.y + 0.2f;
            if (checkHit()) {
                state = EXPLODING;
                stateTime = 0;
            }
        }

        if (state == EXPLODING) {
            if (stateTime > 0.6f) {
                state = FLYING;
                stateTime = 0;
                pos.set(startPos);
                bounds.x = pos.x + 0.2f;
                bounds.y = pos.y + 0.2f;
            }
        }

        stateTime += deltaTime;
    }


    private boolean checkHit () {

        fetchCollidableRects();

        for (int i = 0; i < collisionRectangles.length; i++) {
            if (bounds.overlaps(collisionRectangles[i])) {
                return true;
            }
        }

        if (bounds.overlaps(map.bob.bounds)) {
            if (map.bob.getState() != DemoBob.DYING) {
                map.bob.setState(DemoBob.DYING);
                map.bob.setStateTime(0);
            }
            return true;
        }

        if (bounds.overlaps(map.getCube().getBounds())) {
            return true;
        }

        return false;
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
        int tile1 = tiles[p1x][tiles[0].length - 1 - p1y];
        int tile2 = tiles[p2x][tiles[0].length - 1 - p2y];
        int tile3 = tiles[p3x][tiles[0].length - 1 - p3y];
        int tile4 = tiles[p4x][tiles[0].length - 1 - p4y];

        if (tile1 != DemoMap.EMPTY) {
            collisionRectangles[0].set(p1x, p1y, 1, 1);
        } else {
            collisionRectangles[0].set(-1, -1, 0, 0);
        }

        if (tile2 != DemoMap.EMPTY) {
            collisionRectangles[1].set(p2x, p2y, 1, 1);
        } else {
            collisionRectangles[1].set(-1, -1, 0, 0);
        }

        if (tile3 != DemoMap.EMPTY) {
            collisionRectangles[2].set(p3x, p3y, 1, 1);
        } else {
            collisionRectangles[2].set(-1, -1, 0, 0);
        }

        if (tile4 != DemoMap.EMPTY) {
            collisionRectangles[3].set(p4x, p4y, 1, 1);
        } else {
            collisionRectangles[3].set(-1, -1, 0, 0);
        }
    }
}