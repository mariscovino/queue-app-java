package ui.console;

import model.EventLog;
import model.Song;
import ui.LogPrinter;

import java.util.LinkedList;
import java.util.Objects;

public class AudiencePages extends Main {

    // EFFECTS: first/main Audience window of Artist Performance Queue application
    //          prints to user the number associated with the possible audience actions
    public static void audienceMainPage() {
        while (!exit) {
            System.out.println("Press number to select action: ");
            System.out.println("1. View song queue");
            System.out.println("2. View all song requests");
            System.out.println("3. View feedback on song requests");
            System.out.println("4. Request song");
            System.out.println("5. Go to artist page");
            System.out.println("6. Exit");

            String input = scanner.nextLine();

            QueuePages.viewActions(input);
            audienceActions(input);
        }
    }

    // EFFECTS: user selects the desired action based on numbers shown
    //          user is redirected to another page based on number entered
    public static void audienceActions(String input) {
        switch (input) {
            case "3":
                audience.receiveFeedback();
                break;

            case "4":
                requestSong();
                break;

            case "5":
                ArtistPages.artistMainPage();
                break;

            case "6":
                saveConcert();
                LogPrinter lp = new LogPrinter();
                lp.printLog(EventLog.getInstance());
                exit = true;
                break;
        }
    }

    // EFFECTS: processes Audience action of requesting song
    //          processes user input of song
    //          adds song request to songRequests entered and displays message according to outcome
    //          prints to user the number associated with the possible further actions
    //          processes user input of further action and redirects user
    //          auto-saves if user exists the application
    public static void requestSong() {
        Song s = enterSong();

        LinkedList<Song> played = artist.getPlayed();

        audience.addSongRequest(s, played);

        while (!valid) {
            System.out.println("Press number to select action: ");
            System.out.println("1. Go back");
            System.out.println("2. Try again");
            System.out.println("3. Exit");
            String goBack = scanner.nextLine();

            if (Objects.equals(goBack, "1")) {
                valid = true;
                audienceMainPage();

            } else if (Objects.equals(goBack, "2")) {
                valid = true;
                requestSong();

            } else if (Objects.equals(goBack, "3")) {
                saveConcert();
                valid = true;
            }
        }
    }
}