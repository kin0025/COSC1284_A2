/*
 * Copyright Alex Kinross-Smith (c) 2016.
 * s3603437@student.rmit.edu.au
 * alex@akinrosssmith.id.au
 */

package lms;/*
*@author - Alex Kinross-Smith
*/

import lms.exceptions.*;
import lms.holding.Book;
import lms.holding.Holding;
import lms.holding.Video;
import lms.members.Member;
import lms.members.PremiumMember;
import lms.members.StandardMember;
import lms.ui.SearchInventory;
import lms.util.DateTime;
import lms.util.IDManager;
import lms.util.IO;
import lms.util.Utilities;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;


/**
 * Created by akinr on 11/04/2016 as part of s3603437_A2
 */
public class Inventory {
    private static final String fileExtension = Utilities.FILE_EXTENSION;
    //Start Screen
    private final ArrayList<Holding> holdings;
    private final Holding[] deletedHoldings = new Holding[5];
    private final ArrayList<Member> members;
    private int numberOfHoldings = 0;
    private int numberOfMembers = 0;
    private final IO io = new IO(this);

    /**
     * Creates holding and member arrays of size 15
     */
    public Inventory() {
        this.holdings = new ArrayList<>();
        this.members = new ArrayList<>();
    }

    /**
     * Gets the number of holdings already in use.
     *
     * @return number of holdings currently in inventory.
     */
    int getNumberOfHoldings() {
        return (numberOfHoldings);
    }

    /**
     * Gets the number of members already in use.
     *
     * @return number of members currently in inventory.
     */
    int getNumberOfMembers() {
        return numberOfMembers;
    }

    /**
     * Provides a string form printout of inventory values.
     *
     * @return Returns "Holdings:"number/max "Members:" Number/max
     */
    public String infoPrintout() {
        return ("Holdings:" + numberOfHoldings + "/" + holdings.size() + " | " + "Members:" + numberOfMembers + "/" + members.size());
    }

    /**
     * Creates a 6 digit ID that is valid for use and is not already used for a holding of <code>itemType</code>
     *
     * @param itemType the type of item been examined
     * @return String form 6 digit ID.
     */
    public String generateValidID(char itemType) {
        String randomID;
        //Use the Utilities method to create random IDs until one is unique. This could cause problems if the arrays are larger and thousands of IDs are taken. In this case, sequential generation might be better.
        do {
            randomID = Utilities.randomID();
        } while (!checkID(randomID, itemType).equalsIgnoreCase("valid"));
        return randomID;
    }

    /**
     * Checks an ID for validity, and if it has already been taken.
     *
     * @param ID       the numerical aspect of the ID been examined.
     * @param itemType the type of item been examined
     * @return Returns one of these strings: <code>Wrong Number of Digits,ID contains characters,Already taken, Valid</code>
     */
    String checkID(String ID, char itemType) {

        if (ID.length() != 6) {
            return ("Wrong Number of Digits");
        }

        boolean validID = true;

        int i = 0;

        while (ID.length() > i && validID) {
            //So we need to cast the id to a char to an int. This returns an numerical representation of their ASCII code. The numbers 0-9 are represented by ASCII codes between 48 and 58.
            if (!((int) ID.charAt(i) >= 48 && (int) ID.charAt(i) <= 58)) { //Ascii codes for numbers are between 48 and 58 (including 0). If each character is between these values, they are numbers.
                validID = false;
            }
            i++;
        }
        if (!validID) return ("ID contains characters");
        //Checking for duplicate ID
        switch (itemType) {
            case 'b':
            case 'v':
                for (Holding holding : holdings) {
                    if (holding.getID().equalsIgnoreCase(itemType + ID)) {
                        return "Already Taken";
                    }
                }
                break;
            case 's':

            case 'p':
                for (Member member : members) {
                    if (member.getID().equalsIgnoreCase(itemType + ID)) {
                        return "Already Taken";
                    }
                }
                break;
        }


        return ("Valid");

    }

