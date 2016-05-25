/*
 * Copyright Alex Kinross-Smith (c) 2016.
 * s3603437@student.rmit.edu.au
 * alex@akinrosssmith.id.au
 */

/*
*@author - Alex Kinross-Smith
*/
package lms.members;

import lms.SystemOperations;
import lms.UniqueID;
import lms.exceptions.InsufficientCreditException;
import lms.exceptions.ItemInactiveException;
import lms.exceptions.OnLoanException;
import lms.exceptions.TimeTravelException;
import lms.holding.Holding;
import lms.util.DateTime;
import lms.util.IDManager;
import lms.util.Utilities;

import java.util.ArrayList;

/**
 * The type Member.
 */
@SuppressWarnings("WeakerAccess")
public abstract class Member implements SystemOperations, UniqueID {
    private String ID;
    private String name;
    private final int maxCredit;
    /**
     * The Balance.
     */
    protected double balance;
    /**
     * The Borrowed.
     */
    protected ArrayList<Holding> borrowed = new ArrayList<>();
    private boolean active;
    private String uniqueID;

    /**
     * Used for restoring state. Allows all instance variables to be loaded at once.
     *
     * @param ID        7 long identifier starting with a character, and ending with 6 digits.
     * @param name      the full name of the member
     * @param maxCredit the maximum amount of credit.
     * @param balance   the member's current balance.
     * @param borrowed  an array of holdings that are currently borrowed.
     * @param active    the active status of the member
     * @param uniqueID  a unique id that is never changed. Identifies the member if id or details are changed.
     */
    public Member(String ID, String name, int maxCredit, double balance, ArrayList<Holding> borrowed, boolean active, String uniqueID) {
        this.ID = ID;
        this.name = name;
        this.maxCredit = maxCredit;
        this.balance = balance;
        this.borrowed = borrowed;
        this.active = active;
        this.uniqueID = uniqueID;
    }

    /**
     * Sets the id, name and credit.
     *
     * @param memberID The 7 digit id starting with a character , ending with 6 numbers.
     * @param fullName The name of the member
     * @param credit   the maximum credit of the member
     */
    public Member(String memberID, String fullName, int credit) {
        setID(memberID);
        setName(fullName);
        this.maxCredit = credit;
        setCredit(credit);

        //Activate and validate
        activate();
        setUUID();
    }

/* Setters */

    /**
     * Sets ID, no validation
     *
     * @param ID The ID to be set
     * @return If the ID is set returns true.
     */
    protected boolean setID(String ID) {
        this.ID = ID;
        return true;
    }

    /**
     * Sets the credit/balance of the member
     *
     * @param credit The amount of credit for the user.
     * @return The success value of the operation. If trying to set a value of credit higher than maximum it will fail.
     */
    public boolean setCredit(int credit) {
        if (credit < maxCredit) {
            this.balance = credit;
            return true;
        } else return false;
    }

    /**
     * Sets the name of the member after validation.
     *
     * @param name The name attempted to be set.
     * @return False if name is empty or null.
     */
    public boolean setName(String name) {
        if (name.length() > 0) {
            this.name = name;
            return true;
        } else return false;
    }



    /* Getters */

    /**
     * Gets the balance on the members account
     *
     * @return Returns the balance
     */
    public double getBalance() {
        return balance;
    }

    /**
     * Gets the active status of the member.
     *
     * @return if the member is active returns true. Else false
     */
    public boolean isActive() {
        return active;
    }

    /**
     * Returns the full name of the member
     *
     * @return The full name of the member
     */
    public String getFullName() {
        return (name);
    }

    /**
     * The active status of the member.
     *
     * @return The active status of the member.
     */
    public boolean getStatus() {
        return (active);
    }

    /**
     * Returns the id
     *
     * @return the member's id
     */
    public String getID() {
        return (ID);
    }

    /**
     * Returns the maximum credit.
     *
     * @return Maximum credit of the member
     */
    public int getMaxCredit() {
        return (maxCredit);
    }

    /**
     * Returns the balance of the member
     *
     * @return the balance of the member as an integer- truncated from a double.
     */
    public int calculateRemainingCredit() {
        return ((int) balance);
    }

    /**
     * Resets the balance to the max credit.
     */
    public void resetCredit() { //Done
        balance = maxCredit;
    }

