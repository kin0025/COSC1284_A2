/*
 * Copyright Alex Kinross-Smith (c) 2016.
 * s3603437@student.rmit.edu.au
 * alex@akinrosssmith.id.au
 */

package lms.holding;

import lms.SystemOperations;
import lms.util.DateTime;
import lms.util.Utilities;
/*
*@author - Alex Kinross-Smith
*/

public abstract class Holding implements SystemOperations {
    private String ID;
    private String title;
    private int loanFee;
    private int maxLoanPeriod;
    private double lateFee;
    private DateTime borrowDate;
    private boolean active;

    protected double getLateFee() {
        return lateFee;
    }

    protected void setLateFee(double lateFee) {
        this.lateFee = lateFee;
    }

    public Holding(String holdingID, String title) {
        this.ID = holdingID;
        this.title = title;
    }

    public Holding(String ID, String title, int loanFee, int maxLoanPeriod, DateTime borrowDate, boolean active, boolean unavailable) {
        setID(ID);
        this.title = title;
        setLoanFee(loanFee);
        setMaxLoanPeriod(maxLoanPeriod);
        this.borrowDate = borrowDate;
        this.active = active;
        if (!checkValidity().equalsIgnoreCase("valid")) {
            deactivate();
            System.out.println(Utilities.ERROR_MESSAGE + " Item " + ID + " has been deactivated due to invalid details: " + checkValidity());
        }
    }

    /* Setters */
    public boolean setID(String ID) {
        this.ID = ID;
        return true;
    }

    protected void setLoanFee(int loanFee) {
        this.loanFee = loanFee;
    }

    protected void setMaxLoanPeriod(int maxLoanPeriod) {
        this.maxLoanPeriod = maxLoanPeriod;
    }

    public void setTitle(String title) {
        if (title.length() != 0) {
            this.title = title;
        } else {
            System.out.println(Utilities.ERROR_MESSAGE + " Invalid Title. Title not set and charge deactivated");
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
        return (loanFee);
    }

    public int getMaxLoanPeriod() {
        return (maxLoanPeriod);
    }

    public abstract int calculateLateFee(DateTime dateReturned);

    protected boolean getActiveStatus() {
        return active;
    }

    /* Functional Methods */
    public boolean borrowHolding() {
        if (active && getBorrowDate() == null) {
            borrowDate = new DateTime();
            return true;
        }
        if (!active) {
            System.out.println(Utilities.ERROR_MESSAGE + " Holding was marked as inactive and could not be borrowed.");
        }
        if (getBorrowDate() != null) {
            System.out.println(Utilities.ERROR_MESSAGE + " Holding was already loaned out");
        }
        return (false);
    }

    public boolean returnHolding(DateTime dateReturned) { /** Returns true if it has been returned, false if issues encountered and not marked as returned**/
        if (isOnLoan() && DateTime.diffDays(dateReturned, borrowDate) >= 0) {
            if (!active) {
                System.out.println(Utilities.ERROR_MESSAGE + " Item was previously marked as inactive. It has not been marked as returned");
                return false;
            }
            borrowDate = null;
            return true;
        }
        return (false);
    }

    public void print() {
        if (!getActiveStatus()) {
            System.out.println("ID: " + ID + " : Inactive");
        } else {
            System.out.println("ID: " + ID);
        }
        System.out.println("Title: " + title);
        System.out.println("Loan Fee: $" + loanFee);
        System.out.println("Max Loan Period: " + maxLoanPeriod);
    }

    @Override
    public String toString() {
        return (getID() + ":" + getTitle() + ":" + getDefaultLoanFee() + ":" + getMaxLoanPeriod());
    }

    public String toFile() {
        return (getID() + "," + getTitle() + "," + getDefaultLoanFee() + "," + getMaxLoanPeriod() + "," + getBorrowDate() + "," + active);
    }

    public boolean activate() {
        if (checkValidity().toLowerCase().equals("valid")) {
            this.active = true;
            return true;
        } else {
            System.out.println(Utilities.ERROR_MESSAGE + " Holding was invalid, could not be activated for reason: " + checkValidity());
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

}
