package model;

import java.time.LocalDate;
import java.util.*;

/*
 * Represents a single saving history;
 * includes the date, the amount, and the purpose of the saving
 * Only 1 saving can be made in a day
 */
public class Saving {
    private LocalDate date;
    private int year;
    private int month;
    private int dayOfMonth;
    private int amount;
    private Purpose purpose;


    public Saving(LocalDate date, int amount, Purpose purpose) {
        date = LocalDate.of(year, month, dayOfMonth);
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
