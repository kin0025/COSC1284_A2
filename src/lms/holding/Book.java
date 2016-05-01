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
    public boolean setID(String itemID, char itemType) {
        if (itemType == 'b' && itemID.length() == 6) {
            super.setID(itemType + "" + itemID);
            return (true);
        } else {
            return (false);
        }
    }
@Override
    public boolean setID(String ID) {
        if (ID.charAt(0) == 'b' && ID.length() == 7) {
            super.setID(ID);
            return (true);
        } else {
            return (false);
        }
    }

    public void setAuthor(String author) {
        this.author = author;
    }
    public String getAuthor(){
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
    public String toFile(){
        String result = getID() + "," + getTitle() + "," + getDefaultLoanFee() + "," + getMaxLoanPeriod() + "," + getBorrowDate() + "," + getActiveStatus() + "," + getUnavailable() + "," + getAuthor();
        return (result);
    }

}
