package model;

import java.util.*;


public class SavingsHistory {

    // EFFECTS: constructs a saving list with no savings added
    public SavingsHistory() {
        // stub
    }

    /*
     * MODIFIES: this
     * EFFECTS: adds savings to the list (saving history)
     *          if haven't been added
     */
    public void addSaving(Saving saving) {
        // stub
    }


    /*
     * EFFECTS: get the saving made on the given date
     *          if saving wasn't made on the given date, return null
     */
    //public Saving getSavingOnDate(/*LocalDate?*/) {
    //    return null; // stub
    //}

    /*
     * EFFECTS: get the savings made for the given purpose
     */
    public List<Saving> getSavingsForPurpose(Purpose purpose) {
        return null; // stub
    }

    /*
     * EFFECTS: add all the amount stored for the same purpose
     *          and return how much the purpose have been fulfilled in rounded percentage
     *          if amount stored > amountNeeded
     *          return 100
     *          ** Math.round((totalAmount / amountNeeded) * 100)
     */
    public int savingProgressMade(Purpose purpose) {
        return 0; // stub
    }
    
    /*
     * EFFECTS: add all the amount stored for the same purpose
     *          minus by the amount that's needed to fulfill the purpose (amountNeeded)
     *          and return the amount still need to achieve the purpose
     */
    public int savingAmountStillNeed(Purpose purpose) {
        return 0; // stub
    }

    /*
     * EFFECTS: return the purposes that have been fulfilled 
     *          with sufficient savings;
     *          if none, return null
     */
    public List<Purpose> getFulfilledPurposes() {
        return null;
    }

    /*
     * EFFECTS: return the list of all the savings
     *          ordered from the least to the most recent one
     *          return an empty list if there are no savings added so far
     */
    public List<Saving> getFullSavingHistory() {
        return null;
    }
}
