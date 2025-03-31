package ui;

import java.util.*;

import model.Purpose;
import model.Saving;
import model.SavingsHistory;
import persistence.JsonReader;
import persistence.JsonWriter;

import java.time.LocalDate;

import java.io.FileNotFoundException;
import java.io.IOException;

/*
reference source (other than the TellerApp class):
 1. www.geeksforgeeks.org/enum-in-java/ 
 2. WorkRoomApp class from JSONSERIALIZATIONDEMO*/

public class WhiskerSavesApp {
    private static final String JSON_STORE = "./data/savingshistory.json";
    private SavingsHistory savingsHistory;
    private Saving saving;
    private Scanner input;
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;

    // EFFECTS: constructs savingsHistory and runs the WhiskerSaves application
    public WhiskerSavesApp() throws FileNotFoundException {
        input = new Scanner(System.in);
        savingsHistory = new SavingsHistory();
        jsonWriter = new JsonWriter(JSON_STORE);
        jsonReader = new JsonReader(JSON_STORE);
        runWhisker();
    }

    // MODIFIES: this
    // EFFECTS: processes user input
    private void runWhisker() {
        boolean keepGoing = true;
        String command = null;

        init();

        while (keepGoing) {
            displayMenu();
            command = input.next();
            command = command.toLowerCase();

            if (command.equals("q")) {
                keepGoing = false;
            } else {
                processCommand(command);
            }
        }
        System.out.println("See you next time!");
    }

    // MODIFIES: this
    // EFFECTS: processes user command
    private void processCommand(String command) {
        if (command.equals("a")) {
            addSaving();
        } else if (command.equals("h")) {
            displaySavingsHistory();
        } else if (command.equals("d")) {
            displaySavingMadeOnDate();
        } else if (command.equals("p")) {
            checkPurposeProgress();
        } else if (command.equals("l")) {
            checkFulfilledPurposes();
        } else if (command.equals("s")) {
            saveSavingsHistory();
        } else if (command.equals("r")) {
            reloadSavingsHistory();
        } else {
            System.out.println("Paws! Option does not exist.");
        }
    }

    // MODIFIES: this
    // EFFECTS: initializes accounts
    private void init() {
        savingsHistory = new SavingsHistory();
        input = new Scanner(System.in);
        input.useDelimiter("\r?\n|\r");
    }

    // EFFECTS: display menu of option to user
    private void displayMenu() {
        System.out.println("\ta -> add a new saving");
        System.out.println("\th -> view savings history");
        System.out.println("\td -> check saving made on date");
        System.out.println("\tp -> check saving progress for purpose");
        System.out.println("\tl -> check fulfilled purposes");
        System.out.println("\ts -> save savings history to file");
        System.out.println("\tr -> reload savings history from file");
        System.out.println("\tq -> quit");
    }

    // MODIFIES: this
    // EFFECTS: add a new saving to saving history
    @SuppressWarnings("methodlength")
    private void addSaving() {
        System.out.print("Let's document your saving for today! ");
        
        LocalDate time = getDate();
        Purpose savingPurpose = getPurpose();
        int amount = getAmount();

        saving = new Saving(time, amount, savingPurpose);
        savingsHistory.addSaving(saving);

        System.out.println("Ameowzing! Saving has been added!");
    }

    // EFFECTS: view savings history according to user's choice
    private void displaySavingsHistory() {
        String selection = "";  // force entry into loop

        while (!(selection.equals("f") || selection.equals("p"))) {
            System.out.println("f for viewing full saving history");
            System.out.println("sp for viewing the saving history that's made towards your selected purpose");
            selection = input.next().toLowerCase();
        }

        if (selection.equals("f")) {
            printSavings(savingsHistory.getFullSavingHistory());
        } else if (selection.equals("sp")) {
            System.out.println("What purpose would you like to view? ");
            Purpose purpose = getPurpose();
            if (purpose != null) {
                printSavings(savingsHistory.getSavingsForPurpose(purpose));
            } else {
                System.out.println("Paws! The purpose you've entered does not exist!");
            } 
        } else {
            System.out.println("Paws! Selection is invalid!");
        }
    }

    // EFFECT: get a date from User
    private LocalDate getDate() {
        while (true) {
            try {
                System.out.println("Please enter the year: ");
                Integer year = input.nextInt();
                System.out.println("Please enter the month: ");
                Integer month = input.nextInt();
                System.out.println("Please enter the date: ");
                Integer date = input.nextInt();
                LocalDate time = LocalDate.of(year, month, date);

                if (isDuplicate(time)) {
                    System.out.println("You've already made a saving on this day. Please enter another date.");
                }

                return time;
            } catch (InputMismatchException e) {
                System.out.println("Please re-enter you date in 2-digit integers (ex. Feb -> 02)");
                input.next();
            }
        }
    }

