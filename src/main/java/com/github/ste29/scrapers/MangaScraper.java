package com.github.ste29.scrapers;

import com.github.ste29.Chapter;
import org.zeroturnaround.zip.ZipUtil;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.stream.Collectors;

public abstract class MangaScraper {

    public static final int MIN_PAGES=150;
    public List<Chapter> fullChapters;
    public List<Chapter> chapters;
    public Path downloadFld;
    public int counter;
    public String mangaUrlStr;
    public List<Integer> chaptersFolder = new ArrayList<Integer>();
    public Path downloadDir;

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

    public void zipper(){
        String title = mangaUrlStr.split("/")[mangaUrlStr.split("/").length-1];
        String numbers;
        if (chaptersFolder.get(0) < chaptersFolder.get(chaptersFolder.size()-1)) {
            numbers = chaptersFolder.get(0).toString() + " - " +
                    chaptersFolder.get(chaptersFolder.size() - 1).toString();
        } else {
            numbers = chaptersFolder.get(chaptersFolder.size() - 1).toString() + " - " +
                    chaptersFolder.get(0).toString();
        }
        File newDir = new File(downloadDir.getParent() + "\\" +title+" - "+numbers);
        downloadDir.toFile().renameTo(newDir);
        ZipUtil.pack(newDir, new File(newDir.toString() + ".zip"));
    }

    public abstract List<Chapter> getChapters(String url);

    public abstract void crawl(Chapter chapter);

    public abstract void downloadImage(String url, String number, Path downloadDir);

    public abstract int setNumber(String name);
}
