package com.alokpandey.stat;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.alokpandey.stat.R;
import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;

import java.io.File;

public class ImageAdapter extends PagerAdapter {
    Activity mContext;
    LayoutInflater mLayoutInflater;
    File[] mFiles;
    public ImageAdapter(Activity context, File[] files) {
        mContext = context;
        this.mFiles = files;

        Log.d("file size", Integer.toString(mFiles.length));
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return mFiles.length -1;
    }
    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((LinearLayout) object);
    }
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View itemView = mLayoutInflater.inflate(R.layout.viewpager_item_image, container, false);
        SubsamplingScaleImageView mImage = (SubsamplingScaleImageView) itemView.findViewById(R.id.img_touchview);
        try {

            Bitmap bmp = BitmapFactory.decodeFile(mFiles[position].getAbsolutePath());
            mImage.setImage(ImageSource.bitmap(bmp));
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        container.addView(itemView);
        return itemView;
    }
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }
}
