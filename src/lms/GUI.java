/*
 * Copyright Alex Kinross-Smith (c) 2016.
 * s3603437@student.rmit.edu.au
 * alex@akinrosssmith.id.au
 */

/*
*@author - Alex Kinross-Smith
*/
package lms;

import lms.util.AdminVerify;
import lms.util.DateTime;

import java.io.IOException;
import java.util.Scanner;

/**
 * Created by akinr on 11/04/2016.
 */
public class GUI {
    //ANSI Codes from http://stackoverflow.com/questions/5762491/how-to-print-color-in-console-using-system-out-println
    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_BLACK = "\u001B[30m";
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_GREEN = "\u001B[32m";
    private static final String ANSI_YELLOW = "\u001B[33m";
    private static final String ANSI_BLUE = "\u001B[34m";
    private static final String ANSI_PURPLE = "\u001B[35m";
    private static final String ANSI_CYAN = "\u001B[36m";
    private static final String ANSI_WHITE = "\u001B[37m";
    private static final String[] CHOICE_OPTIONS = {"y (yes)", "n (no)", "e (exit)"};
    private static final String[] MEMBER_OPTIONS = {"s (standard)", "p (premium)"};
    private static final String[] HOLDING_OPTIONS = {"b (book)", "v (video)"};
    private static final char[] MEMBER_TYPES = {'s', 'p'};
    private static final char[] HOLDING_TYPES = {'b', 'v'};
    private Scanner input = new Scanner(System.in);
    private Inventory inv;
    //Functional Methods

    public GUI() {
        String[] choiceOptions = {"y", "n"};
        char choice = receiveStringInput("Do you want to enable expanded inventory?", choiceOptions, "n", 1).charAt(0);
        if (choice == 'n') {
            inv = new Inventory();
        } else {
            System.out.println("Enter an integer for max number of holdings:");
            int holdings = input.nextInt();
            System.out.println("Enter an integer for max number of members:");
            int members = input.nextInt();
            this.inv = new Inventory(holdings, members);
        }

    }

    /**
     * Prints the specified character "c" number of "times", then if new line == true prints a new line at the end
     **/
    public static void printCharTimes(char c, int times, boolean newLine) {
        //Prints the character times.
        for (int i = 0; i < times; i++) {
            System.out.print(c);
        }
        //If we want to print a new line, print a new line.
        if (newLine) {
            System.out.println();
        }
    }

    /**
     * Creates a new page with the title "title"
     **/
    private void newPage(String title) {
        //Sets the width of the page.
        try {
            Runtime.getRuntime().exec("cls");
        }catch (IOException e){}
        int pageWidth = 150;
        //Print a new line a bunch to clear the screen. WHY IS THERE NO PLATFORM INDEPENDENT SOLUTION LIKE CLS
        for (int i = 30; i != 0; i--) {
            System.out.printf("\n");
        }
        //Print the first line, made of equal signs.
        printCharTimes('=', pageWidth, true);
//Set the three components of a menu screen.
        String leftText = DateTime.getCurrentTime();
        String centreText = "Library Management System: " + title;
        String rightText = inv.infoPrintout();
//Find the length of the menu areas.
        int left = leftText.length();
        int centre = centreText.length();
        int right = rightText.length();
//Find the spacing between the three elements. Total width/2 - the size of left and half of the centre.
        int leftSpacing = pageWidth / 2 - left - centre / 2;
        //Due to float to int conversion, subtract 1 more than the length.
        int rightSpacing = (pageWidth / 2) - 1 - right - centre / 2;
//Print the left
        System.out.print(ANSI_YELLOW);
        System.out.print(leftText);
        //Print the spacing
        printCharTimes(' ', leftSpacing, false);
        //Print the centre
        System.out.print(centreText);
        //Print the right spacing
        printCharTimes(' ', rightSpacing, false);
        //Print the right text
        System.out.println(rightText);
        System.out.print(ANSI_RESET);
//Finish off the menu with another border.
        printCharTimes('=', pageWidth, true);
    }

    /**
     * Returns an array of Strings (for example {a,b,c} as a String in the format (a/b/c)
     **/
    private String stringArrayToString(String[] array) {
        String result = ANSI_YELLOW + "{" + ANSI_RESET;
        //Add all the things together
        for (int i = 0; i < array.length; i++) {
            //If this isn't the last entry in the array, add a / on as well
            if (i < array.length - 1) {
                result += array[i] + ANSI_YELLOW + "/" + ANSI_RESET;
            } else { //If it is the last entry, add a closing bracket instead.
                result += array[i] + ANSI_YELLOW + "}" + ANSI_RESET;
            }
        }
        return (result);
    }

