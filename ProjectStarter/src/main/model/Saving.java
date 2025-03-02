package model;

import java.time.LocalDate;

import org.json.JSONObject;
import persistence.Writable;

/*
 * Represents a single saving history;
 * includes the date, the amount, and the purpose of the saving
 * Only 1 saving can be made in a day
 * 
 * referenced work: Jsonserializationdemo
 */
public class Saving implements Writable {
    private LocalDate date;
    private int amount;
    private Purpose purpose;


    public Saving(LocalDate date, int amount, Purpose purpose) {
        this.date = LocalDate.of(date.getYear(), date.getMonth(), date.getDayOfMonth());
        this.amount = amount;
        this.purpose = purpose;
    }

    public void setDate(LocalDate date) {
        this.date = LocalDate.of(date.getYear(), date.getMonth(), date.getDayOfMonth());
    }


    public LocalDate getDate() {
        return date;
    }

    // REQUIRES: amount > 0
    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getAmount() {
        return amount;
    }

    public void setPurpose(Purpose purpose) {
        this.purpose = purpose;
    }

    public Purpose getPurpose() {
        return purpose;
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("date", date);
        json.put("amount", amount);
        json.put("purpose", purpose);
        return json;
    }
}
