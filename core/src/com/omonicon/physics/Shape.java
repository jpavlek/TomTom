package com.omonicon.physics;

import com.badlogic.gdx.math.Shape2D;

/**
 * Created by user on 14/02/16.
 */
public class Shape {
    private Shape2D mShape;

    public String getShapeName() {
        if (mShape != null) {
           return mShape.getClass().getName();
        }
        return "null";
    }

}