    /**
     * Activates the member after validation.
     *
     * @return Whether the activation was successful
     */
    public boolean activate() {
        //Check validity of both ID and name.
        if (Utilities.isIDValid(getID().charAt(0), getID()) && getFullName().length() > 0) {
            this.active = true;
            return (true);
        }
        //If invalid deactivate the member.
        deactivate();
        return false;

    }

    /**
     * Deactivates the holding.
     *
     * @return Returns the success of the action.
     */
    public boolean deactivate() {
        this.active = false;
        return (true);
    }

    /**
     * Returns the current borrowed holdings as an array.
     *
     * @return An array of holdings that are currently borrowed by this member.
     */
    public Holding[] getCurrentHoldings() {
        Holding[] holdings = new Holding[15];
        return (borrowed.toArray(holdings));
    }

    /**
     * Subtracts the loan fee from the balance.
     *
     * @param loanFee The amount to be subtracted from the balance
     * @return If subtracting the loan fee increases balance above max credit returns false.
     */
    public boolean updateRemainingCredit(int loanFee) {
        if (balance - loanFee <= maxCredit) {
            balance -= loanFee;
            return true;
        } else {
            return false;
        }
    }

    /**
     * Checks if borrowing a book will put us into negative balance. If it does, return false.
     *
     * @param loanFee The loan fee to test for
     * @return Whether a holding could be borrowed with the given loan fee.
     */
    public boolean checkAllowedCreditOverdraw(int loanFee) {
        return getBalance() - loanFee >= 0;
    }

    /**
     * Borrows a holding after checking its details.
     *
     * @param holding The holding to be borrowed.
     * @return The success of the borrow operation.
     * @throws InsufficientCreditException the insufficient credit exception
     * @throws ItemInactiveException       the item inactive exception
     * @throws OnLoanException             the on loan exception
     */
    public boolean borrowHolding(Holding holding) throws InsufficientCreditException, ItemInactiveException, OnLoanException {
        //Check if the member is valid to borrow.
        if (checkAllowedCreditOverdraw(holding.getDefaultLoanFee()) && active) {

            //Check if the holding can be borrowed
            if (holding.borrowHolding()) {

                //Borrow the holding.
                borrowed.add(holding);
                updateRemainingCredit(holding.getDefaultLoanFee());
                return true;
            } else
                //If the book cannot be borrowed
                throw new OnLoanException(Utilities.WARNING_MESSAGE + "Holding on Loan: Book was unavailable to be borrowed. Book was not added to your account and you were not charged.");
        } else
            throw new InsufficientCreditException(Utilities.WARNING_MESSAGE + "Balance was insufficient to borrow book");
    }

    /**
     * Returns a holding, calculating and deducting late fee as needed.
     *
     * @param holding    The holding to be returned.
     * @param returnDate The date it is returned.
     * @return Returns whether the return operation was successful.
     * @throws InsufficientCreditException the insufficient credit exception
     * @throws ItemInactiveException       the item inactive exception
     * @throws OnLoanException             the on loan exception
     * @throws TimeTravelException         the time travel exception
     */
    public boolean returnHolding(Holding holding, DateTime returnDate) throws InsufficientCreditException, ItemInactiveException, OnLoanException, TimeTravelException {
        int searchedPos = findHolding(holding);

        //Check that a holding was found.
        if (searchedPos <= 0) {
            //If the holding was found get the late fee and validate it.
            int lateFee = holding.calculateLateFee(returnDate);

            //Check that the
            if (lateFee > balance && holding.returnHolding(returnDate)) {
                //Then reduce the balance
                updateRemainingCredit(lateFee);
                //And remove the holding from the member.
                borrowed.remove(searchedPos);
                return (true);
            } else if (balance < lateFee) {
                throw new OnLoanException("Balance is too low to return item. It must be greater than $" + lateFee + "to return");
            } else {
                throw new ItemInactiveException("Holding was unable to be returned, likely due to deactivation");
            }
        } else {
            throw new OnLoanException("Holding was not borrowed by this user.");
        }

    }

