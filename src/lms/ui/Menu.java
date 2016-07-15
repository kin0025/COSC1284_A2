/*
 * Copyright Alex Kinross-Smith (c) 2016.
 * s3603437@student.rmit.edu.au
 * alex@akinrosssmith.id.au
 */

/*
*@author - Alex Kinross-Smith
*/
package lms.ui;

import lms.SystemOperations;
import lms.util.DateTime;
import lms.util.Utilities;

import java.io.IOException;
import static lms.ui.UI.*;
import static lms.ui.Input.*;
import static lms.ui.Shared.*;
import static lms.ui.Print.*;

import static lms.ui.Operations.InventoryOperations.*;
import static lms.ui.Operations.HoldingOperations.*;
import static lms.ui.Operations.GenericOperations.*;
import static lms.ui.Operations.MemberOperations.*;

/**
 * Created by kin0025 on 15/07/2016.
 */
public class Menu {
    /**
     * Main Menu
     * Used for choice in user facing options.
     * Runs other methods based on user input.
     */
    public static void mainMenu() {
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
                Admin.adminMenuVerify();
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

    /**
     * A tertiary menu for editing holdings.
     */
    public static void editHoldingMenu() {

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
    public static void editMemberMenu() {

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
     * Toggles the display of colours in the UI. Enable if supported by the console
     */
    private static void enableColours() {
        Utilities.toggleColours();
    }

    /**
     * Requests input for a change in console width for formatting.
     */
    private static void consoleWidthEdit() {
        System.out.println("Enter console width:");
        consoleWidth = input.nextInt();
    }

    public static void search() {
        SystemOperations item = searchInventory.searchMenu();
        if (item != null) {
            System.out.println(item.lineSummary());
        } else {
            System.out.println("Nothing was selected");
        }
        System.out.println("Please enter the ID (the content before the colon) in prompts when borrowing or returning");
    }


    public static void save() {
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


    public static void save(String folder) {
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
    public static void load() {
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

    private static void exit() {
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

}
