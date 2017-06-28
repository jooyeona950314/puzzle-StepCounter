package com.localhost.jooyeon.final_project;

import android.content.Context;
import android.content.res.TypedArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;

/**
 * Created by yeona on 2017. 5. 27..
 */

public class ImageAdapter extends BaseAdapter {
    int mGalleryItemBackground;
    private Context mContext;
    private Integer[] mImageIds = {
            R.drawable.puzzle1,
            R.drawable.puzzle2,
            R.drawable.puzzle3,
            R.drawable.puzzle4
    };

    //퍼즐 리스트에 띄울 갤러리에 대한 설정 등록
    public ImageAdapter(Context c) {
        mContext = c;
        TypedArray a = c.obtainStyledAttributes(R.styleable.GalleryTheme);
        mGalleryItemBackground = a.getResourceId(R.styleable.GalleryTheme_android_galleryItemBackground, 0);
        a.recycle();
    }

    //갤러리에 포함시킬 이미지에 대한 정보들

    public int getImageId(int position){
        return mImageIds[position];
    }
    public int getCount() {
        return mImageIds.length;
    }
    public Object getItem(int position) {
        return position;
    }
    public long getItemId(int position) {
        return position;
    }

    //갤러리에 이미지 등록
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView i = new ImageView(mContext);
        i.setImageResource(mImageIds[position]);
        i.setLayoutParams(new Gallery.LayoutParams(250, 200));
        i.setScaleType(ImageView.ScaleType.FIT_XY);
        i.setBackgroundResource(mGalleryItemBackground );
        return i;
    }
}