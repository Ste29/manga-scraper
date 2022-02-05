package com.github.ste29.scrapers;

import com.github.ste29.Chapter;
import com.github.ste29.Utils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.zeroturnaround.zip.ZipUtil;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MangaEdenScraper extends MangaScraper {

    MangaEdenScraper(String mangaUrlStr, Path downloadFld){
        super(downloadFld);
        this.mangaUrlStr = mangaUrlStr;
        System.out.println("Getting chapters");
        fullChapters = getChapters(mangaUrlStr);

        System.out.println("Got " + fullChapters.size());
    }

    @Override
    public List<Chapter> getChapters(String url) {
        Document document;
        try {
            document = Jsoup.connect(url).get();
        } catch(IOException e) {
            throw new RuntimeException(e);
        }
        return document
                .select("#leftContent > table > tbody > tr > td:first-child > a").stream()
                // .filter(elem -> !elem.select("b").text().contains(".")) // Ignore weird half-chapters TODO Make option
                .map(elem -> new Chapter(elem.absUrl("href"), elem.select("b").text(),
                        setNumber(elem.select("b").text())))
                .collect(Collectors.toList());
    }

    @Override
    public void crawl(Chapter chapter) {
        System.out.println("Downloading chapter '" + chapter.name + "' (" + chapter.url + ")");

        if (counter==0 || counter > MIN_PAGES) {
            downloadDir = downloadFld.resolve(chapter.getChapUrl());
            counter = 0;
        }

        Set<String> existing;
        if(!Files.isDirectory(downloadDir)) {
            try {
                Files.createDirectory(downloadDir);
            } catch(IOException e) {
                e.printStackTrace();
                return;
            }
            existing = Collections.emptySet();
        } else {
            Stream<Path> stream;
            try {
                stream = Files.walk(downloadDir, 1);
            } catch(IOException e) {
                throw new RuntimeException(e);
            }
            existing = stream
                    .filter(path -> !path.equals(downloadDir))
                    .map(path -> Utils.stripExtenstion(path.getFileName().toString()))
                    .collect(Collectors.toSet());
        }

        Document document;
        try {
            document = Jsoup.connect(chapter.url).get();
        } catch(IOException e) {
            // TODO Retry?
            throw new RuntimeException(e);
        }
        // prendi id=top-in, div class top-title, prendi il 5 elemento e guarda option(?)
        Elements pages = document.select("#top-in > div.top-title > select:nth-child(5) > option");
        counter += pages.size();
        pages
                //.parallelStream()
                .stream()
                .filter(option -> !existing.contains(option.text()))
                .forEach(option ->
                        downloadImage(option.absUrl("value"), option.text(), downloadDir)
                );
        chaptersFolder.add(chapter.number);
        if (counter >= MIN_PAGES) {
            zipper();
        }
    }

    @Override
    public void downloadImage(String url, String number, Path downloadDir) {
        System.out.println("Downloading " + url);
        Document document;
        try {
            document = Jsoup.connect(url).get();
        } catch(IOException e) {
            // TODO Retry?
            throw new RuntimeException(e);
        }

        String imgSrc = document.select("#mainImg").first().absUrl("src");

        URL imgUrl;
        try {
            imgUrl = new URL(imgSrc);
        } catch(MalformedURLException e) {
            e.printStackTrace();
            return;
        }
        String ext = Utils.last(imgSrc.split("\\."), 1);
        while (number.length() < 4) {
            number = "0" + number;
        }
        String prefix = url.split("/")[url.split("/").length-2];
        while (prefix.length() < 4) {
            prefix = "0" + prefix;
        }

        Path imgPath = downloadDir.resolve(Paths.get(prefix + "_" + number + '.' + ext));

        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(imgSrc))
                    .header("user-agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/535.21 (KHTML, " +
                            "like Gecko) Chrome/19.0.1042.0 Safari/535.21")
                    .build();

            try {
                HttpResponse<Path> prova = client
                        .send(request, HttpResponse.BodyHandlers.ofFile(Paths.get(imgPath.toString())));
                while (prova.statusCode() != 200) {
                    prova = client.send(request, HttpResponse.BodyHandlers.ofFile(Paths.get(imgPath.toString())));
                }
                // System.out.println(prova.toString());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int setNumber(String name) {
        String tmpName = name + " ";
        String[] n = tmpName.split(""); //array of strings
        StringBuffer f = new StringBuffer(); // buffer to store numbers

        for (int i = 0; i < n.length; i++) {
            if((n[i].matches("[0-9]+"))) {// validating numbers
                f.append(n[i]); //appending
            }else {
                //parsing to int and returning value
                return Integer.parseInt(f.toString());
            }
        }
        return 0;
    }

}
