/*
 * Copyright Alex Kinross-Smith (c) 2016.
 * s3603437@student.rmit.edu.au
 * alex@akinrosssmith.id.au
 */

/*
*@author - Alex Kinross-Smith
*/
package lms;

import lms.exceptions.IncorrectDetailsException;
import lms.ui.SearchInventory;
import lms.util.AdminVerify;
import lms.util.DateTime;
import lms.util.Utilities;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;
import java.util.StringTokenizer;

/**
 * Created by akinr on 11/04/2016 as part of s3603437_A2
 */
public class UI {
    public static final char[] MEMBER_TYPES = Utilities.MEMBER_TYPES;
    public static final char[] HOLDING_TYPES = Utilities.HOLDING_TYPES;
    private static final String[] CHOICE_OPTIONS = {"y (yes)", "n (no)", "e (exit)"};
    private static final String[] BORROW_OPTIONS = {"y (yes)", "c (change member)", "n (no)", "e (exit)"};
    private static final String[] MEMBER_OPTIONS = {"s (standard)", "p (premium)"};
    private static final String[] HOLDING_OPTIONS = {"b (book)", "v (video)"};
    private static int consoleWidth = 150;
    private final File RUN_STATUS = new File("./running");
    private final Scanner input = new Scanner(System.in);
    private final Inventory inv = new Inventory();
    private final SearchInventory searchInventory = inv.createSearch();
    //Functional Methods

    /**
     * Runs on creation of a UI object.
     * Prompts user for inventory size input.
     */
    public UI() {


    }
/* Graphical Elements */