    /**
     * Receives an input. Prints the flavourText, then requests input from the user. Will continue requesting input from the user until input matches an entry in the array options. if printOptionText is false will not show the user what options are available.
     * Final number is length of returned string and length of input that will be compared to the strings.
     **/
    private String receiveStringInput(String flavourText, String[] options, boolean printOptionText, int outputLength) {
        //Print the flavour text.
        System.out.print(flavourText + " ");
        //Make sure that we don't npe anywhere
        if (outputLength <= 0) {
            outputLength = 1;
        }
        //If we have enabled option printing, print the array.
        if (printOptionText) {
            System.out.println(stringArrayToString(options));
        } else System.out.println(); //Otherwise end the line.
        //Receive input
        String inputString = input.nextLine().toLowerCase();
        //If it is too short, prompt for input again.
        while (inputString.length() == 0) {
            //Print the stuff again.
            System.out.println("Answer needs to be entered");
            System.out.print(flavourText + " ");
            if (printOptionText) {
                System.out.println(stringArrayToString(options));
            } else System.out.println();
            //Request input again.
            inputString = input.nextLine().toLowerCase();
        }
        //Ensure input is the same.
        String inputChar = inputString.toLowerCase();
        //If the input we are given is longer than the specified maximum output, make it shorter.
        if (inputChar.length() >= outputLength) {
            inputChar = inputChar.substring(0, outputLength);
        }
        boolean isCorrect = false;
        int i = 0;
        while (i < options.length && !isCorrect) {
            String currentExamined;
            if (options[i].length() >= outputLength) {
                currentExamined = options[i].substring(0, outputLength);
            } else currentExamined = options[i];
            if (currentExamined.equals(inputChar)) {
                isCorrect = true;
            }
            i++;
        }
        if (isCorrect) {
            return (inputChar);
        } else {
            System.out.println("Input was not an option. Please try again.");
            inputChar = receiveStringInput(flavourText, options, true, outputLength);//If they didn't get it right the first time, supply options.
        }
        return (inputChar);
    }

    /**
     * Receives an input. Prints the flavourText, then requests input from the user. Will continue requesting input from the user until input matches an entry in the array options or is empty. if it is empty returns the defaultAnswer. Final number is length of returned string
     **/
    private String receiveStringInput(String flavourText, String[] options, String defaultAnswer, int outputLength) {
        //Print a prompt for the user to enter input.
        System.out.println(flavourText + " " + stringArrayToString(options) + ANSI_RED + "[" + defaultAnswer + "]" + ANSI_RESET);
        //Ensure we don't try to access a negative array index.
        if (outputLength <= 0) {
            outputLength = 1;
        }
        //Actually get input.
        String inputString = input.nextLine().toLowerCase();
        //If the user entered nothing, return the default input.
        if (inputString.length() == 0) {
            return ("" + defaultAnswer);
        }
        String inputChar = inputString.toLowerCase();
        if (inputChar.length() >= outputLength) {
            inputChar = inputChar.substring(0, outputLength);
        }
        boolean isCorrect = false;
        int i = 0;
        while (i < options.length && !isCorrect) {
            String currentExamined;
            if (options[i].length() >= outputLength) {
                currentExamined = options[i].substring(0, outputLength);
            } else currentExamined = options[i];
            if (currentExamined.equals(inputChar)) {
                isCorrect = true;
            }
            i++;
        }
        if (isCorrect) {
            return (inputChar);
        } else {
            System.out.println("Input was not an option. Please try again.");
            inputChar = receiveStringInput(flavourText, options, defaultAnswer, outputLength);//If they didn't get it right the first time, supply options.
        }
        return inputChar;
    }

    /**
     * Prompts the user for an ID and returns it.
     * TypeID is a string that will be displayed when asking the user for and ID, and expectedTypes restricts input(i.e if someone enters s000001, but the user is searching for a holding it wont work.
     * If user doesn't want to enter input returns null.
     **/
    private String getExistingID(String typeID, char[] expectedTypes) {
        System.out.println("Enter " + typeID + " ID:");
        String ID = input.nextLine();
        boolean result = inv.idExists(ID);
        if (result) {
            result = false;
            for (int i = 0; i < expectedTypes.length; i++)
                if (ID.charAt(0) == expectedTypes[i]) {
                    result = true;
                }
        }
        char choice;
        //If the ID doesn't exist, prompt the user for a working ID
        if (!result) {
            //Ask them if they want to continue or return to the main menu
            choice = receiveStringInput("Incorrect " + typeID + " ID. Do you want to try again?", CHOICE_OPTIONS, "y", 1).charAt(0);
            if (choice == 'e') return null;
            //If they want to continue give them 4 tries so they don't get stuck here.
            if (choice == 'y') {
                System.out.println("You have 4 tries before returning to main menu");
                int tries = 0;
                //End the loop either upon entering a correct id, or using up all the tries
                while (!result && tries < 4) {
                    System.out.println((tries + 1) + ": Enter a valid " + typeID + " ID:");
                    ID = input.nextLine();
                    result = inv.idExists(ID);
                    if (result) {
                        result = false;
                        for (int i = 0; i < expectedTypes.length; i++)
                            if (ID.charAt(0) == expectedTypes[i]) {
                                result = true;
                            }
                    }
                    tries++;
                }
                //If they time out because of tries, return them to main menu
                if (tries == 4) {
                    return null;
                }
                //If they choose no earlier return them to main menu.
            } else {
                return null;
            }
        }
        return (ID);
    }

