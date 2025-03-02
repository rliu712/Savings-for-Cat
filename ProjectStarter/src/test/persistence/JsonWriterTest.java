package persistence;

import model.Purpose;
import model.Saving;
import model.SavingsHistory;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JsonWriterTest extends JsonTest {
    //NOTE TO CPSC 210 STUDENTS: the strategy in designing tests for the JsonWriter is to
    //write data to a file and then use the reader to read it back in and check that we
    //read in a copy of what was written out.

    @Test
    void testWriterInvalidFile() {
        try {
            SavingsHistory sh = new SavingsHistory();
            JsonWriter writer = new JsonWriter("./data/my\0illegal:fileName.json");
            writer.open();
            fail("IOException was expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testWriterEmptySavingHistory() {
        try {
            SavingsHistory sh = new SavingsHistory();
            JsonWriter writer = new JsonWriter("./data/testWriterEmptySavingsHistory.json");
            writer.open();
            writer.write(sh);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterEmptySavingsHistory.json");
            sh = reader.read();
            assertEquals(0, sh.getFullSavingHistory().size());
            assertEquals(0, sh.getFulfilledPurposes().size());
        } catch (IOException e) {
            fail("IOException should not have been thrown");
        }
    }

    @Test
    void testWriterGeneralSavingsHistory() {
        try {
            SavingsHistory sh = new SavingsHistory();
            sh.addSaving(new Saving(LocalDate.of(2025, 2, 27), 5, Purpose.FISH_CAN));
            sh.addSaving(new Saving(LocalDate.of(2025, 2, 28), 20, Purpose.MICE_TOY));
            JsonWriter writer = new JsonWriter("./data/testWriterGeneralSavingsHistory.json");
            writer.open();
            writer.write(sh);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterGeneralSavingsHistory.json");
            sh = reader.read();
            List<Saving> savings = sh.getFullSavingHistory();
            assertEquals(2, savings.size());
            checkSaving(LocalDate.of(2025, 2, 27), 5, Purpose.FISH_CAN, savings.get(0));
            checkSaving(LocalDate.of(2025, 2, 28), 20, Purpose.MICE_TOY, savings.get(1));
            assertEquals(2, sh.getFulfilledPurposes().size());
            assertEquals(1, sh.getSavingsForPurpose(Purpose.FISH_CAN).size());
            assertEquals(1, sh.getSavingsForPurpose(Purpose.MICE_TOY).size());

        } catch (IOException e) {
            fail("IOException should not have been thrown");
        }
    }
}