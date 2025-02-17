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
    private Saving s6;
    private Saving s7;
    private Saving s8;
    private SavingsHistory sh;

    @BeforeEach
    void runBefore() {
        s1 = new Saving(LocalDate.of(2025, 1, 1), 2, Purpose.FISH_CANS);
        s2 = new Saving(LocalDate.of(2025, 1, 2), 3, Purpose.FISH_CANS);
        s3 = new Saving(LocalDate.of(2025, 1, 5), 20, Purpose.MICE_TOYS);
        s4 = new Saving(LocalDate.of(2025, 1, 6), 40, Purpose.CAT_LITTER);
        s5 = new Saving(LocalDate.of(2025,1,7), 60, Purpose.CAT_TREE);
        s6 = new Saving(LocalDate.of(2025,1,8), 10, Purpose.CAT_TREE);
        s7 = new Saving(LocalDate.of(2025,1,9), 10, Purpose.FISH_CANS);
        s8 = new Saving(LocalDate.of(2025,1,1), 10, Purpose.MICE_TOYS);
        sh = new SavingsHistory();
    }

    @Test
    void testEmptySavingHistory() {
        assertTrue(sh.getFullSavingHistory().isEmpty());
    }

    @Test
    void testGetSavingOnDate() {
        assertNull(sh.getSavingOnDate(LocalDate.of(2025, 1, 1)));
        sh.addSaving(s1);
        sh.addSaving(s2);
        sh.addSaving(s3);
        assertEquals(s1, sh.getSavingOnDate(LocalDate.of(2025, 1, 1)));
        assertNull(sh.getSavingOnDate(LocalDate.of(2025, 1, 3)));
    }

    @Test
    void testGetSavingsForPurpose() {
        sh.addSaving(s1);
        sh.addSaving(s3);
        assertEquals(s1, sh.getSavingsForPurpose(Purpose.FISH_CANS).get(0));
        assertEquals(s3, sh.getSavingsForPurpose(Purpose.MICE_TOYS).get(0));
        assertTrue(sh.getSavingsForPurpose(Purpose.CAT_TREE).isEmpty());
    }

    @Test
    void testSavingProgressMade() {
        sh.addSaving(s1);
        assertEquals(40, sh.savingProgressMade(Purpose.FISH_CANS));
        sh.addSaving(s2);
        assertEquals(100, sh.savingProgressMade(Purpose.FISH_CANS));
    }

    @Test
    void testSavingAmountStillNeed() {
        sh.addSaving(s1);
        assertEquals(3, sh.savingAmountStillNeed(Purpose.FISH_CANS));
        sh.addSaving(s2);
        assertEquals(0, sh.savingAmountStillNeed(Purpose.FISH_CANS));
        sh.addSaving(s7);
        assertEquals(0, sh.savingAmountStillNeed(Purpose.FISH_CANS));
    }

    @Test
    void testGetFulfilledPurposes() {
        sh.addSaving(s1);
        assertTrue(sh.getFulfilledPurposes().isEmpty());
        sh.addSaving(s2);
        assertEquals(Purpose.FISH_CANS, sh.getFulfilledPurposes().get(0));
        sh.addSaving(s3);
        assertEquals(Purpose.MICE_TOYS, sh.getFulfilledPurposes().get(1));
        assertEquals(2, sh.getFulfilledPurposes().size());
    }
    

    @Test
    void testAddOneSavingToSavingsHistory() {
        assertTrue(sh.getFullSavingHistory().isEmpty());
        sh.addSaving(s1);
        assertEquals(5, s1.getPurpose().getAmountNeeded());
        assertEquals(s1, sh.getFullSavingHistory().get(0));
        assertEquals(s1, sh.getSavingsForPurpose(Purpose.FISH_CANS).get(0));
        assertEquals(40, sh.savingProgressMade(Purpose.FISH_CANS));     // 40% 
        assertEquals(3, sh.savingAmountStillNeed(Purpose.FISH_CANS));
        assertTrue(sh.getFulfilledPurposes().isEmpty());
    }

    @Test
    void testAddTwoSavingsWithSameDate() {
        assertTrue(sh.getFullSavingHistory().isEmpty());
        sh.addSaving(s1);
        assertEquals(s1, sh.getFullSavingHistory().get(0));
        assertEquals(1, sh.getFullSavingHistory().size());
        sh.addSaving(s8);
        assertEquals(1, sh.getFullSavingHistory().size());        // saving history list will not be updated
    }

    @Test
    void testAddTwoSavingsWithTwoPurposesToSavingsHistory() {
        assertTrue(sh.getFullSavingHistory().isEmpty());
        sh.addSaving(s3);
        assertEquals(20, s3.getPurpose().getAmountNeeded());
        assertEquals(s3, sh.getFullSavingHistory().get(0));
        assertEquals(s3, sh.getSavingsForPurpose(Purpose.MICE_TOYS).get(0));
        assertEquals(100, sh.savingProgressMade(Purpose.MICE_TOYS));   // 100%
        assertEquals(0, sh.savingAmountStillNeed(Purpose.MICE_TOYS));
        assertEquals(1, sh.getFulfilledPurposes().size());

        sh.addSaving(s4);
        assertEquals(30, s4.getPurpose().getAmountNeeded());
        assertEquals(s4, sh.getFullSavingHistory().get(1));
        assertEquals(s4, sh.getSavingsForPurpose(Purpose.CAT_LITTER).get(0));
        assertEquals(100, sh.savingProgressMade(Purpose.CAT_LITTER));   // 100%
        assertEquals(0, sh.savingAmountStillNeed(Purpose.CAT_LITTER));
        assertEquals(2, sh.getFulfilledPurposes().size());

        assertEquals(2, sh.getFullSavingHistory().size());
    }

    @Test
    void testAddTwoSavingsWithSamePurpose() {
        assertTrue(sh.getFullSavingHistory().isEmpty());
        sh.addSaving(s5);
        assertEquals(70, s5.getPurpose().getAmountNeeded());
        assertEquals(s5, sh.getFullSavingHistory().get(0));
        assertEquals(s5, sh.getSavingsForPurpose(Purpose.CAT_TREE).get(0));
        assertEquals(86, sh.savingProgressMade(Purpose.CAT_TREE));   // 86%
        assertEquals(10, sh.savingAmountStillNeed(Purpose.CAT_TREE));
        assertEquals(0, sh.getFulfilledPurposes().size());

        sh.addSaving(s6);
        assertEquals(70, s6.getPurpose().getAmountNeeded());
        assertEquals(s6, sh.getFullSavingHistory().get(1));
        assertEquals(s6, sh.getSavingsForPurpose(Purpose.CAT_TREE).get(1));
        assertEquals(100, sh.savingProgressMade(Purpose.CAT_TREE));   // 100%
        assertEquals(0, sh.savingAmountStillNeed(Purpose.CAT_TREE));
        assertEquals(1, sh.getFulfilledPurposes().size());
    }

    @Test
    void testMoveSaving() {
        assertTrue(sh.getFullSavingHistory().isEmpty());
        sh.addSaving(s4);
        assertEquals(Purpose.CAT_LITTER, s4.getPurpose());
        assertEquals(100, sh.savingProgressMade(Purpose.CAT_LITTER));
        assertEquals(0, sh.savingProgressMade(Purpose.CAT_TREE));
        sh.moveSaving(s4, Purpose.CAT_TREE);
        assertEquals(Purpose.CAT_TREE, s4.getPurpose());
        assertEquals(57, sh.savingProgressMade(Purpose.CAT_TREE));
        assertEquals(0, sh.savingProgressMade(Purpose.CAT_LITTER));
    }

    @Test
    void testMoveSavingToSamePurpose() {
        assertTrue(sh.getFullSavingHistory().isEmpty());
        sh.addSaving(s4);
        assertEquals(Purpose.CAT_LITTER, s4.getPurpose());
        assertEquals(100, sh.savingProgressMade(Purpose.CAT_LITTER));
        assertEquals(0, sh.savingProgressMade(Purpose.CAT_TREE));
        sh.moveSaving(s4, Purpose.CAT_LITTER);
        assertEquals(Purpose.CAT_LITTER, s4.getPurpose());
        assertEquals(100, sh.savingProgressMade(Purpose.CAT_LITTER));
    }
}
