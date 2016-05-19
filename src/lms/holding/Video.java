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
public class Video extends Holding {
    public Video(String holdingId, String title, int loanFee) {
        super(holdingId,title);
        setLoanFee(loanFee);
        setMaxLoanPeriod(7);
        activate();
        setLateFee(0.5);
        if (!checkValidity().equalsIgnoreCase("valid")) {
            deactivate();
            System.out.println(Utilities.ERROR_MESSAGE + " Item " + getID() + " has been deactivated due to invalid details.");
        }
    }

    public Video(String ID, String title, int loanFee, int maxLoanPeriod, DateTime borrowDate, boolean active, String uniqueID) {
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
        if (ID.charAt(0) == 'v' && ID.length() == 7 && validID) {
            super.setID(ID);
            return (true);
        } else {
            return (false);
        }

    }

    @Override
    public int calculateLateFee(DateTime dateReturned) {
        int daysOut = DateTime.diffDays(dateReturned, getBorrowDate());
        int daysDiff = daysOut - getMaxLoanPeriod();
        if (daysDiff < 0) {
            daysDiff = 0;
        }
        return ((int) (daysDiff * getDefaultLoanFee() * getLateFee()));
    }

    @Override
    public void setLoanFee(int loanFee) {
        if (loanFee == 4 || loanFee == 6) {
            super.setLoanFee(loanFee);
        } else {
            System.out.println(Utilities.ERROR_MESSAGE + " Invalid loan cost. Holding has been deactivated. ID:" + getID());
            deactivate();
        }
    }

    @Override
    public String checkValidity() {
        String result = super.checkValidity();
        if (getDefaultLoanFee() == 6 || getDefaultLoanFee() == 4) {
            return (result);
        } else return ("Invalid Fee");
    }
}

