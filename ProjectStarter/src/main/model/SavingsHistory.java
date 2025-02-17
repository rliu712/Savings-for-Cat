package model;

import java.util.*;

import java.time.LocalDate;

/*Represents a history of savings for various purposes, 
with the same or different amounts, and with different dates.
It can:
- Add new savings to the history (but not with the same dates as previous savings) 
- Showcase the full history of savings that has been made, 
    or based on specific dates or purpose
- Calculate the progress that's been made in percentage
- Conduct the amount that is still in need for a saving to fulfill its purpose
- Showcase all the purposes that has been fulfilled
- Move a Saving object from one purpose to another

reference work: Project Funder (in the practice exam)*/

public class SavingsHistory {
    private List<Saving> savingHistory;
    private List<Saving> savingsWithPurpose; 
    private List<Purpose> fulfilledPurposes; 
     

    // EFFECTS: constructs a saving list with no savings added
    public SavingsHistory() {
        savingHistory = new ArrayList<Saving>();
        savingsWithPurpose = new ArrayList<Saving>();
        fulfilledPurposes = new ArrayList<Purpose>();
    }

    /*
     * EFFECTS: return the list of all the savings
     *          ordered from the least to the most recent one
     *          return an empty list if there are no savings added so far
     */
    public List<Saving> getFullSavingHistory() {
        return savingHistory;
    }

    /*
     * MODIFIES: this
     * EFFECTS: adds savings to the saving history list
     *          if there isn't a saving in the history that has the same date;
     *          (because only 1 saving can be stored per day)
     *          update saving progresses accordingly
     */
    public void addSaving(Saving saving) {
        for (Saving s : savingHistory) {
            if (s.getDate().equals(saving.getDate())) {
                return;
            }
        }
        savingHistory.add(saving);

    }


    /*
     * EFFECTS: get the saving made on the given date
     *          if saving wasn't made on the given date, return null
     */
    public Saving getSavingOnDate(LocalDate date) {
        for (Saving s : savingHistory) {
            if (s.getDate().equals(date)) {          /*Use .equal instead of ==*/
                return s;
            }
        }
        return null;
    }

    /*
     * EFFECTS: get the savings made for the given purpose
     */
    public List<Saving> getSavingsForPurpose(Purpose purpose) {
        savingsWithPurpose = new ArrayList<Saving>();   /*Instantiate a new savingWithPurpose list
                                                        due to that every different purpose will have
                                                        a different list */

        for (Saving s : savingHistory) {
            if (s.getPurpose().equals(purpose)) {
                savingsWithPurpose.add(s);
            }
        }
        return savingsWithPurpose;
    }

    /*
     * EFFECTS: add all the amount stored for the same purpose
     *          and return how much the purpose have been fulfilled in rounded percentage
     *          if amount stored > amountNeeded
     *          return 100
     *          ** Math.round((totalAmount / amountNeeded) * 100)
     */
    public int savingProgressMade(Purpose purpose) {
        int progress = 0;
        int savedForPurpose = 0;
        savingsWithPurpose = getSavingsForPurpose(purpose);
        
        for (Saving s : savingsWithPurpose) {
            savedForPurpose += s.getAmount();
        }
        progress = (int) Math.round(((double) savedForPurpose / purpose.amountNeeded) * 100);
        if (progress >= 100) {
            return 100;
        } else {
            return progress;
        }
    }
    
    /*
     * EFFECTS: add all the amount stored for the same purpose
     *          minus by the amount that's needed to fulfill the purpose (amountNeeded)
     *          and return the amount still need to achieve the purpose
     */
    public int savingAmountStillNeed(Purpose purpose) {
        int savedForPurpose = 0;
        savingsWithPurpose = getSavingsForPurpose(purpose);
        
        for (Saving s : savingsWithPurpose) {
            savedForPurpose += s.getAmount();
        }
        if (savedForPurpose >= purpose.amountNeeded) {
            return 0;
        } else {
            return purpose.amountNeeded - savedForPurpose;
        }
    }

    /*
     * EFFECTS: return the purposes that have been fulfilled 
     *          with sufficient savings;
     *          if none, return null
     */
    public List<Purpose> getFulfilledPurposes() {
        fulfilledPurposes.clear();
        Purpose p;
        
        for (Saving s : savingHistory) {
            p = s.getPurpose();
            if (savingAmountStillNeed(p) == 0 && !fulfilledPurposes.contains(p)) {
                fulfilledPurposes.add(p);
            }
        } 
        return fulfilledPurposes;
    }


    // MODIFIES: this
    // EFFECTS: moves the givcen saving to a new purpose
    //          if saving is already in the purpose, changes will not be made 
    //          (return out of the method)
    public void moveSaving(Saving saving, Purpose desiredP) {
        if (saving.getPurpose() == desiredP) {
            return;
        } else {
            saving.setPurpose(desiredP);
        }
    }
}
