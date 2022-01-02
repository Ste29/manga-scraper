package com.github.ste29;

public class Chapter {

    public final String url;
    public final String name;
    public final int number;

    public Chapter(String url, String name, int number) {
        this.url = url;
        this.name = name;
        this.number = number;
    }

    public String getChapUrl(){
        return name.replace(": ", " ").replace(":", " ").replace(" ", "_");
    }

}