    /**
     * Creates a member based on provided information.
     *
     * @param ID       The id of the member
     * @param itemType the item type of the member
     * @param title    the title of the member
     * @param loanFee  the loan fee for the member
     * @return whether the holding was successfully added
     */
    public boolean addHolding(String ID, char itemType, String title, int loanFee) throws IncorrectDetailsException {
        //Check that there is still room in inventory for a new holding.
        recalculateStatistics();
        if (numberOfHoldings < holdings.size()) {

            //Check that the ID is valid
            if (checkID(ID, itemType).equals("Valid")) {

                //Set the proper concatenated ID.
                String holdingID = itemType + ID;
                //Find the first open slot in inventory
                //Create a book if the type is set to book
                if (itemType == 'b') {
                    holdings.add(new Book(holdingID, title));
                    recalculateStatistics();
                    return true;
                }
                //Create a video if they type is a video.
                else if (itemType == 'v') {
                    holdings.add(new Video(holdingID, title, loanFee));
                    numberOfHoldings++;
                    return true;
                }
            } else System.out.println(Utilities.WARNING_MESSAGE + " Invalid ID");
        } else
            System.out.println(Utilities.ERROR_MESSAGE + " All holding spots are taken. Please pay for a larger subscription to support more holdings, or remove exiting holdings");
        return false;
    }

    /**
     * Recalculates the statistics for number of items.
     */
    public void recalculateStatistics() {
        numberOfHoldings = 0;
        numberOfMembers = 0;
        for (Holding holding : holdings) {
            if (holding != null) {
                numberOfHoldings++;
            }
        }
        for (Member member : members) {
            if (member != null) {
                numberOfMembers++;
            }
        }
    }

    /* Methods that add items to inventory */

    public boolean addMember(String ID, char itemType, String name) {
        //Check there is room for a new member


        //Check the id is valid
        if (checkID(ID, itemType).equals("Valid")) {

            String memberID = itemType + ID;
            //Find the first blank position in the array to fill.

            //Create the member based on type.
            if (itemType == 's') {
                members.add(new StandardMember(memberID, name));
                numberOfMembers++;
                return true;
            } else if (itemType == 'p') {
                members.add(new PremiumMember(memberID, name));
                numberOfMembers++;
                return true;
            }
        }
        System.out.println(Utilities.WARNING_MESSAGE + " Invalid ID");
        return false;
    }

    /**
     * A wrapper method that has forcing removal disabled.
     *
     * @param ID The id to be removed
     * @return whether the item was successfully removed.
     */
    public boolean removeHolding(String ID) {
        return removeHolding(ID, false);
    }

    /* Remove */

    /**
     * Removes a holding from inventory
     *
     * @param ID    The ID of the holding to be removed.
     * @param force Whether to delete holding regardless of errors or borrowed status.
     * @return The success state of the operation.
     */
    public boolean removeHolding(String ID, boolean force) {
        //Find the holding to be removed
        Holding holding = (Holding) searchArrays(ID);
        //If the holding is not in inventory fail immediately.
        if (holding == null) {
            System.out.println("Holding with ID: " + ID + " does not exist.");
            return false;
        }
        //Set the holding.
        //Initialise this for a foreach. If the holding is not found it will not be set.
        boolean holdingIsBorrowed = false;
        Member containingMember = null;
        for (Member member : members) {
            //If the member is valid and contains the holding mark the holding as borrowed and assign the member to a variable.
            if (member != null && member.containsHolding(holding)) {
                holdingIsBorrowed = true;
                containingMember = member;
                //Break out of the loop, as we don't need to look through the rest of the members once we have found one.
                break;

            }
        }
        //If the holding is not assigned to a member delete it
        if (!holdingIsBorrowed) {
            //Add it to the backup array for restoration - this is not saved between runs.
            addDeleted(holding);
            //Remove it from the normal array.
            holdings.remove(holding);
            recalculateStatistics();
            //Tell the user some inane stats.
            System.out.println(Utilities.INFORMATION_MESSAGE + " Holding " + ID + " was removed. There are " + numberOfHoldings + " holdings still in system");
            //Operations succeeded.
            return true;
        }
        //If the holding is borrowed, force has been selected remove it from both the inventory and the member that has borrowed it.
        else if (force) {
            //Run through the deleted process
            addDeleted(holding);
            holdings.remove(holding);
            //Remove it from the member.
            containingMember.removeHolding(holding);
            recalculateStatistics();
            System.out.println(Utilities.INFORMATION_MESSAGE + " Holding " + ID + " was removed from member and inventory. There are " + numberOfHoldings + " holdings still in system");
            return true;
        } else {
            System.out.println("Holding was borrowed and could not be removed");
        }

        return false;
    }

