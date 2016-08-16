package com.omonicon;

/**
 * Created by user on 07/02/16.
 */

import com.badlogic.gdx.math.Rectangle;

public class DemoEndDoor {
    public Rectangle bounds = new Rectangle();

    public DemoEndDoor (float x, float y) {
        this.bounds.x = x;
        this.bounds.y = y;
        this.bounds.width = this.bounds.height = 1;
    }
}