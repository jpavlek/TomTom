package com.omonicon.physics;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by user on 22/03/15.
 */
public class Block {
    static final float SIZE = 1.0f;
    private RigidBody mMechanics;

    public Block(Vector2 position) {
        mMechanics = new RigidBody(position);
        mMechanics.setBounds(new Rectangle(position.x, position.y, Block.SIZE, Block.SIZE));
    }

    public RigidBody getMechanics() {
        return mMechanics;
    }

    public Rectangle getBounds() {
        return mMechanics.getBounds();
    }

    public void setMechanics(RigidBody mechanics) {
        mMechanics = mechanics;
    }
}
