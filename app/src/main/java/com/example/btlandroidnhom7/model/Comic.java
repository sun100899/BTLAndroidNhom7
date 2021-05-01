package com.example.btlandroidnhom7.model;

import java.util.ArrayList;

public class Comic {

    private String Name;
    private String Image;
    private ArrayList<Chapter> Chapter;
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

    public ArrayList<com.example.btlandroidnhom7.model.Chapter> getChapter() {
        return Chapter;
    }

    public void setChapter(ArrayList<com.example.btlandroidnhom7.model.Chapter> chapter) {
        Chapter = chapter;
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
