package com.omonicon;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.omonicon.screens.ScreenType;
import com.omonicon.screens.ScreenManager;
/**
 * Created by Jakov on 11.10.2015..
 * Enables easier GUI creating
 */
public class GUICreator {

    public static ImageButton createButton(Texture texture) {
        return new ImageButton(
               new TextureRegionDrawable(
                   new TextureRegion(texture) ) );
    }

    public static InputListener createListener(final ScreenType dstScreen) {
        return new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                ScreenManager.getScreenManagerInstance().show(dstScreen);
                return false;
            }
        };
    }

}