    /**
     * Adds the default holdings
     **/
    private void addDefault() {
        String[] holdingTitle = {"Intro to Java", "Learning UML", "Design Patterns", "Advanced Java", "Java 1", "Java 2", "UML 1", "UML 2"};
        char[] holdingType = {'b', 'b', 'b', 'b', 'v', 'v', 'v', 'v'};
        String[] holdingID = {"000001", "000002", "000003", "000004", "000001", "000002", "000003", "000004"};
        int[] holdingFee = {0, 0, 0, 0, 4, 6, 6, 4};
        for (int i = 0; i < holdingID.length; i++) {
            inv.addHolding(holdingID[i], holdingType[i], holdingTitle[i], holdingFee[i]);
        }
        String[] memberID = {"000001", "000001", "000002", "000002"};
        String[] memberName = {"Joe Bloggs", "Fred Bloggs", "Jane Smith", "Fred Smith"};
        char[] memberType = {'s', 'p', 's', 'p'};
        for (int i = 0; i < memberID.length; i++) {
            inv.addMember(memberID[i], memberType[i], memberName[i]);
        }
        //inv.recalculateStatistics();
    }

    private String[] getValidID(String typeID, String[] types) {
        char type;
        if (types.length > 1) {
            type = receiveStringInput("Enter type of" + typeID + ":", types, true, 1).charAt(0);
        } else {
            type = types[0].charAt(0);
        }
        //Not a choice, requesting input. If they don't enter anything it will be caught below, and one will be generated for them.
        System.out.println("Enter the unique 6 digit ID or press enter to have one generated:");
        String ID = input.nextLine();

        //Check validity of the entered information
        String IDResult = inv.checkID(ID, type);
        //Print the result
        System.out.println(IDResult);
        switch (IDResult.toLowerCase()) {
            case "wrong number of digits":
            case "already taken":
                //If the ID number was already taken or had the wrong number of digits request a new one or give the user an already generated one.
                String newID = inv.generateValidID(type);
                //Using the variant of the choice input that has a default option. Pressing enter will select that default option, rather than prompting for a non null result.
                char choice = receiveStringInput(newID + " is not taken. Do you want to set it as the id?", CHOICE_OPTIONS, "y", 1).charAt(0);
                //If the user has selected to use the generated ID, use that one.
                if (choice == 'y') {
                    ID = newID;
                } else { //If the user is an idiot and wants to try entering a 6 digit number that they somehow got wrong the first time again, give them the choice.
                    do {
                        //Ask for the new ID
                        System.out.println("Enter new 6 digit ID number");
                        //Receive the new ID as input
                        ID = input.nextLine();
                        //Print the result
                        System.out.println(inv.checkID(ID, type));
                    }
                    while (!inv.checkID(ID, type).equalsIgnoreCase("valid")); //If they somehow still fail to enter a 6 digit number that is different from the other 15 6 digit number, let them try again.
                }
                break;
            case "invalid type":
                //If they entered type is incorrect(somehow, as the input method won't let an incorrect one be entered), request a valid one.
                do {
                    type = receiveStringInput("Invalid type, try again", HOLDING_OPTIONS, true, 1).charAt(0);
                    System.out.println(inv.checkID(ID, type));
                } while (!inv.checkID(ID, type).equalsIgnoreCase("valid"));
                break;
            case "id contains characters":
                do {
                    System.out.println("Invalid ID. Can only contain numbers, try again");
                    ID = input.nextLine();
                    System.out.println(inv.checkID(ID, type));
                } while (!inv.checkID(ID, type).equalsIgnoreCase("valid"));
                break;
            case "valid": //If it is valid, keep going.
                break;
            default: //Any further errors will be caused by the ID been incorrect, so prompt for it again.
                do {
                    System.out.println("Enter new 6 digit ID number");
                    ID = input.nextLine();
                    System.out.println(inv.checkID(ID, type));
                } while (!inv.checkID(ID, type).equalsIgnoreCase("valid"));
        }
        return new String[]{"" + type, ID};
    }

