package model;

import org.json.JSONObject;
import persistence.Writable;

/*
 * Represents the different purposes for a saving
 * Includes the amount of money that each purpose needs to be achieved
 * 
 * reference source: https://www.youtube.com/watch?v=wq9SJb8VeyM
 */
public enum Purpose implements Writable {

    FISH_CAN(5),
    MICE_TOY(20),
    CAT_LITTER(30),
    CAT_TREE(70);

    final int amountNeeded;

    Purpose(int amountNeeded) {
        this.amountNeeded = amountNeeded;
    }

    public int getAmountNeeded() {
        return amountNeeded;
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("name", this.name());  // Store the purpose name as a string
        json.put("amountNeeded", amountNeeded);
        return json;
        
    }
}
