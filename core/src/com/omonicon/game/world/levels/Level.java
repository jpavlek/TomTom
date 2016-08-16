package com.omonicon.game.world.levels;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.omonicon.physics.Block;

/**
 * Created by user on 03/05/15.
 */
public class Level {

    private Vector2 levelSize;

    //holds min and max x values
    private Vector2 levelBoundsX;

    //holds min and max y values
    private Vector2 levelBoundsY;

    private Array<Block> blocks = new Array<Block>();
    private Array<Block> blocksArray = new Array<Block>();

    public Level(int sizeX, int sizeY) {
        levelSize = new Vector2(sizeX, sizeY);
        blocksArray = new Array<Block>(sizeX * sizeY);
        for (int i = 0; i < (int) levelSize.x; i++) {
            for (int j = 0; j < (int) levelSize.y; j++) {
                blocksArray.add(null);
            }
        }
        levelBoundsX = new Vector2(0.0f, 0.0f);
        levelBoundsY = new Vector2(0.0f, 0.0f);
        createLevel();
    }

    public Vector2 getLevelSize() {
        return levelSize;
    }

    public int getWidth(){
        return (int) levelBoundsX.y;
    }

    public int getHeight(){
        return (int) levelBoundsY.y;
    }

    public Array<Block> getBlocks() {
        return blocks;
    }

    public Block getBlock(int i, int j) {
        return blocksArray.get(i*(int) levelSize.y+j);
    }

    public void addBlock(int posX, int posY) {

        if (posX < levelBoundsX.x) {
            levelBoundsX.x = posX;
        }

        if (posX > levelBoundsX.y) {
            levelBoundsX.y = posX;
        }

        if (posY < levelBoundsY.x) {
            levelBoundsY.x = posY;
        }

        if (posY > levelBoundsY.y) {
            levelBoundsY.y = posY;
        }

        //levelSize.x = levelBoundsX.y- levelBoundsX.x+1;
        //levelSize.y = levelBoundsY.y- levelBoundsY.x+1;
        Block newBlock = new Block( new Vector2(posX, posY) );
        blocks.add( newBlock );
        blocksArray.set(posX * (int) levelSize.y + posY, newBlock );
    }

    public void createLevel() {

        // columns 0 & 7
        for (int i = 0; i < 10; i++) {
            addBlock(i, 0);
            addBlock(i, 6);
            //column 1
            if (i > 2) {
                addBlock(i, 1);
            }
        }

        // row 2
        for (int i = 2; i< 6; i++) {
            addBlock(9, i);
        }

        // row 3
        for (int i = 3; i< 6; i++) {
            addBlock(6, i);
        }

    }

}
