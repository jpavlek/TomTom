package com.omonicon.game.world;


import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.omonicon.physics.RigidBody;

/**
 * Created by user on 22/03/15.
 */
public class GameCharacter {

    public enum State {
        IDLE, WALK, JUMP, DEAD
    }

    public enum Orientation {
        FACING_FRONT, FACING_LEFT, FACING_BACK, FACING_RIGHT
    }

    private RigidBody mMoveMechanics;
    private State mState;
    private Orientation mOrientation;

    static final float SPEED = 3f;
    static final float JUMP_VELOCITY = 1f;
    static final float SIZE = 0.5f;

    GameCharacter(Vector2 position) {
        setMoveMechanics(new RigidBody(position, SIZE));

        setState(State.IDLE);
        setOrientation(Orientation.FACING_LEFT);
    }

    public RigidBody getMoveMechanics() {
        return mMoveMechanics;
    }

    public void setMoveMechanics(RigidBody moveMechanics) {
        this.mMoveMechanics = moveMechanics;
    }

    public State getState() {
        return mState;
    }

    public void setState(State state) {
        this.mState = state;
    }

    public Orientation getOrientation() {
        return mOrientation;
    }

    public void setOrientation(Orientation orientation) {
        this.mOrientation = orientation;
    }

    public Vector2 getVelocity() {
        return mMoveMechanics.getVelocity();
    }

    public void setVelocity(Vector2 newVelocity) {
        mMoveMechanics.setVelocity(newVelocity);
    }

    public void setVelocityX(float newVelocityX) {
        mMoveMechanics.setVelocityX(newVelocityX);
    }

    public void setVelocityY(float newVelocityY) {
        mMoveMechanics.setVelocityY(newVelocityY);
    }

    public Vector2 getAcceleration() {
        return mMoveMechanics.getAcceleration();
    }

    public void setAcceleration(Vector2 newAcceleration) {
        mMoveMechanics.setAcceleration(newAcceleration);
    }

    public void setAccelerationX(float newAccelerationX) {
        mMoveMechanics.setAccelerationX(newAccelerationX);
    }

    public void setAccelerationY(float newAccelerationY) {
        mMoveMechanics.setAccelerationY(newAccelerationY);
    }

    public Vector2 getPosition() {
        return mMoveMechanics.getRelPosition();
    }

    public void setPosition(Vector2 newPosition) {
        mMoveMechanics.setRelPosition(newPosition);
    }

    public void setPositionX(float newPositionX) {
        mMoveMechanics.setRelPositionX(newPositionX);
    }

    public void setPositionY(float newPositionY) {
        mMoveMechanics.setRelPositionY(newPositionY);
    }

    public void update(float delta) {
        Vector2 acceleration = getAcceleration().cpy();
        Vector2 velocity = getVelocity().cpy();
        Vector2 position = getPosition().cpy();

        Vector2 deltaPosition = velocity.mulAdd(acceleration, delta/2f).scl(delta);
        Vector2 deltaVelocity = acceleration.scl(delta);
        float maxDelta = 4.0f;
        if (deltaPosition.x > maxDelta) {
            deltaPosition.x = maxDelta;
        }
        if (deltaPosition.x < -maxDelta) {
            deltaPosition.x = -maxDelta;
        }
        if (deltaPosition.y > maxDelta) {
            deltaPosition.y = maxDelta;
        }
        if (deltaPosition.y < -maxDelta) {
            deltaPosition.y = -maxDelta;
        }
        //position += delta * (velocity + delta * acceleration / 2);
        //velocity += delta * acceleration;


        //Vector2 deltaPosition = getVelocity().scl(delta);
        //Vector2 newPosition = getPosition().add(deltaPosition);
        Vector2 newVelocity = velocity.add(deltaVelocity);
        setVelocity(newVelocity);
        Vector2 newPosition = position.add(deltaPosition);
        setPosition(newPosition);
    }

    public Rectangle getBounds() {
        Rectangle bounds = mMoveMechanics.getBounds();
        return bounds;
    }

    public void setBounds(Rectangle bounds) {
        mMoveMechanics.setBounds(bounds);
    }

}
