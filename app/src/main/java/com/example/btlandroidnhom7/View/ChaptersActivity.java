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
import com.example.btlandroidnhom7.service.PicassoLoadingService;
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

    }

    private void initView() {
        imgBannerComic = findViewById(R.id.img_comic_chapter_view);
//        btnFav = findViewById(R.id.btn_fav);
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
