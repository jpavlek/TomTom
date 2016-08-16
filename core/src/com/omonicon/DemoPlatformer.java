package com.omonicon;

/**
 * Created by user on 07/02/16.
 */
import com.badlogic.gdx.Game;

public class DemoPlatformer extends Game {

    @Override
    public void create () {
        setScreen(new DemoMainMenu(this));
    }

}
