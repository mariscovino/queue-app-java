package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SongTest {
    Song song;

    @BeforeEach
    public void runBefore() {
        song = new Song("A A", "B B");
    }

    @Test
    public void getNameTest() {
        assertEquals(song.getName(), "A A");
    }

    @Test
    public void getArtistTest() {
        assertEquals(song.getArtist(), "B B");
    }
}