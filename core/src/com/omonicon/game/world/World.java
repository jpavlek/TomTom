package com.omonicon.game.world;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.omonicon.game.world.levels.Level;
import com.omonicon.physics.Block;

import java.util.ArrayList;
import java.util.List;
//import com.badlogic.gdx.physics.box2d.*;

/**
 * Created by user on 22/03/15.
 */
public class World {

    private Tom tom;
    private Level level;

    /** The collision boxes **/
    private Array<Rectangle> collisionRects = new Array<Rectangle>();

    public Array<Rectangle> getCollisionRects() {
        return collisionRects;
    }

    /** Return only the blocks that need to be drawn **/
    public List<Block> getDrawableBlocks(int width, int height) {
        int x = (int)tom.getPosition().x - width;
        int y = (int)tom.getPosition().y - height;
        if (x < 0) {
            x = 0;
        }
        if (y < 0) {
            y = 0;
        }
        int x2 = x + 2 * width;
        int y2 = y + 2 * height;
        if (x2 >= level.getWidth()) {
            x2 = level.getWidth() - 1;
        }
        if (y2 >= level.getHeight()) {
            y2 = level.getHeight() - 1;
        }

        List<Block> drawableBlocks = new ArrayList<Block>();
        Block block;
        for (int col = x; col <= x2; col++) {
            for (int row = y; row <= y2; row++) {
                block = level.getBlock(col, row);
                if (block != null) {
                    drawableBlocks.add(block);
                }
            }
        }
        return drawableBlocks;
    }


    public World() {
        createDemoWorld();
    }

    public Tom getTom() {
        return tom;
    }

    private void createDemoWorld() {
        tom = new Tom(new Vector2(7, 2));
        level = new Level(10, 7);
    }

    public Level getLevel() {
        return level;
    }

}
