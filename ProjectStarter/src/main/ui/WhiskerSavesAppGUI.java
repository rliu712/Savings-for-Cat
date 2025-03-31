package ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import model.Event;
import model.EventLog;
import model.Purpose;
import model.Saving;
import model.SavingsHistory;
import persistence.*;

import java.awt.*;
import java.awt.event.*;
import java.net.*;
import java.io.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

// This class is the Graphic UI of the app
// It allows users to interact with the app in a more graphical way
// The methods it currently implemented are:
// Loading & Saving the savings history; add savings & view savings
// referenced sources: 
// Alarm System Lecture Lab
//
// https://docs.oracle.com/javase/8/docs/api/javax/swing/JOptionPane.
// html#:~:text=JOptionPane%20makes%20it%20easy%20to,section%20in%20The%20Java%20Tutorial.
//
// https://stackoverflow.com/questions/28017323/how-to-accept-user-input-in-gui-java
//
// https://stackoverflow.com/questions/15694107/how-to-layout-multiple-panels-on-a-jframe-java
//
// https://codehs.com/tutorial/david/java-swing-buttons-layout
//
// https://stackoverflow.com/questions/50196206/save-state-of-java-gui
// 

public class WhiskerSavesAppGUI extends JFrame implements ActionListener {

    private JPanel buttonPanel;
    private JPanel displayPanel;
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;
    private static final String JSON_STORE = "./data/savingshistory.json";
    private SavingsHistory savingsHistory;
    private ArrayList<JLabel> movedSavingsHistory;

