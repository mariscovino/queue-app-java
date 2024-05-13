package persistence;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.stream.Stream;

import model.*;
import org.json.*;

import static java.lang.String.valueOf;

public class JsonReader {
    private final String source;

    // EFFECTS: constructs reader to read from source file
    public JsonReader(String source) {
        this.source = source;
    }

    // EFFECTS: reads Artist from file and returns it;
    // throws IOException if an error occurs reading data from file
    public Queue readQueue() throws IOException {
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);
        return parseQueue(jsonObject);
    }

    // EFFECTS: reads Artist from file and returns it;
    // throws IOException if an error occurs reading data from file
    public Artist readArtist(Queue queue) throws IOException {
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);
        return parseArtist(jsonObject, queue);
    }

    // EFFECTS: reads Audience from file and returns it;
    // throws IOException if an error occurs reading data from file
    public Audience readAudience(Queue queue) throws IOException {
        return parseAudience(queue);
    }

    // EFFECTS: reads source file as string and returns it
    private String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(contentBuilder::append);
        }
        return contentBuilder.toString();
    }

    // EFFECTS: parses a LinkedList<Song> from JSON object and returns it
    private LinkedList<Song> addQueue(String key, JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray(key);
        LinkedList<Song> queue = new LinkedList<>();

        for (Object json : jsonArray) {
            JSONObject nextSong = (JSONObject) json;
            addSong(queue, nextSong);
        }

        return queue;
    }

    // EFFECTS: parses Feedbacks from JSON object and returns it
    private LinkedList<Feedback> addFeedbacks(JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("feedbacks");
        LinkedList<Feedback> feedback = new LinkedList<>();

        for (Object json : jsonArray) {
            JSONObject nextFeedback = (JSONObject) json;
            addFeedback(feedback, nextFeedback);
        }

        return feedback;
    }

    // EFFECTS: parses a.getPlayed() from JSON object and returns it
    private void addPlayed(Artist a, JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("played");
        for (Object json : jsonArray) {
            JSONObject nextSong = (JSONObject) json;
            playSong(a, nextSong);
        }
    }

    // MODIFIES: a.getPlayed()
    // EFFECTS: parses Song from JSON object and adds it to a.getPlayed()
    private void playSong(Artist a, JSONObject jsonObject) {
        String name = jsonObject.getString("name");
        String artist = valueOf(jsonObject.getString("artist"));
        Song song = new Song(name, artist);
        a.playSong(song);
    }

    // MODIFIES: linkedList
    // EFFECTS: parses Song from JSON object and adds it to linkedList
    private void addSong(LinkedList<Song> linkedList, JSONObject jsonObject) {
        String name = jsonObject.getString("name");
        String artist = valueOf(jsonObject.getString("artist"));
        Song song = new Song(name, artist);
        linkedList.addLast(song);
    }

    // MODIFIES: linkedList
    // EFFECTS: parses Feedback from JSON object and adds it to linkedList
    private void addFeedback(LinkedList<Feedback> linkedList, JSONObject jsonObject) {
        String string = valueOf(jsonObject.getString("feedback"));
        String name = valueOf(jsonObject.getString("name"));
        String artist = valueOf(jsonObject.getString("artist"));
        Feedback feedback = new Feedback(string, new Song(name, artist));
        linkedList.addLast(feedback);
    }

    // EFFECTS: parses Queue from JSON object and returns it
    private Queue parseQueue(JSONObject jsonObject) {
        String concertName = jsonObject.getString("concertName");
        Queue q = new Queue();
        q.setConcertName(concertName);
        q.setSongQueue(addQueue("songQueue", jsonObject));
        q.setSongRequests(addQueue("songRequests", jsonObject));
        q.setFeedbacks(addFeedbacks(jsonObject));
        return q;
    }

    // EFFECTS: parses Artist from JSON object and returns it
    private Artist parseArtist(JSONObject jsonObject, Queue q) {
        Artist a = new Artist(q.getSongQueue(),
                q.getSongRequests(), q.getFeedbacks(),
                q.getConcertName());
        addPlayed(a, jsonObject);
        return a;
    }

    // EFFECTS: parses Audience from JSON object and returns it
    private Audience parseAudience(Queue q) {
        return new Audience(q.getSongQueue(),
                q.getSongRequests(), q.getFeedbacks(),
                q.getConcertName());
    }
}
