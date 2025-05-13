# Rhiannon's Personal Project
My project will create a saving tracker that keeps track of the amount of money that's being saved with different categories (purposes). In this case, the categories of the savings will be *buying*:
- fish can (5 dollars a can)
- mice toy (20 dollars each)
- cat litter (30 dollars a bag)
- and cat trees (70 dollars each).
The user can only put in 1 saving per day, so it's quite a journey!

This application is created for cat lovers like me! This project interests me personally because though I'm a cat lover, I can't own any yet (due to that my family is allergic to them). So, I want to build this project to encourage and remind myself to save up money for a cat!



## User Stories

**Phase 0 (& Phase 1)**
- As a user, I want to add a new saving record to my savings collection, and specify the amount of money put in, date, and its purpose (buying a bag of cat litter)
- As a user, I want to view the list of savings I've have for the purpose of buying mice toys
- As a user, I want to view how much money I still need to buy a bag of cat litter ($30)
- As a user, I want to move a saving from the category of buying cat litter to buying cat trees

**Phase 2**
- As a user, when I select the quit option from the application menu, I want to be reminded to save my savings history to file and have the option to do so or not.
- As a user, when I start the application, I want to be given the option to load my saving history from file.

**Phase 3**
- As a user, I want to be able to add multiple savings to a savings history,
    My GUI must include a panel in which all the savings that have already been added to saving history are displayed. 
    In addition, my project will have:
    - A button that can be used to add a saving to a savings history
    - A button that displays the set of fulfilled purposes (subset of savings history but in the form of fulfilled purposes)
- As a user, I wanted to have the option load data from file when the application starts and have the option to save data to file when the application ends
    (My GUI would have have buttons that lets the user load data when the application starts and to save it when the application ends)

**Phase 4: Task 2**
-   Sat Mar 29 13:45:08 PDT 2025
    Added saving: $5 for FISH_CAN
-   Sat Mar 29 13:45:23 PDT 2025
    A $5 saving has been moved from FISH_CAN to CAT_LITTER!

**Phase 4: Task 3**

According to my UML diagram, I believe that I can improve my program by refactoring the inner-classes from WhiskerSavesAppGUI by taking them out of the GUI class and put them into their own, separate classes. This can provide a clearer documentation & readability of the responsibilities each class holds. For example, each JButton in the GUI class would make a call to their according class (which is separated from the GUI class), where the actual functionality of the button executes.

I'd also like the separate different methods into different classes. For example, I'd like to put all the methods that are dedicated to fulfilling the purpose of displaying & adding items on the JPanel into 1 separate class. 
