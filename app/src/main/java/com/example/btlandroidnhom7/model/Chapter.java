package com.example.btlandroidnhom7.model;

import java.util.ArrayList;

public class Chapter {

    private String Name;
    private ArrayList<String> Links;

    public Chapter() {
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public ArrayList<String> getLinks() {
        return Links;
    }

    public void setLinks(ArrayList<String> links) {
        Links = links;
    }
}
