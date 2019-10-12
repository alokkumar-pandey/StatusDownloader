package com.alokpandey.stat;


import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.FileProvider;
import android.widget.MediaController;
import android.widget.VideoView;

import com.alokpandey.stat.R;

import java.io.File;

public class VideoplayerActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_videoplayer);
        Intent intent = getIntent();
        VideoView videoView =(VideoView)findViewById(R.id.videoView);
        MediaController mediaController= new MediaController(this);
        mediaController.setAnchorView(videoView);
        File file = new File(intent.getStringExtra("MY_FILE_STRING"));
        Uri uri = FileProvider.getUriForFile(this, this.getApplicationContext().getPackageName() + ".provider",file);
        videoView.setMediaController(mediaController);
        videoView.setVideoURI(uri);
        videoView.requestFocus();
        videoView.start();


    }

}