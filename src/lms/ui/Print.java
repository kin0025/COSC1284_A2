/*
 * Copyright Alex Kinross-Smith (c) 2016.
 * s3603437@student.rmit.edu.au
 * alex@akinrosssmith.id.au
 */

/*
*@author - Alex Kinross-Smith
*/
package lms.ui;

import static lms.ui.UI.*;
import static lms.ui.Input.*;
import static lms.ui.Shared.*;
/**
 * Created by kin0025 on 15/07/2016.
 */
public class Print {
    /**
     * A menu to receive input that will be passed to the inventory class.
     */
    public static void printAllHoldings() {
        newPage("Holding Listing");
        inv.printAllHoldings(consoleWidth);
        System.out.println("Press enter to return to menu.");
        input.nextLine();
    }

    /**
     * A menu to receive input that will be passed to the inventory class.
     */
    public static void printAllMembers() {
        newPage("Members Listing");
        inv.printAllMembers(consoleWidth);
        System.out.println("Press enter to return to menu.");
        input.nextLine();
    }

    /**
     * A menu to receive input that will be passed to the inventory class.
     */
    public static void printHolding() {
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
    public static void printMember() {
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

    /**
     * Compares the program md5 state to an input md5.
     */
    public static void printState() {
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

}
