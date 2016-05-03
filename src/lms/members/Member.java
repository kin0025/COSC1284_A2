/*
*@author - Alex Kinross-Smith
*/
package lms.members;

import lms.holding.*;
import lms.util.DateTime;


/**
 * Created by akinr on 11/04/2016.
 */
public abstract class Member {
    private String ID;
    private String name;
    private int maxCredit;
    private int balance;
    private Holding[] borrowed = new Holding[50];
    private boolean activeStatus;
    private int currentBorrowedPos = 0;

    public Member(String memberID, String fullName, int credit) {
        setID(memberID);
        setName(fullName);
        setCredit(credit);
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

    public String getFullName() {

        return (name);
    } //Done

    public boolean getStatus() {
        return (activeStatus);
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
        activeStatus = true;
        return (true);
    } //todo Add checking for validity

    public boolean deactivate() {
        this.activeStatus = false;
        return (true);
    } //todo add checking for validity

    public Holding[] getCurrentHoldings() {
        return (borrowed);
    } //todo look at using another collection type throughout

    //    A member must maintain a collection of holdings they currently have on loan. You may implement this collection using an array or one of the Java Collection Framework (JCF) data structures, but regardless of the data structure this data should be returned to the client in the format of an array.
    public boolean updateRemainingCredit(int loanFee) {
        if (balance - loanFee >= 0 && balance - loanFee <= maxCredit) {
            balance -= loanFee;
            return true;
        } else return false;
    }

    /**
     * Subtracts number specified to balance. Cannot go above max balance.
     **/ //todo add overrides for premium members to allow less than 0
    public boolean checkAllowedCreditOverdraw(int loanFee) {
        return (false);
    }//todo what does this mean? Why do we need loan fee?

    public boolean borrowHolding(Holding holding) {
        if (balance - holding.getDefaultLoanFee() < 0 && activeStatus) {
            if (holding.borrowHolding()) {
                borrowed[currentBorrowedPos] = holding;
                balance -= holding.getDefaultLoanFee();
                System.out.print("Borrowed");
                return true;
            } else
                System.out.println("Book was unavaliable to be borrowed. Book was not added to your account and you were not charged.");
        } else System.out.println("Your account was invalid or balance was insufficient to borrow book");
        return (false);
    }/*
    A member can only be borrow a holding if:
             They are currently active in the system
     They have enough credit to pay the initial loan fee*/

    public boolean returnHolding(Holding holding, DateTime returnDate) {
        //todo use search function to find the holding
        int searchedPos = findHolding(holding);
        if (searchedPos <= 0) {
            DateTime currentDate = new DateTime();
            int lateFee = borrowed[searchedPos].calculateLateFee(currentDate);
            if (lateFee < balance) {
                balance -= lateFee;
            } else {
                System.out.println("Balance must be greater than " + lateFee + " to return item");
                return false;
            }
            if (borrowed[searchedPos].returnHolding(currentDate)) {
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

    private int findHolding(Holding holding) {
        int index = -1;
        for (int i = 0; i < borrowed.length; i++) {
            if (borrowed[i] != null && borrowed[i].equals(holding)) {
                index = i;
            }
        }
        return index;
    }

    //    The conditions for returning a holding are different depending on the type of member, please see the functional requirements section above.
    public void print() {
    System.out.println("ID:" + getID());
        System.out.println("Name:" + getFullName());
        System.out.println("Max Credit: " + getMaxCredit());
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

    public String toString() {
        return (getID() + ":" + getFullName() + ":" + calculateRemainingCredit());
    }
  /*  The member class and its sub-classes should override the toString() method to provide a pre-determined string representation of the member. The format for the member representation separates each attribute via the use of a colon ‘:’
    member_id:full_name:remaining_credit
    e.g. p00001:Joe Bloggs:25
*/
}