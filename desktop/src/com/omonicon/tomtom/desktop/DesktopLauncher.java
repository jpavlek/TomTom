package com.omonicon.tomtom.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.omonicon.DemoPlatformer;
import com.omonicon.tomtom.TomTom;

public class DesktopLauncher {

    static int resolutionX = 1440;//960;
    static int resolutionY =  900;//640;

	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.width = resolutionX;
        config.height = resolutionY;
		new LwjglApplication(new TomTom(), config);
        //new LwjglApplication(new DemoPlatformer(), config);
	}
}