    //UI Methods
    public void logoBoot() {
        System.out.printf("                                                            \n" +
                "                                                            \n" +
                "                                                            \n" +
                "                                  +                         \n" +
                "                                ++++'                       \n" +
                "                               +++ +++                      \n" +
                "                             '++'   +++;                    \n" +
                "                            +++       +++                   \n" +
                "                           +++         +++                  \n" +
                "                         '++'           '++;                \n" +
                "                        +++               +++               \n" +
                "                       +++                 +++              \n" +
                "                     +++                     +++            \n" +
                "                    +++                       +++           \n" +
                "                  '+++                         +++          \n" +
                "                 +++                             +++        \n" +
                "                +++                               +++       \n" +
                "              +++                                  '++'     \n" +
                "             +++                                     +++    \n" +
                "            +++++++++++++++++++++++++++++++++++++++++++++   \n" +
                "            +++++++++++++++++++++++++++++++++++++++++++++   \n" +
                "                                                            \n" +
                "                                                            \n" +
                "              +++++++    ++++++++  ;++++++++    +++++++     \n" +
                "              +++++++   +++++++++++++++++++++  '+++++++     \n" +
                "              +;    +   +'       +++       ++  '+    '+     \n" +
                "              +;    +   +        ;+        '+  '+    '+     \n" +
                "              +;    +   +        ;+        '+  '+    '+     \n" +
                "              +;    +   +        ;+        '+  '+    '+     \n" +
                "              +;    +   +        ;+        '+  '+    '+     \n" +
                "              +;    +   +        ;+        '+  '+    '+     \n" +
                "              +;    +   +        ;+        '+  '+    '+     \n" +
                "              +;    +   +        ;+        '+  '+    '+     \n" +
                "              +;    +   +        ;+        '+  '+    '+     \n" +
                "              +;    +   +        ;+        '+  '+    '+     \n" +
                "              +;    +   +         +        '+  '+    '+     \n" +
                "              +;    +   +                  '+  '+    '+     \n" +
                "              +;    +   +                  '+  '+    '+     \n" +
                "              +;    +   +                  '+  '+    '+     \n" +
                "              +;    +   +                  '+  '+    '+     \n" +
                "              +;    +   ++++++++++ ++++++++++  '+    '+     \n" +
                "              +++++++   ++++';'+++++++';'++++  '+++++++     \n" +
                "              +++++++   +         +         +   +++++++     \n" +
                "                                                            \n" +
                "                                                            \n" +
                "            +++++++++++++++++++++++++++++++++++++++++++++   \n" +
                "            +++++++++++++++++++++++++++++++++++++++++++++   \n" +
                "            +                                          '+   \n" +
                "            +                                          '+   \n" +
                "            +++++++++++++++++++++++++++++++++++++++++++++   \n" +
                "            +++++++++++++++++++++++++++++++++++++++++++++   \n" +
                "                                                            \n" +
                "                       " + ANSI_YELLOW + "Press enter key to continue" + ANSI_RESET + "         \n" +
                "                                                            \n" +
                "                                                            \n");
        input.nextLine();
        String[] choiceOptions = {"d (default)", "s (saved)", "n (new)"};
        char choice = receiveStringInput("Do you want to load the default inventory, a saved inventory or start new?", choiceOptions, "s", 1).charAt(0);
        if (choice == 'd') {
            addDefault();
        } else if (choice == 's') {
            load();
        }
    }

    public void mainMenu() {
        newPage("Home");
        System.out.println(" 1. Add Holding");
        System.out.println(" 2. Remove Holding");
        System.out.println(" 3. Add Member");
        System.out.println(" 4. Remove Member");
        System.out.println(" 5. Borrow Holding");
        System.out.println(" 6. Return Holding");
        System.out.println(" 7. Print all Holdings");
        System.out.println(" 8. Print all Members");
        System.out.println(" 9. Print specific Holding");
        System.out.println("10. Print specific Member");
        System.out.println("11. Save to file");
        System.out.println("12. Load from file");
        System.out.println("13. Exit");
        System.out.println("14. Admin");
        printCharTimes('=', 150, true);
        String[] options = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14"};
        int choice = Integer.parseInt(receiveStringInput("Enter an option:", options, false, 2)); //http://stackoverflow.com/questions/5585779/converting-string-to-int-in-java
        switch (choice) {
            case 1:
                addHolding();
                break;
            case 2:
                removeHolding();
                break;
            case 3:
                addMember();
                break;
            case 4:
                removeMember();
                break;
            case 5:
                borrowHolding();
                break;
            case 6:
                returnHolding();
                break;
            case 7:
                printAllHoldings();
                break;
            case 8:
                printAllMembers();
                break;
            case 9:
                printHolding();
                break;
            case 10:
                printMember();
                break;
            case 11:
                save();
                break;
            case 12:
                load();
                break;
            case 13:
                exit();
                break;
            case 14:
                adminMenuVerify();
                break;
            default:
                break;
        }
    }