    /**
     * Searches for the array index of the holding or member with the ID.
     *
     * @param ID Search for this ID in the arrays.
     * @return array index that has an item that has a matching ID.
     */
    private SystemOperations searchArrays(String ID) {
        char itemType = ID.toLowerCase().charAt(0);

        ArrayList<? extends SystemOperations> array = null;
        //If the type is on of the holding types, set the array to the holdings array.
        for (char c : UI.HOLDING_TYPES) {
            if (itemType == c) {
                array = holdings;
            }
        }

        //If the type is of a member set the array to the members array
        for (char c : UI.MEMBER_TYPES) {
            if (itemType == c) {
                array = members;
            }
        }

        //If the array was set as it matched something use it here.
        if (array != null) {
            //Search through the array for something that matches the ID given. If found, return it.
            for (SystemOperations holding : array) {
                if (holding.getID().equals(ID)) {
                    return (holding);
                }

            }
            //If we didn't find anything tell the user.
            System.out.println(Utilities.INFORMATION_MESSAGE + " Unable to find holding with that ID");
            return (null);
        }

        //The array was null- hence the ID did not match one of the accepted types. Report that to the user.
        else {
            System.out.println(Utilities.WARNING_MESSAGE + " Wrong ID format");
            return null;
        }
    }

    /**
     * Adds a deleted holding to a special array so they can be restored if needed.
     *
     * @param deleted The holding to be deleted.
     */
    private void addDeleted(Holding deleted) {
        //Remove the UUID of the holding that will fall off the array.
        if (deletedHoldings[deletedHoldings.length - 1] != null) {
            IDManager.removeIdentifier(deletedHoldings[deletedHoldings.length - 1].getUUID());
        }
//Shift the whole array down one index.
        //Reference IntelliJ IDEA code analyse.
        System.arraycopy(deletedHoldings, 0, deletedHoldings, 1, deletedHoldings.length - 1); //So I had the code below, and my IDE was like "Performance Issues", so I performed its recommended fix.
        /*for (int i = deletedHoldings.length - 1; 0 < i; i--) {
            deletedHoldings[i] = deletedHoldings[i - 1];
        }*/
        //Set the first position to the newly deleted holding.
        deletedHoldings[0] = deleted;
    }

    public boolean undeleteHolding(int deletedIndex) {

        //Set the deleted holding
        Holding deleted = deletedHoldings[deletedIndex];
        //If there is no holding with the index return false
        if (deleted == null) {
            return false;
        }
        //Shift the holding to the normal array.
        else {
            holdings.add(deleted);
            deletedHoldings[deletedIndex] = null;
            return true;
        }

    }

    /**
     * Print the holdings in the deleted array.
     */
    public void printDeleted() {
        for (int i = 0; i < deletedHoldings.length; i++) {
            if (deletedHoldings[i] != null) {
                System.out.print(i + Utilities.ANSI_RED + ": " + Utilities.ANSI_RESET);
                System.out.println(deletedHoldings[i]);
            }
        }
    }

    /**
     * Remove a member using an input ID
     *
     * @param ID the ID to be removed
     * @return The success of the removal
     */
    public boolean removeMember(String ID) {
        //Find the member
        Member member = (Member) searchArrays(ID);
        //Only delete the
        if (member != null) {

            //Only delete the member if all holdings are returned
            if (member.numberOfBorrowedHoldings() == 0) {
                //Delete the ID, remove the member from the array
                IDManager.removeIdentifier(member.getUUID());
                members.remove(member);
                recalculateStatistics();
                System.out.println(Utilities.INFORMATION_MESSAGE + " Member " + ID + " was removed. There are " + numberOfMembers + " members still in system");
                return true;
            } else {
                System.out.println(Utilities.INFORMATION_MESSAGE + " Member still has borrowed holdings, please remove before attempting to remove member.");
                return false;
            }

        } else {
            System.out.println(Utilities.WARNING_MESSAGE + " Nothing was removed as the member could not be found");
            return false;
        }
    }

