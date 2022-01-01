package com.github.ste29;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.ListIterator;
import java.util.stream.Collectors;

public abstract class MangaScraper {

    public static final int MIN_PAGES=150;
    public List<Chapter> fullChapters;
    public List<Chapter> chapters;
    public Path downloadFld;
    public int counter;

    public MangaScraper(Path downloadFld) {
        this.downloadFld = downloadFld;
        this.chapters = null;
        this.fullChapters = null;
        this.counter = 0;
        //this.MIN_PAGES = 150;
    }

    public void rangeChapter(int r1, int r2) {
        chapters = fullChapters.stream().filter(chapter -> chapter.number >= r1 && chapter.number <= r2)
            .collect(Collectors.toList());
    }

    public void startDownload(){
        ListIterator<Chapter> it = chapters.listIterator(chapters.size());
        while(it.hasPrevious()) {
            crawl(it.previous());
        }

        System.out.println("Chapters downloaded! Enjoy the reading! :)");
    }

    public abstract List<Chapter> getChapters(String url);

    public abstract void crawl(Chapter chapter);

    public abstract void downloadImage(String url, String number, Path downloadDir);
}