    private void adminMenuVerify() {
        AdminVerify verify = new AdminVerify();
        newPage("Admin Verification");
        System.out.println("Please enter your admin password (Password1):");
        String password = input.nextLine();
        if (verify.authenticated(password)) {
            adminMenu();
        } else {
            try {
                System.out.println("Incorrect Password. Returning to main menu");
                System.out.print("In 5 seconds");
                for(int i=5;i != 0 ;i--){
                    Thread.sleep(1000);
                    System.out.print("\r");
                    System.out.print("In " + i  +" seconds");


                }
            }catch (InterruptedException e){}
        }

    }

    //Page Main Menu Methods
    private void addHolding() {
        boolean keepGoing = true;
        while (keepGoing) {
            //Using the the method to take input.

            //Creating the first page
            newPage("Add Holding");


            String[] infoID = getValidID("Holding", HOLDING_OPTIONS);
            String ID = infoID[1];
            char type = infoID[0].charAt(0);
            //The ID is done now.

            //Set a String representation of the type for use in user facing prompts.
            String typeName;
            if (type == 'b') {
                typeName = "Book";
            } else if (type == 'v') {
                typeName = "Video";
            } else {
                typeName = "Incorrect";
            }

            //Create a new page.
            newPage("Add " + typeName);
            //Tell the user what they just entered.
            System.out.println("ID: " + type + ID);
            //Now prompt them for the title of the holding
            System.out.println("Enter the title of the " + typeName);
            String title = input.nextLine();
            while (title.length() == 0) { //If the title was not entered, prompt the user until they get it right.
                //Holy shit if they can't even enter a title what are they going to do with a book? How will they know how to put the dvd in?
                System.out.println("Title not entered correctly, try again");
                title = input.nextLine();
            }

            //Title is now set.
            int loanFee = 10;
            //If the item is a video, the loan fee varies. Ask the user what it will be.
            if (type == 'v') {
                //Give the user the possible loan values
                String[] loanOptions = {"4", "6"};
                //Use the choice method to request the loan fee. Convert the string result to int using a built in method. We don't need to catch here, but should.
                loanFee = Integer.parseInt(receiveStringInput("Enter the loan fee", loanOptions, true, 1)); //http://stackoverflow.com/questions/5585779/converting-string-to-int-in-java
            }

            //We have all the info from the user.
            newPage("Add " + typeName + ": Confirmation");
            //Print all the info they have entered.
            System.out.println("ID:" + type + ID);
            System.out.println("Title: " + title);
            if (type == 'v') System.out.println("Loan Fee: " + loanFee);
            //Prompt the user for confirmation before creating the holding. Default to yes.
            char choice = receiveStringInput("Check Details. Are they correct?", CHOICE_OPTIONS, "y", 1).charAt(0);

            if (choice == 'e') return;
            if (choice == 'y') {
                //If they say yes add the method, record its success.
                boolean added = inv.addHolding(ID, type, title, loanFee);
                //If it failed tell the user.
                if (!added) {
                    System.out.println("Adding failed. Please try again.");
                } else System.out.println("Adding successful. Holding created");
                //Done. Return to main menu. // TODO: 2/05/2016 Prompt the user to save database to file here.
            } else if (choice == 'n') {
                //If they choose not to continue, return them to to main menu.
                System.out.println("Creation canceled. No holding was created. Please start from beginning");
                System.out.println("Press enter to return to main menu");
                input.nextLine();
                return;
            }
            choice = receiveStringInput("Do you want to make another holding?", CHOICE_OPTIONS, "n", 1).charAt(0);
            if (choice != 'y') {
                keepGoing = false;
            }
        }
    } //Done


    private void addMember() {
        boolean keepGoing = true;
        while (keepGoing) {
            //Setting options that will be used later on as choices.

            //Creating the first page
            newPage("Add Member");
            //Using the the method to take input.
            String[] infoID = getValidID("Member", MEMBER_OPTIONS);
            String ID = infoID[1];
            char type = infoID[0].charAt(0);
            //The ID is done now.

            //Set a String representation of the type for use in prompts.
            String typeName;
            if (type == 's') {
                typeName = "Standard Member";
            } else if (type == 'v') {
                typeName = "Premium Member";
            } else {
                typeName = "Incorrect";
            }

            //Create a new page.
            newPage("Add " + typeName);
            //Tell the user what they just entered.
            System.out.println("ID: " + type + ID);
            //Now prompt them for the name of the holding
            System.out.println("Enter the name of the " + typeName);
            String name = input.nextLine();
            while (name.length() == 0) { //If the name was not entered, prompt the user until they get it right.
                System.out.println("Name not entered correctly, try again");
                name = input.nextLine();
            }

            //Title is now set.


            //We have all the info from the user.
            newPage("Add " + typeName + ": Confirmation");
            //Print all the info they have entered.
            System.out.println("ID:" + type + ID);
            System.out.println("Name: " + name);
            //Prompt the user for confirmation before creating the holding. Default to yes.
            char choice = receiveStringInput("Check Details. Are they correct?", CHOICE_OPTIONS, "y", 1).charAt(0);
            if (choice == 'e') return;
            if (choice == 'y') {
                //If they say yes add the method, record its success.
                boolean added = inv.addMember(ID, type, name);
                //If it failed tell the user.
                if (!added) {
                    System.out.println("Adding failed. Please try again.");
                } else System.out.println("Adding successful. Member created");
                //Done. Return to main menu. // TODO: 2/05/2016 Prompt the user to save database to file here.

            } else if (choice == 'n') {
                //If they choose not to continue, return them to to main menu.
                System.out.println("Creation canceled. No member was created. Please start from beginning");
                System.out.println("Press enter to return to main menu");
                input.nextLine();
                return;
            }
            choice = receiveStringInput("Do you want to make another member?", CHOICE_OPTIONS, "n", 1).charAt(0);
            if (choice != 'y') {
                keepGoing = false;
            }
        }
    } //Done

