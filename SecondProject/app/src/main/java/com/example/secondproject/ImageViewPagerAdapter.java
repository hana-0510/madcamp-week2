package com.example.secondproject;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import java.util.ArrayList;

public class ImageViewPagerAdapter extends PagerAdapter {
    private Context context;
    private int img_num;
    private ArrayList<Bitmap> data_list;

    public ImageViewPagerAdapter(Context context, ArrayList<Bitmap> _data_list) {
        this.context = context;
        this.data_list = _data_list;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = null ;

        if (context != null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.simple_image, container, false);

            ImageView imageView = view.findViewById(R.id.imageView);

            imageView.setImageBitmap(data_list.get(position));
        }

        container.addView(view) ;
        return view ;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object view) {
        container.removeView((View) view);
    }

    @Override
    public int getCount() {
        return data_list.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return (view == (View) object);

    }

}
