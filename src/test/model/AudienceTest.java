package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.*;

class AudienceTest {
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
        artist = new Artist(queue.getSongQueue(), queue.getSongRequests(), queue.getFeedbacks(), "concert");

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
    public void addSongRequestOkayTest() {
        audience.addSongRequest(A1, artist.getPlayed());

        assertEquals("Your song has been added to the requests.\n", outContent.toString());
        assertEquals(queue.getFeedbacks().get(0).getFeedback(), "Pending approval");
        assertEquals(queue.getFeedbacks().get(0).getSong(), A1);
    }

    @Test
    public void addSongRequestOkayNotEmptyTwoTest() {
        artist.addSongToQueue(B);
        artist.addSongToQueue(C);

        artist.playSong(C);

        audience.addSongRequest(A1, artist.getPlayed());

        assertEquals("Your song has been added to the queue.\n" +
                "Your song has been added to the queue.\n" +
                "Your song has been added to the requests.\n", outContent.toString());
        assertEquals(queue.getFeedbacks().get(0).getFeedback(), "Pending approval");
        assertEquals(queue.getFeedbacks().get(0).getSong(), A1);
    }

    @Test
    public void addSongRequestOkayNotEmptyOneTest() {
        artist.addSongToQueue(B);
        artist.addSongToQueue(C);

        audience.addSongRequest(A1, artist.getPlayed());

        assertEquals("Your song has been added to the queue.\n" +
                "Your song has been added to the queue.\n" +
                "Your song has been added to the requests.\n", outContent.toString());
        assertEquals(queue.getFeedbacks().get(0).getFeedback(), "Pending approval");
        assertEquals(queue.getFeedbacks().get(0).getSong(), A1);
    }

    @Test
    public void addSongRequestRequestedTest() {
        audience.addSongRequest(A1, artist.getPlayed());
        audience.addSongRequest(A1, artist.getPlayed());

        assertEquals("Your song has been added to the requests.\n" +
                "Sorry. This song was already requested.\n", outContent.toString());
    }

    @Test
    public void addSongRequestPlayedTest() {
        artist.addSongToQueue(A1);
        artist.playSong(A1);
        audience.addSongRequest(A1, artist.getPlayed());

        assertEquals("Your song has been added to the queue.\n" +
                "Sorry. This song was already played.\n", outContent.toString());
    }

    @Test
    public void addSongRequestInQueueTest() {
        artist.addSongToQueue(A1);
        audience.addSongRequest(A1, artist.getPlayed());

        assertEquals("Your song has been added to the queue.\n" +
                "Sorry. This song is already in the queue.\n", outContent.toString());
    }

    @Test
    public void foundInListNullTest() {
        assertFalse(audience.foundInList(null, A1));
    }

    @Test
    public void foundInListNotFoundTest() {
        played.add(B);

        assertFalse(audience.foundInList(songQueue, A1));
    }

    @Test
    public void foundInListNotFoundSongEqualTest() {
        songQueue.add(A1);

        assertFalse(audience.foundInList(songQueue, A2));
    }

    @Test
    public void foundInListNotFoundArtistEqualTest() {
        songQueue.add(A1);

        assertFalse(audience.foundInList(songQueue, A3));
    }

    @Test
    public void foundInListFoundTest() {
        songRequests.add(A1);

        assertTrue(audience.foundInList(songRequests, A1));
    }

    @Test
    public void receiveFeedbackEmptyTest() {
        audience.receiveFeedback();

        assertEquals("Cannot view feedbacks: you have no feedbacks.\n", outContent.toString());
    }

    @Test
    public void receiveFeedbackFullTest() {
        audience.addSongRequest(A1, artist.getPlayed());
        audience.addSongRequest(B, artist.getPlayed());
        audience.addSongRequest(C, artist.getPlayed());
        audience.addSongRequest(D, artist.getPlayed());

        artist.acceptSong(B);
        artist.denySong(D, "Sorry, bad song");

        audience.receiveFeedback();

        assertEquals("Your song has been added to the requests.\n" +
                        "Your song has been added to the requests.\n" +
                        "Your song has been added to the requests.\n" +
                        "Your song has been added to the requests.\n" +
                        "Feedback for A by Tom Jobim: Pending approval\n" +
                        "Feedback for B by The Weeknd: " +
                        "Thank you for your request! Your Song has been added to the queue!\n" +
                        "Feedback for C by Orochi: Pending approval\n" +
                        "Feedback for D by Anitta: Sorry, bad song\n",
                outContent.toString());
    }
}
