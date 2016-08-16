package com.omonicon;

/**
 * Created by user on 07/02/16.
 */
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.SpriteCache;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ImmediateModeRenderer20;
import com.badlogic.gdx.math.Vector3;
import com.omonicon.tomtom.actors.traps.DemoLaser;

public class DemoMapRenderer {

    DemoMap map;
    OrthographicCamera cam;
    SpriteCache cache;
    SpriteBatch batch = new SpriteBatch(5460);

    ImmediateModeRenderer20 renderer = new ImmediateModeRenderer20(false, true, 0);

    int[][] blocks;
    TextureRegion tile;

    Animation bobLeft;
    Animation bobRight;
    Animation bobJumpLeft;
    Animation bobJumpRight;
    Animation bobIdleLeft;
    Animation bobIdleRight;
    Animation bobDead;
    Animation zap;

    TextureRegion cube;
    Animation cubeFixed;

    TextureRegion cubeControlled;
    TextureRegion dispenser;

    Animation spawn;
    Animation dying;

    TextureRegion spikes;

    Animation rocket;
    Animation rocketExplosion;

    TextureRegion rocketPad;
    TextureRegion endDoor;
    TextureRegion movingSpikes;
    TextureRegion laser;

    float stateTime = 0;
    Vector3 lerpTarget = new Vector3();

    public DemoMapRenderer (DemoMap map) {

        this.map = map;
        this.cam = new OrthographicCamera(24, 16);
        this.cam.position.set(map.bob.pos.x, map.bob.pos.y, 0);
        this.cache = new SpriteCache(this.map.getTiles().length * this.map.getTiles()[0].length, false);
        this.blocks = new int[(int)Math.ceil(this.map.getTiles().length / 24.0f)][(int)Math.ceil(this.map.getTiles()[0].length / 16.0f)];

        createAnimations();
        createBlocks();
    }

    private void createBlocks () {

        int width = map.getTiles().length;
        int height = map.getTiles()[0].length;

        for (int blockY = 0; blockY < blocks[0].length; blockY++) {
            for (int blockX = 0; blockX < blocks.length; blockX++) {
                cache.beginCache();
                for (int y = blockY * 16; y < blockY * 16 + 16; y++) {
                    for (int x = blockX * 24; x < blockX * 24 + 24; x++) {

                        if (x > width) continue;
                        if (y > height) continue;
                        int posX = x;
                        int posY = height - y - 1;
                        if (map.match(map.getTiles()[x][y], DemoMap.TILE)){
                            cache.add(tile, posX, posY, 1, 1);
                        }
                        if (map.match(map.getTiles()[x][y], DemoMap.SPIKES)){
                            cache.add(spikes, posX, posY, 1, 1);
                        }
                    }
                }
                blocks[blockX][blockY] = cache.endCache();
            }
        }

        Gdx.app.debug("Cubocy", "blocks created");
    }

    private void createAnimations () {

        this.tile = new TextureRegion(new Texture(Gdx.files.internal("data/tile.png")), 0, 0, 20, 20);

        Texture bobTexture = new Texture(Gdx.files.internal("data/bob.png"));
        TextureRegion[] split = new TextureRegion(bobTexture).split(20, 20)[0];
        TextureRegion[] mirror = new TextureRegion(bobTexture).split(20, 20)[0];

        for (TextureRegion region : mirror){
            region.flip(true, false);
        }

        spikes = split[5];
        bobRight = new Animation(0.1f, split[0], split[1]);
        bobLeft = new Animation(0.1f, mirror[0], mirror[1]);

        bobJumpRight = new Animation(0.1f, split[2], split[3]);
        bobJumpLeft = new Animation(0.1f, mirror[2], mirror[3]);

        bobIdleRight = new Animation(0.5f, split[0], split[4]);
        bobIdleLeft = new Animation(0.5f, mirror[0], mirror[4]);

        bobDead = new Animation(0.2f, split[0]);

        split = new TextureRegion(bobTexture).split(20, 20)[1];
        cube = split[0];
        cubeFixed = new Animation(1, split[1], split[2], split[3], split[4], split[5]);

        split = new TextureRegion(bobTexture).split(20, 20)[2];

        cubeControlled = split[0];
        spawn = new Animation(0.1f, split[4], split[3], split[2], split[1]);
        dying = new Animation(0.1f, split[1], split[2], split[3], split[4]);
        dispenser = split[5];


        split = new TextureRegion(bobTexture).split(20, 20)[3];

        rocket = new Animation(0.1f, split[0], split[1], split[2], split[3]);
        rocketPad = split[4];

        split = new TextureRegion(bobTexture).split(20, 20)[4];
        rocketExplosion = new Animation(0.1f, split[0], split[1], split[2], split[3], split[4], split[4]);

        split = new TextureRegion(bobTexture).split(20, 20)[5];

        endDoor = split[2];
        movingSpikes = split[0];
        laser = split[1];
    }

