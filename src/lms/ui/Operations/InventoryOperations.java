/*
 * Copyright Alex Kinross-Smith (c) 2016.
 * s3603437@student.rmit.edu.au
 * alex@akinrosssmith.id.au
 */

/*
*@author - Alex Kinross-Smith
*/
package lms.ui.Operations;

import lms.exceptions.IncorrectDetailsException;
import lms.util.DateTime;
import lms.util.Utilities;

import java.io.IOException;
import java.util.StringTokenizer;

/**
 * Created by kin0025 on 15/07/2016.
 */
public class InventoryOperations {

    /**
     * A menu to receive input that will be passed to the inventory class.
     */
    private void addHolding() {
        boolean keepGoing = true;
        while (keepGoing) {
            //Using the the method to take input.

            //Creating the first page
            Shared.newPage("Add Holding");
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