    /**
     * Borrows a holding <code>holdingID</code> to the member <code>memberID</code>.
     *
     * @param holdingID the ID of the holding been borrowed
     * @param memberID  the ID of the member that is borrowing the holding.
     * @return The success of the borrow operation
     * @throws InsufficientCreditException
     * @throws OnLoanException
     * @throws ItemInactiveException
     */
    public boolean borrowHolding(String holdingID, String memberID) throws InsufficientCreditException, OnLoanException, ItemInactiveException {
        //Find the holding and member positions in the array
        Holding holding = (Holding) searchArrays(holdingID);
        Member member = (Member) searchArrays(memberID);
        //Borrow them
        if (holding != null && member != null) {
            member.borrowHolding(holding);
            return true;
        } else return false;
    }

    /**
     * Returns a holding <code>holdingID</code> for the member <code>memberID</code> with no fee,
     *
     * @param holdingID the ID of the holding been returned
     * @param memberID  the ID of the member that is returning the holding.
     * @return The success of the return operation
     * @throws InsufficientCreditException
     * @throws NotBorrowedException
     * @throws ItemInactiveException
     * @throws TimeTravelException
     */
    public void returnHoldingNoFee(String holdingID, String memberID) throws NotBorrowedException, ItemInactiveException, TimeTravelException {
        //Find the holding and member array positions.
        Holding holding = (Holding) searchArrays(holdingID);
        Member member = (Member) searchArrays(memberID);
        //Return them using the borrow date to ensure no fee (just in case)
        if (holding != null && member != null) {
            member.returnHoldingNoFee(holding, holding.getBorrowDate());
        } else {
        }

    }

    /**
     * Returns a holding <code>holdingID</code> for the member <code>memberID</code>
     *
     * @param holdingID the ID of the holding been returned
     * @param memberID  the ID of the member that is returning the holding.
     * @return The success of the return operation
     * @throws InsufficientCreditException
     * @throws NotBorrowedException
     * @throws ItemInactiveException
     * @throws TimeTravelException
     */
    public boolean returnHolding(String holdingID, String memberID, DateTime returnDate) throws InsufficientCreditException, NotBorrowedException, ItemInactiveException, TimeTravelException {
        //Find the holding and member array positions.
        Holding holding = (Holding) searchArrays(holdingID);
        Member member = (Member) searchArrays(memberID);
        //Return them, passing the returnDate.
        if (holding != null && member != null) {
            member.returnHolding(holding, returnDate);
            return true;
        } else return false;

    }

    /**
     * Print all the holdings in the array, leaving a separator between each.
     *
     * @param pageWidth The width, in number of characters, of the separator
     */
    public void printAllHoldings(int pageWidth) {
        //Iterate through the array, printing each holding.
        for (Holding holding : holdings) {
            if (holding != null) {
                holding.print();
                //Print a separator
                UI.printCharTimes('=', pageWidth, true);
            }
        }
    }

    /**
     * Print all the members in the array, leaving a separator between each.
     *
     * @param pageWidth The width, in number of characters, of the separator
     */
    public void printAllMembers(int pageWidth) {
        for (Member member : members) {
            if (member != null) {
                member.print();
                UI.printCharTimes('=', pageWidth, true);
            }
        }
    }

    /**
     * Print a specific holding via ID
     *
     * @param ID the ID to be used to identify the holding.
     */
    public void printHolding(String ID) {
        Holding holding = (Holding) searchArrays(ID);
        if (holding != null) {
            holding.print();
        } else System.out.println(Utilities.WARNING_MESSAGE + " No Holding Found");

    }

    /**
     * Print a specific member via ID
     *
     * @param ID the ID to be used to identify the holding.
     */
    public void printMember(String ID) {
        Member member = (Member) searchArrays(ID);
        if (member != null) {
            member.print();
        } else System.out.println(Utilities.WARNING_MESSAGE + " No Member Found");

    }

