/*
 * Copyright Alex Kinross-Smith (c) 2016.
 * s3603437@student.rmit.edu.au
 * alex@akinrosssmith.id.au
 */

package lms.holding;/*
*@author - Alex Kinross-Smith
*/

import lms.exceptions.IncorrectDetailsException;
import lms.util.DateTime;
import lms.util.Utilities;

/**
 * Created by akinr on 11/04/2016 as part of s3603437_A2
 */
public class Book extends Holding {
    private static final int LOAN_FEE = 10;
    private static final int LOAN_PERIOD = 28;
    private static final int LATE_FEE = 2;

    /**
     * Construct the holding using the default values and the ID and title passed through.
     *
     * @param holdingID The id of the holding.
     * @param title the title of the holding.
     */
    public Book(String holdingID, String title) throws IncorrectDetailsException{
        super(holdingID, title);
        setLoanFee(LOAN_FEE);
        setMaxLoanPeriod(LOAN_PERIOD);
        //Checks validity and activates/deactivates.
        activate();
    }

    /**
     * Used to reconstruct all instance variables for loading.
     *
     * @param ID            The ID of the item
     * @param title         The title of the item
     * @param loanFee       the loan fee of the item
     * @param maxLoanPeriod the loan period of the item
     * @param borrowDate    the date that the item was borrowed. If it is null the holding is not borrowed.
     * @param active        the active status of the item
     * @param uniqueID      the unique identifier of the item. As ID can be changed, this is used to identify the holding when saving.
     */
    public Book(String ID, String title, int loanFee, int maxLoanPeriod, DateTime borrowDate, boolean active, String uniqueID) {
        super(ID, title, loanFee, maxLoanPeriod, borrowDate, active, uniqueID);
    }

    /**
     * Sets the ID of the holding after validating it.
     *
     * @param ID The string form ID to be set for the holding.
     * @return returns the success state of setting the ID. If false, ID was not set.
     */
    @Override
    public boolean setID(String ID) {
        if (Utilities.isIDValid('b', ID)) {
            super.setID(ID);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Calculates the late fee of the holding and returns it.
     *
     * @param dateReturned The date the holding was returned.
     * @return The late fee in dollars. Will always be a whole number.
     */
    @Override
    public int calculateLateFee(DateTime dateReturned) {
        int daysOut = DateTime.diffDays(dateReturned, getBorrowDate());

        //Calculate how far beyond the max loan date the holding has been out.
        int daysDiff = daysOut - getMaxLoanPeriod();

        if (daysDiff <= 0) {
            //If they haven't had the holding for longer than max loan period, there is no late fee.
            return 0;
        }
        //Return the late fee.
        return (daysDiff * LATE_FEE);
    }

    /**
     * Checks the validity of title, ID and loan fee.
     *
     * @return A string representation of the result.
     */
    @Override
    public String checkValidity() {
        String result = super.checkValidity('b');
        if (getDefaultLoanFee() != LOAN_FEE) {
            return ("Invalid Fee");
        }
        return (result);
    }
}
