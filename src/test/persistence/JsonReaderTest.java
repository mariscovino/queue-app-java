package persistence;

import model.Artist;
import model.Audience;
import model.Queue;

import org.junit.jupiter.api.Test;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JsonReaderTest {
    @Test
    void testReaderNonExistentFileQueue() {
        JsonReader readerQueue= new JsonReader("./data/noSuchFile.json");
        JsonReader readerArtist = new JsonReader("./data/noSuchFile.json");

        try {
            Queue queue = readerQueue.readQueue();
            Artist artist = readerArtist.readArtist(queue);

            fail("IOException expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testReaderNonExistentFileArtist() {
        JsonReader readerQueue= new JsonReader("./data/queue/testReaderEmptyQueue");
        JsonReader readerArtist = new JsonReader("./data/noSuchFile.json");

        try {
            Queue queue = readerQueue.readQueue();
            Artist artist = readerArtist.readArtist(queue);

            fail("IOException expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testReaderEmptyWorkRoom() {
        JsonReader readerQueue= new JsonReader("./data/queue/testReaderEmptyQueue");
        JsonReader readerArtist = new JsonReader("./data/artist/testReaderEmptyArtist");

        try {
            Queue queue = readerQueue.readQueue();
            Artist artist = readerArtist.readArtist(queue);

            assertEquals("", queue.getConcertName());
            assertEquals("", artist.getConcertName());

            assertTrue(queue.getSongQueue().isEmpty());
            assertTrue(queue.getSongRequests().isEmpty());
            assertTrue(queue.getFeedbacks().isEmpty());

            assertTrue(artist.getSongQueue().isEmpty());
            assertTrue(artist.getSongRequests().isEmpty());
            assertTrue(artist.getFeedbacks().isEmpty());

        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }

    @Test
    void testReaderGeneral() {
        JsonReader readerQueue = new JsonReader("./data/queue/testReaderGeneralQueue");
        JsonReader readerArtist = new JsonReader("./data/artist/testReaderGeneralArtist");

        try {
            Queue queue = readerQueue.readQueue();
            Artist artist = readerArtist.readArtist(queue);
            Audience audience = readerArtist.readAudience(queue);

            assertEquals("testWriterGeneral", queue.getConcertName());
            assertEquals("testWriterGeneral", artist.getConcertName());
            assertEquals("testWriterGeneral", audience.getConcertName());

            assertEquals(1, queue.getSongQueue().size());
            assertEquals(1, artist.getSongQueue().size());
            assertEquals(1, audience.getSongQueue().size());

            assertEquals(1, queue.getSongRequests().size());
            assertEquals(1, artist.getSongRequests().size());
            assertEquals(1, audience.getSongRequests().size());


            assertEquals(3, queue.getFeedbacks().size());
            assertEquals(3, artist.getFeedbacks().size());
            assertEquals(3, audience.getFeedbacks().size());

            assertEquals("A", queue.getSongQueue().get(0).getName());
            assertEquals("Tom Jobim", queue.getSongQueue().get(0).getArtist());

            assertEquals("C", queue.getSongRequests().get(0).getName());
            assertEquals("Orochi", queue.getSongRequests().get(0).getArtist());

            assertEquals("Thank you for your request! Your Song has been added to the queue!",
                    queue.getFeedbacks().get(0).getFeedback());
            assertEquals("A", queue.getFeedbacks().get(0).getSong().getName());
            assertEquals("Tom Jobim", queue.getFeedbacks().get(0).getSong().getArtist());


            assertEquals("Sorry, bad song.", queue.getFeedbacks().get(1).getFeedback());
            assertEquals("B", queue.getFeedbacks().get(1).getSong().getName());
            assertEquals("The Weeknd", queue.getFeedbacks().get(1).getSong().getArtist());

            assertEquals("Pending approval", queue.getFeedbacks().get(2).getFeedback());
            assertEquals("C", queue.getFeedbacks().get(2).getSong().getName());
            assertEquals("Orochi", queue.getFeedbacks().get(2).getSong().getArtist());

            assertEquals("D", artist.getPlayed().get(0).getName());
            assertEquals("Anitta", artist.getPlayed().get(0).getArtist());

        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }
}
