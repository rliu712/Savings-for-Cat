package persistence;

import org.json.JSONObject;

//* modified from (reference source): JSONSERIALIZATIONDEMO */

public interface Writable {
    // EFFECTS: returns this as JSON object
    JSONObject toJson();
}
