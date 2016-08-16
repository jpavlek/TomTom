package com.omonicon.tomtom;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.widget.VideoView;

/**
 * Created by user on 29/03/15.
 */
public class VideoSplashScreen extends Activity implements MediaPlayer.OnCompletionListener {
/*
http://gamedev.stackexchange.com/questions/30607/play-videos-with-libgdx
*/
        @Override
        public void onCreate(Bundle savedInstanceState)
        {

            super.onCreate(savedInstanceState);

            setContentView(R.layout.video_layout);
            String fileName = "android.resource://"+  getPackageName() + "/"+ R.raw.zero0;

            VideoView vv = (VideoView) this.findViewById(R.id.videoviewsplashscreen);
            vv.setVideoURI(Uri.parse(fileName));
            vv.setOnCompletionListener(this);
            //vv.setMediaController(new MediaController(this));
            vv.requestFocus();
            vv.start();

        }

        @Override
        public void onCompletion(MediaPlayer mp)
        {
            // TODO Auto-generated method stub
            Intent intent = new Intent(this, AndroidLauncher.class);
            startActivity(intent);
            finish();
        }
}
