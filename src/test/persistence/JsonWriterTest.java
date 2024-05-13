package persistence;
import model.Queue;
import model.Artist;
import model.Audience;
import model.Song;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JsonWriterTest {
    @Test
    void testWriterInvalidFile() {
        try {
            Queue queue = new Queue();
            Artist artist = new Artist(queue.getSongQueue(),
                    queue.getSongRequests(),
                    queue.getFeedbacks(), queue.getConcertName());

            JsonWriter writerQueue = new JsonWriter("./data/my\0illegal:fileName.json");
            JsonWriter writerAudience = new JsonWriter("./data/my\0illegal:fileName.json");

            writerQueue.open();
            writerAudience.open();
            fail("IOException was expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testWriterEmpty() {
        try {
            Queue queue = new Queue();

            Artist artist = new Artist(queue.getSongQueue(),
                    queue.getSongRequests(),
                    queue.getFeedbacks(), queue.getConcertName());

            JsonWriter writerQueue = new JsonWriter("./data/queue/testWriterEmptyQueue");
            JsonWriter writerArtist = new JsonWriter("./data/artist/testWriterEmptyArtist");

            writerQueue.open();
            writerArtist.open();

            writerQueue.writeQueue(queue);
            writerArtist.writeArtist(artist);

            writerQueue.close();
            writerArtist.close();

            JsonReader readerQueue = new JsonReader("./data/queue/testWriterEmptyQueue");
            JsonReader readerArtist = new JsonReader("./data/artist/testWriterEmptyArtist");

            queue = readerQueue.readQueue();
            artist = readerArtist.readArtist(queue);

            assertEquals("", queue.getConcertName());
            assertEquals("", artist.getConcertName());

            assertTrue(queue.getSongQueue().isEmpty());
            assertTrue(queue.getSongRequests().isEmpty());
            assertTrue(queue.getFeedbacks().isEmpty());

            assertTrue(artist.getSongQueue().isEmpty());
            assertTrue(artist.getSongRequests().isEmpty());
            assertTrue(artist.getFeedbacks().isEmpty());

        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }

    @Test
    void testWriterGeneral() {
        try {
            Queue queue = new Queue();

            Artist artist = new Artist(queue.getSongQueue(),
                    queue.getSongRequests(),
                    queue.getFeedbacks(), queue.getConcertName());

            Audience audience = new Audience(queue.getSongQueue(),
                    queue.getSongRequests(),
                    queue.getFeedbacks(), queue.getConcertName());

            Song A = new Song("A", "Tom Jobim");
            Song B = new Song("B", "The Weeknd");
            Song C = new Song("C", "Orochi");
            Song D = new Song("D", "Anitta");

            queue.setConcertName("testWriterGeneral");

            audience.addSongRequest(A, artist.getPlayed());
            audience.addSongRequest(B, artist.getPlayed());
            audience.addSongRequest(C, artist.getPlayed());

            artist.acceptSong(A);
            artist.denySong(B, "Sorry, bad song.");
            artist.playSong(D);

            JsonWriter writerQueue = new JsonWriter("./data/queue/testWriterGeneralQueue");
            JsonWriter writerArtist = new JsonWriter("./data/artist/testWriterGeneralArtist");

            writerQueue.open();
            writerArtist.open();

            writerQueue.writeQueue(queue);
            writerArtist.writeArtist(artist);

            writerQueue.close();
            writerArtist.close();

            JsonReader readerQueue = new JsonReader("./data/queue/testWriterGeneralQueue");
            JsonReader readerArtist = new JsonReader("./data/artist/testWriterGeneralArtist");

            queue = readerQueue.readQueue();
            artist = readerArtist.readArtist(queue);

            assertEquals("testWriterGeneral", queue.getConcertName());
            assertEquals("testWriterGeneral", artist.getConcertName());

            assertEquals(1, queue.getSongQueue().size());
            assertEquals(1, artist.getSongQueue().size());

            assertEquals(1, queue.getSongRequests().size());
            assertEquals(1, artist.getSongRequests().size());


            assertEquals(3, queue.getFeedbacks().size());
            assertEquals(3, artist.getFeedbacks().size());

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
