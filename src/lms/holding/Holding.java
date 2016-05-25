/*
 * Copyright Alex Kinross-Smith (c) 2016.
 * s3603437@student.rmit.edu.au
 * alex@akinrosssmith.id.au
 */

package lms.holding;

import lms.SystemOperations;
import lms.UniqueID;
import lms.exceptions.IncorrectDetailsException;
import lms.exceptions.ItemInactiveException;
import lms.exceptions.OnLoanException;
import lms.exceptions.TimeTravelException;
import lms.util.DateTime;
import lms.util.IDManager;
import lms.util.Utilities;
/*
*@author - Alex Kinross-Smith
*/

@SuppressWarnings("WeakerAccess")
public abstract class Holding implements SystemOperations, UniqueID {
    private String ID;
    private String title;
    private int loanFee;
    private int maxLoanPeriod;
    private DateTime borrowDate;
    private boolean active;
    private String uniqueID;

    /**
     * Construct the holding using the default values and the ID and title passed through.
     *
     * @param holdingID The id of the holding.
     * @param title     the title of the holding.
     */
    public Holding(String holdingID, String title) {
        this.ID = holdingID;
        this.title = title;
        setUUID();
    }

    /**
     * Used to reconstruct all instance variables for loading.
     *
     * @param ID            The ID of the item
     * @param title         The title of the item
     * @param loanFee       the loan fee of the item
     * @param maxLoanPeriod the loan period of the item
     * @param borrowDate    the date that the item was borrowed. If it is null the holding is not borrowed.
     * @param active        the active status of the item
     * @param uniqueID      the unique identifier of the item. As ID can be changed, this is used to identify the holding when saving.
     */
    public Holding(String ID, String title, int loanFee, int maxLoanPeriod, DateTime borrowDate, boolean active, String uniqueID) {
        this.ID = ID;
        this.title = title;
        this.loanFee = loanFee;
        this.maxLoanPeriod = maxLoanPeriod;
        this.borrowDate = borrowDate;
        this.active = active;
        setUUID(uniqueID);
        if (!checkValidity().equalsIgnoreCase("valid")) {
            deactivate();
            System.out.println(Utilities.ERROR_MESSAGE + " Item " + ID + " has been deactivated due to invalid details: " + checkValidity());
        }
    }

    /* Setters */

    /**
     * Sets the ID, no validation
     *
     * @param ID the ID to be set
     * @return Always true.
     */
    public boolean setID(String ID) {
        this.ID = ID;
        return true;
    }

    /**
     * Set the loan fee to the given parameter.
     *
     * @param loanFee the loan fee to be set.
     */
    protected void setLoanFee(int loanFee) throws IncorrectDetailsException {
        this.loanFee = loanFee;
    }

    /**
     * Sets the maximum loan period in days
     *
     * @param maxLoanPeriod An integer representation of the number of days before late fees will be incurred.
     */
    protected void setMaxLoanPeriod(int maxLoanPeriod) {
        this.maxLoanPeriod = maxLoanPeriod;
    }

    /**
     * Sets the title. Validates to ensure it has characters.
     *
     * @param title A string representation of the title.
     */
    public void setTitle(String title) {
        this.title = title;

//Deactivate the holding if the title is invalid.
        if (title.length() == 0) {
            System.out.println(Utilities.ERROR_MESSAGE + " Invalid Title. Title not set and charge deactivated");
            deactivate();
        }
    }

    /* Getters */

    /**
     * Returns the id of the holding
     *
     * @return The identification string of the holding.
     */
    public String getID() {
        return (ID);
    }

    /**
     * Returns the title of the holding
     *
     * @return the title of the holding
     */
    public String getTitle() {
        return (title);
    } //Done

    /**
     * Returns the loan status of the holding based on the borrow date- if <code>borrow date == null</code>, then it is on loan.
     *
     * @return returns the loan status of the holding.
     */
    public boolean isOnLoan() {
        return borrowDate != null;
    }

    /**
     * Returns a date time representation of the borrow date.
     *
     * @return returns a DateTime of the borrowed date.
     */
    public DateTime getBorrowDate() {
        return (borrowDate);
    }

    /**
     * Returns the loan fee
     *
     * @return the loan fee
     */
    public int getDefaultLoanFee() {
        return (loanFee);
    }

    /**
     * Returns the loan period before a fee is incurred as an integer in days.
     *
     * @return An integer representation loan period in days.
     */
    public int getMaxLoanPeriod() {
        return (maxLoanPeriod);
    }

    /**
     * Returns the late fee based on the date returned.
     *
     * @param dateReturned DateTime representation of the date returned.
     * @return Late fee in dollars.
     */
    public abstract int calculateLateFee(DateTime dateReturned);

    /**
     * Returns whether the holding is active
     *
     * @return Returns true if holding is active
     */
    protected boolean getActiveStatus() {
        //Deactivate the holding if it is invalid
        if (!checkValidity().toLowerCase().equals("valid")) {
            deactivate();
        }
        return active;
    }

