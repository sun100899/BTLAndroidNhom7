package com.example.btlandroidnhom7.model;

import java.util.ArrayList;

public class Comic {
    private String Name;
    private String Image;
    private ArrayList<Chapter> Chapters;
    private ArrayList<String> Categories;
    private boolean Like;

    public Comic() {
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public ArrayList<Chapter> getChapters() {
        return Chapters;
    }

    public void setChapters(ArrayList<Chapter> chapters) {
        Chapters = chapters;
    }

    public ArrayList<String> getCategories() {
        return Categories;
    }

    public void setCategories(ArrayList<String> categories) {
        Categories = categories;
    }

    public boolean isLike() {
        return Like;
    }

    public void setLike(boolean like) {
        Like = like;
    }
}
