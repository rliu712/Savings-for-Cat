package model;

/*
 * Represents the different purposes for a saving
 * Includes the amount of money that each purpose needs to be achieved
 * 
 * reference source: https://www.youtube.com/watch?v=wq9SJb8VeyM
 */
public enum Purpose {

    FISH_CANS(5),
    MICE_TOYS(20),
    CAT_LITTER(30),
    CAT_TREE(70);

    final int amountNeeded;

    Purpose(int amountNeeded) {
        this.amountNeeded = amountNeeded;
    }

    public int getAmountNeeded() {
        return amountNeeded;
    }
}