    /**
     * Returns the name of the member through ID.
     *
     * @param ID The id of the member to find the name of
     * @return the members name
     */
    public String getMemberName(String ID) {
        boolean result = false;
        //Validate that the member is of the correct type
        for (char c : Utilities.MEMBER_TYPES) {
            if (c == ID.charAt(0)) {
                result = true;
            }
        }

        //If the member id is of an acceptable type check if it exists
        if (result) {
            result = idExists(ID);
        } else {
            System.out.println(Utilities.WARNING_MESSAGE + " ID not a member. Please try again");
            return null;
        }
        Member member = ((Member) searchArrays(ID));
        if (result && member != null) {
            return (member.getFullName());
        } else {
            return null;
        }
    }

    /**
     * Check that an ID exists.
     *
     * @param ID The ID to be tested.
     * @return whether the ID exists
     */
    public boolean idExists(String ID) {
        if (ID.length() != 7) {
            System.out.println(Utilities.WARNING_MESSAGE + " Incorrect ID");
            return false;
        }
        if (searchArrays(ID) != null) {
            System.out.println(Utilities.WARNING_MESSAGE + " Not Found");
            return false;
        } else
            return true;
    }

    /**
     * Replaces an ID.
     *
     * @param oldID The ID of the thing to be replaced.
     * @param newID The ID to replace with.
     */
    public void replaceID(String oldID, String newID) {

        SystemOperations replace = searchArrays(oldID);

        //If the array was set as it matched something use it here.
        if (replace != null) {
            replace.setID(newID);
        } else {
            System.out.println("Nothing was found with that ID");
        }
    }

    /**
     * Replaces the name of a member
     *
     * @param ID   The ID of the member to be replaced
     * @param name the name to set for the member
     * @return whether the operation was successful
     */
    public boolean replaceName(String ID, String name) {
        Member member = (Member) searchArrays(ID);
        return member != null && member.setName(name);
    }

    /**
     * Replaces the title of a holding
     *
     * @param ID    The ID of the holding to be replaced
     * @param title the title to set for the holding
     */
    public void replaceTitle(String ID, String title) {
        Holding holding = (Holding) searchArrays(ID);
        if (holding != null) {
            holding.setTitle(title);
        }
    }

    /**
     * Activate a holding/member.
     *
     * @param ID the ID of the member or holding to be activated.
     * @return the success of the activation
     * @throws IncorrectDetailsException
     */
    public boolean activate(String ID) throws IncorrectDetailsException {
        SystemOperations operable = searchArrays(ID);

        //Check that the array was properly set.
        if (operable != null) {
            return operable.activate();
        } else {
            System.out.println("ID was invalid");
            return false;
        }
    }

    /**
     * Deactivates a holding/member.
     *
     * @param ID the ID of the member or holding to be deactivated.
     * @return the success of the deactivation
     */
    public boolean deactivate(String ID) {
        SystemOperations operable = searchArrays(ID);

        //Check that the array was properly set.
        if (operable != null) {
            return operable.deactivate();
        } else {
            System.out.println("ID was invalid");
            return false;
        }
    }

    /**
     * Replaces the loan fee
     *
     * @param ID      The id of the holding.
     * @param loanFee the loan fee to set
     * @throws IncorrectDetailsException
     */
    public void replaceLoan(String ID, int loanFee) throws IncorrectDetailsException {
        if (ID.charAt(0) == 'v') {
            Video video = (Video) searchArrays(ID);
            if (video != null) {
                video.setLoanFee(loanFee);
            }
        } else {
            System.out.println("Incorrect ID for setting loan fee");
        }


    }

    /**
     * Resets the member's credit to maximum
     *
     * @param ID the ID of the member to reset
     */
    public void resetMemberCredit(String ID) {
        Member member = (Member) searchArrays(ID);
        char type = ID.charAt(0);
        for (char c : UI.MEMBER_TYPES) {
            if (type == c && member != null) {
                member.resetCredit();
            }
        }

    }

