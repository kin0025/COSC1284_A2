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

import java.util.StringTokenizer;

public abstract class Member implements SystemOperations, UniqueID {
    private String ID;
    private String name;
    private int maxCredit;
    protected int balance;
    protected Holding[] borrowed = new Holding[15];
    private boolean active;
    private String uniqueID;

    public Member(String ID, String name, int maxCredit, int balance, Holding[] borrowed, boolean active, String uniqueID) {
        this.ID = ID;
        this.name = name;
        this.maxCredit = maxCredit;
        this.balance = balance;
        this.borrowed = borrowed;
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

    public int getBalance() {
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
        return (balance);
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
        return (borrowed);
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
                int holdingSlot = findFirstEmptyHoldingSlot();
                if (holdingSlot < 0) {
                    System.out.println(Utilities.ERROR_MESSAGE + " Your account has too many books borrowed and none can be added.");
                    return false;
                }
                borrowed[holdingSlot] = holding;
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
            int lateFee = borrowed[searchedPos].calculateLateFee(returnDate);
            if (lateFee < balance) {
                balance -= lateFee;
            } else {
                System.out.println(Utilities.INFORMATION_MESSAGE + " Balance must be greater than " + lateFee + " to return item");
                return false;
            }
            if (borrowed[searchedPos].returnHolding(returnDate)) {
                borrowed[searchedPos] = null;
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
            if (borrowed[searchedPos].returnHolding(returnDate)) {
                borrowed[searchedPos] = null;
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
        int index = -1;
        for (int i = 0; i < borrowed.length; i++) {
            if (borrowed[i] != null && borrowed[i].equals(holding)) {
                index = i;
            }
        }
        return index;
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

    private int findFirstEmptyHoldingSlot() {
        for (int i = 0; i < borrowed.length; i++) {
            if (borrowed[i] == null) {
                return (i);
            }
        }
        System.out.println(Utilities.INFORMATION_MESSAGE + " Borrower has too many books, return some and try again");
        return (-1);
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
        String result = null;
        for (int i = 0; i < borrowed.length;i++) {
            Holding holding = borrowed[i];
            if (holding != null) {
                if(i < borrowed.length-1) {
                    result += ":" + holding.getUniqueID();
                }else{
                    result += holding.getUniqueID();
                }
            }
        }
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