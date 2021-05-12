package com.example.btlandroidnhom7.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.btlandroidnhom7.SupportClass;
import com.example.btlandroidnhom7.adapter.MyComicAdapter;

import ss.com.bannerslider.Slider;

import com.example.btlandroidnhom7.adapter.MySliderAdapter;
import com.example.btlandroidnhom7.dialog.ProgressLoading;
import com.example.btlandroidnhom7.model.Comic;
import com.example.btlandroidnhom7.service.PicassoLoadingService;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.example.btlandroidnhom7.R;

import java.util.ArrayList;


public class HomeFragment extends Fragment {

    private Slider slider;
    private SwipeRefreshLayout refreshLayout;
    private RecyclerView recycler_comic;
    private MyComicAdapter myComicAdapter;

    DatabaseReference banners;
    DatabaseReference comics;

    public static  HomeFragment newInstance(){
        HomeFragment homeFragment = new HomeFragment();
        return homeFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //init Firebase
        banners = FirebaseDatabase.getInstance().getReference().child("Banners");
        comics  = FirebaseDatabase.getInstance().getReference().child("Comic");
    }

    private void loadBanner() {
        banners.addListenerForSingleValueEvent(new ValueEventListener() {  ////fetch array banner
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<String> bannerList = new ArrayList<>();
                for(DataSnapshot data: dataSnapshot.getChildren()) {
                    String image = data.getValue(String.class);
                    bannerList.add(image);
                }
                //setadapter
                slider.setAdapter(new MySliderAdapter(bannerList));
                slider.setInterval(3000);
                refreshLayout.setRefreshing(false);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), ""+ databaseError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void loadComic() {
        comics.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<Comic> comicList = new ArrayList<>();
                for(DataSnapshot data: dataSnapshot.getChildren()) {
                    Comic comic = data.getValue(Comic.class);
                    comicList.add(comic);
                }
                SupportClass.allListComic = comicList;
                recycler_comic.setHasFixedSize(true);
                RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 3);
                recycler_comic.setLayoutManager(layoutManager);
                try {
                    myComicAdapter = new MyComicAdapter(getContext(), comicList);
                    recycler_comic.setAdapter(myComicAdapter);
                } catch (Exception e) {
                    Log.i("EXCEPTION", e.getMessage());
                }
                refreshLayout.setRefreshing(false);
                ProgressLoading.dismiss();
                //Log.i("ERROR", comicList.size()+"");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), ""+ databaseError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_fragment, container, false);
        recycler_comic = view.findViewById(R.id.recycler_comic);
        slider = view.findViewById(R.id.banner_slider);
        slider.init(new PicassoLoadingService());
        refreshLayout = view.findViewById(R.id.swipe);
        refreshLayout.setColorSchemeResources(R.color.primaryColor, R.color.primaryDarkColor);
        if(SupportClass.isOnline) {
            ProgressLoading.show(getContext());
        }
        setOnRefreshListener();
        return view;
    }
    private void setOnRefreshListener() {
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadBanner();
                loadComic();
            }
        });
        refreshLayout.post(new Runnable() {
            @Override
            public void run() {
                loadBanner();
                loadComic();
            }
        });
    }

}