    private void removeHolding() {
        boolean keepGoing = true;
        while (keepGoing) {
            char choice;
            newPage("Remove Holding");
            if (inv.getNumberOfHoldings() == 0) {
                System.out.printf("There are no holdings to remove. \nPlease press enter to return to main menu");
                input.nextLine();
                return;
            }
            String ID = getExistingID("Holding", HOLDING_TYPES);
            if (ID != null) {
                boolean result;
                inv.printHolding(ID);
                //We now have a correct id. Ask the user if they want to delete the holding.
                choice = receiveStringInput("Do you want to remove this holding? Please confirm", CHOICE_OPTIONS, "n", 1).charAt(0);
                if (choice == 'e') return;
                //If they do, remove the holding.
                if (choice == 'y') {
                    result = inv.removeHolding(ID);
                    //If successful, inform them it was, and return them to the menu. // TODO: 2/05/2016 Add save functionality.
                    if (result) {
                        System.out.println(ID + " deleted.");

                    } else {
                        //If the holding is somehow not deleted after all the checks, inform the user.
                        System.out.println("Holding deletion failed. Please report to developer and try again.");

                    }
                    //If they decide not to delete the holding, return them to the menu.
                } else {
                    System.out.println("Holding deletion canceled.");

                }
            }
            choice = receiveStringInput("Do you want to remove another holding?", CHOICE_OPTIONS, "n", 1).charAt(0);
            if (choice != 'y') {
                keepGoing = false;
            }
        }
    } //Done

    private void removeMember() {
        boolean keepGoing = true;
        while (keepGoing) {
            char choice;
            newPage("Remove Member");
            if (inv.getNumberOfMembers() == 0) {
                System.out.printf("There are no members to remove. \nPlease press enter to return to main menu");
                input.nextLine();
                return;
            }
            //Request member ID
            String ID = getExistingID("Member", MEMBER_TYPES);
            if (ID != null) {
                boolean result;
                inv.printMember(ID);
                //We now have a correct id. Ask the user if they want to delete the member.
                choice = receiveStringInput("Do you want to remove this member? Please confirm", CHOICE_OPTIONS, "n", 1).charAt(0);
                if (choice == 'e') return;
                //If they do, remove the member.
                if (choice == 'y') {
                    result = inv.removeMember(ID);
                    //If successful, inform them it was, and return them to the menu. // TODO: 2/05/2016 Add save functionality.
                    if (result) {
                        System.out.println(ID + " deleted.");
                        input.nextLine();
                    } else {
                        //If the member is somehow not deleted after all the checks, inform the user.
                        System.out.println("Member deletion failed. Please report to developer and try again.");
                        input.nextLine();
                    }
                    //If they decide not to delete the member, return them to the menu.
                } else {
                    System.out.println("Member deletion canceled.");
                    input.nextLine();
                }
            }
            choice = receiveStringInput("Do you want to remove another member?", CHOICE_OPTIONS, "n", 1).charAt(0);
            if (choice != 'y') {
                keepGoing = false;
            }
        }
    } //Done

