package lms.holding;

import lms.util.DateTime;

import java.util.Scanner;
/*
*@author - Alex Kinross-Smith
*/

/**
 * Created by akinr on 11/04/2016.
 */
public abstract class Holding {
    private String ID;
    private String title;
    private int loanCost;
    private int maxLoanPeriod;
    private DateTime borrowDate;
    private boolean active;
    private boolean unavailable;

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
        unavailable = false;
        activate();
    }

    public Holding(String ID, String title, int loanCost, int maxLoanPeriod, DateTime borrowDate, boolean active, boolean unavailable) {
        setID(ID);
        this.title = title;
        setLoanCost(loanCost);
        setMaxLoanPeriod(maxLoanPeriod);
        this.borrowDate = borrowDate;
        this.active = active;
        this.unavailable = unavailable;
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

    public void setUnavailable(Boolean unavailable) {
        this.unavailable = unavailable;
    }

    /* Getters */
    public String getID() {
        return (ID);
    } //Done

    public String getTitle() {
        return (title);
    } //Done

    public boolean isOnLoan() {
        if (borrowDate == null) {
            return (false);
        }
        return (true);
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

    protected boolean getUnavailable() {
        return unavailable;
    }
    /*
            The calculation will depend on the type of lms.model.holding please see the functional requirements above.
    */

    /* Functional Methods */
    public boolean borrowHolding() {
        if (!unavailable && active && getBorrowDate() == null) {
            borrowDate = new DateTime();
            return true;
        } else
        if(unavailable){
            System.out.println("Holding was marked as unavailable");
        }
        if(!active){
            System.out.println("Holding was marked as inactive");
        }
        if(getBorrowDate() != null){
            System.out.println("Holding was already borrowed");
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
            if (unavailable) {
                System.out.println("Item was previously marked as unavailable. It has been marked as returned, but may not be loaned out until changed.");
            } else if (!active) {
                System.out.println("Item was previously marked as inactive. It has been marked as returned, but may not be loaned out until changed.");
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
        System.out.println("ID: " + ID);
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

    public String printfResult() {
        return ("ID:" + ID + "\n" + "Title:" + title + "\n" + "Loan Fee: $" + loanCost + "\n" + "Max Loan Period: " + maxLoanPeriod);
    }

    public String toString() {
        String result = getID() + ":" + getTitle() + ":" + getDefaultLoanFee() + ":" + getMaxLoanPeriod();
        return (result);
    }

    public String toFile() {
        String result = getID() + "," + getTitle() + "," + getDefaultLoanFee() + "," + getMaxLoanPeriod() + "," + getBorrowDate() + "," + active + "," + unavailable;
        return (result);
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
        Scanner input = new Scanner(System.in);
        switch (checkValidity().toLowerCase()) {
            case "valid":
                this.active = true;
                return true;
            case "invalid id":
          /*      System.out.println("Holding was invalid, could not be activated for reason:" + checkValidity());
                System.out.println("Do you want to enter another one? (yes/no/y/n)");
                char result = input.nextLine().toLowerCase().charAt(0);
                if (result == 'y') {
                    do {
                        System.out.println("Enter new ID:");
                        setID(input.nextLine());
                    } while (checkValidity().equalsIgnoreCase("invalid id"));
                    activate();
                } else return false;
                break;
          */  case "invalid title":
            /*    System.out.println("Holding was invalid, could not be activated for reason:" + checkValidity());
                System.out.println("Do you want to enter another one? (yes/no/y/n)");
                result = input.nextLine().toLowerCase().charAt(0);
                if (result == 'y') {
                    do {
                        System.out.println("Enter new title:");
                        setTitle(input.nextLine());
                    } while (checkValidity().equalsIgnoreCase("invalid title"));
                    activate();
                } else return false;
            */default:
                System.out.println("Holding was invalid, could not be activated for reason:" + checkValidity());
                return (false);

        }
//        return false;
    }

    public boolean deactivate() {
        this.active = false;
        return (false);
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

    public boolean isUseable() {
        String result = checkValidity();
        if (!(result.equalsIgnoreCase("valid"))) {
            System.out.println(result);
            deactivate();
        }
        if (unavailable || !active) {
            return false;
        }
        return true;
    }


}
