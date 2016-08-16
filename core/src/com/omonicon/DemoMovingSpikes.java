package com.omonicon;

/**
 * Created by user on 07/02/16.
 */

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class DemoMovingSpikes {

    static final int FORWARD = 1;
    static final int BACKWARD = -1;
    static final float FORWARD_VEL = 10;
    static final float BACKWARD_VEL = 4;

    int state = FORWARD;

    DemoMap map;
    Rectangle bounds = new Rectangle();
    Vector2 vel = new Vector2();
    Vector2 pos = new Vector2();
    float angle = 0;
    int fx = 0;
    int fy = 0;
    int bx = 0;
    int by = 0;

    public DemoMovingSpikes (DemoMap map, float x, float y) {

        this.map = map;
        pos.x = x;
        pos.y = y;
        bounds.x = x;
        bounds.y = y;
        bounds.width = bounds.height = 1;

    }

    public void init () {

        int ix = (int)pos.x;
        int iy = (int)pos.y;

        int left = map.getTiles()[ix - 1][map.getTiles()[0].length - 1 - iy];
        int right = map.getTiles()[ix + 1][map.getTiles()[0].length - 1 - iy];
        int top = map.getTiles()[ix][map.getTiles()[0].length - 1 - iy - 1];
        int bottom = map.getTiles()[ix][map.getTiles()[0].length - 1 - iy + 1];

        if (left == DemoMap.TILE) {
            vel.x = FORWARD_VEL;
            angle = -90;
            fx = 1;
        }
        if (right == DemoMap.TILE) {
            vel.x = -FORWARD_VEL;
            angle = 90;
            bx = 1;
        }
        if (top == DemoMap.TILE) {
            vel.y = -FORWARD_VEL;
            angle = 180;
            by = -1;
        }
        if (bottom == DemoMap.TILE) {
            vel.y = FORWARD_VEL;
            angle = 0;
            fy = -1;
        }
    }

    public void update (float deltaTime) {

        pos.add(vel.x * deltaTime, vel.y * deltaTime);
        boolean change = false;

        if (state == FORWARD) {
            change = map.getTiles()[(int)pos.x + fx][map.getTiles()[0].length - 1 - (int)pos.y + fy] == DemoMap.TILE;
        } else {
            change = map.getTiles()[(int)pos.x + bx][map.getTiles()[0].length - 1 - (int)pos.y + by] == DemoMap.TILE;
        }

        if (change) {
            pos.x -= vel.x * deltaTime;
            pos.y -= vel.y * deltaTime;
            state = -state;
            vel.scl(-1);
            if (state == FORWARD) vel.nor().scl(FORWARD_VEL);
            if (state == BACKWARD) vel.nor().scl(BACKWARD_VEL);
        }

        bounds.x = pos.x;
        bounds.y = pos.y;

        if (map.bob.bounds.overlaps(bounds)) {
            if (map.bob.getState() != DemoBob.DYING) {
                map.bob.setState(DemoBob.DYING);
                map.bob.setStateTime(0);
            }
        }

        if (map.getCube().getBounds().overlaps(bounds)) {
            map.getCube().state = DemoCube.DEAD;
            map.getCube().stateTime = 0;
        }
    }
}
