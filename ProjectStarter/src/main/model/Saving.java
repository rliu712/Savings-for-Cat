package model;

import java.time.LocalDate;

/*
 * Represents a single saving history;
 * includes the date, the amount, and the purpose of the saving
 * Only 1 saving can be made in a day
 */
public class Saving {
    private LocalDate date;
    private int amount;
    private Purpose purpose;


    public Saving(LocalDate date, int amount, Purpose purpose) {
        this.date = LocalDate.of(date.getYear(), date.getMonth(), date.getDayOfMonth());
        this.amount = amount;
        this.purpose = purpose;
    }

    public LocalDate getDate() {
        return date;
    }

    public int getAmount() {
        return amount;
    }

    public Purpose getPurpose() {
        return purpose;
    }
}
