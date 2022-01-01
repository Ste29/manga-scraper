package com.github.ste29;

import org.apache.commons.cli.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import com.github.ste29.OriginSite;


public class Main {
    // TODO UI, funzionalità ricerca, funzionalità selezione range di capitoli da scaricare (da x a y, da x
    //  alla fine del manga, fino ad y), scraper per altri siti tipo mangafreak (che dovrebbe avere apposite api)
    private static final Logger LOG = LoggerFactory.getLogger(Main.class);

    public static void main (String[] args) {
        // Define Parse args
        Options argsOptions = new Options();

        Option urlMangaConfig = new Option("u", "url", true, "manga url");
        urlMangaConfig.setRequired(true);
        argsOptions.addOption(urlMangaConfig);
        Option originConfig = new Option("o", "origin", true,
                "manga site used for download, available: \n* mangaeden");
        originConfig.setRequired(true);
        argsOptions.addOption(originConfig);
        Option downloadConfig = new Option("d", "download", true, "download directory");
        downloadConfig.setRequired(false);
        argsOptions.addOption(downloadConfig);
        Option startConfig = new Option("r1", "range1", true, "integer number chapter " +
                "from which starting download");
        startConfig.setRequired(false);
        argsOptions.addOption(startConfig);
        Option endConfig = new Option("r2", "range2", true, "integer number chapter" +
                " where download will be stopped");
        endConfig.setRequired(false);
        argsOptions.addOption(endConfig);

        CommandLineParser argsParser = new DefaultParser();
        CommandLine argsParsed = null;
        HelpFormatter helper = new HelpFormatter();

        // Parse argument, if something is missing log errors and print helper
        try {
            argsParsed = argsParser.parse(argsOptions, args);
        } catch (ParseException e) {
            LOG.error("Error parsing input parameters", e);
            helper.printHelp("Usage:", argsOptions);
            System.exit(1);
        }

        // Creating variables from parsed args
        String urlManga = argsParsed.getOptionValue("url");

        Path download;
        try {
            download = Paths.get(argsParsed.getOptionValue("download"));
        } catch (NullPointerException e) {
            download = Paths.get("C:\\Users\\Stefano\\Downloads");
        }

        int r1, r2;
        try {
            r1 = Integer.parseInt(argsParsed.getOptionValue("r1"));
        } catch (NumberFormatException e) {
            r1 = 0;
        }
        try {
            r2 = Integer.parseInt(argsParsed.getOptionValue("r2"));
        } catch (NumberFormatException e) {
            r2 = 9999;
        }
        if (r1 > r2){
            LOG.error("Error lower chapter (r1) limit greater than upper limit (r2), r1: " + r1 + " r2: " + r2);
            helper.printHelp("Usage:", argsOptions);
            System.exit(1);
        }

        MangaScraper scraper = null;
        try {
            scraper = OriginSite.valueOf(argsParsed.getOptionValue("origin")).createScraper(urlManga, download);
            System.out.println(scraper.toString());
        } catch (IllegalArgumentException e) {
            LOG.error("Error parsing input parameters", e);
            helper.printHelp("Usage:", argsOptions);
            System.exit(1);
        }

        // Filter chapters
        scraper.rangeChapter(r1, r2);
        scraper.startDownload();

        System.out.println("ciao");
    }
}
