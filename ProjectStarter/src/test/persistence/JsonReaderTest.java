package persistence;

import model.Purpose;
import model.Saving;
import model.SavingsHistory;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JsonReaderTest extends JsonTest {

    @Test
    void testReaderNonExistentFile() {
        JsonReader reader = new JsonReader("./data/noSuchFile.json");
        try {
            SavingsHistory sh = reader.read();
            fail("IOException expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testReaderEmptySavingHistory() {
        JsonReader reader = new JsonReader("./data/testReaderEmptySavingsHistory.json");
        try {
            SavingsHistory sh = reader.read();
            List<Saving> savingsHistory = sh.getFullSavingHistory();
            assertEquals(0, savingsHistory.size());
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testReaderGeneralSavingHistory() {
        JsonReader reader = new JsonReader("./data/testReaderGeneralSavingsHistory.json");
        try {
            SavingsHistory sh = reader.read();
            List<Saving> savingsHistory = sh.getFullSavingHistory();
            assertEquals(4, savingsHistory.size());
            checkSaving(LocalDate.of(2025, 2, 25), 5, Purpose.FISH_CAN, savingsHistory.get(0));
            checkSaving(LocalDate.of(2025, 2, 26), 20, Purpose.MICE_TOY, savingsHistory.get(1));
            checkSaving(LocalDate.of(2025, 2, 27), 40, Purpose.CAT_LITTER, savingsHistory.get(2));
            checkSaving(LocalDate.of(2025, 2, 28), 70, Purpose.CAT_TREE, savingsHistory.get(3));
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }

}