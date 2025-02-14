package model;

import java.util.*;

/*
 * Represents the user of this tracking service;
 * Includes the user's name (userName) and their total savings (savings)
 */
public class User {
    private String userName;
    private Saving savings;
    

    /*
     * REQUIRES: length of userName must be non-zero
     * EFFECTS: name the user with the provided userName;
     *          set savings to 0 as the initial value.
     */
    public User(String name, Saving newSaving) {
        userName = name;
        savings = newSaving;
    }

    public String getUserName() {
        return userName;
    }

    public Saving getSavings() {
        return savings;
    }


    /*
     * EFFECTS: return a list of Savings 
     *          (includes the amount, date, & purpose of the saving)
     *          that the user stored for the given purpose
     */
    public List<Saving> savingsWithPurpose(Purpose purpose) {
        return null; //stub
    }



}

