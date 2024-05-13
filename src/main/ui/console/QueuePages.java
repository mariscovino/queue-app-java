package ui.console;

public class QueuePages extends Main {
    public static void viewActions(String input) {
        switch (input) {
            case "1":
                queue.viewQueue();
                break;

            case "2":
                queue.viewAllRequests();
                break;
        }
    }
}
