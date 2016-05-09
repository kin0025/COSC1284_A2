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
import lms.holding.*;
import lms.util.DateTime;


/**
 * Created by akinr on 11/04/2016 as part of s3603437_A2
 */
public abstract class Member implements SystemOperations {
    private String ID;
    private String name;
    private int maxCredit;
    protected int balance;
    protected Holding[] borrowed = new Holding[50];
    private boolean active;

    public Member(String memberID, String fullName, int maxCredit, int balance) {
        setID(memberID);
        setName(fullName);
        setCredit(balance);
        this.maxCredit = maxCredit;
    }

    public Member(String memberID, String fullName, int credit) {
        setID(memberID);
        setName(fullName);
        this.maxCredit = credit;
        this.balance = credit;
    }

    protected Member() {
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
    } //Done

    public boolean getStatus() {
        return (active);
    } //Done

    public String getID() {
        return (ID);
    } //Done

    public int getMaxCredit() {
        return (maxCredit);
    } //Done

    public int calculateRemainingCredit() {
        return (balance);
    } //Done

    public void resetCredit() { //Done
        balance = maxCredit;
    } //Done

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
        } else return false;
    }

    public abstract boolean checkAllowedCreditOverdraw(int loanFee);
    //todo what does this mean? Why do we need loan fee?

    public boolean borrowHolding(Holding holding) {
        if (balance - holding.getDefaultLoanFee() > 0 && active) {
            if (holding.borrowHolding()) {
                borrowed[findFirstEmptyHoldingSlot()] = holding;
                balance -= holding.getDefaultLoanFee();
                System.out.print("Borrowed");
                return true;
            } else
                System.out.println("Book was unavailable to be borrowed. Book was not added to your account and you were not charged.");
        } else System.out.println("Your account was invalid or balance was insufficient to borrow book");
        return (false);
    }/*
    A member can only be borrow a holding if:
             They are currently active in the system
     They have enough credit to pay the initial loan fee*/

    public boolean returnHolding(Holding holding, DateTime returnDate) {
        int searchedPos = findHolding(holding);
        if (searchedPos <= 0) {
            int lateFee = borrowed[searchedPos].calculateLateFee(returnDate);
            if (lateFee < balance) {
                balance -= lateFee;
            } else {
                System.out.println("Balance must be greater than " + lateFee + " to return item");
                return false;
            }
            if (borrowed[searchedPos].returnHolding(returnDate)) {
                borrowed[searchedPos] = null;
                return (true);
            } else {
                System.out.println("Holding could not be returned");
                return false;
            }
        } else {
            System.out.println("User has not borrowed holding");
            return false;
        }
    }

    public boolean returnHoldingNoFee(Holding holding, DateTime returnDate) {
        int searchedPos = findHolding(holding);
        if (searchedPos <= 0) {
            if (borrowed[searchedPos].returnHolding(returnDate)) {
                borrowed[searchedPos] = null;
                return (true);
            } else {
                System.out.println("Holding could not be returned");
                return false;
            }
        } else {
            System.out.println("User has not borrowed holding");
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
        for (int i = 0; i < borrowed.length; i++) {
            if (borrowed[i] != null) {
                System.out.print(borrowed[i].getTitle() + " (" + borrowed[i].getID() + " )");
            }
        }
    }

    private int findFirstEmptyHoldingSlot() {
        for (int i = 0; i < borrowed.length; i++) {
            if (borrowed[i] != null) {
                return (i);
            }
        }
        System.out.println("Borrower has too many books, return some and try again");
        return (0);
    }

    public int numberOfBorrowedHoldings(){
        int result = 0;
        for(int i= 0; i < borrowed.length;i++){
            if(borrowed[i] != null){
                result++;
            }
        }
        return result;
    }
    public String toString() {
        return (getID() + ":" + getFullName() + ":" + calculateRemainingCredit());
    }
  /*  The member class and its sub-classes should override the toString() method to provide a pre-determined string representation of the member. The format for the member representation separates each attribute via the use of a colon ‘:’
    member_id:full_name:remaining_credit
    e.g. p00001:Joe Bloggs:25
*/
}