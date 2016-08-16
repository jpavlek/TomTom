package com.omonicon.game.world;

import com.badlogic.gdx.math.Vector2;
import com.omonicon.physics.RigidBody;

/**
 * Created by user on 22/03/15.
 */
public class Tom extends GameCharacter {

    public Tom(Vector2 position) {
        super(position);
        RigidBody mechanics = getMoveMechanics();
        setPosition(position);
    }
}
