package com.example.btlandroidnhom7;



import com.example.btlandroidnhom7.model.Chapter;
import com.example.btlandroidnhom7.model.Comic;

import java.util.ArrayList;

public class SupportClass {
    public static Comic comicSelected;
    public static Chapter chapterSelected;

    public static ArrayList<Comic> actionList = new ArrayList<>();
    public static ArrayList<Comic> comedyList = new ArrayList<>();
    public static ArrayList<Comic> adventureList = new ArrayList<>();
    public static ArrayList<Comic> sportsList = new ArrayList<>();
    public static ArrayList<Comic> allListComic = new ArrayList<>();

    public static boolean isOnline;
}