    /* Functional Methods */

    /**
     * Borrows the holding
     *
     * @return Returns whether the borrow was successful.
     */
    public boolean borrowHolding() throws OnLoanException, ItemInactiveException {
        //Checks if item can be borrowed.
        if (active && !isOnLoan()) {
            //Set the borrow date as the current date.
            borrowDate = new DateTime();
            return true;
        }
        //Throw exceptions if can't be borrowed.
        else if (!active) {
            throw new ItemInactiveException("Holding was marked as inactive and could not be borrowed.");
        } else if (getBorrowDate() != null) {
            throw new OnLoanException("Holding was already loaned out");
        }
        return (false);
    }

    /**
     * Returns a holding. Checks that it is been returned after it was borrowed.
     *
     * @param dateReturned The date the holding was returned.
     * @return the success of returning.
     */
    public boolean returnHolding(DateTime dateReturned) throws ItemInactiveException, OnLoanException, TimeTravelException { /** Returns true if it has been returned, false if issues encountered and not marked as returned**/
        if (isOnLoan() && DateTime.diffDays(dateReturned, borrowDate) >= 0 && active) {
            borrowDate = null;
            return true;
        } else if (!active) {
            throw new ItemInactiveException("Item was previously marked as inactive. It has not been marked as returned");
        } else if (!isOnLoan()) {
            throw new OnLoanException("Item was not borrowed, hence cannot be returned.");
        } else {
            throw new TimeTravelException("Attempt to return book in the past.");
        }
    }

    /**
     * Prints the ID, the title, the loan fee and max loan period to console.
     */
    public void print() {
        //If the holding is inactive print it
        if (!getActiveStatus()) {
            System.out.println("ID: " + ID + " : Inactive");
        } else {
            System.out.println("ID: " + ID);
        }
        System.out.println("Title: " + title);
        System.out.println("Loan Fee: $" + loanFee);
        System.out.println("Max Loan Period: " + maxLoanPeriod);
    }

    /**
     * A string representation of key file elements
     *
     * @return Returns a string in form ID:Title:LoanFee:LoanPeriod
     */
    @Override
    public String toString() {
        return (getID() + ":" + getTitle() + ":" + getDefaultLoanFee() + ":" + getMaxLoanPeriod());
    }

    /**
     * Makes a String based representation of the holdings instance variables.
     *
     * @return Returns comma sorted values in the order: ID,title,LoanFee,LoanPeriod,BorrowDate,ActiveStatus,UniqueID
     */
    public String toFile() {
        String date;
        if (getBorrowDate() != null) {
            date = getBorrowDate().toString();
        } else {
            date = null;
        }
        return (getID() + "," + getTitle() + "," + getDefaultLoanFee() + "," + getMaxLoanPeriod() + "," + date + "," + active + "," + getUUID());
    }

    /**
     * Checks validity of the holding and activates it if possible.
     *
     * @return whether the holding was activated.
     */
    public boolean activate() throws IncorrectDetailsException {
        if (checkValidity().toLowerCase().equals("valid")) {
            this.active = true;
            return true;
        } else {
            deactivate();
            throw new IncorrectDetailsException("Holding was invalid, could not be activated for reason: " + checkValidity());

        }
    }

    /**
     * Deactivates holdings.
     *
     * @return Returns whether deactivation was successful
     */
    public boolean deactivate() {
        this.active = false;
        return true;
    }

    /**
     * Assumes first character of the ID is correct, checks that the ID and title are correct.
     *
     * @return A string form of the validity.
     */
    public String checkValidity() {
        return checkValidity(getID().charAt(0));
    }

    /**
     * Checks the first character of the ID is the itemType, checks that the ID and title are correct.
     *
     * @param itemType the character that the first character of the ID must equal
     * @return A string form of the validity.
     */
    public String checkValidity(char itemType) {
        //Checks the length of the ID and its validity.
        if (getID().length() != 7 || !Utilities.isIDValid(itemType, getID())) {
            return ("Invalid ID");
        }
        //Check the title's validity.
        else if (getTitle().length() <= 0) {
            return ("Invalid Title");
        } else {
            //Must be valid.
            return ("Valid");
        }
    }

    /**
     * Returns the unique ID of the item.
     * @return a 6 digit String form unique ID
     */
    public String getUUID() {
        return uniqueID;
    }

    /**
     * Sets a unique ID and adds it to the database to ensure no duplicates.
     * @return If there is already a unique ID returns false and does not add a new unique id.
     */
    public boolean setUUID() {
        if (uniqueID == null) {
            this.uniqueID = IDManager.generateUUID();
            return true;
        } else return false;
    }

    /**
     * Sets unique ID for the item, and adds it to IDManager.
     * @param uniqueID The unique ID to be set.
     */
    public void setUUID(String uniqueID) {
        this.uniqueID = uniqueID;
        IDManager.addIdentifier(uniqueID);
    }
}
