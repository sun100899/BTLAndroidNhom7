package com.example.btlandroidnhom7.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import com.example.btlandroidnhom7.R;
import com.example.btlandroidnhom7.service.PicassoLoadingService;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;


import java.util.ArrayList;

import uk.co.senab.photoview.PhotoView;

public class ViewPagerAdapter extends PagerAdapter {

    Context context;
    LayoutInflater inflater;
    ArrayList<String> links;

    public ViewPagerAdapter(Context context, ArrayList<String> links) {
        this.context = context;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.links = links;
    }

    @Override
    public int getCount() {
        return links.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view==o;
        //return true neu doi tuong da duoc tao ra roi va nguoc lai
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        //super.destroyItem(container, position, object);  //super.destroyItem only works on FragmentPagerAdapter
        //Giai phong bo nho cac phan tu bi an di
        Log.i("PAGE", "REMOVE");
        ((ViewPager) container).removeView((View) object);

    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup viewGroup, int position) {
        //Giong ham getView trong BaseAdapter
        View view = inflater.inflate(R.layout.view_pager_item, viewGroup, false);
        PhotoView imageView = view.findViewById(R.id.page_image);
        PicassoLoadingService picasso = new PicassoLoadingService();
        picasso.loadImage(links.get(position), imageView);

        viewGroup.addView(view);
        Log.i("PAGE", links.size()+"");
        return view;
    }
}
