package com.example.btlandroidnhom7.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.btlandroidnhom7.model.Comic;

import java.util.ArrayList;
import com.example.btlandroidnhom7.R;
import com.example.btlandroidnhom7.service.PicassoLoadingService;

public class MyComicAdapter extends RecyclerView.Adapter<MyComicAdapter.MyViewHolder> {

    Context context;
    LayoutInflater inflater;
    ArrayList<Comic> comics;

    public MyComicAdapter(Context context, ArrayList<Comic> comics) {
        this.context = context;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.comics = comics;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.comic_item, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int position) {
        PicassoLoadingService picasso = new PicassoLoadingService();
        picasso.loadImage(comics.get(position).getImage(), myViewHolder.imgComic);
        myViewHolder.tvComicName.setText(comics.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return comics.size();
    }

    public class MyViewHolder extends  RecyclerView.ViewHolder {

        ImageView imgComic;
        TextView tvComicName;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imgComic = itemView.findViewById(R.id.img_comic);
            tvComicName = itemView.findViewById(R.id.tv_comic_name);
        }
    }

}
