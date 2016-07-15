/*
 * Copyright Alex Kinross-Smith (c) 2016.
 * s3603437@student.rmit.edu.au
 * alex@akinrosssmith.id.au
 */

/*
*@author - Alex Kinross-Smith
*/
package lms.ui;

import lms.exceptions.IncorrectDetailsException;
import lms.util.AdminVerify;
import lms.util.DateTime;
import lms.util.Utilities;

import java.io.File;
import java.io.IOException;

import static lms.ui.UI.*;
import static lms.ui.Input.*;
import static lms.ui.Shared.*;

/**
 * Created by kin0025 on 15/07/2016.
 */
public class Shared {
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
    public static void logoBoot() {
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
        char choice = Input.receiveStringInput("Do you want to load the default inventory, a saved inventory or start new?", choiceOptions, defaultChoice, 1).charAt(0);

        //Set default
        if (choice == 'd') {
            addDefault();
        }
        //Choices of folder, and default set before
        else if (choice == 's') {

            choice = Input.receiveStringInput("What do folder do you want to use?", new String[]{"c (custom)", "b (backup)", "s (save)", "l (lastrun)"}, saveLocation, 1).charAt(0);
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
     * Creates a new page. Formatted, displays current date and the status of inventory.
     *
     * @param title Used to make the central element of the page.
     **/
    public static void newPage(String title) {
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
    private static void addDefault() {
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


}
