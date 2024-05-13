package ui.console;

import model.Song;

import java.util.Objects;

public class ArtistPages extends Main {

    // EFFECTS: first/main Artist window of Artist Performance Queue application
    //          prints to user the number associated with the possible artist actions
    public static void artistMainPage() {
        while (!exit) {
            System.out.println("Press number to select action: ");
            System.out.println("1. View song queue");
            System.out.println("2. View all song requests");
            System.out.println("3. View songs played");
            System.out.println("4. Add song to queue");
            System.out.println("5. Accept song");
            System.out.println("6. Deny song");
            System.out.println("7. Play song");
            System.out.println("8. Go to audience page");
            System.out.println("9. Exit");

            String input = scanner.nextLine();

            QueuePages.viewActions(input);
            artistActions(input);
        }
    }

    // EFFECTS: user selects the desired action based on numbers shown
    //          user is redirected to another page based on number entered
    public static void artistActions(String input) {
        switch (input) {
            case "3":
                artist.viewPlayed();
                break;

            case "4":
                addSongToQueue();
                break;

            case "5":
                acceptSong();
                break;

            case "6":
                denySong();
                break;

            case "7":
                playSong();
                break;

            case "8":
                AudiencePages.audienceMainPage();
                break;

            case "9":
                saveConcert();
                break;
        }
    }

    // EFFECTS: prints to user that an action is invalid if songRequests is empty
    public static void isInvalid() {
        if (queue.isEmptySongRequests()) {
            System.out.println("Sorry, it is not possible to do this at this time: there are no song requests");
            artistMainPage();
        }
    }

    // EFFECTS: processes Artist action of accepting song
    //          tells user if action is invalid
    //          processes user input of song
    //          accepts song entered and displays message according to outcome
    //          prints to user the number associated with the possible further actions
    //          processes user input of further action and redirects user
    //          auto-saves if user exists the application
    public static void acceptSong() {
        isInvalid();

        Song s = enterSong();

        artist.acceptSong(s);

        if (artist.getSongQueue().contains(s)) {
            System.out.println("The song has been accepted");
        }

        while (!valid) {
            System.out.println("Press number to select action: ");
            System.out.println("1. Go back");
            System.out.println("2. Try again");
            System.out.println("3. Exit");
            String goBack = scanner.nextLine();

            if (Objects.equals(goBack, "1")) {
                valid = true;
                artistMainPage();

            } else if (Objects.equals(goBack, "2")) {
                valid = true;
                acceptSong();

            } else if (Objects.equals(goBack, "3")) {
                saveConcert();
                valid = true;
            }
        }
    }

    // EFFECTS: processes Artist action of denying song
    //          tells user if action is invalid
    //          processes user input of song
    //          processes user input of feedback
    //          denies song entered and displays message according to outcome
    //          prints to user the number associated with the possible further actions
    //          processes user input of further action and redirects user
    //          auto-saves if user exists the application
    public static void denySong() {
        isInvalid();

        Song s = enterSong();

        System.out.println("Provide feedback for audience: ");
        String feedback = scanner.nextLine();

        artist.denySong(s, feedback);

        System.out.println("The song has been denied.");

        while (!valid) {
            System.out.println("Press number to select action: ");
            System.out.println("1. Go back");
            System.out.println("2. Try again");
            System.out.println("3. Exit");
            String goBack = scanner.nextLine();

            if (Objects.equals(goBack, "1")) {
                artistMainPage();

            } else if (Objects.equals(goBack, "2")) {
                denySong();

            } else if (Objects.equals(goBack, "3")) {
                saveConcert();
                valid = true;
            }
        }
    }

    // EFFECTS: processes Artist action of playing song
    //          processes user input of song
    //          plays song entered and displays message according to outcome
    //          prints to user the number associated with the possible further actions
    //          processes user input of further action and redirects user
    //          auto-saves if user exists the application
    public static void playSong() {

        Song s = enterSong();

        artist.playSong(s);

        System.out.println("The song has been added to list of songs played.");

        while (!valid) {
            System.out.println("Press number to select action: ");
            System.out.println("1. Go back");
            System.out.println("2. Try again");
            System.out.println("3. Exit");
            String goBack = scanner.nextLine();

            if (Objects.equals(goBack, "1")) {
                valid = true;
                artistMainPage();

            } else if (Objects.equals(goBack, "2")) {
                valid = true;
                playSong();

            } else if (Objects.equals(goBack, "3")) {
                saveConcert();
                valid = true;
            }
        }
    }

    // EFFECTS: processes Artist action of adding song to songQueue
    //          processes user input of song
    //          plays song entered and displays message according to outcome
    //          prints to user the number associated with the possible further actions
    //          processes user input of further action and redirects user
    //          auto-saves if user exists the application
    public static void addSongToQueue() {

        Song s = enterSong();

        artist.addSongToQueue(s);

        System.out.println("The song has been added to the queue.");

        while (!valid) {
            System.out.println("Press number to select action: ");
            System.out.println("1. Go back");
            System.out.println("2. Try again");
            System.out.println("3. Exit");
            String goBack = scanner.nextLine();


            if (Objects.equals(goBack, "1")) {
                valid = true;
                artistMainPage();

            } else if (Objects.equals(goBack, "2")) {
                valid = true;
                playSong();

            } else if (Objects.equals(goBack, "3")) {
                saveConcert();
                valid = true;
            }
        }
    }
}
