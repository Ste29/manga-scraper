package com.github.ste29;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class ChapterTest {
    // todo aggiornare test
    @Test
    void getNumber() throws IOException {
        Chapter chapter = new Chapter("https://www.mangaeden.com/it/it-manga/berserk/331/1/", "331", 1);
        assertEquals(331, chapter.number);
    }

}
