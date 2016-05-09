/*
 * Copyright Alex Kinross-Smith (c) 2016.
 * s3603437@student.rmit.edu.au
 * alex@akinrosssmith.id.au
 */

package lms.holding;

import lms.SystemOperations;
import lms.util.DateTime;

import java.util.Scanner;
/*
*@author - Alex Kinross-Smith
*/

public abstract class Holding implements SystemOperations {
    private String ID;
    private String title;
    private int loanCost;
    private int maxLoanPeriod;
    private DateTime borrowDate;
    private boolean active;

    protected Holding() {
    }

    public Holding(String holdingID, String title) {
        this.ID = holdingID;
        this.title = title;
        if (!checkValidity().equalsIgnoreCase("valid")) {
            deactivate();
            System.out.println(checkValidity());
            System.out.println("Item has been deactivated due to invalid details.");
        }
        activate();
    }

    public Holding(String ID, String title, int loanCost, int maxLoanPeriod, DateTime borrowDate, boolean active, boolean unavailable) {
        setID(ID);
        this.title = title;
        setLoanCost(loanCost);
        setMaxLoanPeriod(maxLoanPeriod);
        this.borrowDate = borrowDate;
        this.active = active;
        if (!checkValidity().equalsIgnoreCase("valid")) {
            deactivate();
            System.out.println(checkValidity());
            System.out.println("Item " + ID + " has been deactivated due to invalid details.");
        }
    }


    /* Setters */
    public boolean setID(String ID) {
        this.ID = ID;
        return true;
    }

    protected void setLoanCost(int loanCost) {
        this.loanCost = loanCost;
    }

    protected void setMaxLoanPeriod(int maxLoanPeriod) {
        this.maxLoanPeriod = maxLoanPeriod;
    }

    public void setTitle(String title) {
        if (title.length() != 0) {
            this.title = title;
        } else {
            System.out.println("Invalid Title. Title not set and charge deactivated");
            deactivate();
        }
    }


    /* Getters */
    public String getID() {
        return (ID);
    } //Done

    public String getTitle() {
        return (title);
    } //Done

    public boolean isOnLoan() {
        return borrowDate != null;
    } //Done

    public DateTime getBorrowDate() {
        return (borrowDate);
    } //Done


    public int getDefaultLoanFee() {
        return (loanCost);
    }

    public int getMaxLoanPeriod() {
        return (maxLoanPeriod);
    }

    public abstract int calculateLateFee(DateTime dateReturned);

    protected boolean getActiveStatus() {
        return active;
    }


    /*
            The calculation will depend on the type of lms.model.holding please see the functional requirements above.
    */

    /* Functional Methods */
    public boolean borrowHolding() {
        if (active && getBorrowDate() == null) {
            borrowDate = new DateTime();
            return true;
        }
        if (!active) {
            System.out.println("Holding was marked as inactive and could not be borrowed.");
        }
        if (getBorrowDate() != null) {
            System.out.println("Holding was already loaned out");
        }
        return (false);
    }

    /*
            A lms.model.holding can only be borrowed if:
             it is currently active in the system
             it is not already on loan
            If an item is borrowed, then it must have it’s borrowDate set to the current date
    */
    public boolean returnHolding(DateTime dateReturned) { /** Returns true if it has been returned, false if issues encountered and not marked as returned**/
        if (isOnLoan() && DateTime.diffDays(dateReturned, borrowDate) >= 0) {
            if (!active) {
                System.out.println("Item was previously marked as inactive. It has not been marked as returned");
                return false;
            }
            borrowDate = null;
            return true;
        }
        return (false);
    }

    /*
        A lms.model.holding can only be returned if:
             it is currently active in the system
                     it is already on loan
                     the return date is on or after the date on which the item was borrowed
                    If an item is returned, then it must have it’s borrowDate set to null
    */
    public void print() {
        if (!getActiveStatus()) {
            System.out.println("ID: " + ID + " : Inactive");
        } else {
            System.out.println("ID: " + ID);
        }
        System.out.println("Title: " + title);
        System.out.println("Loan Fee: $" + loanCost);
        System.out.println("Max Loan Period: " + maxLoanPeriod);
    }

    /*
            The print method should display the current state of the object in a user friendly format designed for display on the console.
            For example:
            ID: b000001
            Title: Introduction to Java Programming
            Loan Fee: 10
            Max Loan Period: 28
    */

    public String toString() {
        return (getID() + ":" + getTitle() + ":" + getDefaultLoanFee() + ":" + getMaxLoanPeriod());
    }

    public String toFile() {
        return (getID() + "," + getTitle() + "," + getDefaultLoanFee() + "," + getMaxLoanPeriod() + "," + getBorrowDate() + "," + active);
    }

    /*
            COSC1284 – Programming Techniques
            Programming Techniques Assignment 2 - Inventory April 2016 Page 5 of 14
            The Holding classes should override the toString() method to provide a pre-determined string representation of the lms.model.holding. The format for the lms.model.holding representation separates each attribute via the use of a colon ‘:’
            holding_code:holding_title:standard_loan_fee:max_loan_period
            e.g. b000001:Intro to Java:10:28
            v000001:Intro to Java 1:4:7
    */
    public boolean activate() {
        switch (checkValidity().toLowerCase()) {
            case "valid":
                this.active = true;
                return true;
            case "invalid id":

            case "invalid title":
            default:
                System.out.println("Holding was invalid, could not be activated for reason:" + checkValidity());
                return (false);

        }
    }

    public boolean deactivate() {
        this.active = false;
        return true;
    }

    public String checkValidity() {
        if (getID().length() != 7) {
            if ((ID.charAt(0) != 'b' && ID.charAt(0) != 'v') && ID.lastIndexOf('b') == 0 && ID.lastIndexOf('v') == 0)
                return ("Invalid ID");
        }
        if (getTitle().length() <= 0) {
            return ("Invalid Title");
        }
        return ("Valid");
    }

    public boolean isUsable() {
        String result = checkValidity();
        if (!(result.equalsIgnoreCase("valid"))) {
            System.out.println(result);
            deactivate();
        }
        return active;
    }


}
