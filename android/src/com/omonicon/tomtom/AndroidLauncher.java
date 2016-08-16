package com.omonicon.tomtom;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.omonicon.DemoPlatformer;
import com.omonicon.tomtom.TomTom;

public class AndroidLauncher extends AndroidApplication {
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		//initialize(new TomTom(), config);
        initialize(new DemoPlatformer(), config);
	}
}
