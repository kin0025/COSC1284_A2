/*
 * Copyright Alex Kinross-Smith (c) 2016.
 * s3603437@student.rmit.edu.au
 * alex@akinrosssmith.id.au
 */

package lms.holding;/*
*@author - Alex Kinross-Smith
*/

import lms.util.DateTime;

/**
 * Created by akinr on 11/04/2016.
 */
public class Book extends Holding {
    private String author;

    public Book(String holdingID, String title) {
        setLoanCost(10);
        setMaxLoanPeriod(28);
        setTitle(title);
        setID(holdingID);
        if (!checkValidity().equalsIgnoreCase("valid")) {
            deactivate();
            System.out.println(checkValidity());
            System.out.println("Item has been deactivated due to invalid details.");
        }
        setUnavailable(false);
        activate();
    }

    public Book(String ID, String title, String author, int loanCost, int maxLoanPeriod, DateTime borrowDate, boolean active, boolean unavailable) {
        super(ID, title, loanCost, maxLoanPeriod, borrowDate, active, unavailable);
        setAuthor(author);

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
        return (daysDiff * 2);
    }

    @Override
    public String checkValidity() {
        String result = super.checkValidity();
        if (getDefaultLoanFee() != 10) {
            return ("Invalid Fee");
        }
        return (result);
    }

    @Override
    public String toFile() {
        return (getID() + "," + getTitle() + "," + getDefaultLoanFee() + "," + getMaxLoanPeriod() + "," + getBorrowDate() + "," + getActiveStatus() + "," + getUnavailable() + "," + getAuthor());
    }

}
