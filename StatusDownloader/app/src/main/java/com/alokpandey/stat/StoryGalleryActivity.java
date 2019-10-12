package com.alokpandey.stat;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.alokpandey.stat.R;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Objects;

public class StoryGalleryActivity extends AppCompatActivity {
    Toolbar mToolbar;
    private GalleryAdapter recyclerViewAdapter;
    private RecyclerView recyclerView;
    private File[] files;
    private SwipeRefreshLayout recyclerLayout;
    private final String KEY_RECYCLER_STATE = "recycler_state";
    private static Bundle mBundleRecyclerViewState;
    Parcelable listState;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        initComponents();
Log.d("galler","chal gya");
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        setUpRecyclerView();
    }

    private void setUpRecyclerView() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewAdapter = new GalleryAdapter(StoryGalleryActivity.this, getData());
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerViewAdapter.notifyDataSetChanged();
    }

    private ArrayList<GalleryModel> getData() {
        ArrayList<GalleryModel> filesList = new ArrayList<>();
        GalleryModel f;
        String targetPath = Environment.getExternalStorageDirectory().getAbsolutePath() + Constants.SAVE_FOLDER_NAME;
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
                f = new GalleryModel();
                f.setName("Saved Stories: "+(i+1));
                f.setFilename(file.getName());
                f.setUri(Uri.fromFile(file));
                f.setPath(files[i].getAbsolutePath());
                filesList.add(f);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return filesList;
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
                        Toast.makeText(StoryGalleryActivity.this, "Refreshed!", Toast.LENGTH_SHORT).show();
                    }
                }, 2000);

            }
        });
    }
    @Override
    protected void onPause() {
        super.onPause();
        // save RecyclerView state
        if (mBundleRecyclerViewState != null && recyclerView != null) {
            Parcelable listState = mBundleRecyclerViewState.getParcelable(KEY_RECYCLER_STATE);
            if (recyclerView.getLayoutManager() != null) {
                recyclerView.getLayoutManager().onRestoreInstanceState(listState);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // restore RecyclerView state
        if (mBundleRecyclerViewState != null) {
            Parcelable listState = mBundleRecyclerViewState.getParcelable(KEY_RECYCLER_STATE);
            recyclerView.getLayoutManager().onRestoreInstanceState(listState);
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}

