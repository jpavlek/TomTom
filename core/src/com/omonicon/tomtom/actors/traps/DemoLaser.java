package com.omonicon.tomtom.actors.traps;

/**
 * Created by user on 07/02/16.
 */
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.omonicon.DemoBob;
import com.omonicon.DemoMap;

public class DemoLaser {

    static final int FORWARD = 1;
    static final int BACKWARD = -1;
    static final float FORWARD_VEL = 10;
    static final float BACKWARD_VEL = 4;

    int state = FORWARD;

    DemoMap map;
    private Vector2 pos = new Vector2();
    Vector2 endPoint = new Vector2();
    private Vector2 cappedEndPoint = new Vector2();
    private float angle = 0;

    public DemoLaser (DemoMap map, float x, float y) {
        this.map = map;
        pos.x = x;
        pos.y = y;
    }

    public void init () {
        int[][] tiles = map.getTiles();
        int ix = (int)pos.x;
        int iy = tiles[0].length - 1 - (int)pos.y;

        int left = tiles[ix - 1][iy];
        int right = tiles[ix + 1][iy];
        int top = tiles[ix][iy - 1];
        int bottom = tiles[ix][iy + 1];

        if (left == DemoMap.TILE) {
            angle = -90;
            for (int x = ix; x < tiles.length; x++) {
                if (map.getTiles()[x][iy] == DemoMap.TILE) {
                    endPoint.set(x, pos.y);
                    break;
                }
            }
        }
        if (right == DemoMap.TILE) {
            angle = 90;
            for (int x = ix; x >= 0; x--) {
                if (map.getTiles()[x][iy] == DemoMap.TILE) {
                    endPoint.set(x, pos.y);
                    break;
                }
            }
        }
        if (top == DemoMap.TILE) {
            angle = 180;
            for (int y = iy; y < tiles[0].length; y++) {
                if (tiles[ix][y] == DemoMap.TILE) {
                    endPoint.set(pos.x, tiles[0].length - y - 1);
                    break;
                }
            }
        }
        if (bottom == DemoMap.TILE) {
            angle = 0;
            for (int y = iy; y >= 0; y--) {
                if (tiles[ix][y] == DemoMap.TILE) {
                    endPoint.set(pos.x, tiles[0].length - y - 1);
                    break;
                }
            }
        }
    }

    private Vector2 startPoint = new Vector2();

    public void update () {
        startPoint.set(pos).add(0.5f, 0.5f);
        cappedEndPoint.set(endPoint).add(0.5f, 0.5f);

        Rectangle cbounds = map.getCube().getBounds();
        Rectangle bbounds = map.bob.bounds;

        boolean kill = false;

        if (angle == -90) {
            if (startPoint.x < cbounds.x && endPoint.x > cbounds.x) {
                if (cbounds.y < startPoint.y && cbounds.y + cbounds.height > startPoint.y) {
                    cappedEndPoint.x = cbounds.x;
                }
            }
        }
        if (angle == 90) {
            if (startPoint.x > cbounds.x && endPoint.x < cbounds.x) {
                if (cbounds.y < startPoint.y && cbounds.y + cbounds.height > startPoint.y) {
                    cappedEndPoint.x = cbounds.x + cbounds.width;
                }
            }
        }

        if (angle == 0) {
            if (startPoint.y < cbounds.y && endPoint.y > cbounds.y) {
                if (cbounds.x < startPoint.x && cbounds.x + cbounds.width > startPoint.x) {
                    cappedEndPoint.y = cbounds.y;
                }
            }
        }

        if (angle == 180) {
            if (startPoint.y > cbounds.y && endPoint.y < cbounds.y) {
                if (cbounds.x < startPoint.x && cbounds.x + cbounds.width > startPoint.x) {
                    cappedEndPoint.y = cbounds.y + cbounds.height;
                }
            }
        }

        if (angle == -90) {
            if (startPoint.x < bbounds.x) {
                if (bbounds.y < startPoint.y && bbounds.y + bbounds.height > startPoint.y) {
                    if (cappedEndPoint.x > bbounds.x) kill = true;
                }
            }
        }
        if (angle == 90) {
            if (startPoint.x > bbounds.x) {
                if (bbounds.y < startPoint.y && bbounds.y + bbounds.height > startPoint.y) {
                    if (cappedEndPoint.x < bbounds.x + bbounds.width) kill = true;
                }
            }
        }

        if (angle == 0) {
            if (pos.y < bbounds.y) {
                if (bbounds.x < startPoint.x && bbounds.x + bbounds.width > startPoint.x) {
                    if (cappedEndPoint.y > bbounds.y) kill = true;
                }
            }
        }

        if (angle == 180) {
            if (pos.y > bbounds.y) {
                if (bbounds.x < startPoint.x && bbounds.x + bbounds.width > startPoint.x) {
                    if (cappedEndPoint.y < bbounds.y + bbounds.height) kill = true;
                }
            }
        }

        if (kill && map.bob.getState() != DemoBob.DYING) {
            map.bob.setState(DemoBob.DYING);
            map.bob.setStateTime(0);
        }
    }

    public Vector2 getPos() {
        return pos;
    }

    public void setPos(Vector2 pos) {
        this.pos = pos;
    }

    public float getAngle() {
        return angle;
    }

    public void setAngle(float angle) {
        this.angle = angle;
    }

    public Vector2 getStartPoint() {
        return startPoint;
    }

    public void setStartPoint(Vector2 startPoint) {
        this.startPoint = startPoint;
    }

    public Vector2 getCappedEndPoint() {
        return cappedEndPoint;
    }

    public void setCappedEndPoint(Vector2 cappedEndPoint) {
        this.cappedEndPoint = cappedEndPoint;
    }
}