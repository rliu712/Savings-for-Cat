package model;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TestSaving {
    private Saving s1;

    @BeforeEach
    void runBefore() {
        s1 = new Saving(LocalDate.of(2025, 1, 10), 5, Purpose.FISH_CANS);
    }

    @Test
    void testSavingConstructor() {
        assertEquals(LocalDate.of(2025, 1, 10), s1.getDate());
        assertEquals(5, s1.getAmount());
        assertEquals(Purpose.FISH_CANS, s1.getPurpose());
    }
}
