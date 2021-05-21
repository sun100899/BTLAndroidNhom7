package com.example.btlandroidnhom7.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.btlandroidnhom7.R;
import com.example.btlandroidnhom7.SupportClass;
import com.example.btlandroidnhom7.View.ChaptersActivity;
import com.example.btlandroidnhom7.View.ViewComicActivity;
import com.example.btlandroidnhom7.interfaces.IRecyclerItemCLickListener;
import com.example.btlandroidnhom7.model.Chapter;

import java.util.ArrayList;

public class MyChapterAdapter extends RecyclerView.Adapter<MyChapterAdapter.MyViewHolder>{

    Context context;
    LayoutInflater inflater;
    ArrayList<Chapter> chapters;

    public MyChapterAdapter(Context context, ArrayList<Chapter> chapters) {
        this.context = context;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.chapters = chapters;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = inflater.inflate(R.layout.chapter_item, viewGroup, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);

        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        myViewHolder.tvChapterName.setText(chapters.get(i).getName());

        //Event
        myViewHolder.setRecyclerItemCLickListener((view1, position) -> {
            SupportClass.chapterSelected = chapters.get(position);
            Intent intent = new Intent(context, ViewComicActivity.class);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return chapters.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvChapterName;
        IRecyclerItemCLickListener recyclerItemCLickListener;

        public void setRecyclerItemCLickListener(IRecyclerItemCLickListener recyclerItemCLickListener) {
            this.recyclerItemCLickListener = recyclerItemCLickListener;
        }

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvChapterName = itemView.findViewById(R.id.tv_chapter_name_item);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            recyclerItemCLickListener.onClick(view, getAdapterPosition());
        }
    }
}
