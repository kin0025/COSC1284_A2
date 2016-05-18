/*
 * Copyright Alex Kinross-Smith (c) 2016.
 * s3603437@student.rmit.edu.au
 * alex@akinrosssmith.id.au
 */

package lms.holding;/*
*@author - Alex Kinross-Smith
*/

import lms.util.DateTime;
import lms.util.Utilities;

/**
 * Created by akinr on 11/04/2016 as part of s3603437_A2
 */
public class Book extends Holding {
    private String author;

    public Book(String holdingID, String title) {
        super(holdingID,title);
        setLoanFee(10);
        setMaxLoanPeriod(28);
        //setLateFee(2);
        activate();
        if (!checkValidity().equalsIgnoreCase("valid")) {
            deactivate();
            System.out.println(Utilities.WARNING_MESSAGE + " Item " + getID() + " has been deactivated due to invalid details.");
        }
    }

    public Book(String ID, String title, int loanFee, int maxLoanPeriod, DateTime borrowDate, boolean active, String uniqueID) {
        super(ID, title, loanFee, maxLoanPeriod, borrowDate, active, uniqueID);
    }

    @Override
    public boolean setID(String ID) {
        boolean validID = true;
        int i = 1;
        while (ID.length() > i && validID) {
            if (!((int) ID.charAt(i) >= 48 && (int) ID.charAt(i) <= 58)) { //Ascii codes for numbers are between 48 and 58 (including 0). If each character is between these values, they are numbers.
                validID = false;
            }
            i++;
        }
        if (ID.charAt(0) == 'b' && ID.length() == 7 && validID) {
            super.setID(ID);
            return (true);
        } else {
            return (false);
        }
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getAuthor() {
        return author;
    }

    @Override
    public int calculateLateFee(DateTime dateReturned) {
        int daysOut = DateTime.diffDays(dateReturned, getBorrowDate());
        int daysDiff = daysOut - getMaxLoanPeriod();
        if (daysDiff < 0) {
            daysDiff = 0;
        }
        return (int)(daysDiff /* getLateFee()*/);
    }

    @Override
    public String checkValidity() {
        String result = super.checkValidity();
        if (getDefaultLoanFee() != 10) {
            return ("Invalid Fee");
        }
        return (result);
    }
}
