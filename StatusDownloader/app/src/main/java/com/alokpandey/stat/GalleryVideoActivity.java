package com.alokpandey.stat;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;

import com.alokpandey.stat.R;
import com.alokpandey.stat.whatsappvideo.VideosDownload;
import com.geniusforapp.fancydialog.FancyAlertDialog;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GalleryVideoActivity extends AppCompatActivity implements Animation.AnimationListener {
    String myAppName;
    Toolbar mToolbar;
    ViewPager mviewPager;
    VideosDownload tab2Videos;
    public static MenuItem delete;
    public static MenuItem share;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_mygallery);
        myAppName = getResources().getString(R.string.app_name);
        tab2Videos = new VideosDownload();
        mviewPager = (ViewPager) findViewById(R.id.viewpager_video);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        GalleryVideoActivity.this.setTitle("WhatsApp Videos");
        String path= Environment.getExternalStorageDirectory()+"/WhatsApp/Media/WhatsApp Video/Sent";
        File file = new File(path);
        try{
            if(file.exists()){
                deleteRecursive(file);
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        try {
            Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        }catch (Exception e)
        {
            e.printStackTrace();
        }


        viewPagerSetup(mviewPager);
        loadInterstitialAd();
    }

    private void loadInterstitialAd() {
    }
    private void showInterstitial() {
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();
        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }
        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }
        @Override
        public int getCount() {
            return (mFragmentList.size());
        }
        public void addFragment(Fragment fragment, String title) {
            Log.d("debug video",title);
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }
        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
    public void onBackPressed() {
        // TODO Auto-generated method stub
        File oldFoldervideo = new File(Environment.getExternalStorageDirectory()+"/WhatsApp/Media/","Sent2");
        File newFoldervideo = new File(Environment.getExternalStorageDirectory()+"/WhatsApp/Media/WhatsApp Video","Sent");
        boolean successvideo = oldFoldervideo.renameTo(newFoldervideo);
        showInterstitial();
        finish();
    }
    private void viewPagerSetup(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(tab2Videos, "VIDEOS");
        viewPager.setAdapter(adapter);
    }
    void deleteRecursive(File fileOrDirectory) {
        if (fileOrDirectory.isDirectory())
            for (File child : fileOrDirectory.listFiles())
                deleteRecursive(child);

        fileOrDirectory.delete();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mygallery, menu);
        delete = menu.findItem(R.id.action_delete);
        share = menu.findItem(R.id.action_share);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == android.R.id.home) {
            finish();
        } else if (itemId == R.id.action_delete) {
            FancyAlertDialog.Builder alert = new FancyAlertDialog.Builder(GalleryVideoActivity.this)
                    .setTextTitle("DELETE?")
                    .setBody("Are you sure you want to delete these videos?")
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
            if (mviewPager.getCurrentItem() == 0) {
                Log.i("images", "" + tab2Videos.mShareVideos);
                for (int i = 0; i < tab2Videos.mShareVideos.size(); i++) {
                    if (!tab2Videos.mShareVideos.get(i).equalsIgnoreCase("null")) {
                        File file = new File(tab2Videos.mShareVideos.get(i));
                        boolean deleted = file.delete();
                    }
                }
                tab2Videos.refresh();
            }
                            dialog.dismiss();

                        }
                    })
                    .build();
            alert.show();
        }
        else if (itemId == R.id.action_share) {
            if (mviewPager.getCurrentItem() == 0) {
                Intent sharingIntent = new Intent(Intent.ACTION_SEND_MULTIPLE);
                //sharingIntent.putExtra(Intent.EXTRA_TEXT, "Downloaded using " + myAppName + " android application");
                sharingIntent.setType("video/mp4");
                ArrayList<Uri> files = new ArrayList<>();

                for (String path : tab2Videos.mShareVideos) {
                    File file = new File(path);
                    Uri uri = Uri.fromFile(file);
                    files.add(uri);
                }
                sharingIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, files);
                sharingIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
                StrictMode.setVmPolicy(builder.build());
                startActivity(Intent.createChooser(sharingIntent, "Share videos using"));
            }
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onAnimationStart(Animation animation) {
        // TODO Auto-generated method stub
    }
    @Override
    public void onAnimationEnd(Animation animation) {
        // TODO Auto-generated method stub
    }
    @Override
    public void onAnimationRepeat(Animation animation) {
        // TODO Auto-generated method stub
    }
    @Override
    protected void onPostResume() {
        super.onPostResume();
    }
}
