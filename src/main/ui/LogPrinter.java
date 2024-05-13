package ui;

import model.Event;
import model.EventLog;

import java.io.FileWriter;
import java.io.IOException;

/**
 * Defines behaviours that event log printers must support.
 */
public class LogPrinter {
    public void printLog(EventLog el) {
        for (Event next : el) {
            System.out.println(next.toString());
        }
    }
}
