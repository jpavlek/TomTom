package com.omonicon.physics;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.math.Polygon;

/**
 * Created by user on 22/03/15.
 */
public class RigidBody {
   private Vector2 mRelPosition;
   private Vector2 mVelocity;
   private Vector2 mAcceleration;

   private Rectangle mBounds = new Rectangle();
   private Circle mCircle = new Circle();


   public RigidBody() {
       setRelPosition(new Vector2());
       setAcceleration(new Vector2());
       setVelocity(new Vector2());
       mBounds.set(0,0,0,0);
   }

   public RigidBody(Vector2 position) {
       mRelPosition = position;
       setAcceleration(new Vector2());
       setVelocity(new Vector2());
       mBounds.set(mRelPosition.x,mRelPosition.y,0,0);
   }

   public RigidBody(Vector2 position, float size) {
       mRelPosition = position;
       setAcceleration(new Vector2());
       setVelocity(new Vector2());
       mBounds.set(mRelPosition.x, mRelPosition.y, size, size);
   }

    public RigidBody(Vector2 position, float sizeX, float sizeY) {
        mRelPosition = position;
        setAcceleration(new Vector2());
        setVelocity(new Vector2());
        mBounds.set(mRelPosition.x, mRelPosition.y, sizeX, sizeY);
    }

    public Vector2 getRelPosition() {
        return mRelPosition;
    }

    public void setRelPosition(Vector2 position) {
        this.mRelPosition = position;
        this.mBounds.setPosition(position);
    }

    public void setRelPositionX(float positionX) {
        this.mRelPosition.x = positionX;
        this.mBounds.setX(positionX);
    }

    public void setRelPositionY(float positionY) {
        this.mRelPosition.y = positionY;
        this.mBounds.setY(positionY);
    }

    public Vector2 getAcceleration() {
        return mAcceleration;
    }

    public void setAcceleration(Vector2 acceleration) {
        this.mAcceleration = acceleration;
    }

    public void setAccelerationX(float accelerationX) {
        this.mAcceleration.x = accelerationX;
    }

    public void setAccelerationY(float accelerationY) {
        this.mAcceleration.y = accelerationY;
    }

    public Vector2 getVelocity() {
        return mVelocity;
    }

    public void setVelocity(Vector2 velocity) {
        this.mVelocity = velocity;
    }

    public void setVelocityX(float velocityX) {
        this.mVelocity.x = velocityX;
    }

    public void setVelocityY(float velocityY) {
        this.mVelocity.y = velocityY;
    }

    public Rectangle getBounds() {
        return mBounds;
    }

    public void setBounds(Rectangle bounds) {
        this.mBounds = bounds;
        //set mRelPosition to bounds position;
        bounds.getPosition(this.mRelPosition);
    }

    protected void setBoundsX(float boundX) {
        this.mBounds.setX(boundX);
    }

    protected void setBoundsY(float boundY) {
        this.mBounds.setY(boundY);
    }

}