    // EFFECT: check if current saving's date duplicates with previous savings' dates
    private boolean isDuplicate(LocalDate date) {
        for (Saving s : savingsHistory.getFullSavingHistory()) {
            if (s.getDate().equals(date)) {
                return true;
            }
        }
        return false;
    }

    // EFFECT: get a Purpose from User
    private Purpose getPurpose() {
        int purposeNum = 1;
        for (Purpose p : Purpose.values()) {
            System.out.println(purposeNum + ": " + p + "($" + p.getAmountNeeded() + ")");
            purposeNum++;
        }

        while (true) {                // loop will run forever until a Purpose is returned!
            try {
                System.out.println("Please select a purpose by entering its number: ");
                int purposeSelected = input.nextInt();
                if (purposeSelected >= 1 && purposeSelected <= Purpose.values().length) {
                    return Purpose.values()[purposeSelected - 1];
                } else {
                    System.out.println("Paws! The choice you selected doesn't exist! Please try again. ");
                }
            } catch (InputMismatchException e) {
                System.out.println("Paws! That's not a valid number. Please try again. ");
                input.next();        // next() clears the invalid input that's previously put in
            }
        }
    }

    // EFFECT: get a valid amount from User
    private int getAmount() {
        while (true) {
            try {
                System.out.println("How much do you want to save today? ");
                int amount = input.nextInt();
                if (amount > 0) {
                    return amount;
                } else {
                    System.out.println("Paws! The amount must be greater than 0, Please try again. ");
                }
            } catch (InputMismatchException e) {
                System.out.println("Paws! Please enter a valid number. ");
                input.next();
            }
        }
    }

    // EFFECTS: display the saving that's made on a specific date
    private void displaySavingMadeOnDate() {
        LocalDate time = null;
        while (time == null) {
            System.out.println("Please enter the year: ");
            Integer year = input.nextInt();
            System.out.println("Please enter the month: ");
            Integer month = input.nextInt();
            System.out.println("Please enter the date: ");
            Integer date = input.nextInt();
            Saving savingOnDate = savingsHistory.getSavingOnDate(time);
            time = LocalDate.of(year, month, date);
            savingOnDate = savingsHistory.getSavingOnDate(time);
            if (savingOnDate != null) {
                printSaving(savingOnDate);
                break;
            } else {
                System.out.println("No saving was made on this date.");
            }
        }
    }

    // EFFECTS: check progress towards the chosen purpose
    private void checkPurposeProgress() {
        System.out.println("Which purpose would you like to check progress for? ");
        Purpose purpose = getPurpose();

        boolean isValidPurpose = false;
        if (purpose != null) {
            isValidPurpose = true;
            int progress = savingsHistory.savingProgressMade(purpose);
            int stillNeed = savingsHistory.savingAmountStillNeed(purpose);
                    
            if (progress == 100 && stillNeed == 0) {
                System.out.println("Progress is at 100%! Purpose has been met!!!");
            } else {
                System.out.println("Progress is at " + progress + "%");
                System.out.println("Purpose still needs $" + stillNeed + " to fulfill!");
            }
        }
            
        if (isValidPurpose == false) {
            System.out.println("Paws! The purpose you've entered does not exist!");
        }
    }

    // EFFECTS: print a list of purposes that has been fulfilled
    private void checkFulfilledPurposes() {
        List<Purpose> fulfilledP = savingsHistory.getFulfilledPurposes();

        if (fulfilledP.isEmpty()) {
            System.out.println("No purposes have been fulfilled yet.");
        } else {
            System.out.println("Fulfilled purposes: " + fulfilledP);
        }
    }
    
    // EFFECTS: print a single saving
    private void printSaving(Saving saving) {
        System.out.println(saving.getDate() + ": $" + saving.getAmount());
        System.out.println(" was stored for buying " + saving.getPurpose());
    }

    // EFFECTS: print a list of savings
    private void printSavings(List<Saving> savings) {
        if (savings.isEmpty()) {
            System.out.println("There's no savings :(");
        } else {
            for (Saving s : savings) {
                printSaving(s);
            }
        }
    }

    // EFFECTS: save the entire state of WhiskerSavesApp
    private void saveSavingsHistory() {
        try {
            jsonWriter.open();
            jsonWriter.write(savingsHistory);
            jsonWriter.close();
            System.out.println("Saved " + savingsHistory.getFullSavingHistory() + " to " + JSON_STORE);
        } catch (FileNotFoundException e) {
            System.out.println("Unable to write to file: " + JSON_STORE);
        }
    }

    // EFFECTS: reload the state from saved file and resume 
    //          from where they left off last time
    private void reloadSavingsHistory() {
        try {
            savingsHistory = jsonReader.read();
            System.out.println("Loaded " + savingsHistory.getFullSavingHistory() + "from " + JSON_STORE);
        } catch (IOException e) {
            System.out.println("Unable to read from file: " + JSON_STORE);
        }
    }
}
