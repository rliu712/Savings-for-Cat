package persistence;

import model.Purpose;
import model.Saving;
import model.SavingsHistory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;
import java.time.LocalDate;

import org.json.*;

// modified from (reference source): JSONSERIALIZATIONDEMO 
// other reference sources:
// https://www.geeksforgeeks.org/localdate-parse-method-in-java-with-examples/

// Represents a reader that reads saving history from JSON data stored in file
public class JsonReader {
    private String source;

    // EFFECTS: constructs reader to read from source file
    public JsonReader(String source) {
        this.source = source;
    }

    // EFFECTS: reads saving history from file and returns it;
    // throws IOException if an error occurs reading data from file
    public SavingsHistory read() throws IOException {
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);
        return parseSavingsHistory(jsonObject);
    }

    // EFFECTS: reads source file as string and returns it
    private String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s));
        }

        return contentBuilder.toString();
    }

    // EFFECTS: parses saving history from JSON object and returns it
    private SavingsHistory parseSavingsHistory(JSONObject jsonObject) {
        SavingsHistory sh = new SavingsHistory();
        addSavings(sh, jsonObject);
        return sh;
    }

    // MODIFIES: sh
    // EFFECTS: parses savings from JSON object and adds them to savings history
    private void addSavings(SavingsHistory sh, JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("full savings history");
        for (Object json : jsonArray) {
            JSONObject nextSaving = (JSONObject) json;
            addSaving(sh, nextSaving);
        }
    }

    // MODIFIES: sh
    // EFFECTS: parses a saving from JSON object and adds it to savings history
    private void addSaving(SavingsHistory sh, JSONObject jsonObject) {
        LocalDate date = LocalDate.parse(jsonObject.getString("date"));
        int amount = jsonObject.getInt("amount");
        Purpose purpose = Purpose.valueOf(jsonObject.getString("purpose"));
        
        Saving saving = new Saving(date, amount, purpose);
        sh.addSaving(saving);
    }
}
