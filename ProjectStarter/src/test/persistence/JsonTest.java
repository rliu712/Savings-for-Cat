package persistence;

import model.Purpose;
import model.Saving;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class JsonTest {
    
    protected void checkSaving(LocalDate date, int amount, Purpose purpose, Saving saving) {
        assertEquals(date, saving.getDate());
        assertEquals(amount, saving.getAmount());
        assertEquals(purpose, saving.getPurpose());
    }
}