    /**
     * Prints a character a number of times, and can terminate the line at the end.
     *
     * @param times   The number of times to print the <code>char</code>
     * @param newLine If it is true print a new line at the end of the char sequence.
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
     * Prints a pretty logo.
     * Requests what will initialise the inventory
     * Requests console width for page formatting
     */
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
                "                                                            \n" +
                "                                                            \n");
        newPage("Initialise Database");

        //Set the default load location. This is used if there is no last run detected or the program closed correctly.
        String saveLocation = "lastrun";
        String defaultChoice = "s";
        boolean result;

        //Checks for incorrect closing of the program.
        try {
            //try to create the file to tell program status. This will be deleted before close. If file already exists, close must have been completed incorrectly, and assume crash.
            result = RUN_STATUS.createNewFile();
            if (!result) {
                System.out.println("Program did not close correctly last run.");
                //check if a backup exists.
                File backupMembersRun = new File("./backup/members" + Utilities.FILE_EXTENSION);
                File backupHoldingsRun = new File("./backup/holdings" + Utilities.FILE_EXTENSION);


                //If it exists set default load location to the backup.
                if (backupHoldingsRun.exists() && backupMembersRun.exists()) {
                    System.out.println(Utilities.WARNING_MESSAGE + "We recommend loading from backup save.");
                    saveLocation = "backup";
                } else {
                    System.out.println("No backup was found. If there are issues with lastrun we recommend restoring a daily backup.");
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }


        try {
            File lastRun = new File("./lastrun");
            result = lastRun.exists();

            //if there is no lastrun folder assume first launch. Recommend default inventory
            if (!result) {
                System.out.println(Utilities.WARNING_MESSAGE + "Detecting first run. We recommend loading from a default.");
                defaultChoice = "d";

            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

//Choices of what to load
        String[] choiceOptions = {"d (default)", "s (saved)", "n (new)"};
        char choice = receiveStringInput("Do you want to load the default inventory, a saved inventory or start new?", choiceOptions, defaultChoice, 1).charAt(0);

        //Set default
        if (choice == 'd') {
            addDefault();
        }
        //Choices of folder, and default set before
        else if (choice == 's') {

            choice = receiveStringInput("What do folder do you want to use?", new String[]{"c (custom)", "b (backup)", "s (save)", "l (lastrun)"}, saveLocation, 1).charAt(0);
//Get the folder to load from via the choice before
            switch (choice) {
                case 'b':
                    saveLocation = "backup";
                    break;
                case 's':
                    saveLocation = "save";
                    break;
                case 'l':
                    saveLocation = "lastrun";
                    break;
                case 'c':
                default:
                    System.out.println("Enter the name of the folder to load from.");
                    saveLocation = input.nextLine();
            }
//Try to load.
            try {
                inv.load(saveLocation);
            } catch (IOException e) {
                System.out.println("No folder or files were found with the name. Files were not loaded.");
            }

        }

        //Load the terminal options
        newPage("Terminal Settings");
        //Show the user what 150 chars looks like
        System.out.println("\nA terminal width of 100-150 characters is recommended. \n" + Utilities.ANSI_YELLOW + "If the line below is cut off or on two lines consider changing your console window or choosing another console width." + Utilities.ANSI_RESET);
        printCharTimes('-', 150, true);
        //Ask them if they want a custom width. Give them 5 seconds to answer, or assume no.
        choice = receiveStringInput("Do you want to specify a custom width? This may produce unexpected results.", CHOICE_OPTIONS, "n", 1, 5).charAt(0);
        if (choice == 'y') {
            System.out.println("Enter the new terminal width:");
            while (consoleWidth == 150) {
                try {
                    System.out.print(Utilities.INPUT_MESSAGE);
                    consoleWidth = Integer.parseInt(input.nextLine());
                } catch (Exception e) {
                    System.out.println("A number must entered");
                    throw e;
                }
            }
        }
        //Exit the program, and delete the run status before.
        else if (choice == 'e') {
            RUN_STATUS.delete();
            System.exit(0);
        }
    }

    /**
     * Used to limit access to admin menu
     */
    private void adminMenuVerify() {
        AdminVerify verify = new AdminVerify();
        newPage("Admin Verification");
        System.out.println("Please enter your admin password (Password1):");
        System.out.print(Utilities.INPUT_MESSAGE);
        //Check the password
        String password = input.nextLine();
        //Run the admin menu
        if (verify.authenticated(password)) {
            adminMenu();
        }
        //Wait 5 seconds so they can't spam passwords.
        else {
            try {
                System.out.println(Utilities.INFORMATION_MESSAGE + "Incorrect Password. Returning to main menu");
                System.out.print("In 5 seconds");
                for (int i = 5; i != 0; i--) {
                    Thread.sleep(1000);
                    System.out.print("\r");
                    System.out.print("In " + i + " seconds");


                }
            } catch (InterruptedException ignored) {
            }
        }

    }

    /**
     * Creates a new page. Formatted, displays current date and the status of inventory.
     *
     * @param title Used to make the central element of the page.
     **/
    private void newPage(String title) {
        //Sets the width of the page.
        try {
            Runtime.getRuntime().exec("cls");
        } catch (IOException ignored) {
        }
        //Print a new line a bunch to clear the screen. WHY IS THERE NO PLATFORM INDEPENDENT SOLUTION LIKE CLS
        for (int i = 30; i != 0; i--) {
            System.out.printf("\n");
        }
        //Print the first line, made of equal signs.
        printCharTimes('=', consoleWidth, true);

//Set the three components of a menu screen.
        String leftText = DateTime.getCurrentTime();
        String centreText = "Library Management System: " + title;
        String rightText = inv.infoPrintout();

//Find the length of the menu areas.
        int left = leftText.length();
        int centre = centreText.length();
        int right = rightText.length();

//Find the spacing between the three elements. Total width/2 - the size of left and half of the centre.
        int leftSpacing = consoleWidth / 2 - left - centre / 2;
        //Due to float to int conversion, subtract 1 more than the length.
        int rightSpacing = (consoleWidth / 2) - 1 - right - centre / 2;
        if (leftSpacing == 0) {
            leftSpacing = 1;
        }
        if (rightSpacing == 0) {
            rightSpacing = 1;
        }
//Print the left
        System.out.print(Utilities.ANSI_YELLOW);
        System.out.print(leftText);
        //Print the spacing
        printCharTimes(' ', leftSpacing, false);
        //Print the centre
        System.out.print(centreText);
        //Print the right spacing
        printCharTimes(' ', rightSpacing, false);
        //Print the right text
        System.out.println(rightText);
        System.out.print(Utilities.ANSI_RESET);
//Finish off the menu with another border.
        printCharTimes('=', consoleWidth, true);
    }
/* Functional Methods */

    /**
     * Adds the default holdings as specified by assignment spec.
     **/
    private void addDefault() {
        //Use arrays cos I am lazy
        String[] holdingTitle = {"Intro to Java", "Learning UML", "Design Patterns", "Advanced Java", "Java 1", "Java 2", "UML 1", "UML 2"};
        char[] holdingType = {'b', 'b', 'b', 'b', 'v', 'v', 'v', 'v'};
        String[] holdingID = {"000001", "000002", "000003", "000004", "000001", "000002", "000003", "000004"};
        int[] holdingFee = {0, 0, 0, 0, 4, 6, 6, 4};

        //Add all the holdings.
        for (int i = 0; i < holdingID.length; i++) {
            try {
                inv.addHolding(holdingID[i], holdingType[i], holdingTitle[i], holdingFee[i]);
            } catch (IncorrectDetailsException details) {
                System.out.println("The details for the holding were incorrect, and the holding was not added." + holdingType[i] + holdingID[i]);
                System.out.println(details.getMessage());
            }
        }

        String[] memberID = {"000001", "000001", "000002", "000002"};
        String[] memberName = {"Joe Bloggs", "Fred Bloggs", "Jane Smith", "Fred Smith"};
        char[] memberType = {'s', 'p', 's', 'p'};

        //Add all the members
        for (int i = 0; i < memberID.length; i++) {
            inv.addMember(memberID[i], memberType[i], memberName[i]);
        }
    }

    /**
     * Returns an array of Strings (for example {a,b,c} as a String in the format (a/b/c)
     *
     * @param array used as the input to formulate the final string.
     * @return A string in the format of (0/1/2/3/...) of the input array {0,1,2,3,...}
     **/
    private String stringArrayToString(String[] array) {
        String result = Utilities.ANSI_YELLOW + "{" + Utilities.ANSI_RESET;
        //Add all the things together
        for (int i = 0; i < array.length; i++) {
            //If this isn't the last entry in the array, add a / on as well
            if (i < array.length - 1) {
                result += array[i] + Utilities.ANSI_YELLOW + "/" + Utilities.ANSI_RESET;
            } else { //If it is the last entry, add a closing bracket instead.
                result += array[i] + Utilities.ANSI_YELLOW + "}" + Utilities.ANSI_RESET;
            }
        }
        return (result);
    }
/* Input methods */

    /**
     * Receives an input. Prints the flavourText, then requests input from the user. Will continue requesting input from the user until input matches an entry in the array options. if printOptionText is false will not show the user what options are available.
     * Final number is length of returned string and length of input that will be compared to the strings.
     *
     * @param flavourText     Printed before every request for input.
     * @param options         The array of <code>Strings</code> that is used for validation of input.
     * @param printOptionText If true, display the potential inputs to the user. If false, don't display it.
     * @param outputLength    The length of input that will be validated and output.
     * @return The final validated chosen user input of the first <code>int outputLength</code> positions.
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
        System.out.print(Utilities.INPUT_MESSAGE);
        String inputString = input.nextLine().toLowerCase();
        //If it is too short, prompt for input again.
        while (inputString.length() == 0) {
            //Print the stuff again.
            System.out.println(Utilities.INFORMATION_MESSAGE + "Answer needs to be entered");
            System.out.print(flavourText + " ");
            if (printOptionText) {
                System.out.println(stringArrayToString(options));
            } else System.out.println();
            //Request input again.
            System.out.print(Utilities.INPUT_MESSAGE);
            inputString = input.nextLine().toLowerCase();
        }
        //Ensure input is the same.
        inputString = inputString.toLowerCase();
        //If the input we are given is longer than the specified maximum output, make it shorter.
        if (inputString.length() >= outputLength) {
            inputString = inputString.substring(0, outputLength);
        }
        boolean isCorrect = false;
        int i = 0;
        //Check it matches input parameters.
        while (i < options.length && !isCorrect) {
            String currentExamined;
            if (options[i].length() >= outputLength) {
                currentExamined = options[i].substring(0, outputLength);
            } else currentExamined = options[i];
            if (currentExamined.equals(inputString)) {
                isCorrect = true;
            }
            i++;
        }
        if (isCorrect) {
            return (inputString);
        } else {
            System.out.println(Utilities.ERROR_MESSAGE + "Input was not an option. Please try again.");
            inputString = receiveStringInput(flavourText, options, true, outputLength);//If they didn't get it right the first time, supply options.
        }
        return (inputString);
    }

    /**
     * Receives an input. Prints the flavourText, then requests input from the user. Will continue requesting input from the user until input matches an entry in the array options or is empty. if it is empty returns the defaultAnswer. Final number is length of returned string
     *
     * @param flavourText   Printed before every request for input.
     * @param options       The array of <code>Strings</code> that is used for validation of input.
     * @param defaultAnswer The default solutions. If no input is received this is returned.
     * @param outputLength  The length of input that will be validated and output.
     * @return The final validated chosen user input of the first <code>int outputLength</code> positions.
     **/
    private String receiveStringInput(String flavourText, String[] options, String defaultAnswer, int outputLength, int waitTime) {
        //Print a prompt for the user to enter input.
        System.out.println(flavourText + " " + stringArrayToString(options) + Utilities.ANSI_RED + "[" + defaultAnswer + "]" + Utilities.ANSI_RESET);
        //Ensure we don't try to access a negative array index.
        if (outputLength <= 0) {
            outputLength = 1;
        }
        //Actually get input.
        String inputString;
        if (waitTime == 0) {
            System.out.print(Utilities.INPUT_MESSAGE);
            inputString = input.nextLine().toLowerCase();
        } else {
            inputString = returnWaitInput(waitTime, defaultAnswer).toLowerCase();

        }//If the user entered nothing, return the default input.
        if (inputString.length() == 0) {
            System.out.println(defaultAnswer);
            return ("" + defaultAnswer);
        }
        inputString = inputString.toLowerCase();
        if (inputString.length() >= outputLength) {
            inputString = inputString.substring(0, outputLength);
        }
        boolean isCorrect = false;
        int i = 0;
        //Check it matches a valid input
        while (i < options.length && !isCorrect) {
            String currentExamined;
            if (options[i].length() >= outputLength) {
                currentExamined = options[i].substring(0, outputLength);
            } else currentExamined = options[i];
            if (currentExamined.equals(inputString)) {
                isCorrect = true;
            }
            i++;
        }
        if (isCorrect) {
            return (inputString);
        } else {
            System.out.println(Utilities.ERROR_MESSAGE + "Input was not an option. Please try again.");
            inputString = receiveStringInput(flavourText, options, defaultAnswer, outputLength);//If they didn't get it right the first time, supply options.
        }
        return inputString;
    }

    /**
     * Wrapper for default string input with no auto input.
     *
     * @param flavourText   Printed before every request for input.
     * @param options       The array of <code>Strings</code> that is used for validation of input.
     * @param defaultAnswer The default solutions. If no input is received this is returned.
     * @param outputLength  The length of input that will be validated and output.
     * @return The final validated chosen user input of the first <code>int outputLength</code> positions.
     **/
    private String receiveStringInput(String flavourText, String[] options, String defaultAnswer, int outputLength) {
        return receiveStringInput(flavourText, options, defaultAnswer, outputLength, 0);
    }

    /**
     * Prompts the user for an ID that already exists.
     * TypeID is a string that will be displayed when asking the user for and ID, and expectedTypes restricts input(i.e if someone enters s000001, but the user is searching for a holding it wont work.
     * If user doesn't want to enter input returns null.
     *
     * @param typeID        Used strictly for printing to the user. Displayed in user facing prompts.
     * @param expectedTypes The input ID must start with one of these expected types.
     * @return The ID chosen by the user.
     **/
    private String getExistingID(String typeID, char[] expectedTypes) {
        System.out.println("Enter " + typeID + " ID:");
        System.out.print(Utilities.INPUT_MESSAGE);
        String ID = input.nextLine();
        boolean result = inv.idExists(ID);
        if (result) {
            result = false;
            for (char expectedType : expectedTypes)
                if (ID.charAt(0) == expectedType) {
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
                System.out.println(Utilities.INFORMATION_MESSAGE + "You have 4 tries before returning to main menu");
                int tries = 0;
                //End the loop either upon entering a correct id, or using up all the tries
                while (!result && tries < 4) {
                    System.out.println((tries + 1) + ": Enter a valid " + typeID + " ID:");
                    System.out.print(Utilities.INPUT_MESSAGE);
                    ID = input.nextLine();
                    result = inv.idExists(ID);
                    if (result) {
                        result = false;
                        for (char expectedType : expectedTypes)
                            if (ID.charAt(0) == expectedType) {
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
     * Prompts the user for a formatting valid ID, conforming to the types possible.
     *
     * @param typeID Used for user input in user facing prompts i.e Enter type of <code>typeID</code> :
     * @param types  Used by <code>{@link #receiveStringInput(String, String[], boolean, int)}</code> to get the type of ID to be created.
     * @return 2 long array of Strings. First position is the type, Second position is the 6 digit ID.
     */
    private String[] getValidID(String typeID, String[] types) {
        char type;
        if (types.length > 1) {
            type = receiveStringInput("Enter type of " + typeID + ":", types, true, 1).charAt(0);
        } else {
            type = types[0].charAt(0);
        }
        //Not a choice, requesting input. If they don't enter anything it will be caught below, and one will be generated for them.
        System.out.println("Enter the unique 6 digit ID or press enter to have one generated:");
        System.out.print(Utilities.INPUT_MESSAGE);
        String ID = input.nextLine();
        if (ID != null && ID.toLowerCase().equals("e")) {
            return null;
        }
        //Check validity of the entered information
        String IDResult = inv.checkID(ID, type);
        //Print the result
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
                        System.out.println(Utilities.INFORMATION_MESSAGE + "Enter new 6 digit ID number");
                        //Receive the new ID as input
                        System.out.print(Utilities.INPUT_MESSAGE);
                        ID = input.nextLine();
                        //Print the result
                        System.out.println(Utilities.ERROR_MESSAGE + inv.checkID(ID, type));
                    }
                    while (!inv.checkID(ID, type).equalsIgnoreCase("valid")); //If they somehow still fail to enter a 6 digit number that is different from the other 15 6 digit number, let them try again.
                }
                break;
            case "invalid type":
                //If they entered type is incorrect(somehow, as the input method won't let an incorrect one be entered), request a valid one.
                do {
                    type = receiveStringInput("Invalid type, try again", HOLDING_OPTIONS, true, 1).charAt(0);
                    System.out.println(Utilities.ERROR_MESSAGE + inv.checkID(ID, type));
                } while (!inv.checkID(ID, type).equalsIgnoreCase("valid"));
                break;
            case "id contains characters":
                do {
                    System.out.println(Utilities.WARNING_MESSAGE + "Invalid ID. Can only contain numbers, try again");
                    System.out.print(Utilities.INPUT_MESSAGE);
                    ID = input.nextLine();
                    System.out.println(Utilities.ERROR_MESSAGE + inv.checkID(ID, type));
                } while (!inv.checkID(ID, type).equalsIgnoreCase("valid"));
                break;
            case "valid": //If it is valid, keep going.
                break;
            default: //Any further errors will be caused by the ID been incorrect, so prompt for it again.
                do {
                    System.out.println(Utilities.INFORMATION_MESSAGE + "Enter new 6 digit ID number");
                    System.out.print(Utilities.INPUT_MESSAGE);
                    ID = input.nextLine();
                    System.out.println(Utilities.ERROR_MESSAGE + inv.checkID(ID, type));
                } while (!inv.checkID(ID, type).equalsIgnoreCase("valid"));
        }
        return new String[]{"" + type, ID};
    }

    /* Menus */

    /**
     * Main Menu
     * Used for choice in user facing options.
     * Runs other methods based on user input.
     */
    public void mainMenu() {
        //Print options. Debug stuff on option 211 and 404
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
        System.out.println("11. Search");
        System.out.println("12. Save to file");
        System.out.println("13. Load from file");
        System.out.println("14. Exit");
        System.out.println("15. Admin");
        printCharTimes('=', consoleWidth, true);
        String[] options = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15","211", "301", "404"};
        int choice = Integer.parseInt(receiveStringInput("Enter an option:", options, false, 16)); //http://stackoverflow.com/questions/5585779/converting-string-to-int-in-java
        //Run options
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
                search();
                break;
            case 12:
                save();
                break;
            case 13:
                load();
                break;
            case 14:
                exit();
                break;
            case 15:
                adminMenuVerify();
                break;
            case 211:
                printState();
                break;
            case 301:
                enableColours();
                break;
            case 404:
                consoleWidthEdit();
            default:
                break;
        }
    }

    private void search(){
        SystemOperations item = searchInventory.searchMenu();
        if(item != null){
            System.out.println(item.lineSummary());
        }else{
            System.out.println("Nothing was selected");
        }
        System.out.println("Please enter the ID (the content before the colon) in prompts when borrowing or returning");
    }

    /**
     * Secondary menu for admin functions used to choose what admin methods to run.
     */
    private void adminMenu() {
        while (true) {
            newPage("Admin");
//The menu for admin stuff
            System.out.println(" 1. Activate");
            System.out.println(" 2. Deactivate");
            System.out.println(" 3. Edit Holding Details");
            System.out.println(" 4. Reset Member Credit");
            System.out.println(" 5. Edit Member Details");
            System.out.println(" 6. Return holding ignoring fees");
            System.out.println(" 7. Undelete holdings");
            System.out.println(" 8. Back to Main Menu");
            printCharTimes('=', 50, true);
            String[] adminOptions = {"1", "2", "3", "4", "5", "6", "7", "8"};
            int options = Integer.parseInt(receiveStringInput("Enter an option:", adminOptions, false, 1)); //http://stackoverflow.com/questions/5585779/converting-string-to-int-in-java
            switch (options) {
                case 1:
                    activate();
                    break;
                case 2:
                    deactivate();
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
                    undeleteHolding();
                    break;
                case 8:
                    return;
                default:
                    adminMenu();
            }
        }
    }

    /**
     * A tertiary menu for editing holdings.
     */
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

    /**
     * A tertiary menu for editing members.
     */
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

    /**
     * Does nothing WIP
     */
    private void enableColours() {
        Utilities.toggleColours();
    }

    /**
     * Requests input for a change in console width for formatting.
     */
    private void consoleWidthEdit() {
        System.out.println("Enter console width:");
        consoleWidth = input.nextInt();
    }

    /**
     * A menu to receive input that will be passed to the inventory class.
     */
    private void addHolding() {
        boolean keepGoing = true;
        while (keepGoing) {
            //Using the the method to take input.

            //Creating the first page
            newPage("Add Holding");
            String[] infoID = getValidID("Holding", HOLDING_OPTIONS);
            if (infoID == null) {
                return;
            }
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
            System.out.print(Utilities.INPUT_MESSAGE);
            String title = input.nextLine();
            while (title.length() == 0) { //If the title was not entered, prompt the user until they get it right.
                //Holy shit if they can't even enter a title what are they going to do with a book? How will they know how to put the dvd in?
                System.out.println(Utilities.INFORMATION_MESSAGE + "Title not entered correctly, try again");
                System.out.print(Utilities.INPUT_MESSAGE);
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
                try {
                    boolean added = inv.addHolding(ID, type, title, loanFee);
                    if (!added) {
                        System.out.println(Utilities.ERROR_MESSAGE + "Adding failed. Please try again.");
                    } else System.out.println(Utilities.INFORMATION_MESSAGE + "Adding successful. Holding created");
                } catch (IncorrectDetailsException details) {
                    System.out.println(Utilities.ERROR_MESSAGE + " Adding failed due to " + details + " with error message: \n" + details.getMessage());
                }
                //If it failed tell the user.

                choice = receiveStringInput("Do you want to save?", CHOICE_OPTIONS, "y", 1, 5).charAt(0);
                if (choice == 'y') {
                    save("save");

                }
                //Done. Return to main menu.
            } else if (choice == 'n') {
                //If they choose not to continue, return them to to main menu.
                System.out.println(Utilities.ERROR_MESSAGE + "Creation canceled. No holding was created. Please start from beginning");
            }
            choice = receiveStringInput("Do you want to make another holding?", CHOICE_OPTIONS, "n", 1).charAt(0);
            if (choice != 'y') {
                keepGoing = false;
            }
        }
    } //Done

    /**
     * A menu to receive input that will be passed to the inventory class.
     */
    private void addMember() {
        boolean keepGoing = true;
        while (keepGoing) {
            //Setting options that will be used later on as choices.

            //Creating the first page
            newPage("Add Member");
            //Using the the method to take input.
            String[] infoID = getValidID("Member", MEMBER_OPTIONS);
            if (infoID == null) {
                return;
            }
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
            System.out.print(Utilities.INPUT_MESSAGE);
            String name = input.nextLine();
            while (name.length() == 0) { //If the name was not entered, prompt the user until they get it right.
                System.out.println(Utilities.WARNING_MESSAGE + "Name not entered correctly, try again");
                System.out.print(Utilities.INPUT_MESSAGE);
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
                    System.out.println(Utilities.ERROR_MESSAGE + "Adding failed. Please try again.");
                } else System.out.println("Adding successful. Member created");
                choice = receiveStringInput("Do you want to save?", CHOICE_OPTIONS, "y", 1).charAt(0);
                if (choice == 'y') {
                    try {
                        inv.save("save");
                    } catch (IOException e) {
                        System.out.println(e.getMessage());

                    }
                }
            } else if (choice == 'n') {
                //If they choose not to continue, return them to to main menu.
                System.out.println("Creation canceled. No member was created. Please start from beginning");
            }
            choice = receiveStringInput("Do you want to make another member?", CHOICE_OPTIONS, "n", 1).charAt(0);
            if (choice != 'y') {
                keepGoing = false;
            }
        }
    } //Done

    /**
     * A menu to receive input that will be passed to the inventory class.
     */
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
                    //If successful, inform them it was, and return them to the menu.
                    if (result) {
                        System.out.println(ID + " deleted.");
                        choice = receiveStringInput("Do you want to save?", CHOICE_OPTIONS, "y", 1).charAt(0);
                        if (choice == 'y') {
                            try {
                                inv.save("save");
                            } catch (IOException e) {
                                System.out.println(e.getMessage());

                            }
                        }

                    } else {
                        //If the holding is somehow not deleted after all the checks, inform the user.
                        System.out.println(Utilities.ERROR_MESSAGE + "Holding deletion failed. This is likely because the holding is borrowed out.");
                        choice = receiveStringInput("Do you want to force removal? No fees will be incurred by the member. If holding is restored member will not get holding back.", CHOICE_OPTIONS, "n", 1).charAt(0);
                        if (choice == 'y') {
                            inv.removeHolding(ID, true);
                        }
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

    /**
     * A menu to receive input that will be passed to the inventory class.
     */
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
                    //If successful, inform them it was, and return them to the menu.
                    if (result) {
                        System.out.println(ID + " deleted.");
                        choice = receiveStringInput("Do you want to save?", CHOICE_OPTIONS, "y", 1).charAt(0);
                        if (choice == 'y') {
                            try {
                                inv.save("save");
                            } catch (IOException e) {
                                System.out.println(e.getMessage());

                            }
                        }
                    } else {
                        //If the member is somehow not deleted after all the checks, inform the user.
                        System.out.println(Utilities.ERROR_MESSAGE + "Member deletion failed. Please report to developer and try again.");
                    }
                    //If they decide not to delete the member, return them to the menu.
                } else {
                    System.out.println("Member deletion canceled.");
                }
            }
            choice = receiveStringInput("Do you want to remove another member?", CHOICE_OPTIONS, "n", 1).charAt(0);
            if (choice != 'y') {
                keepGoing = false;
            }
        }
    } //Done

    /**
     * A menu to receive input that will be passed to the inventory class.
     */
    private void borrowHolding() {
        char choice = 'c';
        boolean keepGoing = true;
        String memberID = null;
        do {
            newPage("Borrow");

            //On the first run choice always == c. Get a new member name.
            if (choice == 'c') {
                //Get the member name
                memberID = getExistingID("Member", MEMBER_TYPES);

                //Check that the member name was entered correctly. Don't give the user options if it was not.
                if (memberID != null) {
                    //Tell the user who they are.
                    System.out.println(inv.getMemberName(memberID));

                    //Get them to confirm that they are who they say they are.
                    choice = receiveStringInput("Is this your name?", CHOICE_OPTIONS, "y", 1).charAt(0);

                    //Exit if they want to
                    if (choice == 'e') {
                        return;
                    }
                    //If they want to enter a different user name, set the new one and tell them to go around again.
                    else if (choice == 'n') {
                        System.out.println(Utilities.INFORMATION_MESSAGE + "Holding has not been borrowed.");
                        memberID = getExistingID("Member", MEMBER_TYPES);
                        continue;
                    }
                } else {
                    System.out.println("No member was entered.");
                }
            }

            //If the user selected y after member selection or after re-run choice == y
            if (choice == 'y') {

                //Get the holding ID
                String holdingID = getExistingID("Holding", HOLDING_TYPES);

                //Check that the holding and member IDs have been entered correctly.
                if (holdingID != null && memberID != null) {

                    inv.printHolding(holdingID);

                    //Get the time when the holding will have to be returned by or fees will be incurred and print it
                    DateTime returnDate = new DateTime(inv.getHoldingReturnTime(holdingID));
                    System.out.println("You will have to return holding by the " + returnDate.getFormattedDate());

                    //Confirm they want to proceed.
                    choice = receiveStringInput("Do you want to borrow this holding?", CHOICE_OPTIONS, "y", 1).charAt(0);

                    //Exit if they want to exit
                    if (choice == 'e') return;

                    //If they want to proceed print error messages based on result.
                    if (choice == 'y') {
                        try {
                            boolean result = inv.borrowHolding(holdingID, memberID);

                            if (result) {
                                System.out.println("Holding " + holdingID + " has been borrowed. Your balance is " + inv.getMemberBalance(memberID));
                            } else {
                                System.out.println(Utilities.INFORMATION_MESSAGE + "Holding was not been borrowed. ");
                            }
                            //Catch any exceptions that might be thrown by the borrowing of a holding and print the associated message.
                        } catch (Exception e) {
                            System.out.println(Utilities.ERROR_MESSAGE + " Adding failed due to " + e + " with error message: \n" + e.getMessage());
                        }
                    }
                    //Must have selected n, tel; them nothing has happened.
                    else {
                        System.out.println(Utilities.INFORMATION_MESSAGE + "Holding has not been borrowed.");
                    }
                }
                //If one or both of them are null, they weren't entered correctly. Tell the user.
                else {
                    System.out.println("Either the holding or member was not entered. Nothing was changed.");
                }
            }

//Ask them if they want to perform another operation
            choice = receiveStringInput("Do you want to borrow another holding?", BORROW_OPTIONS, "n", 1).charAt(0);
            keepGoing = !(choice != 'y' && choice != 'c');

//Keep going while they want to keep going
        } while (keepGoing);
    }

    /**
     * A menu to receive input that will be passed to the inventory class.
     */
    private void returnHolding() {
        char choice = 'c';
        boolean keepGoing = true;
        String memberID = null;
        do {
            newPage("Return");
            //On the first run choice always == c. Get a new member name.

            if (choice == 'c') {
                memberID = getExistingID("Member", MEMBER_TYPES);

                //Check that the member name was entered correctly. Don't give the user options if it was not.
                if (memberID != null) {
                    //Tell the user who they are.
                    System.out.println(inv.getMemberName(memberID));

                    //Get them to confirm that they are who they think they are.
                    choice = receiveStringInput("Is this your name?", CHOICE_OPTIONS, "y", 1).charAt(0);

                    //Exit if they want to
                    if (choice == 'e') {
                        return;
                    }
                    //If they want to enter a different user name, set the new one and tell them to go around again.

                    else if (choice == 'n') {
                        System.out.println(Utilities.INFORMATION_MESSAGE + "Holding has not been returned.");
                        memberID = getExistingID("Member", MEMBER_TYPES);
                        continue;
                    }
                } else {
                    System.out.println("No member was entered.");
                }
            }

            //If the user selected y after member selection or after re-run choice == y
            if (choice == 'y') {

                //Get the holding ID
                String holdingID = getExistingID("Holding", HOLDING_TYPES);

                //Check that the holding and member IDs have been entered correctly.
                if (holdingID != null && memberID != null) {
                    inv.printHolding(holdingID);

                    //Get the DateTime for when they want to return it.
                    DateTime returnDate = new DateTime();
                    System.out.println("Enter a single number in days  for when the holding will be returned or that date in the format DD-MM-YYYY. Alternatively, press enter to select the current day.");
                    String date = input.nextLine();

                    //Support for entered dates
                    try {
                        if (date != null && date.length() == 10 && date.charAt(2) == '-' && date.charAt(5) == '-') {
                            StringTokenizer dateTokens = new StringTokenizer(date, "-");
                            if (dateTokens.countTokens() == 3) {
                                try {
                                    int day = Integer.parseInt(dateTokens.nextToken());
                                    int month = Integer.parseInt(dateTokens.nextToken());
                                    int year = Integer.parseInt(dateTokens.nextToken());
                                    returnDate = new DateTime(day, month, year);

                                } catch (Exception e) {
                                    System.out.println("Date was entered in incorrect format. Date has been set to current day");
                                    returnDate = new DateTime();
                                }
                            }
                        }

                        //If it is not a date it must be a number of days. Otherwise set it to current date.

                        else if (date != null && !date.isEmpty()) {
                            try {
                                returnDate = new DateTime(Integer.parseInt(date));
                            } catch (Exception e) {
                                System.out.println("Date was entered in incorrect format. Date has been set to current day");
                                returnDate = new DateTime();
                            }
                        }
                    } catch (Exception e) {
                        System.out.println("Date was entered in incorrect format. Date has been set to current day");
                        returnDate = new DateTime();
                    }

                    //Tell the user their late fee.
                    System.out.println("Holding will be returned with a late fee of :" + inv.getHoldingLateFee(holdingID, returnDate));

                    //Confirm they want to proceed.
                    choice = receiveStringInput("Do you want to return this holding?", CHOICE_OPTIONS, "y", 1).charAt(0);

                    //Exit if they want to exit
                    if (choice == 'e') return;

                    //If they want to proceed print error messages based on result.
                    if (choice == 'y') {
                        try {
                            boolean result = inv.returnHolding(holdingID, memberID, returnDate);
                            if (result) {
                                System.out.println("Holding " + holdingID + " has been returned. Your balance is " + inv.getMemberBalance(memberID));
                            } else {
                                System.out.println(Utilities.INFORMATION_MESSAGE + "Holding was not been returned. ");
                            }
                        } catch (Exception e) {
                            System.out.println(Utilities.ERROR_MESSAGE + " Adding failed due to " + e + " with error message: \n" + e.getMessage());
                        }
                    } else {
                        System.out.println(Utilities.INFORMATION_MESSAGE + "Holding has not been returned.");
                    }
                }
            } else {
                System.out.println("Either the holding or member was not entered. Nothing was changed.");
            }


            choice = receiveStringInput("Do you want to return another holding?", BORROW_OPTIONS, "n", 1).charAt(0);
            keepGoing = !(choice != 'y' && choice != 'c');


        } while (keepGoing);
    }

    /**
     * A menu to receive input that will be passed to the inventory class.
     */
    private void printAllHoldings() {
        newPage("Holding Listing");
        inv.printAllHoldings(consoleWidth);
        System.out.println("Press enter to return to menu.");
        input.nextLine();
    }

    /**
     * A menu to receive input that will be passed to the inventory class.
     */
    private void printAllMembers() {
        newPage("Members Listing");
        inv.printAllMembers(consoleWidth);
        System.out.println("Press enter to return to menu.");
        input.nextLine();
    }

    /**
     * A menu to receive input that will be passed to the inventory class.
     */
    private void printHolding() {
        boolean keepGoing = true;
        while (keepGoing) {
            newPage("Holding");
            //Get the ID
            String ID = getExistingID("Holding", HOLDING_TYPES);
            if (ID != null) {
                newPage("Holding: " + ID);
                inv.printHolding(ID);
            }
            char choice = receiveStringInput("Do you want to print another holding?", CHOICE_OPTIONS, "n", 1).charAt(0);
            if (choice != 'y') {
                keepGoing = false;
            }
        }
    }

    /**
     * A menu to receive input that will be passed to the inventory class.
     */
    private void printMember() {
        boolean keepGoing = true;
        while (keepGoing) {
            newPage("Member");

            //Get the ID
            String ID = getExistingID("Member", MEMBER_TYPES);
            if (ID != null) {
                newPage("Member: " + ID);
                inv.printMember(ID);
            }
            char choice = receiveStringInput("Do you want to print another member?", CHOICE_OPTIONS, "n", 1).charAt(0);
            if (choice != 'y') {
                keepGoing = false;
            }
        }
    }

    private void save() {
        newPage("Save");
        String folderName;
        System.out.println("Enter the folder that you want to save to. You have 5 seconds to comply or a default will be chosen:");
        //Get the save
        folderName = returnWaitInput(10, "save");

        try {
            inv.save(folderName);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        System.out.println("Press enter to return to menu");
        input.nextLine();

    }

    private String returnWaitInput(int waitTime, String defaultResult) { //http://stackoverflow.com/questions/10059068/set-timeout-for-users-input (Jeffrey's answer)
        //Start an input
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        //Get the start time
        long startTime = System.currentTimeMillis();
        String result;

        //Tell them how long they have
        System.out.println(Utilities.INFORMATION_MESSAGE + "You have " + waitTime + " seconds until an answer is chosen for you. You can press enter to choose the default." + Utilities.ANSI_GREEN + "Press enter once you have entered your input" + Utilities.ANSI_RESET);
        System.out.print(Utilities.INPUT_MESSAGE);
        try {
            //noinspection StatementWithEmptyBody
            //Wait for 1000 * wait time milliseconds
            //noinspection StatementWithEmptyBody
            while ((System.currentTimeMillis() - startTime) < waitTime * 1000
                    && !in.ready()) {
            }
            //Get the input
            if (in.ready()) {
                result = in.readLine();
            }
            //If there was no input default will be selected.
            else {
                System.out.println(Utilities.INFORMATION_MESSAGE + "No input was detected and a default answer has been selected");
                result = defaultResult;
            }
            if (result == null) {
                result = defaultResult;
            }
            return result;
        } catch (IOException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            result = defaultResult;

        }
        return result;
    }

    public void save(String folder) {
        newPage("Save");
        try {
            inv.save(folder);
        } catch (IOException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     *
     */
    private void load() {
        newPage("Load File");
        //Get their choice
        char choice = receiveStringInput("Do you want to use the default save location?", CHOICE_OPTIONS, "y", 1).charAt(0);
        String folder;
        //Set it to the default location
        if (choice == 'y') {
            folder = "save";
        } else {
            System.out.println("Enter the relative folder path");
            folder = input.nextLine();
        }
        //Load
        try {
            inv.load(folder);
        } catch (IOException e) {
            System.out.println("No folder or files were found with the name. Files were not loaded.");
        }
        System.out.println("Press enter to return to main menu.");
        input.nextLine();
    }

    private void exit() {
        newPage("Exit");
        char choice = receiveStringInput("Do you want to exit? \"y\" for save and exit, \"e\" for exit.", CHOICE_OPTIONS, "y", 1, 30).charAt(0);
        if (choice == 'e') {
            //Cleanup and exit without save
            RUN_STATUS.delete();
            System.exit(0);
        }
        if (choice == 'y') {
            try {
                //Save to lastrun and a backup- one for every day.
                inv.save("lastrun");
                DateTime currentDay = new DateTime();
                inv.save("backup\\" + currentDay.toString());
            } catch (IOException e) {
                System.out.print("An error occurred and state could not be saved.");
            }
            //Cleanup and exit
            RUN_STATUS.delete();
            System.exit(0);
        }
    }

//Admin Menu Pages


    /**
     * Edits the ID of either members or holdings.
     *
     * @param types    Used for valid types for input.
     * @param typeName Used in user facing prompts.
     */
    private void editID(char[] types, String typeName) {
        //Get the ID
        String oldID = getExistingID(typeName, types);
        //If it was entered correctly keep going
        if (oldID != null) {
            String[] options = {oldID.substring(0, 1)};
            String[] idInfo = getValidID(typeName, options);
            if (idInfo == null) {
                return;
            }
            String newID = idInfo[0] + idInfo[1];
            //Replace the ID
            inv.replaceID(oldID, newID);
        } else System.out.println(Utilities.INFORMATION_MESSAGE + "Incorrect ID, nothing was changed.");

    }

    /**
     * Edits the title of holdings.
     *
     * @param types    Used for valid types for input.
     * @param typeName Used in user facing prompts.
     */
    private void editTitle(char[] types, String typeName) {
        //Get the ID
        String oldID = getExistingID(typeName, types);
        //If it was entered correctly keep going
        if (oldID != null) {
            //Get input after prompting
            System.out.println("Please enter replacement title");
            System.out.print(Utilities.INPUT_MESSAGE);
            String title = input.nextLine();

            //Validate it
            while (title.length() == 0) {
                System.out.println(Utilities.WARNING_MESSAGE + "Please enter at least one character:");
                System.out.print(Utilities.INPUT_MESSAGE);
                title = input.nextLine();
            }
            //Replace the title
            inv.replaceTitle(oldID, title);
        } else System.out.println(Utilities.INFORMATION_MESSAGE + "Incorrect ID, nothing was changed.");

    }

    /**
     * Edits the name of members.
     *
     * @param types    Used for valid types for input.
     * @param typeName Used in user facing prompts.
     */
    private void editName(char[] types, String typeName) {
        //Get the ID
        String oldID = getExistingID(typeName, types);
        //If it was entered correctly keep going
        if (oldID != null) {
            //Get input after prompting
            System.out.println("Please enter replacement name");
            System.out.print(Utilities.INPUT_MESSAGE);
            String name = input.nextLine();

            //Validate it
            while (name.length() > 0) {
                System.out.println(Utilities.WARNING_MESSAGE + "Please enter at least one character:");
                System.out.print(Utilities.INPUT_MESSAGE);
                name = input.nextLine();
            }
            //Replace the name and inform the user
            boolean result = inv.replaceName(oldID, name);
            if (!result) {
                System.out.println(Utilities.ERROR_MESSAGE + "Change failed.");
            }
        } else System.out.println(Utilities.INFORMATION_MESSAGE + "Incorrect ID, nothing was changed.");
    }

    /**
     * Edits the loan cost of Videos.
     */
    private void editLoanCost() {
        //We can only edit loan cost on Videos.
        char[] type = {'v'};
        String oldID = getExistingID("Video", type);

        //Validate ID
        if (oldID != null) {
            //Run input validation
            String[] choiceOptions = {"4", "6"};
            int newLoan = Integer.parseInt(receiveStringInput("Enter new Loan Fee:", choiceOptions, true, 1));
            try {
                //Replace the loan fee
                inv.replaceLoan(oldID, newLoan);
            } catch (IncorrectDetailsException e) {
                System.out.println(Utilities.ERROR_MESSAGE + " Adding failed due to " + e + " with error message: \n" + e.getMessage());
            }
        } else System.out.println(Utilities.INFORMATION_MESSAGE + "Incorrect ID, nothing was changed.");
    }

    /**
     * Activates the ID input by the user. Can be a member or holding.
     */
    private void activate() {
        //Get the ID
        String ID = getExistingID("Item", new char[]{'b', 'v', 's', 'p'});
        //If it was entered correctly keep going
        if (ID != null) {
            try {
                //Run the activation and report result
                boolean result = inv.activate(ID);
                if (!result) {
                    System.out.println(Utilities.WARNING_MESSAGE + " Activation failed");
                } else {
                    System.out.println(Utilities.INFORMATION_MESSAGE + " Activation success");
                }
            } catch (IncorrectDetailsException e) {
                System.out.println(Utilities.ERROR_MESSAGE + " Adding failed due to " + e + " with error message: \n" + e.getMessage());
            }
        }
    }

    /**
     * Deactivates the ID input by the user. Can be a member or holding.
     */
    private void deactivate() {
        //Get the ID
        String ID = getExistingID("Item", new char[]{'b', 'v', 's', 'p'});
        //If it was entered correctly keep going
        if (ID != null) {
            //Run the activation and report result
            boolean result = inv.deactivate(ID);
            if (!result) {
                System.out.println(Utilities.WARNING_MESSAGE + "Deactivation failed");
            } else {
                System.out.println(Utilities.INFORMATION_MESSAGE + "Deactivation success");
            }
        }
    }

    /**
     * Resets the credit of the ID input by the user. Is for a member.
     */
    private void resetMemberCredit() {
        //Get the ID
        String ID = getExistingID("Member", MEMBER_TYPES);
        if (ID != null) {
//Reset the credit
            inv.resetMemberCredit(ID);
            System.out.println(Utilities.INFORMATION_MESSAGE + "Credit was reset");
        }
    }

    /**
     * Returns a holding with no fee.
     */
    private void returnHoldingNoFee() {
        //Get both IDs
        String holdingID = getExistingID("Holding", HOLDING_TYPES);
        String memberID = getExistingID("Member", MEMBER_TYPES);
        try {
            //Return it
            inv.returnHoldingNoFee(holdingID, memberID);
        } catch (Exception e) {
            System.out.println(Utilities.ERROR_MESSAGE + " Adding failed due to " + e + " with error message: \n" + e.getMessage());
        }
    }

    /**
     * Compares the program md5 state to an input md5.
     */
    private void printState() {
        //Print an MD5 of the state.
        String outputHash = inv.outputState();
        System.out.println(outputHash);

        //Request an MD5 from a previous run
        System.out.println("Enter past MD5");
        String pastHash = input.nextLine();

        //Tell the user if they are the same
        if (pastHash.equals(outputHash)) {
            System.out.println("State was preserved");
        } else {
            System.out.println("State was changed.");
        }
    }

    /**
     * A UI method for retrieving deleted holdings.
     */
    private void undeleteHolding() {
        //Print the options
        inv.printDeleted();
        //Give them a selection
        int selection = Integer.parseInt(receiveStringInput("Enter the number of the holding you want to undelete", new String[]{"0", "1", "2", "3", "4"}, true, 1));

        //Run the program and get result
        boolean result = inv.undeleteHolding(selection);
        if (!result) {
            System.out.println(Utilities.WARNING_MESSAGE + "Retrieval Failed");
        } else {
            System.out.println(Utilities.INFORMATION_MESSAGE + "Holding was retrieved");
        }
    }

}
