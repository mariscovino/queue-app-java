package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.*;

class ArtistTest {
    Queue queue;
    Audience audience;
    Artist artist;

    LinkedList<Feedback> feedbacks;
    LinkedList<Song> songRequests;
    LinkedList<Song> songQueue;
    LinkedList<Song> played;

    Song A;
    Song B;
    Song C;
    Song D;

    ByteArrayOutputStream outContent;

    @BeforeEach
    public void runBefore() {
        queue = new Queue();
        audience = new Audience(queue.getSongQueue(), queue.getSongRequests(), queue.getFeedbacks(),
                "concert");
        artist = new Artist(queue.getSongQueue(), queue.getSongRequests(), queue.getFeedbacks(),
                "concert");

        feedbacks = new LinkedList<>();
        songRequests = new LinkedList<>();
        songQueue = new LinkedList<>();
        played = new LinkedList<>();

        A = new Song("A", "Tom Jobim");
        B = new Song("B", "The Weeknd");
        C = new Song("C", "Orochi");
        D = new Song("D", "Anitta");

        outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
    }

    @Test
    public void getPlayedTest() {
        artist.playSong(A);
        artist.playSong(B);

        played.addLast(A);
        played.addLast(B);

        assertEquals(played, artist.getPlayed());
    }

    @Test
    public void addSongToQueueTest() {
        artist.addSongToQueue(A);

        songQueue.addLast(A);

        assertEquals(songQueue, artist.getSongQueue());
        assertEquals(artist.getSongQueue().get(0), A);
        assertEquals("Your song has been added to the queue.\n", outContent.toString());
    }

    @Test
    public void viewPlayedEmptyTest() {
        artist.viewPlayed();

        assertEquals("You have no played songs.\n", outContent.toString());
    }


    @Test
    public void viewPlayedFullTest() {
        artist.addSongToQueue(A);
        artist.addSongToQueue(B);
        artist.addSongToQueue(C);
        artist.addSongToQueue(D);

        artist.playSong(A);
        artist.playSong(B);
        artist.playSong(C);
        artist.playSong(D);

        artist.viewPlayed();

        assertEquals("Your song has been added to the queue.\n" +
                        "Your song has been added to the queue.\n" +
                        "Your song has been added to the queue.\n" +
                        "Your song has been added to the queue.\n" +
                        "1. A by Tom Jobim\n" +
                        "2. B by The Weeknd\n" +
                        "3. C by Orochi\n" +
                        "4. D by Anitta\n",
                outContent.toString());
    }

    @Test
    public void acceptSongTest() {
        audience.addSongRequest(A, artist.getPlayed());
        audience.addSongRequest(B, artist.getPlayed());
        audience.addSongRequest(C, artist.getPlayed());
        audience.addSongRequest(D, artist.getPlayed());

        artist.acceptSong(B);
        assertEquals("Thank you for your request! Your Song has been added to the queue!",
                queue.getFeedbacks().get(1).getFeedback());
        assertTrue(queue.getSongQueue().contains(B));
        assertFalse(queue.getSongRequests().contains(B));
    }

    @Test
    public void denySongTest() {
        audience.addSongRequest(A, artist.getPlayed());
        audience.addSongRequest(B, artist.getPlayed());
        audience.addSongRequest(C, artist.getPlayed());
        audience.addSongRequest(D, artist.getPlayed());

        artist.denySong(D, "sorry, bad song");
        assertEquals("sorry, bad song",
                queue.getFeedbacks().get(3).getFeedback());
        assertFalse(queue.getSongQueue().contains(D));
        assertFalse(queue.getSongRequests().contains(D));
    }

    @Test
    public void playSongTest() {
        artist.addSongToQueue(A);
        artist.addSongToQueue(B);
        artist.addSongToQueue(C);
        artist.addSongToQueue(D);

        artist.playSong(C);

        assertFalse(artist.getSongQueue().contains(C));
        assertTrue(artist.getPlayed().contains(C));
    }
}
