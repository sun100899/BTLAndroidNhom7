package com.example.btlandroidnhom7.View;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.btlandroidnhom7.R;
import com.example.btlandroidnhom7.adapter.MyComicAdapter;
import com.example.btlandroidnhom7.model.Comic;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.ArrayList;

import static com.example.btlandroidnhom7.SupportClass.allListComic;


public class SearchActivity extends AppCompatActivity {
    private String newText;
    private Toolbar toolbar_search;
    private MaterialSearchView materialSearchView;
    private RecyclerView recycler_search;
    private TextView tvNotFound;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_comic_activity);
        initView();
        eventSearchClicked();
    }

    private void initView() {
        toolbar_search = findViewById(R.id.toolbar_search);
        materialSearchView = findViewById(R.id.material_search);
        recycler_search = findViewById(R.id.recycler_search);
        tvNotFound = findViewById(R.id.tv_not_found);

        setSupportActionBar(toolbar_search);
        getSupportActionBar().setTitle("Search");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_item, menu);
        MenuItem menuItem = menu.findItem(R.id.search_home);
        materialSearchView.setMenuItem(menuItem);
        return true;
    }




    private void eventSearchClicked() {

        materialSearchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                //Do some magic
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //Do some magic
                newText = newText.toLowerCase().trim();
                if(newText != null && !newText.isEmpty()) {
                    ArrayList<Comic> lstFound = new ArrayList<>();
                    int i = 0;
                    for(Comic item: allListComic) {
                        if(allListComic.get(i).getName().toLowerCase().contains(newText)) {
                            lstFound.add(item);
                        }
                        i++;
                    }
                    showByRecyclerView(lstFound);
                }

                return false;
            }
        });
    }

    private void showByRecyclerView(ArrayList<Comic> lstFound) {
        if(lstFound == null) {
            tvNotFound.setVisibility(View.VISIBLE);
            recycler_search.setVisibility(View.INVISIBLE);
        } else {
            tvNotFound.setVisibility(View.INVISIBLE);
            recycler_search.setVisibility(View.VISIBLE);

            MyComicAdapter myComicAdapter = new MyComicAdapter(this, lstFound);
            RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 3);
            recycler_search.setHasFixedSize(true);
            recycler_search.setLayoutManager(layoutManager);
            recycler_search.setAdapter(myComicAdapter);
        }

    }
}
