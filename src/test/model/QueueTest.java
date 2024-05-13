package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.*;

public class QueueTest {
    Queue queue;
    Audience audience;
    Artist artist;

    LinkedList<Feedback> feedbacks;
    LinkedList<Song> songRequests;
    LinkedList<Song> songQueue;
    LinkedList<Song> played;

    Song A1;
    Song A2;
    Song A3;
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

        A1 = new Song("A", "Tom Jobim");
        A2 = new Song("A", "The Weeknd");
        A3 = new Song("B", "Tom Jobim");
        B = new Song("B", "The Weeknd");
        C = new Song("C", "Orochi");
        D = new Song("D", "Anitta");

        outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
    }

    @Test
    public void getConcertNameTest() {
        assertEquals("concert", artist.getConcertName());
        assertEquals("concert", audience.getConcertName());
    }

    @Test
    public void setConcertNameTest() {
        queue.setConcertName("A");

        Artist newArtist = new Artist(queue.getSongQueue(), queue.getSongRequests(), queue.getFeedbacks(),
                queue.getConcertName());
        Audience newAudience = new Audience(queue.getSongQueue(), queue.getSongRequests(), queue.getFeedbacks(),
                queue.getConcertName());

        assertEquals("A", queue.getConcertName());
        assertEquals("A", newArtist.getConcertName());
        assertEquals("A", newAudience.getConcertName());
    }

    @Test
    public void getSongQueueTest() {
        songQueue.add(A1);

        audience.addSongRequest(A1, artist.getPlayed());
        artist.acceptSong(A1);

        assertEquals(queue.getSongQueue(), songQueue);
    }

    @Test
    public void getSongRequestsTest() {
        songRequests.add(A1);
        audience.addSongRequest(A1, artist.getPlayed());

        assertEquals(queue.getSongRequests(), songRequests);
        assertEquals(queue.getSongRequests(), artist.getSongRequests());
        assertEquals(queue.getSongRequests(), audience.getSongRequests());
        assertEquals(artist.getSongRequests(), audience.getSongRequests());
    }

    @Test
    public void getFeedbacksTest() {
        audience.addSongRequest(A1, artist.getPlayed());

        assertEquals(queue.getFeedbacks(), audience.getFeedbacks());
        assertEquals(queue.getFeedbacks(), artist.getFeedbacks());
        assertEquals(audience.getFeedbacks(), artist.getFeedbacks());

    }

    @Test
    public void isEmptySongRequestsTest() {
        assertTrue(queue.isEmptySongRequests());
        assertTrue(audience.isEmptySongRequests());
        assertTrue(artist.isEmptySongRequests());

        audience.addSongRequest(A1, artist.getPlayed());

        assertFalse(queue.isEmptySongRequests());
        assertFalse(audience.isEmptySongRequests());
        assertFalse(artist.isEmptySongRequests());

    }

    @Test
    public void viewQueueEmptyTest() {
        queue.viewQueue();

        assertEquals("Cannot view song queue: queue is empty\n", outContent.toString());
    }

    @Test
    public void viewQueueFullTest() {
        audience.addSongRequest(A1, artist.getPlayed());
        audience.addSongRequest(B, artist.getPlayed());
        audience.addSongRequest(C, artist.getPlayed());
        audience.addSongRequest(D, artist.getPlayed());

        artist.acceptSong(A1);
        artist.acceptSong(B);
        artist.acceptSong(C);
        artist.acceptSong(D);

        queue.viewQueue();

        assertEquals("Your song has been added to the requests.\n" +
                        "Your song has been added to the requests.\n" +
                        "Your song has been added to the requests.\n" +
                        "Your song has been added to the requests.\n" +
                        "1. A by Tom Jobim\n" +
                        "2. B by The Weeknd\n" +
                        "3. C by Orochi\n" +
                        "4. D by Anitta\n",
                outContent.toString());
    }

    @Test

    public void viewAllRequestsAllTest() {
        queue.viewAllRequests();

        assertEquals("Cannot view song requests: there are no requests\n", outContent.toString());
    }

    @Test
    public void viewAllRequestsEmptyTest() {
        audience.addSongRequest(A1, artist.getPlayed());
        audience.addSongRequest(B, artist.getPlayed());
        audience.addSongRequest(C, artist.getPlayed());
        audience.addSongRequest(D, artist.getPlayed());

        queue.viewAllRequests();

        assertEquals("Your song has been added to the requests.\n" +
                        "Your song has been added to the requests.\n" +
                        "Your song has been added to the requests.\n" +
                        "Your song has been added to the requests.\n" +
                        "1. A by Tom Jobim\n" +
                        "2. B by The Weeknd\n" +
                        "3. C by Orochi\n" +
                        "4. D by Anitta\n",
                outContent.toString());
    }

    @Test
    public void findSongInFeedbackTestNotFound() {
        queue.findSongInFeedback(A1, "Hello World");

        assertEquals("Song was not found in the feedbacks\n", outContent.toString());
    }

    @Test
    public void findSongInFeedbackTestNotSongEqualsFound() {
        queue.getFeedbacks().add(new Feedback("Pending approval", A1));

        queue.findSongInFeedback(A2, "Hello World");

        assertEquals("Song was not found in the feedbacks\n", outContent.toString());
    }

    @Test
    public void findSongInFeedbackTestNotArtistEqualsFound() {
        queue.getFeedbacks().add(new Feedback("Pending approval", A1));

        queue.findSongInFeedback(A3, "Hello World");

        assertEquals("Song was not found in the feedbacks\n", outContent.toString());
    }

    @Test
    public void findSongInFeedbackTestFound() {
        audience.addSongRequest(A1, artist.getPlayed());
        audience.addSongRequest(B, artist.getPlayed());
        audience.addSongRequest(C, artist.getPlayed());
        audience.addSongRequest(D, artist.getPlayed());

        queue.findSongInFeedback(B, "Hello");
        queue.findSongInFeedback(D, "World");

        System.out.println(queue.getFeedbacks());

        assertEquals(queue.getFeedbacks().get(1).getFeedback(), "Hello");
        assertEquals(queue.getFeedbacks().get(3).getFeedback(), "World");
    }

    @Test
    public void removeSongRequestNotFoundTest() {
        queue.removeSongRequest(A1);

        assertEquals("Song cannot be removed from the requests: song not found\n", outContent.toString());
    }

    @Test
    public void removeSongRequestNotFoundSongEqualTest() {
        audience.addSongRequest(A1, artist.getSongRequests());

        queue.removeSongRequest(A2);

        assertEquals("Your song has been added to the requests.\n" +
                "Song cannot be removed from the requests: song not found\n", outContent.toString());
    }

    @Test
    public void removeSongRequestNotFoundArtistEqualTest() {
        audience.addSongRequest(A1, artist.getSongRequests());

        queue.removeSongRequest(A3);

        assertEquals("Your song has been added to the requests.\n" +
                "Song cannot be removed from the requests: song not found\n", outContent.toString());
    }

    @Test
    public void removeSongRequestFoundTest() {
        audience.addSongRequest(A1, artist.getPlayed());
        audience.addSongRequest(B, artist.getPlayed());
        audience.addSongRequest(C, artist.getPlayed());
        audience.addSongRequest(D, artist.getPlayed());

        queue.removeSongRequest(B);
        queue.removeSongRequest(D);

        assertFalse(queue.getSongRequests().contains(B));
        assertFalse(queue.getSongRequests().contains(D));
    }

    @Test
    public void removeSongQueueNotFoundTest() {
        queue.removeSongQueue(A1);

        assertEquals("Song cannot be removed from the queue: song not found\n", outContent.toString());
    }

    @Test
    public void removeSongQueueNotFoundSongEqualTest() {
        artist.addSongToQueue(A1);

        queue.removeSongQueue(A2);

        assertEquals("Your song has been added to the queue.\n" +
                "Song cannot be removed from the queue: song not found\n", outContent.toString());
    }

    @Test
    public void removeSongQueueNotFoundArtistEqualTest() {
        artist.addSongToQueue(A1);

        queue.removeSongQueue(A3);

        assertEquals("Your song has been added to the queue.\n" +
                "Song cannot be removed from the queue: song not found\n", outContent.toString());
    }

    @Test
    public void removeSongQueueFoundTest() {
        audience.addSongRequest(A1, artist.getPlayed());
        artist.acceptSong(A1);
        queue.removeSongQueue(A1);

        assertFalse(queue.getSongQueue().contains(A1));

        queue.removeSongQueue(A2);
        queue.removeSongQueue(A3);
        assertEquals("Your song has been added to the requests.\n" +
                "Song cannot be removed from the queue: song not found\n" +
                "Song cannot be removed from the queue: song not found\n", outContent.toString());
    }
}
