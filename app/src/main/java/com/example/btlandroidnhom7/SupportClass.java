package com.application.tuanlv.comicapp;

import com.application.tuanlv.comicapp.model.Chapter;
import com.application.tuanlv.comicapp.model.Comic;

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
