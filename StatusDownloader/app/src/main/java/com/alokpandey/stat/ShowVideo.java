package com.alokpandey.stat;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.alokpandey.stat.R;
import com.geniusforapp.fancydialog.FancyAlertDialog;

import org.apache.commons.io.comparator.LastModifiedFileComparator;

import java.io.File;
import java.util.Arrays;
import java.util.Objects;

public class ShowVideo extends AppCompatActivity {
    String myAppName, path;
    Toolbar toolbar;
    int mPosition;
    VideoAdapter mCustomPagerAdapter;
    ViewPager mViewPager;
    File[] videoFiles;
    File file;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_view);
        myAppName = getResources().getString(R.string.app_name);
        mViewPager = (ViewPager) findViewById(R.id.pager);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        mPosition = getIntent().getIntExtra("pos", 0);
        CreateFileArray();
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);
            getSupportActionBar().setTitle("" + ((int) mPosition + 1) + "/" + (videoFiles.length-1));
        }
        path = videoFiles[mPosition].getAbsolutePath();
        mCustomPagerAdapter = new VideoAdapter(ShowVideo.this, videoFiles);
        mViewPager.setAdapter(mCustomPagerAdapter);
        mViewPager.setCurrentItem(mPosition, true);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }
            @Override
            public void onPageSelected(int position) {
                mPosition = position;
                path = videoFiles[mViewPager.getCurrentItem()].getAbsolutePath();
                if (toolbar != null) {
                    toolbar.setTitle("" + ((int) mPosition + 1) + "/" + (videoFiles.length-1));
                }
            }
            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }
    private void CreateFileArray() {
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            Toast.makeText(getApplicationContext(), "Error! No SDCARD Found!", Toast.LENGTH_LONG).show();
        } else {
            file = new File(Environment.getExternalStorageDirectory(), StaticVariables.rootDir + "/" + StaticVariables.videoDir + "/");
            file.mkdirs();
        }
        if (file.isDirectory()) {
            videoFiles = file.listFiles();
            Arrays.sort(videoFiles, LastModifiedFileComparator.LASTMODIFIED_REVERSE);
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.single_image, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == android.R.id.home) {
            finish();
        }
        switch (itemId) {
            case R.id.action_delete:
                FancyAlertDialog.Builder alert = new FancyAlertDialog.Builder(ShowVideo.this)
                        .setTextTitle("DELETE?")
                        .setBody("Are you sure you want to delete this video?")
                        .setNegativeColor(R.color.colorNegative)
                        .setNegativeButtonText("Cancel")
                        .setOnNegativeClicked(new FancyAlertDialog.OnNegativeClicked() {
                            @Override
                            public void OnClick(View view, Dialog dialog) {
                                dialog.dismiss();
                            }
                        })
                        .setPositiveButtonText("Delete")
                        .setPositiveColor(R.color.colorPositive)
                        .setOnPositiveClicked(new FancyAlertDialog.OnPositiveClicked() {
                            @Override
                            public void OnClick(View view, Dialog dialog) {
                path = videoFiles[mViewPager.getCurrentItem()].getAbsolutePath();
                File file = new File(path);
                boolean deleted = file.delete();
                CreateFileArray();
                if ((videoFiles.length-1) != 0) {
                    mCustomPagerAdapter = new VideoAdapter(ShowVideo.this, videoFiles);
                    mViewPager.setAdapter(mCustomPagerAdapter);
                    mViewPager.setCurrentItem(mPosition, true);
                    path = videoFiles[mViewPager.getCurrentItem()].getAbsolutePath();
                    if (toolbar != null) {
                        toolbar.setTitle("" + ((int) mPosition + 1) + "/" +( videoFiles.length-1));
                    }
                } else {
                    finish();
                }
                                dialog.dismiss();

                            }
                        })
                        .build();
                alert.show();
                break;
            case R.id.action_share:
                path = videoFiles[mViewPager.getCurrentItem()].getAbsolutePath();
                Log.e("SHARE_PATH", path);
                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
               // sharingIntent.putExtra(Intent.EXTRA_TEXT, "#" + myAppName);
                sharingIntent.setType("video/*");
                sharingIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(path)));
                sharingIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
                StrictMode.setVmPolicy(builder.build());
                startActivity(Intent.createChooser(sharingIntent, "Share video using"));
                break;
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
