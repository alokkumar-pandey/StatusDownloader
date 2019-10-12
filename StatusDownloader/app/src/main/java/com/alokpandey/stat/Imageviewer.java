package com.alokpandey.stat;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.alokpandey.stat.R;

import java.io.File;

public class Imageviewer extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imageviewer);
        Intent intent = getIntent();
        File file = new File(intent.getStringExtra("MY_FILE_STRING"));
        Uri uri = FileProvider.getUriForFile(this, this.getApplicationContext().getPackageName() + ".provider",file);
        ImageView imgView = (ImageView) findViewById(R.id.image_viewer);
        imgView.setImageURI(uri);
    }
}