    @SuppressWarnings("methodlength")
    public WhiskerSavesAppGUI() {
        super("Whisker Saves");
        new SplashScreen();

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        addWindowListener(new CloseTheWindow());

        setLayout(new BorderLayout());
        setLayout(new GridLayout(2, 1));

        movedSavingsHistory = new ArrayList<JLabel>();
        
        buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setPreferredSize(new Dimension(500, 250));
        buttonPanel.setBackground(new Color(150, 250, 200));
        buttonPanel.setOpaque(true);
        getContentPane().add(buttonPanel, BorderLayout.CENTER);

        displayPanel = new JPanel(new FlowLayout());
        displayPanel.setPreferredSize(new Dimension(500, 250));
        displayPanel.setBackground(new Color(255, 250, 230));
        displayPanel.setOpaque(true);
        getContentPane().add(displayPanel, BorderLayout.SOUTH);

        addButtonPanel();
        addDisplayPanel();

        jsonWriter = new JsonWriter(JSON_STORE);
        jsonReader = new JsonReader(JSON_STORE);
        savingsHistory = new SavingsHistory();

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    

    // EFFECT: Adds all the buttons onto a panel in the frame
    private void addButtonPanel() {
        JPanel buttons = new JPanel();
        buttons.setLayout(new GridLayout(3, 0));
        buttons.add(new JButton(new LoadSavingActionJButton()));
        buttons.add(new JButton(new AddSavingActionJButton()));
        buttons.add(new JButton(new ViewSavingsHistoryJButton()));
        buttons.add(new JButton(new CheckFulfilledPurposesJButton()));
        buttons.add(new JButton(new SaveSavingsHistoryJButton()));
        buttons.add(new JButton(new MoveSavingJButton()));
        
        buttonPanel.add(buttons);
    }

    // EFFECT: Adds all savings history onto the display panel in the frame
    private void addDisplayPanel() {
        displayPanel.removeAll();
        displayPanel.setLayout(new BoxLayout(displayPanel, BoxLayout.Y_AXIS));
        
        if (savingsHistory != null) {
            for (Saving s : savingsHistory.getFullSavingHistory()) {
                String d = s.getDate().toString();
                Integer saved = s.getAmount();
                String p = s.getPurpose().toString();
                JLabel label = new JLabel(d + ": Saved $" + saved + " for " + p + "!");
                displayPanel.add(label);
            }
        } else {
            JLabel noSavingsLabel = new JLabel("No savings history.");
            displayPanel.add(noSavingsLabel);    // if sh is null, still put empty
        }

        if (movedSavingsHistory != null) {
            if (!movedSavingsHistory.isEmpty()) {
                for (JLabel ms : movedSavingsHistory) {
                    displayPanel.add(ms);
                }
            }
        }

        displayPanel.revalidate();
        displayPanel.repaint();
    }

    private void addMoveSavingRecord(Purpose original, Purpose desired, int amount) {
        String d = LocalDate.now().toString();

        JLabel moveSavingLabel = new JLabel(
                    d + ": $" + amount + " was moved from " + original + " to " + desired + "!");
        displayPanel.add(moveSavingLabel);

        movedSavingsHistory.add(moveSavingLabel);
    }



    private class CloseTheWindow extends WindowAdapter {
        public void windowClosing(WindowEvent e) {
            printLog(EventLog.getInstance());
            System.exit(0);
        }

        public void printLog(EventLog el) {
            for (Event next : el) {
                System.out.println(next.toString() + "\n\n");
            }
        }
    }

    // Represents the action that will be taken 
    // when user wants load saved saving history
    private class LoadSavingActionJButton extends AbstractAction {
        LoadSavingActionJButton() {
            super("Load saved savings history!");
        }

        // EFFECT: Load saved files for user
        @Override
        public void actionPerformed(ActionEvent evt) {
            WhiskerSavesAppGUI wsa = WhiskerSavesAppGUI.this;
            String m = "Unable to read from file";
            String t = "Loading failed";

            try {
                savingsHistory = jsonReader.read();
                addDisplayPanel();
            } catch (IOException e) {
                JOptionPane.showMessageDialog(wsa, m, t, JOptionPane.ERROR_MESSAGE);
            }
        }
    }


    // Represents the action that will be taken 
    // when user wants to add a new saving to the saving history
    private class AddSavingActionJButton extends AbstractAction {

        AddSavingActionJButton() {
            super("Add your saving of the day!");
        }

        // EFFECT: add the saving and display a "saved successfully" message if it's the first valid saving of the day
        //         if duplicate date - display "duplicated date" error message
        //         if non-existent purpose input - display "non-existent purpose" error message
        @SuppressWarnings("methodlength")
        @Override
        public void actionPerformed(ActionEvent evt) {
            LocalDate date = LocalDate.now();
            int amount = 0;
            
            WhiskerSavesAppGUI wsa = WhiskerSavesAppGUI.this;

            String msuccess = "Saving has been added successfully!";
            String tsuccess = "Saving added";

            // check if saving has been made today
            String m = "You've already made a saving today! Please try again!";
            String t = "Duplicated saving for today";
            if (isDuplicate(date)) {
                JOptionPane.showMessageDialog(wsa, m, t, JOptionPane.WARNING_MESSAGE);
                return;
            }

            // get amount of value from user
            String m1 = "How much do you want to save today?";
            String t1 = "Add saving";
            String amountInput = JOptionPane.showInputDialog(wsa, m1, t1, JOptionPane.QUESTION_MESSAGE);
            
            String em1 = "Amount is invalid. Please re-enter.";
            String et1 = "Invalid amount";
            try {
                amount = Integer.parseInt(amountInput);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(wsa, em1, et1, JOptionPane.ERROR_MESSAGE);
                return;
            }

            // get purpose of saving from user
            String m2 = "What purpose would you like to store it for?";
            String t2 = "Choose purpose";
            String purposeInput = JOptionPane.showInputDialog(wsa, m2, t2, JOptionPane.QUESTION_MESSAGE);
            
            String em2 = "Purpose doesn't exist, please re-enter.";
            String et2 = "Non-existent purpose.";

            Purpose purpose = getPurposeByName(purposeInput);
            if (purpose == null) {
                JOptionPane.showMessageDialog(wsa, em2, et2, JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            Saving saving = new Saving(date, amount, getPurposeByName(purposeInput));
            savingsHistory.addSaving(saving);

            addDisplayPanel();
            JOptionPane.showMessageDialog(wsa, msuccess, tsuccess, JOptionPane.INFORMATION_MESSAGE);
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
    }

    // Represents the action that will be taken 
    // when user wants to view all the fulfilled purposes
    private class CheckFulfilledPurposesJButton extends AbstractAction {

        private CheckFulfilledPurposesJButton() {
            super("Check fulfilled purposes!");
        }

        // EFFECT: display all the fulfilled purposes,
        //         if none - display "no fulfilled purposes" error message
        @Override
        public void actionPerformed(ActionEvent evt) {
            displayPanel.removeAll();
            
            WhiskerSavesAppGUI wsa = WhiskerSavesAppGUI.this;
            if (savingsHistory == null) {
                String sem = "No savings were made -- no purposes have been fulfilled! Please keep saving!";
                String set = "No savings history";
                JOptionPane.showMessageDialog(wsa, sem, set, JOptionPane.INFORMATION_MESSAGE);
            }
            
            if (savingsHistory.getFulfilledPurposes().isEmpty()) {
                String pem = "No purposes have been fulfilled! Please keep saving!";
                String pet = "No fulfilled purposes";
                JOptionPane.showMessageDialog(wsa, pem, pet, JOptionPane.INFORMATION_MESSAGE);
                JLabel noFpLabel = new JLabel("No fulfilled purposes.");
                displayPanel.add(noFpLabel);
            } else {
                JLabel label = new JLabel(savingsHistory.getFulfilledPurposes().toString());
                displayPanel.add(label);
            }

            displayPanel.revalidate();
            displayPanel.repaint();
    
        }
    }

    // Represents the action that will be taken 
    // when user wants to view the savings history (only implemented ALL history)
    private class ViewSavingsHistoryJButton extends AbstractAction {

        ViewSavingsHistoryJButton() {
            super("View savings!");
        }

        // EFFECT: display the full saving history on the panel
        @Override
        public void actionPerformed(ActionEvent evt) {
            addDisplayPanel();
        }
    }

    // Represents the action that will be taken 
    // when user wants to view the savings history (only implemented ALL history)
    private class MoveSavingJButton extends AbstractAction {
        MoveSavingJButton() {
            super("Move saving!");
        }

        WhiskerSavesAppGUI wsa = WhiskerSavesAppGUI.this;

        @SuppressWarnings("methodlength")
        // EFFECT: save user's saving history for future loading
        @Override
        public void actionPerformed(ActionEvent evt) {

            // get purpose to remove from from user
            String moriginal = "What purpose would you like to move the saving from?";
            String toriginal = "Choose purpose";
            String purposeInputOriginal = JOptionPane.showInputDialog(
                        wsa, moriginal, toriginal, JOptionPane.QUESTION_MESSAGE);
            
            String emOriginal = "Purpose doesn't exist, please re-enter.";
            String etOriginal = "Non-existent purpose.";

            Purpose purposeOriginal = getPurposeByName(purposeInputOriginal);
            int originalSize = savingsHistory.getSavingsForPurpose(purposeOriginal).size();
            if (purposeOriginal == null) {
                JOptionPane.showMessageDialog(wsa, emOriginal, etOriginal, JOptionPane.ERROR_MESSAGE);
                return;
            }

            // get purpose to add to from user
            String mdesired = "What purpose would you like to move the saving to?";
            String tdesired = "Choose purpose";
            String purposeInputDesired = JOptionPane.showInputDialog(
                    wsa, mdesired, tdesired, JOptionPane.QUESTION_MESSAGE);
            
            String emOriginal1 = "Purpose doesn't exist, please re-enter.";
            String etOriginal1 = "Non-existent purpose.";

            Purpose purposeDesired = getPurposeByName(purposeInputDesired);
            if (purposeDesired == null) {
                JOptionPane.showMessageDialog(wsa, emOriginal1, etOriginal1, JOptionPane.ERROR_MESSAGE);
                return;
            }

            // get amount to move from user
            int amount = 0;
            String m1 = "What's the amount of the saving?";
            String t1 = "Move saving";
            String amountInput = JOptionPane.showInputDialog(wsa, m1, t1, JOptionPane.QUESTION_MESSAGE);
            
            String em1 = "Amount is invalid. Please re-enter.";
            String et1 = "Invalid amount";
            try {
                amount = Integer.parseInt(amountInput);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(wsa, em1, et1, JOptionPane.ERROR_MESSAGE);
                return;
            }

            savingsHistory.moveSaving(purposeOriginal, purposeDesired, amount);

            String msuccess = "Saving has been moved successfully!";
            String tsuccess = "Saving moved";
            
            String merror = "The saving you want to move was not saved. Please try again.";
            String terror = "Move saving failed";
            if (savingsHistory.getSavingsForPurpose(purposeOriginal).size() < originalSize) {
                addMoveSavingRecord(purposeOriginal, purposeDesired, amount);
                JOptionPane.showMessageDialog(wsa, msuccess, tsuccess, JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(wsa, merror, terror, JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Represents the action that will be taken 
    // when user wants to view the savings history (only implemented ALL history)
    private class SaveSavingsHistoryJButton extends AbstractAction {

        SaveSavingsHistoryJButton() {
            super("Save current savings history!");
        }

        // EFFECT: save user's saving history for future loading
        @Override
        public void actionPerformed(ActionEvent evt) {
            WhiskerSavesAppGUI wsa = WhiskerSavesAppGUI.this;

            String m = "Saved savings history successfully";
            String t = "Save success";

            String em = "Unable to save your progress.";
            String et = "Saving failed";

            try {
                jsonWriter.open();
                jsonWriter.write(savingsHistory);
                jsonWriter.close();
            } catch (FileNotFoundException e) {
                JOptionPane.showMessageDialog(wsa, em, et, JOptionPane.ERROR_MESSAGE);
            }
            JOptionPane.showMessageDialog(wsa, m, t, JOptionPane.INFORMATION_MESSAGE);
        }
    }

    

    // EFFECT: returns the purpose that matches with the input purpose
    private Purpose getPurposeByName(String purposeInput) {
        for (Purpose p : Purpose.values()) {
            if (p.checkEquals(purposeInput)) {
                return p;
            }
        }
        return null;
    }

    // EFFECT: Saves progress once the a button is pressed
    public void actionPerformed(ActionEvent e) {
    }

    public static void main(String[] args) {
        new WhiskerSavesAppGUI();
    }
}
