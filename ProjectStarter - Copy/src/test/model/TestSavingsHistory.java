package model;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/*
 * reference resource: https://www.w3schools.com/java/java_enums.asp
 */

public class TestSavingsHistory {
    private Saving s1;
    private Saving s2;
    private Saving s3;
    private Saving s4;
    private Saving s5;
    private SavingsHistory sh;

    @BeforeEach
    void runBefore() {
        s1 = new Saving(LocalDate.of(2025, 1, 1), 2, Purpose.FISH_CANS);
        s2 = new Saving(LocalDate.of(2025, 1, 2), 3, Purpose.FISH_CANS);
        s3 = new Saving(LocalDate.of(2025, 1, 5), 20, Purpose.MICE_TOYS);
        s4 = new Saving(LocalDate.of(2025, 1, 6), 40, Purpose.CAT_LITTER);
        s5 = new Saving(LocalDate.of(2025,1,7), 60, Purpose.CAT_TREE);
        sh = new SavingsHistory();
    }

    @Test
    void testEmptySavingHistory() {
        assertTrue(sh.getFullSavingHistory().isEmpty());
    }
    
    @Test
    void testAddOneSavingToSavingsHistory() {
        assertTrue(sh.getFullSavingHistory().isEmpty());
        sh.addSaving(s1);
        assertEquals(Purpose.FISH_CANS, s1.getPurpose());
        assertEquals(5, s1.getPurpose().amountNeeded);
        assertEquals(s1, sh.getSavingsForPurpose(Purpose.FISH_CANS));
        assertEquals(40, sh.savingProgressMade(Purpose.FISH_CANS));     // 20% 
        assertEquals(3, sh.savingAmountStillNeed(Purpose.FISH_CANS));
        assertTrue(sh.getFulfilledPurposes().isEmpty());
    }

    @Test
    void testAddTwoSavingsWithTwoPurposesToSavingsHistory() {
        assertTrue(sh.getFullSavingHistory().isEmpty());
        sh.addSaving(s3);
        assertEquals(Purpose.MICE_TOYS, s3.getPurpose());
        assertEquals(20, s3.getPurpose().amountNeeded);
        assertEquals(s3, sh.getSavingsForPurpose(Purpose.MICE_TOYS));
        assertEquals(100, sh.savingProgressMade(Purpose.MICE_TOYS));   // 100%
        assertEquals(0, sh.savingAmountStillNeed(Purpose.MICE_TOYS));
        assertEquals(1, sh.getFulfilledPurposes().size());

        sh.addSaving(s4);
        assertEquals(Purpose.CAT_LITTER, s4.getPurpose());
        assertEquals(30, s4.getPurpose().amountNeeded);
        assertEquals(s4, sh.getSavingsForPurpose(Purpose.CAT_LITTER));
        assertEquals(100, sh.savingProgressMade(Purpose.CAT_LITTER));   // 100%
        assertEquals(0, sh.savingAmountStillNeed(Purpose.CAT_LITTER));
        assertEquals(2, sh.getFulfilledPurposes().size());

        assertEquals(2, sh.getFullSavingHistory().size());
    }
}
