package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FeedbackTest {
    Song song;
    Feedback feedback;

    @BeforeEach
    public void runBefore() {
        song = new Song("A", "B");
        feedback = new Feedback("C", song);
    }

    @Test
    public void getFeedbackTest() {
        assertEquals(feedback.getFeedback(), "C");
    }

    @Test
    public void getSongTest() {
        assertEquals(feedback.getSong(), song);
    }

    @Test
    public void setFeedbackTest() {
        feedback.setFeedback("feedback");

        assertEquals(feedback.getFeedback(), "feedback");
    }
}
