package com.example.btlandroidnhom7.View;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.btlandroidnhom7.R;
import com.example.btlandroidnhom7.SupportClass;
import com.example.btlandroidnhom7.adapter.ViewPagerAdapter;
import com.wajahatkarim3.easyflipviewpager.BookFlipPageTransformer;

import java.util.ArrayList;

public class ViewComicActivity extends AppCompatActivity {
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_comic);

        viewPager = findViewById(R.id.view_pager);
        ArrayList<String> links = SupportClass.chapterSelected.getLinks();
        fetchLinks(links);

        for(int i = 0; i< SupportClass.chapterSelected.getLinks().size(); i++) {
            Log.d("LINK", SupportClass.chapterSelected.getLinks().get(i));
        }

    }

    private void fetchLinks(ArrayList<String> links) {
        if(links!=null) {
            if(links.size()>0) {
                ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getBaseContext(), SupportClass.chapterSelected.getLinks());
                if(viewPagerAdapter!=null) {
                    viewPager.setAdapter(viewPagerAdapter);

                    //Animation
                    BookFlipPageTransformer flipPageTransformer = new BookFlipPageTransformer();
                    flipPageTransformer.setScaleAmountPercent(3f);
                    viewPager.setPageTransformer(true, flipPageTransformer);
                }
            }
        } else {
            Toast.makeText(this, "No image here", Toast.LENGTH_LONG).show();
        }
    }
}
