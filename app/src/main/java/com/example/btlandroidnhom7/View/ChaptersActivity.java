package com.example.btlandroidnhom7.View;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.btlandroidnhom7.SupportClass;
import com.example.btlandroidnhom7.adapter.MyChapterAdapter;
import com.example.btlandroidnhom7.model.Chapter;
import com.example.btlandroidnhom7.model.Comic;
import com.example.btlandroidnhom7.service.PicassoLoadingService;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.example.btlandroidnhom7.R;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ChaptersActivity extends AppCompatActivity {
    private RecyclerView recycler_chapter;
    private Toolbar toolbar;
    private ImageView imgBannerComic;
    private FirebaseAuth mAuth;
    private DatabaseReference user;
    private FloatingActionButton btnFav;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chapters);
        initView();
        //Load comic banner image
        PicassoLoadingService picasso = new PicassoLoadingService();
        picasso.loadImage(SupportClass.comicSelected.getImage(), imgBannerComic);
        ArrayList<Chapter> chapters = SupportClass.comicSelected.getChapters();
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recycler_chapter.setLayoutManager(layoutManager);
        recycler_chapter.setHasFixedSize(true);
        recycler_chapter.addItemDecoration(new DividerItemDecoration(this, ((LinearLayoutManager) layoutManager).getOrientation()));
        recycler_chapter.setAdapter(new MyChapterAdapter(this, chapters));



        mAuth = FirebaseAuth.getInstance();
        user = FirebaseDatabase.getInstance().getReference().child("Users");

        intialStatusFloatingButton();

        btnFav.setOnClickListener((view) -> {
            user.child(mAuth.getCurrentUser().getUid()).child("Favorites").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    // tạo biến kiểm tra xem truyện đã tồn tại trong Favorites chưa
                    boolean isExist = false;
                    for(DataSnapshot data: dataSnapshot.getChildren()){
                        if(SupportClass.comicSelected.getImage().equals(data.getValue(Comic.class).getImage())){
                            isExist = true;
                            break;
                            // nếu ok thì break
                        }
                    }
                    if(isExist == false){
                        //nếu chưa thì thêm vào list Favorites
                        user.child(mAuth.getCurrentUser().getUid()).child("Favorites").push().setValue(SupportClass.comicSelected);
                        //set trạng thái cho btn Fav
                        btnFav.setImageResource(R.drawable.ic_clear_white_24dp);
                        Toast.makeText(getBaseContext(), "Added to favorites list", Toast.LENGTH_LONG).show();
                    } else {
                        // nếu truyện tồn tại ->> remove khỏi list truyện
                        removeComicFromFavorites();
                        //set trạng thái của btnFav
                        btnFav.setImageResource(R.drawable.ic_favorite_white_24dp);
                        Toast.makeText(getBaseContext(), "Removed from favorites list", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(getBaseContext(), "Error", Toast.LENGTH_SHORT).show();
                }
            });
        });

    }

    private void intialStatusFloatingButton(){
        try{
            user.child(mAuth.getCurrentUser().getUid()).child("Favorites").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for(DataSnapshot data: dataSnapshot.getChildren()){
                        Comic comic = data.getValue(Comic.class);
                        if(comic.getImage().equals(SupportClass.comicSelected.getImage())){
                            btnFav.setImageResource(R.drawable.ic_clear_white_24dp);
                            break;
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        } catch (NullPointerException e) {

        }
        btnFav.setImageResource(R.drawable.ic_favorite_white_24dp);
    }

    private void removeComicFromFavorites(){
        user.child(mAuth.getCurrentUser().getUid()).child("Favorites").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot data: dataSnapshot.getChildren()){
                    Comic comic = data.getValue(Comic.class);
                    if(SupportClass.comicSelected.getImage().equals(comic.getImage())){
                        user.child(mAuth.getCurrentUser().getUid()).child("Favorites").child(data.getKey()).removeValue();

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void initView() {
        imgBannerComic = findViewById(R.id.img_comic_chapter_view);
        btnFav = findViewById(R.id.btn_fav);
        recycler_chapter = findViewById(R.id.recycler_chapter);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true); //tao nut thoat tren toolbar
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }

}