    private void borrowHolding() {
        boolean keepGoing = true;
        while (keepGoing) {
            newPage("Borrow");
            String memberID = getExistingID("Member", MEMBER_TYPES);
            if (memberID != null) {
                System.out.println(inv.getMemberName(memberID));
                char choice = receiveStringInput("Is this your name?", CHOICE_OPTIONS, "y", 1).charAt(0);
                if (choice == 'e') return;
                if (choice == 'n') {
                    System.out.println("Holding has not been borrowed. Press enter to return to menu.");
                    input.nextLine();
                } else {
                    String holdingID = getExistingID("Holding", HOLDING_TYPES);
                    if (holdingID != null) {

                        inv.printHolding(holdingID);
                        choice = receiveStringInput("Do you want to borrow this holding?", CHOICE_OPTIONS, "y", 1).charAt(0);
                        if (choice == 'e') return;
                        if (choice == 'y') {
                            boolean result = inv.borrowHolding(holdingID, memberID);
                            if (result) {
                                System.out.println("Holding " + holdingID + " has been borrowed. Press enter to return to menu.");
                                input.nextLine();
                            } else {
                                System.out.println("Holding was not been borrowed. Press enter to return to menu.");
                                input.nextLine();
                            }
                        } else {
                            System.out.println("Holding has not been borrowed. Press enter to return to menu.");
                            input.nextLine();
                        }
                    }
                }
            }
            char choice = receiveStringInput("Do you want to borrow another holding?", CHOICE_OPTIONS, "n", 1).charAt(0);
            if (choice != 'y') {
                keepGoing = false;
            }
        }
    }

    private void returnHolding() {
        boolean keepGoing = true;
        while (keepGoing) {
            newPage("Return");
            String memberID = getExistingID("Member", MEMBER_TYPES);
            if (memberID != null) {
                System.out.println(inv.getMemberName(memberID));
                char choice = receiveStringInput("Is this your name?", CHOICE_OPTIONS, "y", 1).charAt(0);
                if (choice == 'e') return;
                if (choice == 'n') {
                    System.out.println("No holding was returned. Press enter to return to menu.");
                    input.nextLine();
                } else {
                    String holdingID = getExistingID("Holding", HOLDING_TYPES);
                    if (holdingID != null) {

                        inv.printHolding(holdingID);
                        choice = receiveStringInput("Do you want to return this holding?", CHOICE_OPTIONS, "y", 1).charAt(0);
                        if (choice == 'e') return;
                        if (choice == 'y') {
                            boolean result = inv.returnHolding(holdingID, memberID);
                            if (result) {
                                System.out.println("Holding " + holdingID + " has been returned. Press enter to return to menu.");
                                input.nextLine();
                            } else {
                                System.out.println("Holding was not been returned. Press enter to return to menu.");
                                input.nextLine();
                            }
                        } else {
                            System.out.println("Holding has not been returned. Press enter to return to menu.");
                            input.nextLine();
                        }
                    }
                }
            }
            char choice = receiveStringInput("Do you want to return another holding?", CHOICE_OPTIONS, "n", 1).charAt(0);
            if (choice != 'y') {
                keepGoing = false;
            }
        }
    }

    private void printAllHoldings() {
        newPage("Holding Listing");
        inv.printAllHoldings();
        System.out.println("Press enter to return to menu.");
        input.nextLine();
    }

    private void printAllMembers() {
        newPage("Members Listing");
        inv.printAllMembers();
        System.out.println("Press enter to return to menu.");
        input.nextLine();
    }

    private void printHolding() {
        boolean keepGoing = true;
        while (keepGoing) {
            newPage("Holding");
            String ID = getExistingID("Holding", HOLDING_TYPES);
            if (ID != null) {
                newPage("Holding: " + ID);
                inv.printHolding(ID);
                System.out.println("Press enter to return to menu.");
                input.nextLine();
            }
            char choice = receiveStringInput("Do you want to print another holding?", CHOICE_OPTIONS, "n", 1).charAt(0);
            if (choice != 'y') {
                keepGoing = false;
            }
        }
    }

    private void printMember() {
        boolean keepGoing = true;
        while (keepGoing) {
            newPage("Member");
            String ID = getExistingID("Member", MEMBER_TYPES);
            if (ID != null) {
                newPage("Member: " + ID);
                inv.printHolding(ID);
                System.out.println("Press enter to return to menu.");
                input.nextLine();
            }
            char choice = receiveStringInput("Do you want to print another member?", CHOICE_OPTIONS, "n", 1).charAt(0);
            if (choice != 'y') {
                keepGoing = false;
            }
        }
    }

    private void save() {
        newPage("Save");
        System.out.println("Enter Holding ID:");
        // TODO: 19/04/2016 Holding Logic Here
    }

    private void load() {
        newPage("Load File");
        System.out.println("Enter Holding ID:");
        // TODO: 19/04/2016 Holding Logic Here
    }

    private void exit() {
        newPage("Exit");
        char choice = receiveStringInput("You you want to exit?", CHOICE_OPTIONS, "y", 1).charAt(0);
        if (choice == 'e') return;
        if (choice == 'y') {
            System.exit(0);
        }
    }

    //Page Admin Menu Pages

