# manga-scraper

Script to scrape popular manga sites and extract zip files ready to be ingested in calibre for ereader.
In order to use the script run main file with following flags:
```
-d,--download <arg>   download directory, where the files will be downloaded
-o,--origin <arg>     required field, manga site used for download, available:
                            * mangaeden
                            * mangafreak
-r1,--range1 <arg>    integer number chapter from which starting download
-r2,--range2 <arg>    integer number chapter where download will be
stopped
-u,--url <arg>        required field, manga url
```

The script group chapters in a single file up to 150 pages togheters in order to avoid too many small files.