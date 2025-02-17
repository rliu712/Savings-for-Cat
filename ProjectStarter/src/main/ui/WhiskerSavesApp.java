package ui;

import java.util.*;

import model.Purpose;
import model.Saving;
import model.SavingsHistory;

import java.time.LocalDate;

/*
reference source (other than the TellerApp class):
 www.geeksforgeeks.org/enum-in-java/ */

public class WhiskerSavesApp {
    private SavingsHistory savingsHistory;
    private Scanner input;

    // EFFECTS: runs the WhiskerSaves application
    public WhiskerSavesApp() {
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
        System.out.println("\tq -> quit");
    }

    // MODIFIES: this
    // EFFECTS: add a new saving to saving history
    @SuppressWarnings("methodlength")
    private void addSaving() {
        System.out.print("Let's document your saving for today!");
        
        System.out.println("Please enter the year: ");
        Integer year = input.nextInt();
        System.out.println("Please enter the month: ");
        Integer month = input.nextInt();
        System.out.println("Please enter the date: ");
        Integer date = input.nextInt();
        LocalDate time = LocalDate.of(year, month, date);

        System.out.println("Awesome! What purpose would you like to store your money for? ");
        
        Purpose savingPurpose = null;
        while (savingPurpose == null) {
            System.out.println(Purpose.FISH_CANS + " -> $" + Purpose.FISH_CANS.getAmountNeeded());
            System.out.println(Purpose.MICE_TOYS + " -> $" + Purpose.MICE_TOYS.getAmountNeeded());
            System.out.println(Purpose.CAT_LITTER + " -> $" + Purpose.CAT_LITTER.getAmountNeeded());
            System.out.println(Purpose.CAT_TREE + " -> $" + Purpose.CAT_TREE.getAmountNeeded());
            
            System.out.println("Enter the purpose name: ");
            String purposeName = input.next().toUpperCase();
            for (Purpose p : Purpose.values()) {
                if (p.name().equals(purposeName)) {
                    savingPurpose = p;
                    break;
                }
            }

            if (savingPurpose == null) {
                System.out.println("Paws! The purpose you've entered does not exist!");
            }
        }
        
        System.out.println("How much do you want to save today? ");
        int amount = input.nextInt();

        Saving newSaving = new Saving(time, amount, savingPurpose);
        savingsHistory.addSaving(newSaving);

        System.out.println("Ameowzing! Saving has been added!");
    }



    // EFFECTS: view savings history according to user's choice
    @SuppressWarnings("methodlength")
    private void displaySavingsHistory() {

        String selection = "";  // force entry into loop

        while (!(selection.equals("f") || selection.equals("p"))) {
            System.out.println("f for viewing full saving history");
            System.out.println("p for viewing the saving history that's made towards your selected purpose");
            selection = input.next().toLowerCase();
        }

        if (selection.equals("f")) {
            printSavings(savingsHistory.getFullSavingHistory());
        } else if (selection.equals("p")) {
            System.out.println("What purpose would you like to view? ");
            System.out.println(Purpose.FISH_CANS + " -> $" + Purpose.FISH_CANS.getAmountNeeded());
            System.out.println(Purpose.MICE_TOYS + " -> $" + Purpose.MICE_TOYS.getAmountNeeded());
            System.out.println(Purpose.CAT_LITTER + " -> $" + Purpose.CAT_LITTER.getAmountNeeded());
            System.out.println(Purpose.CAT_TREE + " -> $" + Purpose.CAT_TREE.getAmountNeeded());
            System.out.println("Enter the purpose name: ");
            String purposeName = input.next().toUpperCase();

            boolean isValidPurpose = false;
            for (Purpose p : Purpose.values()) {
                if (p.name().equals(purposeName)) {
                    isValidPurpose = true;
                    printSavings(savingsHistory.getSavingsForPurpose(p));
                }
            }

            if (isValidPurpose == false) {
                System.out.println("Paws! The purpose you've entered does not exist!");
            } 
        } else {
            System.out.println("Paws! Selection is invalid!");
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
    @SuppressWarnings("methodlength")
    private void checkPurposeProgress() {
        System.out.println("Which purpose would you like to check progress for? ");
        System.out.println(Purpose.FISH_CANS + " -> $" + Purpose.FISH_CANS.getAmountNeeded());
        System.out.println(Purpose.MICE_TOYS + " -> $" + Purpose.MICE_TOYS.getAmountNeeded());
        System.out.println(Purpose.CAT_LITTER + " -> $" + Purpose.CAT_LITTER.getAmountNeeded());
        System.out.println(Purpose.CAT_TREE + " -> $" + Purpose.CAT_TREE.getAmountNeeded());

        System.out.println("Enter the purpose name: ");
        String purposeName = input.next().toUpperCase();

        boolean isValidPurpose = false;
        for (Purpose p : Purpose.values()) {
            if (p.name().equals(purposeName)) {
                isValidPurpose = true;

                int progress = savingsHistory.savingProgressMade(p);
                int stillNeed = savingsHistory.savingAmountStillNeed(p);
                    
                if (progress == 100 && stillNeed == 0) {
                    System.out.println("Progress is at 100%! Purpose has been met!!!");
                } else {
                    System.out.println("Progress is at " + progress + "%");
                    System.out.println("Purpose still needs $" + stillNeed + " to fulfill!");
                }
                break;
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


}