    private void adminMenu() {
        while (true) {
            newPage("Admin");

            System.out.println(" 1. Activate Holding");
            System.out.println(" 2. Deactivate Holding");
            System.out.println(" 3. Edit Holding Details");
            System.out.println(" 4. Reset Member Credit");
            System.out.println(" 5. Edit Member Details");
            System.out.println(" 6. Return holding ignoring fees");
            System.out.println(" 7. Back to Main Menu");
            printCharTimes('=', 50, true);
            String[] adminOptions = {"1", "2", "3", "4", "5", "6", "7"};
            int options = Integer.parseInt(receiveStringInput("Enter an option:", adminOptions, false, 1)); //http://stackoverflow.com/questions/5585779/converting-string-to-int-in-java
            switch (options) {
                case 1:
                    activateHolding();
                    break;
                case 2:
                    deactivateHolding();
                    break;
                case 3:
                    editHoldingMenu();
                    break;
                case 4:
                    resetMemberCredit();
                    break;
                case 5:
                    editMemberMenu();
                    break;
                case 6:
                    returnHoldingNoFee();
                    break;
                case 7:
                    return;
                default:
                    adminMenu();
            }
        }
    }

    private void editHoldingMenu() {

        newPage("Edit Holding");
        System.out.println(" 1. ID");
        System.out.println(" 2. Title");
        System.out.println(" 3. Loan Cost (Videos Only)");
        String[] choiceOptions = {"1", "2", "3", "4"};
        int choice = Integer.parseInt(receiveStringInput("Enter your selection:", choiceOptions, false, 1));
        switch (choice) {
            case 1:
                editID(HOLDING_TYPES, "Holding");
                break;
            case 2:
                editTitle(HOLDING_TYPES, "Holding");
                break;
            case 3:
                editLoanCost();
                break;
            default:

        }
    }

    private void editMemberMenu() {

        newPage("Edit Member");
        System.out.println(" 1. ID");
        System.out.println(" 2. Name");
        String[] choiceOptions = {"1", "2"};
        int choice = Integer.parseInt(receiveStringInput("Enter your selection:", choiceOptions, false, 1));
        switch (choice) {
            case 1:
                editID(MEMBER_TYPES, "Member");
                break;
            case 2:
                editName(MEMBER_TYPES, "Member");
                break;
            default:

        }
    }

    private void editID(char[] types, String typeName) {
        String oldID = getExistingID(typeName, types);
        if (oldID != null) {
            String[] options = {oldID.substring(0, 1)};
            String[] idInfo = getValidID(typeName, options);
            String newID = idInfo[0] + idInfo[1];
            inv.replaceID(oldID, newID);
        } else System.out.println("Incorrect ID, nothing was changed.");

    }

    private void editTitle(char[] types, String typeName) {
        String oldID = getExistingID(typeName, types);
        if (oldID != null) {
            System.out.println("Please enter replacement title");
            String title = input.nextLine();
            while (title.length() == 0) {
                System.out.println("Please enter at least one character:");
                title = input.nextLine();
            }
            inv.replaceTitle(oldID, title);
        } else System.out.println("Incorrect ID, nothing was changed.");

    }

    private void editName(char[] types, String typeName) {
        String oldID = getExistingID(typeName, types);
        if (oldID != null) {

            System.out.println("Please enter replacement name");
            String name = input.nextLine();
            while (name.length() > 0) {
                System.out.println("Please enter at least one character:");
                name = input.nextLine();
            }
            boolean result = inv.replaceName(oldID, name);
            if (!result) {
                System.out.println("Change failed.");
            }
        } else System.out.println("Incorrect ID, nothing was changed.");
    }

    private void editLoanCost() {
        char[] type = {'v'};
        String oldID = getExistingID("Holding", type);
        if (oldID != null) {
            String[] choiceOptions = {"4", "6"};
            int newLoan = Integer.parseInt(receiveStringInput("Enter new Loan Fee:", choiceOptions, true, 1));
            inv.replaceLoan(oldID, newLoan);
        } else System.out.println("Incorrect ID, nothing was changed.");
    }

    private void activateHolding() {
        String ID = getExistingID("Holding", HOLDING_TYPES);
        if (ID != null) {

            boolean result = inv.activateHolding(ID);
            if (!result) {
                System.out.println("Holding activation failed");
            } else {
                System.out.println("Holding was activated");
            }
        }
    }

    private void deactivateHolding() {
        String ID = getExistingID("Holding", HOLDING_TYPES);
        if (ID != null) {

            boolean result = inv.deactivateHolding(ID);
            if (!result) {
                System.out.println("Holding deactivation failed");
            } else {
                System.out.println("Holding was deactivated");
            }
        }
    }

    private void resetMemberCredit() {
        String ID = getExistingID("Member", MEMBER_TYPES);
        if (ID != null) {

            inv.resetMemberCredit(ID);
            System.out.println("Credit was reset");
        }
    }

    private void returnHoldingNoFee() {

    }
// TODO: 8/05/2016 Needs more functionality
}
