package com.alokpandey.stat;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.alokpandey.stat.R;
import com.geniusforapp.fancydialog.FancyAlertDialog;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    Toolbar mToolbar;
    private StoryAdapter recyclerViewAdapter;
    private RecyclerView recyclerView;
    private File[] files;
    private SwipeRefreshLayout recyclerLayout;
    public static int i =0;



    //RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView.Adapter adapter;

    private static final int REQUEST_EXTERNAL_STORAGE_RESULT = 0;



    ArrayList<WhatsppImages> mListImages;
    ArrayList<WhatsppVideos> mListVideos;
    ArrayList<WhatsppImages> mListProfile;
    ArrayList<WhatsppImages> mListVoice;
    ArrayList<WhatsppImages> mListAudio;
    ArrayList<WhatsppImages> mListWallpaper;
    ArrayList<WhatsppImages> mListDatabase;
    ArrayList<WhatsppImages> mListDocuments;

    String imagepath = "";
    String databasePath = "";
    String videoPath = "";
    String audioPath = "";
    String voicePath = "";
    String profilePath = "";
    String wallpaperPath = "";
    String docPath = "";
    String myAppName;
    public static Context context;
    boolean doubleBackToExitPressedOnce = false;


    ProgressDialog mDialog;
    static boolean firstTime = false;
    private DrawerLayout dl;
    private ActionBarDrawerToggle t;
    private NavigationView nv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        context = getApplicationContext();


        myAppName = getResources().getString(R.string.app_name);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        REQUEST_EXTERNAL_STORAGE_RESULT);
            }
        }

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_EXTERNAL_STORAGE_RESULT);
            }
        }


        dl = (DrawerLayout) findViewById(R.id.drawer);
        t = new ActionBarDrawerToggle(this, dl, R.string.open, R.string.close);
        dl.addDrawerListener(t);
        t.syncState();

        nv = (NavigationView) findViewById(R.id.nv);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("WhatsApp statuses");

        nv.setItemIconTintList(null);

        nv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                dl.closeDrawers();

                int id = item.getItemId();
                switch (id) {

                    case R.id.WS:
                        MainActivity.i = 0;
                        refresh(0);
                        Objects.requireNonNull(getSupportActionBar()).setTitle("WhatsApp statuses");

                        return true;
                    case R.id.WBS:
                        boolean isAppInstalled = appInstalledOrNot("com.whatsapp.w4b");

                        if (isAppInstalled) {
                            Log.i("app", "Application is already installed.");
                            String targetPath = Environment.getExternalStorageDirectory().getAbsolutePath() + Constants.str[1] + "Media/.Statuses";

                            File targetDirector = new File(targetPath);
                            files = targetDirector.listFiles();
                            if (files == null) {
                                FancyAlertDialog.Builder alert = new FancyAlertDialog.Builder(MainActivity.this)
                                        .setTextTitle("Oops!")
                                        .setBody("It seems like no  Business status  is available")
                                        .setNegativeColor(R.color.colorNegative)
                                        .setNegativeButtonText("ok")
                                        .setOnNegativeClicked(new FancyAlertDialog.OnNegativeClicked() {
                                            @Override
                                            public void OnClick(View view, Dialog dialog) {
                                                dialog.dismiss();
                                            }
                                        })


                                        .build();
                                alert.show();

                                //  Toast.makeText(MainActivity.this, "not found", Toast.LENGTH_SHORT).show();
                            } else {
                                MainActivity.i = 1;
                                refresh(1);
                                Objects.requireNonNull(getSupportActionBar()).setTitle("Business statuses");

                            }


                        } else {


                            FancyAlertDialog.Builder alert = new FancyAlertDialog.Builder(MainActivity.this)
                                    .setTextTitle("Oops!")
                                    .setBody("We couldn't find WhatsApp Business on this device.")
                                    .setNegativeColor(R.color.colorNegative)
                                    .setNegativeButtonText("No thanks!")
                                    .setOnNegativeClicked(new FancyAlertDialog.OnNegativeClicked() {
                                        @Override
                                        public void OnClick(View view, Dialog dialog) {
                                            dialog.dismiss();
                                        }
                                    })
                                    .setPositiveButtonText("Install now")
                                    .setPositiveColor(R.color.colorPositive)
                                    .setOnPositiveClicked(new FancyAlertDialog.OnPositiveClicked() {
                                        @Override
                                        public void OnClick(View view, Dialog dialog) {

                                            startActivity(new Intent(Intent.ACTION_VIEW,
                                                    Uri.parse("http://play.google.com/store/apps/details?id=" + "com.whatsapp.w4b")));
                                            dialog.cancel();

                                        }
                                    })
                                    .build();
                            alert.show();


                            Log.i("app", "Application is not currently installed.");
                        }

                        return true;
                    case R.id.PS:
                        boolean isAppInstalled1 = appInstalledOrNot("com.lbe.parallel.intl.arm64");
                        if (isAppInstalled1) {
                            Log.i("app", "Application is already installed.");
                            String targetPath = Environment.getExternalStorageDirectory().getAbsolutePath() + Constants.str[2] + "Media/.Statuses";

                            File targetDirector = new File(targetPath);
                            files = targetDirector.listFiles();
                            if (files == null) {
                                Toast.makeText(MainActivity.this, "not found", Toast.LENGTH_SHORT).show();
                                FancyAlertDialog.Builder alert = new FancyAlertDialog.Builder(MainActivity.this)
                                        .setTextTitle("Oops!")
                                        .setBody("It seems like WhatsApp  is not installed on parallel space. please install it first")
                                        .setNegativeColor(R.color.colorNegative)
                                        .setNegativeButtonText("ok")
                                        .setOnNegativeClicked(new FancyAlertDialog.OnNegativeClicked() {
                                            @Override
                                            public void OnClick(View view, Dialog dialog) {
                                                dialog.dismiss();
                                            }
                                        })

                                        .build();
                                alert.show();

                            } else {
                                MainActivity.i = 2;
                                refresh(2);
                                Objects.requireNonNull(getSupportActionBar()).setTitle("Parallel space");

                            }
                        } else {

                            FancyAlertDialog.Builder alert = new FancyAlertDialog.Builder(MainActivity.this)
                                    .setTextTitle("Oops!")
                                    .setBody("We couldn't find Parallel space 64bit on this device")
                                    .setNegativeColor(R.color.colorNegative)
                                    .setNegativeButtonText("No thanks!")
                                    .setOnNegativeClicked(new FancyAlertDialog.OnNegativeClicked() {
                                        @Override
                                        public void OnClick(View view, Dialog dialog) {
                                            dialog.dismiss();
                                        }
                                    })
                                    .setPositiveButtonText("Install now")
                                    .setPositiveColor(R.color.colorPositive)
                                    .setOnPositiveClicked(new FancyAlertDialog.OnPositiveClicked() {
                                        @Override
                                        public void OnClick(View view, Dialog dialog) {

                                            startActivity(new Intent(Intent.ACTION_VIEW,
                                                    Uri.parse("http://play.google.com/store/apps/details?id=" + "com.lbe.parallel.intl&hl=en")));
                                            dialog.cancel();

                                        }
                                    })
                                    .build();
                            alert.show();

                            Log.i("app", "Application is not currently installed.");
                        }
                        return true;
                    case R.id.PSL:

                        boolean isAppInstalled2 = appInstalledOrNot("com.parallel.space.lite.arm64");

                        if (isAppInstalled2) {
                            Log.i("app", "Application is already installed.");
                            String targetPath = Environment.getExternalStorageDirectory().getAbsolutePath() + Constants.str[3] + "Media/.Statuses";

                            File targetDirector = new File(targetPath);
                            files = targetDirector.listFiles();
                            if (files == null) {

                                FancyAlertDialog.Builder alert = new FancyAlertDialog.Builder(MainActivity.this)
                                        .setTextTitle("Oops!")
                                        .setBody("It seems like WhatsApp  is not installed on parallel space lite. please install it first")
                                        .setNegativeColor(R.color.colorNegative)
                                        .setNegativeButtonText("ok")
                                        .setOnNegativeClicked(new FancyAlertDialog.OnNegativeClicked() {
                                            @Override
                                            public void OnClick(View view, Dialog dialog) {
                                                dialog.dismiss();
                                            }
                                        })

                                        .build();
                                alert.show();


                                //  Toast.makeText(MainActivity.this, "not found", Toast.LENGTH_SHORT).show();
                            } else {
                                MainActivity.i = 3;
                                refresh(3);
                                Objects.requireNonNull(getSupportActionBar()).setTitle("Parallel space lite");

                            }


                        } else {

                            FancyAlertDialog.Builder alert = new FancyAlertDialog.Builder(MainActivity.this)
                                    .setTextTitle("Oops!")
                                    .setBody("We couldn't find Parallel space lite 64bit on this device")
                                    .setNegativeColor(R.color.colorNegative)
                                    .setNegativeButtonText("No thanks!")
                                    .setOnNegativeClicked(new FancyAlertDialog.OnNegativeClicked() {
                                        @Override
                                        public void OnClick(View view, Dialog dialog) {
                                            dialog.dismiss();
                                        }
                                    })
                                    .setPositiveButtonText("Install now")
                                    .setPositiveColor(R.color.colorPositive)
                                    .setOnPositiveClicked(new FancyAlertDialog.OnPositiveClicked() {
                                        @Override
                                        public void OnClick(View view, Dialog dialog) {

                                            startActivity(new Intent(Intent.ACTION_VIEW,
                                                    Uri.parse("http://play.google.com/store/apps/details?id=" + "com.parallel.space.lite&hl=en")));
                                            dialog.cancel();
                                        }
                                    })
                                    .build();
                            alert.show();


                            Log.i("app", "Application is not currently installed.");
                        }
                        return true;
                    case R.id.G:
                        Intent intent1 = new Intent(MainActivity.this, StoryGalleryActivity.class);
                        startActivity(intent1);
                        return true;
                   /* case R.id.Images:
                        File oldFolder = new File(Environment.getExternalStorageDirectory() + "/WhatsApp/Media/WhatsApp Images/", "Sent");
                        File newFolder = new File(Environment.getExternalStorageDirectory() + "/WhatsApp/Media/", "Sent");
                        boolean success = oldFolder.renameTo(newFolder);
                        Intent intent6 = new Intent(MainActivity.this, GalleryActivity.class);
                        startActivity(intent6);
                        return true;
                    case R.id.videos:
                        File oldFoldervideo = new File(Environment.getExternalStorageDirectory() + "/WhatsApp/Media/WhatsApp Video/", "Sent");
                        File newFoldervideo = new File(Environment.getExternalStorageDirectory() + "/WhatsApp/\\Media/", "Sent2");
                        boolean successvideo = oldFoldervideo.renameTo(newFoldervideo);
                        Intent intent7 = new Intent(MainActivity.this, GalleryVideoActivity.class);
                        startActivity(intent7);
                        return true;*/

                    /*case R.id.rate:
                        rateMe(findViewById(R.id.rate));
                        return true;
*/
                    case R.id.mshare:
                        Toast.makeText(MainActivity.this, "Share", Toast.LENGTH_SHORT).show();
                        Intent ShareIntent = new Intent(Intent.ACTION_SEND);
                        ShareIntent.setType("text/plain");
                        ShareIntent.putExtra(Intent.EXTRA_SUBJECT, "Must Install");
                        ShareIntent.putExtra(Intent.EXTRA_TEXT, "Hey check out my apps at: https://drive.google.com/open?id=0BwrIR5eJvPh2dU9NM3ZlZV85SDA");
                        startActivity(Intent.createChooser(ShareIntent, "Share using"));
                        break;

                    default:
                        return true;
                }
                return true;
            }
        });
        Intent a =getIntent();
        i= a.getIntExtra("value",0);
        initComponents(MainActivity.i);
        setUpRecyclerView(MainActivity.i);
        databasePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/WhatsApp/Databases";
        wallpaperPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/WhatsApp/Media/WallPaper";
        imagepath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/WhatsApp/Media/WhatsApp Images";
        voicePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/WhatsApp/Media/WhatsApp Voice Notes";
        audioPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/WhatsApp/Media/WhatsApp Audio";
        videoPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/WhatsApp/Media/WhatsApp Video";
        profilePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/WhatsApp/Media/WhatsApp Profile Photos";
        docPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/WhatsApp/Media/WhatsApp Documents";

        if (firstTime) {
            Log.e("first", "first if " + firstTime);
        } else {
            firstTime = true;
            Log.e("first", "first else " + firstTime);
            new UploadFeed().execute();
        }


       // AppRater.app_launched(MainActivity.this);
    }

    public void rateMe(View view) {
        try {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("market://details?id=" + this.getPackageName())));
        } catch (android.content.ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=" + this.getPackageName())));
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        if (t.onOptionsItemSelected(item))
            return true;
        Log.d("alokkkk","chal");
        switch (item.getItemId()) {
            case R.id.ic_whatapp:
                if (item.getItemId() == R.id.ic_whatapp) {
                    try{
                        Intent launchIntent = MainActivity.this.getPackageManager().getLaunchIntentForPackage("com.whatsapp");//Constants.whatsapp[MainActivity.i]
                        if (launchIntent != null) {
                            launchIntent.setFlags(0);
                        }
                        startActivity(launchIntent);
                    }catch (Exception e){
                        e.printStackTrace();
                        Log.e("MYAPP", "exception", e);
                    }

                }
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_EXTERNAL_STORAGE_RESULT: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    new UploadFeed().execute();

                } else {
                }
                return;
            }
        }
    }



    public class UploadFeed extends AsyncTask<Void, Void, Void> {
        String response = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            File oldFolder = new File(Environment.getExternalStorageDirectory()+"/WhatsApp/Media/WhatsApp Images/","Sent");
            File newFolder = new File(Environment.getExternalStorageDirectory()+"/WhatsApp/Media/","Sent");
            boolean success = oldFolder.renameTo(newFolder);
            File oldFoldervideo = new File(Environment.getExternalStorageDirectory()+"/WhatsApp/Media/WhatsApp Video/","Sent");
            File newFoldervideo = new File(Environment.getExternalStorageDirectory()+"/WhatsApp/Media/","Sent2");
            boolean successvideo = oldFoldervideo.renameTo(newFoldervideo);

            File oldFolderaudio = new File(Environment.getExternalStorageDirectory()+"/WhatsApp/Media/WhatsApp Audio/","Sent");
            File newFolderaudio = new File(Environment.getExternalStorageDirectory()+"/WhatsApp/Media/","Sent3");
            boolean successaudio = oldFolderaudio.renameTo(newFolderaudio);
            mDialog = Utils.SetProgressBar(mDialog, MainActivity.this);
            TAGS.clearTags();
        }

        @Override
        protected Void doInBackground(Void... params) {
            getWhatsAppVidoes(videoPath, true);
            getWhatsImages(imagepath, true);
            // getWhatsVoice(voicePath, true);
            //getWhatsAudio(audioPath, true);
            //getWhatsProfileImages(profilePath);
            //getWhatsAppWallpapers(wallpaperPath);
            //getdatebasefile(databasePath);
            //getDocfile(docPath);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            mDialog.dismiss();

            recyclerLayout.setRefreshing(true);
            setUpRecyclerView(MainActivity.i);
            (new Handler()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    recyclerLayout.setRefreshing(false);
                    //  Toast.makeText(MainActivity.this, "Refreshed!", Toast.LENGTH_SHORT).show();
                }
            }, 1000);
        }
    }

    public void getWhatsImages(String path, boolean first) {
        File files = new File(path);
        FileFilter filter = new FileFilter() {
            private final List<String> exts = Arrays.asList("jpeg", "jpg", "png", "bmp", "gif");

            @Override
            public boolean accept(File pathname) {
                String ext;
                String path = pathname.getPath();
                ext = path.substring(path.lastIndexOf(".") + 1);
                return exts.contains(ext);
            }
        };

        final File[] filesFound = files.listFiles(filter);
        mListImages = new ArrayList<>();
        if (filesFound != null && filesFound.length > 0) {
            for (File file : filesFound) {
                WhatsppImages ld = new WhatsppImages();
                ld.setName(file.getName());
                ld.setPath(file.getAbsolutePath());
                ld.setSize(file.length() / 1024);
                TAGS.imageSize += file.length() / 1024;
                TAGS.imageCounter++;
                mListImages.add(ld);
            }
        }
        if (first) {
            TAGS.mListImages = mListImages;
        } else {
            TAGS.mListImages.addAll(mListImages);
        }
    }

    public void getWhatsAppVidoes(String path, boolean first) {
        File files = new File(path);
        FileFilter filter = new FileFilter() {
            private final List<String> exts = Arrays.asList("3gp", "mp4", "mkv");

            @Override
            public boolean accept(File pathname) {
                String ext;
                String path = pathname.getPath();
                ext = path.substring(path.lastIndexOf(".") + 1);
                return exts.contains(ext);
            }
        };
        final File[] filesFound = files.listFiles(filter);
        mListVideos = new ArrayList<>();
        if (filesFound != null && filesFound.length > 0) {
            for (File file : filesFound) {
                WhatsppVideos vd = new WhatsppVideos();
                vd.setPath(file.getAbsolutePath());
                vd.setSize(file.length() / 1024);
                TAGS.videoSize += file.length() / 1024;
                vd.setBitmap(ThumbnailUtils.createVideoThumbnail(file.getAbsolutePath(), MediaStore.Video.Thumbnails.MICRO_KIND));
                mListVideos.add(vd);
                TAGS.videoCounter++;
            }
        }
        if (first) {
            TAGS.mListVideos = mListVideos;
        } else {
            TAGS.mListVideos.addAll(mListVideos);
        }
        Log.e("size", "video size: " + TAGS.videoSize);
    }


    @Override

    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.option_menu, menu);

        return super.onCreateOptionsMenu(menu);

    }
    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 1000);
    }

    private void initComponents(int  i) {
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRecyclerView);
        recyclerLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                recyclerLayout.setRefreshing(true);
                setUpRecyclerView(MainActivity.i);
                (new Handler()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        recyclerLayout.setRefreshing(false);
                        Toast.makeText(MainActivity.this, "Refreshed!", Toast.LENGTH_SHORT).show();
                    }
                }, 1000);
            }
        });
    }
    private void setUpRecyclerView(int i) {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewAdapter = new StoryAdapter(MainActivity.this, getData(i));
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerViewAdapter.notifyDataSetChanged();
    }
    private ArrayList<StoryModel> getData(int i) {
        ArrayList<StoryModel> filesList = new ArrayList<>();
        StoryModel f;

        String targetPath = Environment.getExternalStorageDirectory().getAbsolutePath() + Constants.str[i] + "Media/.Statuses";

        File targetDirector = new File(targetPath);
        files = targetDirector.listFiles();
        if (files == null) {

        }
        try {
            Arrays.sort(Objects.requireNonNull(files), new Comparator() {
                public int compare(Object o1, Object o2) {

                    return Long.compare(((File) o2).lastModified(), ((File) o1).lastModified());
                }

            });

            for (int j = 0; j < files.length; j++) {
                File file = files[j];
                f = new StoryModel();
                f.setName("Status : " + (j + 1));
                f.setUri(Uri.fromFile(file));
                f.setPath(files[j].getAbsolutePath());
                f.setFilename(file.getName());
                filesList.add(f);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(filesList.size()==0){
            Toast.makeText(this,"Empty Status",Toast.LENGTH_SHORT).show();
        }
        return filesList;


    }

    private boolean appInstalledOrNot(String uri) {
        PackageManager pm = getPackageManager();
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
        }

        return false;
    }


    public void getWhatsProfileImages(String path) {

        File files = new File(path);

        FileFilter filter = new FileFilter() {

            private final List<String> exts = Arrays.asList("jpeg", "jpg", "png", "bmp", "gif");
            //	        private final List<String> exts = Arrays.asList("3gp", "mp4");

            @Override
            public boolean accept(File pathname) {
                String ext;
                String path = pathname.getPath();
                ext = path.substring(path.lastIndexOf(".") + 1);
                return exts.contains(ext);
            }
        };

        final File[] filesFound = files.listFiles(filter);
        mListProfile = new ArrayList<>();
        if (filesFound != null && filesFound.length > 0) {
            for (File file : filesFound) {
                WhatsppImages ld = new WhatsppImages();
                ld.setPath(file.getAbsolutePath());
                ld.setSize(file.length() / 1024);
                TAGS.profileSize += file.length() / 1024;
                mListProfile.add(ld);
                TAGS.profileCounter++;
            }
        }
        TAGS.mListProfileImages = mListProfile;
    }
    public void getWhatsAppWallpapers(String path) {

        File files = new File(path);

        FileFilter filter = new FileFilter() {

            private final List<String> exts = Arrays.asList("jpeg", "jpg", "png", "bmp", "gif");
            //	        private final List<String> exts = Arrays.asList("3gp", "mp4");

            @Override
            public boolean accept(File pathname)
            {
                String ext;
                String path = pathname.getPath();
                ext = path.substring(path.lastIndexOf(".") + 1);
                return exts.contains(ext);
            }
        };

        final File [] filesFound = files.listFiles(filter);
        mListWallpaper = new ArrayList<>();
        if (filesFound != null && filesFound.length > 0) {
            for (File file : filesFound)
            {
                WhatsppImages ld=new WhatsppImages();
                ld.setPath(file.getAbsolutePath());
                ld.setSize(file.length()/1024);
                TAGS.wallpaperSize+=file.length()/1024;
                mListWallpaper.add(ld);
                TAGS.wallpaperCounter++;
            }
        }
        TAGS.mListWallpaper=mListWallpaper;
    }
    public void getWhatsVoice(String path,boolean first) {

        //		mDialog=Utils.SetProgressBar(mDialog, MainActivity.this);
        File files = new File(path);

        FileFilter filter = new FileFilter() {

            private final List<String> exts = Arrays.asList("aac", "m4a", "amr");
            //	        private final List<String> exts = Arrays.asList("3gp", "mp4");

            @Override
            public boolean accept(File pathname)
            {
                String ext;
                String path = pathname.getPath();
                ext = path.substring(path.lastIndexOf(".") + 1);
                return exts.contains(ext);
            }
        };

        final File [] filesFound = files.listFiles(filter);
        mListVoice = new ArrayList<>();
        if (filesFound != null && filesFound.length > 0) {
            for (File file : filesFound)
            {
                WhatsppImages ld=new WhatsppImages();
                ld.setPath(file.getAbsolutePath());
                ld.setSize(file.length()/1024);
                TAGS.voiceSize+=file.length()/1024;
                mListVoice.add(ld);
                TAGS.voiceCounter++;
            }
        }
        if(first)
        {
            TAGS.mListVoice=mListVoice;
        }
        else
        {
            TAGS.mListVoice.addAll(mListVoice);
        }
    }
    public void getdatebasefile(String path)
    {
        File files = new File(path);

        FileFilter filter = new FileFilter() {

            private final List<String> exts = Collections.singletonList("crypt12");
            //	        private final List<String> exts = Arrays.asList("3gp", "mp4");

            @Override
            public boolean accept(File pathname)
            {
                String ext;
                String path = pathname.getPath();
                ext = path.substring(path.lastIndexOf(".") + 1);
                return exts.contains(ext);
            }
        };

        final File [] filesFound = files.listFiles(filter);
        mListDatabase = new ArrayList<>();
        if (filesFound != null && filesFound.length > 0) {
            for (File file : filesFound)
            {
                WhatsppImages ld=new WhatsppImages();
                ld.setPath(file.getAbsolutePath());
                ld.setSize(file.length()/1024);
                mListDatabase.add(ld);
                TAGS.databaseSize+=file.length()/1024;
                TAGS.databaseCounter++;
            }
        }
    }
    public void getDocfile(String path)
    {
        File files = new File(path);

        FileFilter filter = new FileFilter() {

            private final List<String> exts = Arrays.asList("docx","pdf","doc");
            //	        private final List<String> exts = Arrays.asList("3gp", "mp4");

            @Override
            public boolean accept(File pathname)
            {
                String ext;
                String path = pathname.getPath();
                ext = path.substring(path.lastIndexOf(".") + 1);
                return exts.contains(ext);
            }
        };

        final File [] filesFound = files.listFiles(filter);
        mListDocuments = new ArrayList<>();
        if (filesFound != null && filesFound.length > 0) {
            for (File file : filesFound)
            {
                WhatsppImages ld=new WhatsppImages();
                ld.setPath(file.getAbsolutePath());
                ld.setSize(file.length()/1024);
                mListDocuments.add(ld);
                TAGS.docSize+=file.length()/1024;
                TAGS.docCounter++;
            }
        }
    }
    public void getWhatsAudio(String path,boolean first) {

        //		mDialog=Utils.SetProgressBar(mDialog, MainActivity.this);
        File files = new File(path);

        FileFilter filter = new FileFilter() {

            private final List<String> exts = Arrays.asList("aac", "m4a", "amr", "mp3");
            //	        private final List<String> exts = Arrays.asList("3gp", "mp4");

            @Override
            public boolean accept(File pathname)
            {
                String ext;
                String path = pathname.getPath();
                ext = path.substring(path.lastIndexOf(".") + 1);
                return exts.contains(ext);
            }
        };

        final File [] filesFound = files.listFiles(filter);
        mListAudio = new ArrayList<>();
        if (filesFound != null && filesFound.length > 0) {
            for (File file : filesFound)
            {
                WhatsppImages ld=new WhatsppImages();
                ld.setPath(file.getAbsolutePath());
                ld.setSize(file.length()/1024);
                TAGS.audioSize+=file.length()/1024;
                mListAudio.add(ld);
                TAGS.audioCounter++;
            }
        }
        if(first)
        {
            TAGS.mListAudio=mListAudio;
        }
        else
        {
            TAGS.mListAudio.addAll(mListAudio);
        }

    }

    public void refresh(int j){
        recyclerLayout.setRefreshing(true);
        setUpRecyclerView(j);
        (new Handler()).postDelayed(new Runnable() {
            @Override
            public void run() {
                recyclerLayout.setRefreshing(false);
                Toast.makeText(MainActivity.this, "Refreshed!", Toast.LENGTH_SHORT).show();
            }
        }, 1000);
    }

}
