package lms.holding;

import lms.util.DateTime;
/*
*@author - Alex Kinross-Smith
*/

/**
 * Created by akinr on 11/04/2016.
 */
public class Holding {
    private String ID;
    private String title;
    private int loanCost;
    private int maxDaysToLoan;
    private int loanDailyCost;
    private DateTime loanedDate;
    private boolean loanedOut;
    private boolean active;


    protected Holding() {
    }

    /* MANDATORY METHODS */
    public boolean setID(String ID) {
        this.ID = ID;
        return true;
    }



    public Holding(String ID, String title, int loanCost, int maxDaysToLoan, int returnedDate, boolean loanedOut, boolean active) {
        this.ID = ID;
        this.title = title;
        this.loanCost = loanCost;
        this.maxDaysToLoan = maxDaysToLoan;

    }


    public String getID() {
        return (ID);
    } //Done

    public String getTitle() {
        return (title);
    } //Done

    public boolean isOnLoan() {
        return (loanedOut);
    }

    public DateTime getBorrowDate() {
        return (loanedDate);
    }

    public void setLoanDailyCost(int loanDailyCost) {
        this.loanDailyCost = loanDailyCost;
    }

    public int getDefaultLoanFee() {
        return (1);
    }

    public int getMaxLoanPeriod() {
        return (1);
    }

    public int calculateLateFee(DateTime dateReturned) {
        return (1);
    }

    /*
            The calculation will depend on the type of lms.model.holding please see the functional requirements above.
    */
    public boolean borrowHolding() {
        return (false);
    }

    /*
            A lms.model.holding can only be borrowed if:
             it is currently active in the system
             it is not already on loan
            If an item is borrowed, then it must have it’s borrowDate set to the current date
    */
    public boolean returnHolding(DateTime dateReturned) {
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
        return (null);
    }

    /*
            COSC1284 – Programming Techniques
            Programming Techniques Assignment 2 - LMS April 2016 Page 5 of 14
            The Holding classes should override the toString() method to provide a pre-determined string representation of the lms.model.holding. The format for the lms.model.holding representation separates each attribute via the use of a colon ‘:’
            holding_code:holding_title:standard_loan_fee:max_loan_period
            e.g. b000001:Intro to Java:10:28
            v000001:Intro to Java 1:4:7
    */
    public boolean activate() {
        return (false);
    }

    public boolean deactivate() {
        return (false);
    }

    protected void setLoanCost(int loanCost) {
        this.loanCost = loanCost;
    }
}
