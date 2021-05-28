package com.example.btlandroidnhom7.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.btlandroidnhom7.R;
import com.example.btlandroidnhom7.SupportClass;
import com.example.btlandroidnhom7.adapter.MyComicAdapter;
import com.example.btlandroidnhom7.dialog.ProgressLoading;
import com.example.btlandroidnhom7.model.Comic;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FavoritesFragment extends Fragment {
    private RecyclerView recycler_favorites;
    private DatabaseReference mUsers;
    private FirebaseAuth mAuth;
    public static FavoritesFragment newInstance() {
        FavoritesFragment favoritesFragment = new FavoritesFragment();
        return favoritesFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.favorites_fragment, container, false);
        recycler_favorites = view.findViewById(R.id.recycler_favorites);
        mAuth = FirebaseAuth.getInstance();
        mUsers = FirebaseDatabase.getInstance().getReference().child("Users");
        if(SupportClass.isOnline) {
            ProgressLoading.show(getContext());
        }
        loadListFavoriteComics();
        Toolbar toolbar = view.findViewById(R.id.toolbar_favorites);
        ((AppCompatActivity)getActivity()).getSupportActionBar();
        toolbar.setTitle("Favorites");


        return view;
    }

    private void loadListFavoriteComics() {
        mUsers.child(mAuth.getCurrentUser().getUid())
                .child("Favorites")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        ArrayList<Comic> comics = new ArrayList<>();
                        for(DataSnapshot data: dataSnapshot.getChildren()) {
                            comics.add(data.getValue(Comic.class));
                        }
                        if(comics!=null) {
                            try {
                                MyComicAdapter comicAdapter = new MyComicAdapter(getContext(), comics);
                                RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 3);
                                recycler_favorites.setHasFixedSize(true);
                                recycler_favorites.setLayoutManager(layoutManager);
                                recycler_favorites.setAdapter(comicAdapter);
                            } catch (Exception e) {
                                Log.i("EXCEPTION", e.getMessage());
                            }
                        }
                        ProgressLoading.dismiss();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }


}

