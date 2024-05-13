package ui.console;

import model.*;
import persistence.JsonReader;
import persistence.JsonWriter;
import ui.LogPrinter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

public class Main {
    protected static Queue queue;
    protected static Audience audience;
    protected static Artist artist;

    protected static Scanner scanner = new Scanner(System.in);
    protected static JsonReader jsonReaderQueue;
    protected static JsonReader jsonReaderAudience;
    protected static JsonReader jsonReaderArtist;

    protected static JsonWriter jsonWriterQueue;
    protected static JsonWriter jsonWriterArtist;

    protected static Boolean exit = false;
    protected static Boolean valid = false;
    private static final String JSON_STORE_QUEUE = "./data/queue/";
    private static final String JSON_STORE_ARTIST = "./data/artist/";
    protected static String concertName;

    // EFFECTS: first/main window of Artist Performance Queue application
    //          user enters concertName
    //          user selects if they wish to load from existing file
    //          user selects the desired page (Artist or Audience) or Exit
    public static void main(String[] args) {
        EventLog.getInstance().clear();

        enterConcertName();

        while (!exit) {
            System.out.println("Press number to select view: ");
            System.out.println("1. Artist");
            System.out.println("2. Audience");
            System.out.println("3. Exit");

            String input = scanner.nextLine();

            switch (input) {
                case "1":
                    ArtistPages.artistMainPage();
                    break;
                case "2":
                    AudiencePages.audienceMainPage();
                    break;
                case "3":
                    exit = true;
                    LogPrinter lp = new LogPrinter();
                    lp.printLog(EventLog.getInstance());
                    break;
            }
        }
    }

    // EFFECTS: processes user input of song
    public static Song enterSong() {
        System.out.println("Provide song name: ");
        String name = scanner.nextLine();

        System.out.println("Provide song artist: ");
        String a = scanner.nextLine();

        return new Song(name, a);
    }

    // EFFECTS: processes user input of concertName
    //          if user presses 1, a new concert is created
    //          if user presses 2, data is loaded from existing file
    private static void enterConcertName() {
        System.out.println("Enter concert name: ");
        concertName = scanner.nextLine();

        while (!valid) {
            System.out.println("Press number to select action: ");
            System.out.println("1. Create a new concert");
            System.out.println("2. Load existing concert");

            String input = scanner.nextLine();

            switch (input) {
                case "1":
                    queue = new Queue();
                    queue.setConcertName(concertName);

                    audience = new Audience(queue.getSongQueue(), queue.getSongRequests(), queue.getFeedbacks(),
                            queue.getConcertName());
                    artist = new Artist(queue.getSongQueue(), queue.getSongRequests(), queue.getFeedbacks(),
                            queue.getConcertName());
                    valid = true;
                    break;
                case "2":
                    loadConcert();
                    valid = true;
                    break;
            }
        }
    }

    // EFFECTS: loads a concert with file name concertName
    private static void loadConcert() {
        jsonReaderQueue = new JsonReader(JSON_STORE_QUEUE + concertName);
        jsonReaderAudience = new JsonReader(JSON_STORE_QUEUE + concertName);
        jsonReaderArtist = new JsonReader(JSON_STORE_ARTIST + concertName);

        try {
            queue = jsonReaderQueue.readQueue();
            System.out.println("Loaded " + queue.getConcertName() + " from " + JSON_STORE_QUEUE + concertName);

            artist = jsonReaderArtist.readArtist(queue);
            System.out.println("Loaded " + queue.getConcertName() + " from " + JSON_STORE_ARTIST + concertName);

            audience = jsonReaderAudience.readAudience(queue);
            System.out.println("Loaded " + queue.getConcertName() + " from " + JSON_STORE_QUEUE + concertName);
        } catch (IOException e) {
            System.out.println("Unable to read file from " + JSON_STORE_ARTIST + concertName);
            LogPrinter lp = new LogPrinter();
            lp.printLog(EventLog.getInstance());
            exit = true;
        }
    }

    // EFFECTS: saves concert with file name concertName
    public static void saveConcert() {
        jsonWriterQueue = new JsonWriter(JSON_STORE_QUEUE + queue.getConcertName());
        jsonWriterArtist = new JsonWriter(JSON_STORE_ARTIST + artist.getConcertName());

        try {
            jsonWriterQueue.open();
            jsonWriterQueue.writeQueue(queue);
            jsonWriterQueue.close();

            jsonWriterArtist.open();
            jsonWriterArtist.writeArtist(artist);
            jsonWriterArtist.close();

            System.out.println("Saved " + queue.getConcertName() + " to " + JSON_STORE_QUEUE);
        } catch (FileNotFoundException e) {
            System.out.println("Unable to write to file: " + JSON_STORE_QUEUE);
        }
        LogPrinter lp = new LogPrinter();
        lp.printLog(EventLog.getInstance());
        exit = true;
    }
}