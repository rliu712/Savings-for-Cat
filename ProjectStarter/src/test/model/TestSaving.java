package model;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TestSaving {
    private Saving s1;

    @BeforeEach
    void runBefore() {
        s1 = new Saving(LocalDate.of(2025, 1, 10), 5, Purpose.FISH_CAN);
    }

    @Test
    void testSavingConstructor() {
        assertEquals(LocalDate.of(2025, 1, 10), s1.getDate());
        assertEquals(5, s1.getAmount());
        assertEquals(Purpose.FISH_CAN, s1.getPurpose());
    }

    @Test
    void testSetDate() {
        s1.setDate(LocalDate.of(2024, 9, 1));
        assertEquals(LocalDate.of(2024, 9, 1), s1.getDate());
    }

    @Test
    void testSetAmount() {
        s1.setAmount(10);
        assertEquals(10, s1.getAmount());
    }

    @Test
    void testSetPurpose() {
        s1.setPurpose(Purpose.FISH_CAN);
        assertEquals(Purpose.FISH_CAN, s1.getPurpose());
    }
}
