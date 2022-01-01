package com.github.ste29;

import com.github.ste29.scrapers.MangaEdenScraper;
import java.nio.file.Path;

// Todo fare in modo che ogni enum instanzi la sua relativa classe
public enum OriginSite {

    mangaeden {
        @Override
        public MangaScraper createScraper(String mangaUrlStr, Path downloadFld) {
            return new MangaEdenScraper(mangaUrlStr, downloadFld);
        }
    },
    mangafreak {
        @Override
        public MangaScraper createScraper(String mangaUrlStr, Path downloadFld)  {
            System.out.println("To be implemented");
            return new MangaEdenScraper(mangaUrlStr, downloadFld);
        }
    };

    public abstract MangaScraper createScraper(String mangaUrlStr, Path downloadFld);
}
