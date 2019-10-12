package com.alokpandey.stat;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.alokpandey.stat.R;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Objects;

public class WaActivity extends AppCompatActivity {

    Toolbar mToolbar;
    private StoryAdapter recyclerViewAdapter;
    private RecyclerView recyclerView;
    private File[] files;
    private SwipeRefreshLayout recyclerLayout;
    public static int i =0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wa);
        Intent a =getIntent();
        i= a.getIntExtra("value",0);
        initComponents();
        //Toolbar
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setTitle("Status Downloader");
        setUpRecyclerView();
    }

    private void initComponents() {
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRecyclerView);
        recyclerLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                recyclerLayout.setRefreshing(true);
                setUpRecyclerView();
                (new Handler()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        recyclerLayout.setRefreshing(false);
                        Toast.makeText(WaActivity.this, "Refreshed!", Toast.LENGTH_SHORT).show();
                    }
                }, 2000);
            }
        });
    }
    private void setUpRecyclerView() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewAdapter = new StoryAdapter(WaActivity.this, getData());
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerViewAdapter.notifyDataSetChanged();
    }
    private ArrayList<StoryModel> getData() {
        ArrayList<StoryModel> filesList = new ArrayList<>();
        StoryModel f;
        if (WaActivity.i == 0) {
            String targetPath = Environment.getExternalStorageDirectory().getAbsolutePath() + Constants.FOLDER_NAME + "Media/.Statuses";

            File targetDirector = new File(targetPath);
            files = targetDirector.listFiles();
            if (files == null) {
//            noImageText.setVisibility(View.INVISIBLE);
            }
            try {
                Arrays.sort(Objects.requireNonNull(files), new Comparator() {
                    public int compare(Object o1, Object o2) {

                        return Long.compare(((File) o2).lastModified(), ((File) o1).lastModified());
                    }

                });

                for (int i = 0; i < files.length; i++) {
                    File file = files[i];
                    f = new StoryModel();
                    f.setName("Story Saver: "+(i+1));
                    f.setUri(Uri.fromFile(file));
                    f.setPath(files[i].getAbsolutePath());
                    f.setFilename(file.getName());
                    filesList.add(f);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return filesList;



        } else {

            String targetPath = Environment.getExternalStorageDirectory().getAbsolutePath() + Constants.FOLDER_NAME_BUSINESS + "Media/.Statuses";


            File targetDirector = new File(targetPath);
            files = targetDirector.listFiles();
            if (files == null) {
//            noImageText.setVisibility(View.INVISIBLE);
            }
            try {
                Arrays.sort(Objects.requireNonNull(files), new Comparator() {
                    public int compare(Object o1, Object o2) {

                        return Long.compare(((File) o2).lastModified(), ((File) o1).lastModified());
                    }

                });

                for (int i = 0; i < files.length; i++) {
                    File file = files[i];
                    f = new StoryModel();
                    f.setName("Story Saver: " + (i + 1));
                    f.setUri(Uri.fromFile(file));
                    f.setPath(files[i].getAbsolutePath());
                    f.setFilename(file.getName());
                    filesList.add(f);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return filesList;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_wa, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.ic_whatapp) {
            try{
                Intent launchIntent = WaActivity.this.getPackageManager().getLaunchIntentForPackage("com.whatsapp");
                startActivity(launchIntent);
            }catch (Exception e){
                e.printStackTrace();
            }

        }else if(item.getItemId() == android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}
