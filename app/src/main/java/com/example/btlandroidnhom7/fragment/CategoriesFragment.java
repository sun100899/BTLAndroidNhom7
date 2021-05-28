package com.example.btlandroidnhom7.fragment;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.Toast;

import com.example.btlandroidnhom7.R;
import com.example.btlandroidnhom7.SupportClass;
import com.example.btlandroidnhom7.adapter.MyComicAdapter;
import com.example.btlandroidnhom7.dialog.ProgressLoading;
import com.example.btlandroidnhom7.model.Comic;
import com.example.btlandroidnhom7.View.SearchActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class CategoriesFragment extends Fragment {
    private MyComicAdapter comedyAdapter, adventureAdapter, actionAdapter, sportsAdapter;
    private RecyclerView recycler_action, recycler_adventure, recycler_comedy, recycler_sports;
    private Toolbar toolbar_categories;
    private DatabaseReference mComic;
    public ScrollView scrollView;
    public static CategoriesFragment newInstance() {
        CategoriesFragment categoriesFragment = new CategoriesFragment();
        return categoriesFragment;
    }




    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.categories_fragment, container, false);
        initView(view);
        getActivity().invalidateOptionsMenu();
        setHasOptionsMenu(true);
        if(SupportClass.isOnline) {
            ProgressLoading.show(getContext());
        }
        fetchComicData();
        return view;
    }



    private void setAdapter() {
        comedyAdapter = new MyComicAdapter(getContext(), SupportClass.comedyList);
        recycler_comedy.setAdapter(comedyAdapter);
        adventureAdapter = new MyComicAdapter(getContext(), SupportClass.adventureList);
        recycler_adventure.setAdapter(adventureAdapter);
        sportsAdapter = new MyComicAdapter(getContext(), SupportClass.sportsList);
        recycler_sports.setAdapter(sportsAdapter);
        actionAdapter = new MyComicAdapter(getContext(), SupportClass.actionList);
        recycler_action.setAdapter(actionAdapter);
    }

    private void setLayoutManager() {
        recycler_comedy.setHasFixedSize(true);
        recycler_adventure.setHasFixedSize(true);
        recycler_sports.setHasFixedSize(true);
        recycler_action.setHasFixedSize(true);
        RecyclerView.LayoutManager lmComedy = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        RecyclerView.LayoutManager lmAdventure = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        RecyclerView.LayoutManager lmSports = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        RecyclerView.LayoutManager lmAction = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recycler_comedy.setLayoutManager(lmComedy);
        recycler_adventure.setLayoutManager(lmAdventure);
        recycler_sports.setLayoutManager(lmSports);
        recycler_action.setLayoutManager(lmAction);
    }

    private void fetchComicData() {
        mComic = FirebaseDatabase.getInstance().getReference().child("Comic");

        mComic.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot data: dataSnapshot.getChildren()) {
                    Comic comic = data.getValue(Comic.class);
                    for(String category: comic.getCategories()) {
                        if(category.equals("Comedy")) {
                            if(!isExist(comic, SupportClass.comedyList)) SupportClass.comedyList.add(comic);
                        }
                        if(category.equals("Adventure")) {
                            if(!isExist(comic, SupportClass.adventureList)) SupportClass.adventureList.add(comic);
                        }
                        if(category.equals("Action")) {
                            if(!isExist(comic, SupportClass.actionList)) SupportClass.actionList.add(comic);
                        }
                        if(category.equals("Sports")) {
                            if(!isExist(comic, SupportClass.sportsList)) SupportClass.sportsList.add(comic);
                        }
                    }
                }
                setLayoutManager();
                try {
                    setAdapter();
                } catch (Exception e) {
                    Log.i("EXCEPTION", e.getMessage());
                }
                ProgressLoading.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), ""+ databaseError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private boolean isExist(Comic comic, ArrayList<Comic> listComic) {
        for(Comic cm: listComic) {
            if(cm.getImage().equals(comic.getImage())) {
                return true;
            }
        }
        return false;
    }


    private void initView(View view) {
        recycler_action = view.findViewById(R.id.recycler_action_categories);
        recycler_adventure = view.findViewById(R.id.recycler_adventure_categories);
        recycler_comedy = view.findViewById(R.id.recycler_comedy_categories);
        recycler_sports = view.findViewById(R.id.recycler_sports_categories);
        toolbar_categories = view.findViewById(R.id.toolbar_categories);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar_categories);

        scrollView = view.findViewById(R.id.scroll_view);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.search_item, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.search_home:
                Intent intent = new Intent(getContext(), SearchActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
