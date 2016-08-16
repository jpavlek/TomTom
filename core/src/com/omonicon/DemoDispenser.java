package com.omonicon;

/**
 * Created by user on 07/02/16.
 */

import com.badlogic.gdx.math.Rectangle;

public class DemoDispenser {
    Rectangle bounds = new Rectangle();
    boolean active = false;

    public DemoDispenser (float x, float y) {
        bounds.x = x;
        bounds.y = y;
        bounds.width = bounds.height = 1;
    }
}