    public void render (float deltaTime) {

        if (map.getCube().state != DemoCube.CONTROLLED){
            cam.position.lerp(lerpTarget.set(map.bob.pos.x, map.bob.pos.y, 0), 2f * deltaTime);
        }else{
            cam.position.lerp(lerpTarget.set(map.getCube().pos.x, map.getCube().pos.y, 0), 2f * deltaTime);
        }

        cam.update();

        cache.setProjectionMatrix(cam.combined);

        Gdx.gl.glDisable(GL20.GL_BLEND);
        cache.begin();

        for (int blockY = 0; blockY < 4; blockY++) {
            for (int blockX = 0; blockX < 6; blockX++) {
                cache.draw(blocks[blockX][blockY]);
            }
        }


        cache.end();
        stateTime += deltaTime;

        batch.setProjectionMatrix(cam.combined);
        batch.begin();

        renderLasers();
        renderBob();
        renderCube();
        renderDispensers();
        renderRockets();
        renderMovingSpikes();

        batch.end();

        renderLaserBeams();

    }

    private void renderBob () {

        Animation anim = null;
        boolean loop = true;

        if (map.bob.getState() == DemoBob.RUN) {
            if (map.bob.dir == DemoBob.LEFT)
                anim = bobLeft;
            else
                anim = bobRight;
        }

        if (map.bob.getState() == DemoBob.IDLE) {
            if (map.bob.dir == DemoBob.LEFT)
                anim = bobIdleLeft;
            else
                anim = bobIdleRight;
        }

        if (map.bob.getState() == DemoBob.JUMP) {
            if (map.bob.dir == DemoBob.LEFT)
                anim = bobJumpLeft;
            else
                anim = bobJumpRight;
        }

        if (map.bob.getState() == DemoBob.SPAWN) {
            anim = spawn;
            loop = false;
        }

        if (map.bob.getState() == DemoBob.DYING) {
            anim = dying;
            loop = false;
        }

        batch.draw(anim.getKeyFrame(map.bob.getStateTime(), loop), map.bob.pos.x, map.bob.pos.y, 1, 1);
    }

    private void renderCube () {

        if (map.getCube().state == DemoCube.FOLLOW) {
            batch.draw(cube, map.getCube().pos.x, map.getCube().pos.y, 1.5f, 1.5f);
        }
        if (map.getCube().state == DemoCube.FIXED) {
            batch.draw(cubeFixed.getKeyFrame(map.getCube().stateTime, false), map.getCube().pos.x, map.getCube().pos.y, 1.5f, 1.5f);
        }
        if (map.getCube().state == DemoCube.CONTROLLED) {
            batch.draw(cubeControlled, map.getCube().pos.x, map.getCube().pos.y, 1.5f, 1.5f);
        }
    }

    private void renderLasers () {
        for (int i = 0; i < map.lasers.size; i++) {
            DemoLaser laser = map.lasers.get(i);
            batch.draw(this.laser, laser.getPos().x, laser.getPos().y, 0.5f, 0.5f, 1, 1, 1, 1, laser.getAngle());
        }
    }

    private void renderLaserBeams () {
        cam.update(false);
        renderer.begin(cam.combined, GL20.GL_LINES);
        for (int i = 0; i < map.lasers.size; i++) {
            DemoLaser laser = map.lasers.get(i);
            float sx = laser.getStartPoint().x, sy = laser.getStartPoint().y;
            float ex = laser.getCappedEndPoint().x, ey = laser.getCappedEndPoint().y;
            renderer.color(0, 1, 0, 1);
            renderer.vertex(sx, sy, 0);
            renderer.color(0, 1, 0, 1);
            renderer.vertex(ex, ey, 0);
        }
        renderer.end();
    }

    private void renderMovingSpikes () {
        for (int i = 0; i < map.movingSpikes.size; i++) {
            DemoMovingSpikes spikes = map.movingSpikes.get(i);
            batch.draw(movingSpikes, spikes.pos.x, spikes.pos.y, 0.5f, 0.5f, 1, 1, 1, 1, spikes.angle);
        }
    }

    private void renderRockets () {
        for (int i = 0; i < map.rockets.size; i++) {
            DemoRocket rocket = map.rockets.get(i);
            if (rocket.state == DemoRocket.FLYING) {
                TextureRegion frame = this.rocket.getKeyFrame(rocket.stateTime, true);
                batch.draw(frame, rocket.pos.x, rocket.pos.y, 0.5f, 0.5f, 1, 1, 1, 1, rocket.vel.angle());
            } else {
                TextureRegion frame = this.rocketExplosion.getKeyFrame(rocket.stateTime, false);
                batch.draw(frame, rocket.pos.x, rocket.pos.y, 1, 1);
            }
            batch.draw(rocketPad, rocket.startPos.x, rocket.startPos.y, 1, 1);
        }
    }

    private void renderDispensers () {
        for (int i = 0; i < map.dispensers.size; i++) {
            DemoDispenser dispenser = map.dispensers.get(i);
            batch.draw(this.dispenser, dispenser.bounds.x, dispenser.bounds.y, 1, 1);
        }
    }

    public void dispose () {
        cache.dispose();
        batch.dispose();
        tile.getTexture().dispose();
        cube.getTexture().dispose();
    }

}
