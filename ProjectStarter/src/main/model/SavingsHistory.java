package model;

import java.util.*;

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

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

reference work: Project Funder (in the practice exam)
                JSONSERIALIZATIONDEMO */

public class SavingsHistory implements Writable {
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
     * EFFECTS: adds savings to the savings history list
     *          if there isn't a saving in the history that has the same date;
     *          (because only 1 saving can be stored per day)
     *          update saving progresses accordingly
     *          - additionally, check if the purpose of the saving is fulfilled
     *          after adding it to the savings history
     *            -> if fulfilled, add to fulfilledPurposes
     */
    public void addSaving(Saving saving) {
        for (Saving s : savingHistory) {
            if (s.getDate().equals(saving.getDate())) {
                return;
            }
        }
        
        String date = saving.getDate().toString();
        int amount = saving.getAmount();
        Purpose purpose = saving.getPurpose();

        EventLog.getInstance().logEvent(new Event("Added saving: $" + amount + " for " + purpose));

        savingHistory.add(saving);

        Purpose p = saving.getPurpose();
        if (savingAmountStillNeed(p) == 0 && !fulfilledPurposes.contains(p)) {
            fulfilledPurposes.add(p);
        }
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
    // EFFECTS: moves the givcen saving to a desired purpose from original
    //          if saving is already in the purpose, changes will not be made 
    //          (return out of the method)
    public void moveSaving(Purpose originalP, Purpose desiredP, int amount) {
        int amountToMove = amount;
        if (originalP == desiredP) {
            return;
        } 
        if (getSavingsForPurpose(originalP).isEmpty()) {
            return;
        } else {
            for (Saving s : getSavingsForPurpose(originalP)) {
                if (s.getAmount() == amount) {
                    s.setPurpose(desiredP);
                    EventLog.getInstance().logEvent(new Event(
                            "A $" + amount + " saving has been moved from " + originalP + " to " + desiredP + "!"));

                }
            }
        }
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("fulfilled purposes", purposesToJson());
        json.put("savings with purpose", savingsToJson(savingsWithPurpose));
        json.put("full savings history", savingsToJson(savingHistory));

        return json;
    }

    // EFFECTS: returns savings in this savings history as a JSON array, according to specific needs
    private JSONArray savingsToJson(List<Saving> savings) {
        JSONArray jsonArray = new JSONArray();

        for (Saving s : savingHistory) {
            jsonArray.put(s.toJson());
        }

        return jsonArray;
    }

    // EFFECTS: returns the purposes that have been fulfilled in this savings history as a JSON array
    private JSONArray purposesToJson() {
        JSONArray jsonArray = new JSONArray();

        for (Purpose p : fulfilledPurposes) {
            jsonArray.put(p.toJson());
        }

        return jsonArray;
    }
    
}