    /**
     * Returns a member's balance.
     *
     * @param ID The ID of the member
     * @return The balance of the member
     */
    public double getMemberBalance(String ID) {
        Member member = (Member) searchArrays(ID);
        char type = ID.charAt(0);
        //Check that the ID is correct, and a member
        for (char c : UI.MEMBER_TYPES) {
            if (type == c && member != null) {
                return member.getBalance();
            }

        }
        System.out.println("Item not found");
        return 0;
    }


    /**
     * Loads the hash from the state file
     *
     * @param folder the name of the folder to load from relative to project root
     * @return Returns the hash of the last run.
     */
    public String loadLastHash(String folder) {
        //The file to load from
        File stateFile = new File("./" + "\\" + folder + "\\state" + fileExtension);
        Scanner state = null;

        try {
            state = new Scanner(stateFile);
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        //Check that we can get a result
        try {
            if (state != null && state.hasNextLine()) {
                return state.nextLine();
            }
        } catch (NullPointerException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Outputs the state as a hashed string
     *
     * @return Creates a string that represents full state that needs to be saved and MD5 hashes it
     */
    public String outputState() {
        return Utilities.hashString(toStateString());
    }

    /**
     * Returns a string that comprises of all holdings and members details.
     *
     * @return A string of all member and holding details.
     */
    private String toStateString() {
        String megaString = null;
        //Add all holdings to the string
        for (Holding h : holdings
                ) {
            if (h != null) {
                megaString += h.toFile();
            }
        }

        //Add all members to string
        for (Member m : members
                ) {
            if (m != null) {
                megaString += m.toFile();
            }
        }
        megaString += IDManager.stateString();
        return megaString;
    }

    /**
     * Gets a UUID, returns a holding with the same UUID
     *
     * @param uuid The UUID to check
     * @return A holding with the uuid.
     */
    public Holding uuidToHolding(String uuid) {
        for (Holding h : holdings
                ) {
            if (h != null) {
                if (h.getUUID().equals(uuid)) {
                    return h;
                }
            }
        }
        return null;
    }

    /**
     * Gets the loan time in days of the holding
     *
     * @param ID The ID of the holding
     * @return An integer in number of days in loan period.
     */
    public int getHoldingReturnTime(String ID) {
        Holding holding = (Holding) searchArrays(ID);
        if (holding != null) {
            return holding.getMaxLoanPeriod();
        } else return 0;
    }

    /**
     * Gets the late fee of the holding given a day
     *
     * @param ID        The ID of the holding
     * @param returnDay the date it was returned
     * @return number of days the holding can be borrowed for.
     */
    public int getHoldingLateFee(String ID, DateTime returnDay) {
        Holding holding = (Holding) searchArrays(ID);
        if (holding != null) {
            return holding.calculateLateFee(returnDay);
        } else return 0;
    }

    public void addHolding(String identifier, String title, int loanFee, int maxLoanPeriod, DateTime borrowDate, boolean active, String uniqueID) {
        if (identifier.charAt(0) == 'b') {
            holdings.add(new Book(identifier, title, loanFee, maxLoanPeriod, borrowDate, active, uniqueID));
        } else if (identifier.charAt(0) == 'v') {
            holdings.add(new Video(identifier, title, loanFee, maxLoanPeriod, borrowDate, active, uniqueID));
        }
    }

    public void addMember(String identifier, String name, int maxCredit, double balance, ArrayList<Holding> borrowed, boolean active, String uniqueID) {
        if (identifier.charAt(0) == 's') {
            members.add(new StandardMember(identifier, name, maxCredit, balance, borrowed, active, uniqueID));
        } else if (identifier.charAt(0) == 'p') {
            members.add(new PremiumMember(identifier, name, maxCredit, balance, borrowed, active, uniqueID));
        }
    }

    public void save(String folder) throws IOException {
        io.save(folder);
    }

    public void load(String folder) throws IOException {
        io.load(folder);
    }

    SearchInventory createSearch(){
        return new SearchInventory(members,holdings);
    }
    public Holding[] getHoldings() {
        return holdings.toArray(new Holding[holdings.size()]);
    }

    public Member[] getMembers() {
        return members.toArray(new Member[members.size()]);
    }

}

