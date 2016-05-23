/*
 * Copyright Alex Kinross-Smith (c) 2016.
 * s3603437@student.rmit.edu.au
 * alex@akinrosssmith.id.au
 */

/*
*@author - Alex Kinross-Smith
*/
package lms.members;

import lms.UniqueID;
import lms.exceptions.InsufficientCreditException;
import lms.SystemOperations;
import lms.exceptions.ItemInactiveException;
import lms.exceptions.OnLoanException;
import lms.holding.*;
import lms.util.DateTime;
import lms.util.IDManager;
import lms.util.Utilities;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.StringTokenizer;

@SuppressWarnings("WeakerAccess")
public abstract class Member implements SystemOperations, UniqueID {
    private String ID;
    private String name;
    private int maxCredit;
    protected double balance;
    protected ArrayList<Holding> borrowed = new ArrayList<Holding>();
    private boolean active;
    private String uniqueID;

    /**
     * Used for restoring state. Allows all instance variables to be loaded at once.
     * @param ID 7 long identifier starting with a character, and ending with 6 digits.
     * @param name the full name of the member
     * @param maxCredit the maximum amount of credit.
     * @param balance the member's current balance.
     * @param borrowed an array of holdings that are currently borrowed.
     * @param active the active status of the member
     * @param uniqueID a unique id that is never changed. Identifies the member if id or details are changed.
     */
    public Member(String ID, String name, int maxCredit, int balance, Holding[] borrowed, boolean active, String uniqueID) {
        this.ID = ID;
        this.name = name;
        this.maxCredit = maxCredit;
        this.balance = balance;
        for (Holding h:borrowed
             ) {
            this.borrowed.add(h);
        }
        this.active = active;
        this.uniqueID = uniqueID;
        IDManager.addIdentifier(uniqueID);
    }

    public Member(String memberID, String fullName, int credit) {
        setID(memberID);
        setName(fullName);
        setCredit(credit);
        this.balance = credit;
        activate();
        setUniqueID();
    }

/* Setters */

    protected boolean setID(String ID) {
        this.ID = ID;
        return true;
    } //Done

    public boolean setCredit(int credit) {
        if (credit < maxCredit) {
            this.balance = credit;
            return true;
        } else return false;
    } //Done

    public boolean setName(String name) {
        if (name.length() > 0) {
            this.name = name;
            return true;
        } else return false;
    } //Done



    /* Getters */

    public double getBalance() {
        return balance;
    }

    public boolean isActive() {
        return active;
    }

    public String getFullName() {
        return (name);
    }

    public boolean getStatus() {
        return (active);
    }

    public String getID() {
        return (ID);
    }

    public int getMaxCredit() {
        return (maxCredit);
    }

    public int calculateRemainingCredit() {
        return ((int) balance);
    }

    public void resetCredit() { //Done
        balance = maxCredit;
    }

    public boolean activate() {
        this.active = true;
        return (true);
    }

    public boolean deactivate() {
        this.active = false;
        return (true);
    }

    public Holding[] getCurrentHoldings() {
        Holding[] holdings = new Holding[15];
        return (borrowed.toArray(holdings));
    }

    public boolean updateRemainingCredit(int loanFee) {
        if (balance - loanFee >= 0 && balance - loanFee <= maxCredit) {
            balance -= loanFee;
            return true;
        } else {
            return false;
        }
    }

    public boolean checkAllowedCreditOverdraw(int loanFee) {
        return getBalance() - loanFee >= 0;
    }

    public boolean borrowHolding(Holding holding) throws InsufficientCreditException, ItemInactiveException, OnLoanException {
        if (balance - holding.getDefaultLoanFee() > 0 && active) {
            if (holding.borrowHolding()) {
                borrowed.add(holding);
                balance -= holding.getDefaultLoanFee();
                return true;
            } else
                throw new OnLoanException(Utilities.WARNING_MESSAGE + "Holding on Loan: Book was unavailable to be borrowed. Book was not added to your account and you were not charged.");
        } else
            throw new InsufficientCreditException(Utilities.WARNING_MESSAGE + "Balance was insufficient to borrow book");
    }/*
    A member can only be borrow a holding if:
             They are currently active in the system
     They have enough credit to pay the initial loan fee*/