    /**
     * Returns a holding, calculating and deducting late fee as needed.
     *
     * @param holding    The holding to be returned.
     * @param returnDate The date it is returned.
     * @return Returns whether the return operation was successful.
     * @throws ItemInactiveException the item inactive exception
     * @throws OnLoanException       the on loan exception
     * @throws TimeTravelException   the time travel exception
     */
    public boolean returnHoldingNoFee(Holding holding, DateTime returnDate) throws ItemInactiveException, OnLoanException, TimeTravelException {
        int searchedPos = findHolding(holding);

        //Validate that the holding is borrowed by the user
        if (searchedPos <= 0) {

            //Try to return the holding
            if (holding.returnHolding(returnDate)) {

                //Remove the holding from the member.
                borrowed.remove(searchedPos);
                return (true);
            } else {
                throw new ItemInactiveException("Holding could not be returned");
            }
        } else {
            throw new ItemInactiveException("Holding was not borrowed and could not be returned");
        }
    }

    /**
     * Returns the index of a holding that is passed to it from the borrowed arraylist
     *
     * @param holding The holding to be found
     * @return The index of the found holding. Returns -1 if no holding is found.
     */
    protected int findHolding(Holding holding) {
        return borrowed.indexOf(holding);
    }

    /**
     * Prints about the member to console.
     */
    public void print() {
        //If active just print the ID
        if (active) {
            System.out.println("ID:" + getID());
        }
        // Otherwise tell the user that the holding is inactive.
        else {
            System.out.println("ID:" + getID() + " : Inactive");
        }
        //Print the rest of the info.
        System.out.println("Name:" + getFullName());
        System.out.println("Max Credit: " + getMaxCredit());
        System.out.println("Current Balance: " + getBalance());
        System.out.println("Current Holdings:" + numberOfBorrowedHoldings());
        //Print all the holdings on a new line.
        for (Holding holding : borrowed) {
            //Iterate through the arraylist and print the title and ID.
            if (holding != null) {
                System.out.print(":" + holding.getTitle() + "(" + holding.getID() + ")");
            }
        }
        System.out.println();
    }

    /**
     * Calculates the number of holdings borrowed
     *
     * @return An integer of number of holdings borrowed
     */
    public int numberOfBorrowedHoldings() {
        int result = 0;
        for (Holding holding : borrowed) {
            if (holding != null) {
                result++;
            }
        }
        System.out.print(result == borrowed.size());
        return borrowed.size();
    }

    /**
     * Returns a string representation of some of the properties of the member.
     * @return A string of ":" seperated member properties.
     */
    public String toString() {
        return (getID() + ":" + getFullName() + ":" + calculateRemainingCredit());
    }

    /**
     * Returns a string that represents the file's state in a comma separate format
     *
     * @return the string
     */
    public String toFile() {
        return (getID() + "," + getFullName() + "," + calculateRemainingCredit() + "," + balance + "," + borrowedToString() + "," + active + "," + getUUID());
    }

    /**
     * @return
     */
    private String borrowedToString() {
        ArrayList<String> borrowedUniqueID = new ArrayList<>();
        for (Holding h : borrowed) {
            borrowedUniqueID.add(h.getUUID());
        }
        String result;
        //  This requires java 1.8. A version that is less performant is commented out below and should work on 1.7
        //http://stackoverflow.com/questions/1978933/a-quick-and-easy-way-to-join-array-elements-with-a-separator-the-opposite-of-sp

        result = String.join(":", borrowedUniqueID);

        /*    System.out.println("Wrong java version");
            result = null;
            for (int i = 0; i < borrowed.size(); i++) {
                Holding holding = borrowed.get(i);
                if (holding != null) {
                    if (i < borrowed.size() - 1) {
                        result += ":" + holding.getUUID();
                    } else {
                        result += holding.getUUID();
                    }
        }        }
        */

        return result;
    }

    /**
     * Returns the unique ID of the member
     * @return A string version of the unique id
     */
    public String getUUID() {
        return uniqueID;
    }

    /**
     * Generates a Unique ID and sets it.
     * @return Whether the operation was succesful.
     */
    public boolean setUUID() {
        if (uniqueID == null) {
            this.uniqueID = IDManager.generateUUID();
            return true;
        } else return false;
    }

    /**
     * Sets the unique ID to one given to it. Does not add it to the ID manager.
     * @param uniqueID The unique Id to be set.
     */
    public void setUUID(String uniqueID) {
        this.uniqueID = uniqueID;
        IDManager.addIdentifier(uniqueID);
    }
}