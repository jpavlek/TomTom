package com.omonicon;

/**
 * Created by user on 07/02/16.
 */

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.utils.Array;
import com.omonicon.tomtom.actors.traps.DemoLaser;

public class DemoMap {

    public static final int EMPTY = 0;
    public static final int TILE = 0xffffff;
    static int START = 0xff0000;
    static int END = 0xff00ff;
    static int DISPENSER = 0xff0100;
    static int SPIKES = 0x00ff00;
    static int ROCKET = 0x0000ff;
    static int MOVING_SPIKES = 0xffff00;
    static int LASER = 0x00ffff;

    private int[][] tiles;

    public DemoBob bob;

    Array<DemoDispenser> dispensers;
    DemoDispenser activeDispenser = null;
    Array<DemoRocket> rockets = new Array<DemoRocket>();
    Array<DemoMovingSpikes> movingSpikes = new Array<DemoMovingSpikes>();
    Array<DemoLaser> lasers = new Array<DemoLaser>();
    public DemoEndDoor endDoor;
    private DemoCube cube;

    public DemoMap () {
        loadBinary();
    }

    private void loadBinary () {

        dispensers = new Array<DemoDispenser>();

        Pixmap pixmap = new Pixmap(Gdx.files.internal("data/levels.png"));

        tiles = new int[pixmap.getWidth()][pixmap.getHeight()];

        for (int y = 0; y < 35; y++) {

            for (int x = 0; x < 150; x++) {

                int pix = (pixmap.getPixel(x, y) >>> 8) & 0xffffff;

                if (match(pix, START)) {
                    DemoDispenser dispenser = new DemoDispenser(x, pixmap.getHeight() - 1 - y);
                    dispensers.add(dispenser);
                    activeDispenser = dispenser;
                    bob = new DemoBob(this, activeDispenser.bounds.x, activeDispenser.bounds.y);
                    bob.setState(DemoBob.SPAWN);
                    cube = new DemoCube(this, activeDispenser.bounds.x, activeDispenser.bounds.y);
                    cube.state = DemoCube.DEAD;
                } else if (match(pix, DISPENSER)) {
                    DemoDispenser dispenser = new DemoDispenser(x, pixmap.getHeight() - 1 - y);
                    dispensers.add(dispenser);
                } else if (match(pix, ROCKET)) {
                    DemoRocket rocket = new DemoRocket(this, x, pixmap.getHeight() - 1 - y);
                    rockets.add(rocket);
                } else if (match(pix, MOVING_SPIKES)) {
                    movingSpikes.add(new DemoMovingSpikes(this, x, pixmap.getHeight() - 1 - y));
                } else if (match(pix, LASER)) {
                    lasers.add(new DemoLaser(this, x, pixmap.getHeight() - 1 - y));
                } else if (match(pix, END)) {
                    endDoor = new DemoEndDoor(x, pixmap.getHeight() - 1 - y);
                } else {
                    tiles[x][y] = pix;
                }
            }
        }

        for (int i = 0; i < movingSpikes.size; i++) {
            movingSpikes.get(i).init();
        }
        for (int i = 0; i < lasers.size; i++) {
            lasers.get(i).init();
        }
    }

    boolean match (int src, int dst) {
        return src == dst;
    }

    public void update (float deltaTime) {

        bob.update(deltaTime);

        if (bob.getState() == DemoBob.DEAD) {
            bob = new DemoBob(this, activeDispenser.bounds.x, activeDispenser.bounds.y);
        }

        cube.update(deltaTime);

        if (cube.state == DemoCube.DEAD) {
            cube = new DemoCube(this, bob.bounds.x, bob.bounds.y);
        }

        for (int i = 0; i < dispensers.size; i++) {
            if (bob.bounds.overlaps(dispensers.get(i).bounds)) {
                activeDispenser = dispensers.get(i);
            }
        }

        if (cube.state == DemoCube.DEAD){
            cube = new DemoCube(this, bob.bounds.x, bob.bounds.y);
        }

        for (int i = 0; i < rockets.size; i++) {
            DemoRocket rocket = rockets.get(i);
            rocket.update(deltaTime);
        }
        for (int i = 0; i < movingSpikes.size; i++) {
            DemoMovingSpikes spikes = movingSpikes.get(i);
            spikes.update(deltaTime);
        }
        for (int i = 0; i < lasers.size; i++) {
            lasers.get(i).update();
        }
    }

    public boolean isDeadly (int tileId) {
        return tileId == SPIKES;
    }

    public int[][] getTiles() {
        return tiles;
    }

    public void setTiles(int[][] tiles) {
        this.tiles = tiles;
    }

    public DemoCube getCube() {
        return cube;
    }

    public void setCube(DemoCube cube) {
        this.cube = cube;
    }
}