    public boolean returnHolding(Holding holding, DateTime returnDate) throws InsufficientCreditException, ItemInactiveException, OnLoanException {
        int searchedPos = findHolding(holding);
        if (searchedPos <= 0) {
            int lateFee = borrowed.get(searchedPos).calculateLateFee(returnDate);
            if (lateFee < balance) {
                balance -= lateFee;
            } else {
                System.out.println(Utilities.INFORMATION_MESSAGE + " Balance must be greater than " + lateFee + " to return item");
                return false;
            }
            if (borrowed.get(searchedPos).returnHolding(returnDate)) {
                borrowed.remove(searchedPos);
                return (true);
            } else {
                System.out.println(Utilities.ERROR_MESSAGE + " Holding could not be returned");
                return false;
            }
        } else {
            System.out.println(Utilities.WARNING_MESSAGE + " User has not borrowed holding");
            return false;
        }
    }

    public boolean returnHoldingNoFee(Holding holding, DateTime returnDate) throws ItemInactiveException, OnLoanException {
        int searchedPos = findHolding(holding);
        if (searchedPos <= 0) {
            if (borrowed.get(searchedPos).returnHolding(returnDate)) {
                borrowed.remove(searchedPos);
                return (true);
            } else {
                System.out.println(Utilities.WARNING_MESSAGE + " Holding could not be returned");
                return false;
            }
        } else {
            System.out.println(Utilities.INFORMATION_MESSAGE + " User has not borrowed holding");
            return false;
        }
    }

    protected int findHolding(Holding holding) {
           return borrowed.indexOf(holding);
    }

    public void print() {
        if (active) {
            System.out.println("ID:" + getID());
        } else {
            System.out.println("ID:" + getID() + " : Inactive");

        }
        System.out.println("Name:" + getFullName());
        System.out.println("Max Credit: " + getMaxCredit());
        System.out.println("Current Balance: " + getBalance());
        System.out.println("Current Holdings:");
        for (Holding holding : borrowed) {
            if (holding != null) {
                System.out.print(holding.getTitle() + " (" + holding.getID() + " )");
            }
        }
        System.out.println();
    }

        public int numberOfBorrowedHoldings() {
        int result = 0;
        for (Holding holding : borrowed) {
            if (holding != null) {
                result++;
            }
        }
        return result;
    }

    public String toString() {
        return (getID() + ":" + getFullName() + ":" + calculateRemainingCredit());
    }

    public String toFile() {
        return (getID() + "," + getFullName() + "," + calculateRemainingCredit() + "," + balance + "," + borrowedToString() + "," + active + "," + getUniqueID());
    }

    private String borrowedToString() {
        //http://stackoverflow.com/questions/1978933/a-quick-and-easy-way-to-join-array-elements-with-a-separator-the-opposite-of-sp
        ArrayList<String> borrowedUniqueID = new ArrayList<String>();
        for (Holding h:borrowed
             ) {
            borrowedUniqueID.add(h.getUniqueID());
        }

        //  This requires java 1.8. A version that is less performant is commented out below and should work on 1.7
        String result = String.join(":", borrowedUniqueID);

        //This code works. Replaced with more concise code.
        /*String result = null;
        for (int i = 0; i < borrowed.size();i++) {
            Holding holding = borrowed.get(i);
            if (holding != null) {
                if(i < borrowed.size()-1) {
                    result += ":" + holding.getUniqueID();
                }else{
                    result += holding.getUniqueID();
                }
            }
        }*/
        return result;
    }


    public String getUniqueID() {
        return uniqueID;
    }

    public boolean setUniqueID() {
        if (uniqueID == null) {
            this.uniqueID = IDManager.generateUniqueID();
            return true;
        } else return false;
    }

    public void setUniqueID(String uniqueID) {
        this.uniqueID = uniqueID;
    }
}